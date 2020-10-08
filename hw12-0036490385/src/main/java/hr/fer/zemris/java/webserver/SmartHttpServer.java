package hr.fer.zemris.java.webserver;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

/**
 * Poslužitelj koji omogućuje izvođenje pohranjenih skripti sa zadanim
 * vrijednostima argumenata. Omogućuje dohvat pohranjenih datoteka na serveru
 * (odnosno u mapi koja je dostupna klijentu) te pokretanje nekoliko različitih
 * radnji opisanih sučeljem {@link IWebWorker}. Server ne dozvoljava klijentu
 * čitanje mape /private. Postavke servera se očitavaju iz konfiguracijske
 * datoteke server.properties.
 *
 * @author Alen Magdić
 *
 */
public class SmartHttpServer {
	/**
	 * Adresa na kojoj poslužitelj sluša zahtjeve.
	 */
	private String address;
	/**
	 * Port na kojem poslužitelj sluša zahtjeve.
	 */
	private int port;
	/**
	 * Broj dretvi radnica koje poslužuju klijente.
	 */
	private int workerThreads;
	/**
	 * Vrijeme neaktivnosti nakon koje poslužitelj zaboravlja sjednicu.
	 */
	private int sessionTimeout;
	/**
	 * Mapa koja mapira ekstenziju datoteke na odgovarajući mime tip.
	 **/
	private Map<String, String> mimeTypes = new HashMap<String, String>();
	/**
	 * Dretva na kojoj se poslužitelj vrti, odnosno dretva koja čeka zahtjeve i
	 * šalje ih dretvama radnicama na posluživanjes.
	 */
	private ServerThread serverThread;
	/**
	 * Bazen dretvi radnica koje poslužuju klijente.
	 */
	private ExecutorService threadPool;
	/**
	 * Vršna mapa poslužitelja. Klijentu nije dozvoljeno čitati mape koje su
	 * hijerarhijski iznad vršne mape.
	 */
	private Path documentRoot;
	/**
	 * Zastavica kojom se poslužitelju poručuje da se zaustavi.
	 */
	private boolean stopRequested;
	/**
	 * Mapa koja mapira ime radnika {@link IWebWorker} na njegov objekt (tj. na
	 * radnika s tim imenom).
	 */
	private Map<String, IWebWorker> workersMap;
	/**
	 * Mapa sjednica sa identifikacijskim brojem sjednice kao ključem i
	 * sjednicom ({@link SmartHttpServer.SessionMapEntry} kao vrijednošću.
	 */
	private Map<String, SessionMapEntry> sessions = new HashMap<String, SmartHttpServer.SessionMapEntry>();
	/**
	 * Generator nasumičnih brojeva korišten za generiranje identifikacijskih
	 * brojeva sjednica.
	 */
	private Random sessionRandom = new Random();
	/**
	 * Pristupna točka preko koje poslužitelj prihvaća zahtjeve klijenata.
	 */
	ServerSocket serverSocket;

	/**
	 * Metoda od koje počinje izvođenje programa.
	 *
	 * @param args
	 *            ulazni argumenti
	 */
	public static void main(String[] args) {
		SmartHttpServer server;
		try {
			server = new SmartHttpServer("./server.properties");
			server.start();
		} catch (RuntimeException | IOException | InstantiationException | IllegalAccessException
				| ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Konstruktor.
	 *
	 * @param configFileName
	 *            ime konfiguracijske datoteke poslužitelja
	 * @throws IOException
	 *             ukoliko dođe do problema čitanja konfiguracijskih datoteka
	 * @throws NumberFormatException
	 *             ukoliko se u konfiguracijskoj datoteci na mjestu gdje se
	 *             očekuje cijeli broj nađe nešto što nije cijeli broj
	 * @throws InstantiationException
	 *             ukoliko dođe do problema s instanciranjem nekog od radnika
	 *             {@link IWebWorker}
	 * @throws IllegalAccessException
	 *             ukoliko dođe do problema s učitavanjem nekog od radnika
	 *             {@link IWebWorker}
	 * @throws ClassNotFoundException
	 *             ukoliko dođe to problema s učitavanjem nekog od radnika
	 *             {@link IWebWorker}
	 */
	public SmartHttpServer(String configFileName) throws IOException, InstantiationException, NumberFormatException,
			IllegalAccessException, ClassNotFoundException {
		Properties p = new Properties();
		p.load(Files.newInputStream(Paths.get(configFileName)));
		address = p.getProperty("server.address");
		port = Integer.parseInt(p.getProperty("server.port"));
		workerThreads = Integer.parseInt(p.getProperty("server.workerThreads"));
		sessionTimeout = Integer.parseInt(p.getProperty("session.timeout"));

		Properties mimeProp = new Properties();
		mimeProp.load(Files.newInputStream(Paths.get(p.getProperty("server.mimeConfig"))));
		for (Entry<Object, Object> entry : mimeProp.entrySet()) {
			mimeTypes.put((String) entry.getKey(), (String) entry.getValue());
		}

		serverThread = new ServerThread();
		documentRoot = Paths.get(p.getProperty("server.documentRoot"));

		parseWorkers(Paths.get(p.getProperty("server.workers")));
	}

	/**
	 * Pomoćna dretva koja iz mape sjednica čisti sjednice koje više nisu
	 * aktivne.
	 */
	private Thread expiredSessionCleaner = new Thread(() -> {
		while (true) {
			synchronized (this) {
				List<String> expiredSessions = new ArrayList<>();
				for (Entry<String, SessionMapEntry> entry : sessions.entrySet()) {
					if (entry.getValue().validUntil < System.currentTimeMillis() / 1000) {
						expiredSessions.add(entry.getKey());
					}
				}

				for (String SID : expiredSessions) {
					sessions.remove(SID);
				}
			}

			try {
				Thread.sleep(300_000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	});

	/**
	 * Puni mapu radnika {@link IWebWorker} učitavanjem podataka iz zadane
	 * konfiguracijske datoteke.
	 *
	 * @param path
	 *            konfiguracijska datoteka koja mapira radnike
	 *            {@link IWebWorker} na paket u kojem se nalaze
	 * @throws IOException
	 *             ukoliko dođe do problema s čitanjem konfiguracijske datoteke
	 * @throws InstantiationException
	 *             ukoliko dođe do problema s instanciranjem nekog od radnika
	 *             {@link IWebWorker}
	 * @throws IllegalAccessException
	 *             ukoliko dođe do problema s učitavanjem nekog od radnika
	 *             {@link IWebWorker}
	 * @throws ClassNotFoundException
	 *             ukoliko dođe to problema s učitavanjem nekog od radnika
	 *             {@link IWebWorker}
	 */
	private void parseWorkers(Path path)
			throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		workersMap = new HashMap<>();
		List<String> lines = Files.readAllLines(path);

		for (String line : lines) {
			if (line.startsWith("//")) {
				continue;
			}

			String[] keyValue = line.split("[=]");
			if (keyValue.length != 2) {
				throw new RuntimeException("Invalid workers properties file.");
			}

			String key = keyValue[0];
			String value = keyValue[1];

			if (workersMap.containsKey(key)) {
				throw new RuntimeException(
						"Multiple entries with the same key in workers properties file. Key: " + key);
			}

			Class<?> referenceToClass = this.getClass().getClassLoader().loadClass(value.trim());
			Object newObject = referenceToClass.newInstance();
			workersMap.put(key.trim(), (IWebWorker) newObject);
		}
	}

	/**
	 * Pokreće poslužitelja.
	 */
	protected synchronized void start() {
		if (!serverThread.isAlive()) {
			serverThread.start();

			expiredSessionCleaner.start();
		}
		threadPool = Executors.newFixedThreadPool(workerThreads);
	}

	/**
	 * Zaustavlja poslužitelja.
	 */
	protected synchronized void stop() {
		stopRequested = true;
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		threadPool.shutdown();
	}

	/**
	 * Dretva koja čeka klijente i šalje ih na posluživanje bazenu dretvi
	 * radnica.
	 *
	 * @author Alen Magdić
	 *
	 */
	protected class ServerThread extends Thread {
		@Override
		public void run() {
			try {
				runServer();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		/**
		 * Čeka klijente i šalje ih na obradu bazenu dretvi radnica.
		 *
		 * @throws IOException
		 *             ukoliko dođe do problema s pisanjem ili čitanjem podataka
		 */
		private void runServer() throws IOException {
			ServerSocket serverSocket = new ServerSocket();
			serverSocket.bind(new InetSocketAddress((InetAddress) null, port));

			while (!stopRequested) {
				Socket client;
				try {
					client = serverSocket.accept();
				} catch (SocketException ex) {
					if (stopRequested) {
						return;
					}
					ex.printStackTrace();
					return;
				}
				ClientWorker cw = new ClientWorker(client);
				threadPool.submit(cw);
			}

			serverSocket.close();
		}
	}

	/**
	 * Klasa koja predstavlja sjednicu. Sadrži identifikacijski broj sjednice,
	 * vrijeme do koje je sjednica valjana i mapu kolačića.
	 *
	 * @author Alen Magdić
	 *
	 */
	private static class SessionMapEntry {
		@SuppressWarnings("unused")
		/**
		 * Identifikacijski broj sjednice.
		 */
		String sid;
		/**
		 * Vrijeme do kojeg je sjednica valjana.
		 */
		long validUntil;
		/**
		 * Mapa kolačića.
		 */
		Map<String, String> map;

		/**
		 * Konstruktor.
		 *
		 * @param sid
		 *            identifikacijski broj sjednice
		 * @param validUntil
		 *            vrijeme do kojeg je sjednica valjana
		 */
		public SessionMapEntry(String sid, long validUntil) {
			this.sid = sid;
			this.validUntil = validUntil;
			map = new ConcurrentHashMap<>();
		}
	}

	/**
	 * Dretva koja obrađuje klijente.
	 *
	 * @author Alen Magdić
	 *
	 */
	private class ClientWorker implements Runnable, IDispatcher {
		/**
		 * Pristupna točka klijenta.
		 */
		private Socket csocket;
		/**
		 * Input stream za čitanje klijentova zahtjeva.
		 */
		private PushbackInputStream istream;
		/**
		 * Output stream za zapisivanje odgovora na klijentov zahtjev.
		 */
		private OutputStream ostream;
		/**
		 * Verzija zahtjeva.
		 */
		private String version;
		/**
		 * Parametri zahtjeva.
		 */
		private Map<String, String> params = new HashMap<String, String>();
		/**
		 * Privremeni parametri zahtjeva.
		 */
		private Map<String, String> tempParams = new HashMap<String, String>();
		/**
		 * Trajni parametri zahtjeva. Vrijede za čitavo trajanje sjednice.
		 */
		private Map<String, String> permParams = new HashMap<String, String>();
		/**
		 * Lista kolačića.
		 */
		private List<RCCookie> outputCookies = new ArrayList<RequestContext.RCCookie>();
		/**
		 * Kontekst zahtjeva.
		 *
		 */
		private RequestContext context;

		/**
		 * Konstruktor.
		 *
		 * @param csocket
		 *            pristupna točka klijenta
		 */
		public ClientWorker(Socket csocket) {
			super();
			this.csocket = csocket;
		}

		@Override
		public void run() {
			try {
				processRequest();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/**
		 * Procesira zahtjev klijenta.
		 *
		 * @throws Exception
		 *             ako se dogodi problem u procesiranju zahtjeva
		 */
		public void processRequest() throws Exception {
			istream = new PushbackInputStream(csocket.getInputStream());
			ostream = new BufferedOutputStream(csocket.getOutputStream());

			List<String> request = readRequest();
			if (request == null || request.size() == 0) {
				sendError(ostream, 400, "Bad request");
				return;
			}

			String firstLine = request.get(0);
			String[] fLineParts = firstLine.split(" ");
			if (fLineParts.length != 3) {
				sendError(ostream, 400, "Bad request");
				return;
			}

			String method = fLineParts[0].toUpperCase();
			if (!method.equals("GET")) {
				sendError(ostream, 405, "Method Not Allowed");
				return;
			}

			String requestedPath = fLineParts[1];
			version = fLineParts[2];
			if (!version.equals("HTTP/1.0") && !version.equals("HTTP/1.1")) {
				sendError(ostream, 505, "HTTP Version Not Supported");
				return;
			}

			String[] reqPathParts = requestedPath.split("[?]");
			String path = reqPathParts[0];
			String paramString;
			if (reqPathParts.length > 1) {
				paramString = reqPathParts[1];
			} else {
				paramString = "";
			}

			checkSession(request);

			parseParameters(paramString);

			Path reqPath = Paths.get(documentRoot.toString(), path);
			if (!reqPath.startsWith(documentRoot)) {
				sendError(ostream, 403, "Forbidden");
				return;
			}

			internalDispatchRequest(path.toString(), true);
			ostream.flush();
			csocket.close();
		}

		/**
		 * Provjerava kojoj sjednici pripada zadani zahtjev. Ako u zahtjevu nije
		 * naveden identifikacijski broj sjednice, stvara se novi objekt
		 * sjednice s generiranim identifikacijskim brojem. Ako je naveden
		 * identifikacijski broj sjednice, učitava parametre te sjednice kao
		 * trajne parametre.
		 *
		 * @param request
		 *            zahtjev klijenta
		 */
		private void checkSession(List<String> request) {
			String sidCandidate = null;
			String domainValue = null;
			for (String line : request) {
				if (line.startsWith("Host:")) {
					domainValue = line.substring("Host:".length()).trim();
					domainValue = domainValue.substring(0, domainValue.indexOf(":"));
					continue;
				}
				if (!line.startsWith("Cookie:")) {
					continue;
				}
				int sidIndex = line.indexOf("sid=\"");
				if (sidIndex == -1) {
					continue;
				}

				String lineSubstring = line.substring(sidIndex + "sid=\"".length());
				sidCandidate = lineSubstring.substring(0, lineSubstring.indexOf('\"'));
			}

			synchronized (SmartHttpServer.this) {

				if (sidCandidate != null) {
					SessionMapEntry session = sessions.get(sidCandidate);
					if (session == null || session.validUntil < System.currentTimeMillis() / 1000) {
						sessions.remove(sidCandidate);
						sidCandidate = null;
					} else {
						session.validUntil = System.currentTimeMillis() / 1000 + sessionTimeout;
					}
				}

				if (sidCandidate == null) {
					sidCandidate = createSID();
					sessions.put(sidCandidate,
							new SessionMapEntry(sidCandidate, System.currentTimeMillis() / 1000 + sessionTimeout));
					RCCookie cookie = new RCCookie("sid", sidCandidate, null,
							domainValue == null ? address : domainValue, "/");
					cookie.setHttpOnly(true);
					outputCookies.add(cookie);
				}

				permParams = sessions.get(sidCandidate).map;
			}
		}

		/**
		 * Generira identifikacijski broj sjednice. To je niz od 20 znakova
		 * velikih slova engleske abecede.
		 *
		 * @return identifikacijski broj sjednice
		 */
		private String createSID() {
			StringBuilder sidB = new StringBuilder();
			for (int i = 0; i < 20; i++) {
				sidB.append('A' + sessionRandom.nextInt('Z' - 'A'));
			}
			return sidB.toString();
		}

		/**
		 * Vraća ekstenziju zadane datoteke.
		 *
		 * @param filePath
		 *            datoteka
		 * @return ekstenzija zadane datoteke
		 */
		private String getFileExtension(Path filePath) {
			String path = filePath.toString();
			int dotIndex = path.lastIndexOf(".");
			if (dotIndex == -1) {
				return null;
			}
			return path.substring(dotIndex + 1);
		}

		/**
		 * Parsira parametre zahtjeva.
		 *
		 * @param paramString
		 *            string koji sadrži popisane parametre zahtjeva u obliku
		 *            "a=5&b=6&c=3"
		 */
		private void parseParameters(String paramString) {
			String[] pairs = paramString.split("[&]");
			for (String pair : pairs) {
				String[] array = pair.split("[=]");
				if (array.length != 2 || array[0].isEmpty() || array[1].isEmpty()) {
					continue;
				}

				params.put(array[0].trim(), array[1].trim());
			}
		}

		/**
		 * Čita klijentov zahtjev.
		 *
		 * @return lista redaka klijentovog zahtjeva
		 * @throws IOException
		 *             ukoliko dođe do problema u čitanju zahtjeva
		 */
		private List<String> readRequest() throws IOException {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int state = 0;
			l: while (true) {
				int b;
				try {
					b = istream.read();
				} catch (IOException ex) {
					if (bos.size() == 0) {
						return null;
					}
					return null;
				}
				if (b == -1) {
					return null;
				}
				if (b != 13) {
					bos.write(b);
				}
				switch (state) {
				case 0:
					if (b == 13) {
						state = 1;
					} else if (b == 10) {
						state = 4;
					}
					break;
				case 1:
					if (b == 10) {
						state = 2;
					} else {
						state = 0;
					}
					break;
				case 2:
					if (b == 13) {
						state = 3;
					} else {
						state = 0;
					}
					break;
				case 3:
					if (b == 10) {
						break l;
					} else {
						state = 0;
					}
					break;
				case 4:
					if (b == 10) {
						break l;
					} else {
						state = 0;
					}
					break;
				}
			}
			String requestHeader = new String(bos.toByteArray(), StandardCharsets.US_ASCII);

			List<String> headers = new ArrayList<String>();
			String currentLine = null;
			for (String s : requestHeader.split("\n")) {
				if (s.isEmpty()) {
					break;
				}
				char c = s.charAt(0);
				if (c == 9 || c == 32) {
					currentLine += s;
				} else {
					if (currentLine != null) {
						headers.add(currentLine);
					}
					currentLine = s;
				}
			}
			if (!currentLine.isEmpty()) {
				headers.add(currentLine);
			}
			return headers;
		}

		/**
		 * Šalje klijentu poruku o pogrešci.
		 *
		 * @param cos
		 *            {@link OutputStream} na koji se šalje poruka
		 * @param statusCode
		 *            kod pogreške
		 * @param statusText
		 *            ime pogreške
		 * @throws IOException
		 *             ukoliko dođe do problema sa slanjem poruke
		 */
		private void sendError(OutputStream cos, int statusCode, String statusText) throws IOException {
			cos.write(("HTTP/1.1 " + statusCode + " " + statusText + "\r\n" + "Server: simple java server\r\n"
					+ "Content-Type: text/plain;charset=UTF-8\r\nContent-Length: 0\r\nConnection: close\r\n" + "\r\n")
							.getBytes(StandardCharsets.US_ASCII));
			cos.flush();

		}

		@Override
		public void dispatchRequest(String urlPath) throws Exception {
			internalDispatchRequest(urlPath, false);
		}

		/**
		 * Obrađuje zahtjev specificiran url putanjom. Ako je poziv direktan,
		 * odnosno ako je dana putanja putanja koju je sam klijent specificirao
		 * te ako je ta putanja hijerarhijski ispod mape private, tada se
		 * klijentu šalje poruka pogreške da specificirana datoteka ne postoji.
		 *
		 * @param urlPath
		 *            url putanja zahtjeva
		 * @param directCall
		 *            true ako je zadana url putanja putanja koju je sam
		 *            korisnik zadao
		 * @throws Exception
		 *             ako dođe do problema u obradi zahtjeva
		 */
		public void internalDispatchRequest(String urlPath, boolean directCall) throws Exception {
			if (directCall && (urlPath.startsWith("/private/") || urlPath.equals("/private"))) {
				sendError(ostream, 404, "File not found");
				return;
			}

			if (context == null) {
				context = new RequestContext(ostream, params, permParams, outputCookies, tempParams, this);
			}

			if (urlPath.startsWith("/ext/")) {
				Class<?> referenceToClass = this.getClass().getClassLoader()
						.loadClass("hr.fer.zemris.java.webserver.workers."
								+ urlPath.substring(urlPath.lastIndexOf("/") + 1).trim());
				IWebWorker worker = (IWebWorker) referenceToClass.newInstance();
				worker.processRequest(context);
				return;
			}

			if (workersMap.containsKey(urlPath)) {
				workersMap.get(urlPath).processRequest(context);
				return;
			}

			Path reqPath = Paths.get(documentRoot.toString(), urlPath);
			String extension = getFileExtension(reqPath);

			if (!(Files.exists(reqPath) && Files.isRegularFile(reqPath) && Files.isReadable(reqPath))) {
				sendError(ostream, 404, "File not found");
				return;
			}

			if (extension.equals("smscr")) {
				String documentBody = readFromDisk(reqPath);
				new SmartScriptEngine(new SmartScriptParser(documentBody).getDocumentNode(), context).execute();
			} else {

				String mimeType = mimeTypes.get(extension);
				if (mimeType == null) {
					mimeType = "application/octet-stream";
				}

				context.setMimeType(mimeType);
				context.setStatusCode(200);

				byte[] reqFileBytes = Files.readAllBytes(reqPath);

				context.addHeaderLine("Content-Length: " + reqFileBytes.length);
				context.write(reqFileBytes);
			}
		}

		/**
		 * Učitava zadanu datoteka i vraća njen sadržaj kao {@link String}.
		 *
		 * @param path
		 *            datoteka koju je potrebno pročitati
		 * @return sadržaj zadane datoteke
		 * @throws IOException
		 *             ako dođe do problema s čitanjem datoteke
		 */
		private String readFromDisk(Path path) throws IOException {
			return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
		}

	}
}