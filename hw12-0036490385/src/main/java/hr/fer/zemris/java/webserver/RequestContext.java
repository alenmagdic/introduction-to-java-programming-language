package hr.fer.zemris.java.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Kontekst zahtjeva. Sadrži podatke kao što su statusni kod, statusni tekst,
 * {@link OutputStream} na koji vrši ispis odgovora na zahtjev, parametre,
 * privremene parametre, trajne parametre, kolačiće.
 *
 * @author Alen Magdić
 *
 */
public class RequestContext {
	/**
	 * {@link OutputStream} na koji se ispisuje odgovor zahtjeva.
	 */
	private OutputStream outputStream;
	/**
	 * Kodna stranica za ispis.
	 */
	private Charset charset;
	/**
	 * Encoding - defaultni UTF-8.
	 */
	private String encoding;
	/**
	 * Statusni kod.
	 */
	private int statusCode;
	/**
	 * Statusni tekst.
	 */
	private String statusText;
	/**
	 * Mime tip.
	 */
	private String mimeType;
	/**
	 * Parametri.
	 */
	private Map<String, String> parameters;
	/**
	 * Privremeni parametri.
	 */
	private Map<String, String> temporaryParameters;
	/**
	 * Trajni parametri.
	 */
	private Map<String, String> persistentParameters;
	/**
	 * Kolačići.
	 */
	private List<RCCookie> outputCookies;
	/**
	 * Zastavica koja označava je li zaglavlje generirano.
	 */
	private boolean headerGenerated;
	/**
	 * Dodatne linije zaglavlja, dodane pozivima metode addHeaderLine.
	 */
	private List<String> additionalHeaderLines;
	/**
	 * Dispatcher koji se koristi za obradu zahtjeva.
	 */
	private IDispatcher dispatcher;

	/**
	 * Konstruktor.
	 *
	 * @param outputStream
	 *            {@link OutputStream} na koji se ispisuje odgovor zahtjeva
	 * @param parameters
	 *            parametri
	 * @param persistentParameters
	 *            trajni parametri
	 * @param outputCookies
	 *            kolačići
	 * @param temporaryParameters
	 *            privremeni parametri
	 * @param dispatcher
	 *            dispatcher koji se koristi za obradu zahtjeva
	 */
	public RequestContext(OutputStream outputStream, Map<String, String> parameters,
			Map<String, String> persistentParameters, List<RCCookie> outputCookies,
			Map<String, String> temporaryParameters, IDispatcher dispatcher) {
		if (outputStream == null) {
			throw new IllegalArgumentException("Output stream can not be null.");
		}

		this.outputStream = outputStream;
		this.parameters = parameters;
		this.persistentParameters = persistentParameters;
		this.outputCookies = outputCookies;
		this.temporaryParameters = temporaryParameters;
		this.dispatcher = dispatcher;

		encoding = "UTF-8";
		statusCode = 200;
		statusText = "OK";
		mimeType = "text/html";
		additionalHeaderLines = new ArrayList<>();
	}

	/**
	 * Konstruktor.
	 *
	 * @param outputStream
	 *            {@link OutputStream} na koji se ispisuje odgovor zahtjeva
	 * @param parameters
	 *            parametri
	 * @param persistentParameters
	 *            trajni parametri
	 * @param outputCookies
	 *            kolačići
	 */
	public RequestContext(OutputStream outputStream, Map<String, String> parameters,
			Map<String, String> persistentParameters, List<RCCookie> outputCookies) {
		this(outputStream, parameters, persistentParameters, outputCookies, new HashMap<>(), null);
	}

	/**
	 * Dodaje dodatnu liniju u zaglavlje odgovora.
	 *
	 * @param line
	 *            dodatna linija
	 */
	public void addHeaderLine(String line) {
		additionalHeaderLines.add(line);
	}

	/**
	 * Vraća dispatchera.
	 *
	 * @return dispatcher
	 */
	public IDispatcher getDispatcher() {
		return dispatcher;
	}

	/**
	 * Vraća vrijednost zadanog parametra.
	 *
	 * @param name
	 *            ime parametra
	 * @return vrijednost zadanog parametra
	 */
	public String getParameter(String name) {
		return parameters.get(name);
	}

	/**
	 * Vraća set imena parametara.
	 *
	 * @return set imena parametara
	 */
	public Set<String> getParameterNames() {
		return Collections.unmodifiableSet(parameters.keySet());
	}

	/**
	 * Dohvaća vrijednost trajnog parametra sa zadanim imenom.
	 *
	 * @param name
	 *            ime trajnog parametra
	 * @return vrijednost trajnog parametra sa zadanim imenom
	 */
	public String getPersistentParameter(String name) {
		return persistentParameters.get(name);
	}

	/**
	 * Vraća set imena trajnih parametara.
	 *
	 * @return set imena trajnih parametara
	 */
	public Set<String> getPersistentParameterNames() {
		return Collections.unmodifiableSet(persistentParameters.keySet());
	}

	/**
	 * Postavlja vrijednost trajnog parametra sa zadanim imenom na zadanu
	 * vrijednost.
	 *
	 * @param name
	 *            ime trajnog parametra
	 * @param value
	 *            vrijednost zadanog parametra
	 */
	public void setPersistentParameter(String name, String value) {
		persistentParameters.put(name, value);
	}

	/**
	 * Briše trajni parametar sa zadanim imenom.
	 *
	 * @param name
	 *            ime trajnog parametra
	 */
	public void removePersistentParameter(String name) {
		persistentParameters.remove(name);
	}

	/**
	 * Vraća vrijednost privremenog parametra sa zadanim imenom.
	 *
	 * @param name
	 *            ime privremenog parametra
	 * @return vrijednost privremenog parametra sa zadanim imenom
	 */
	public String getTemporaryParameter(String name) {
		return temporaryParameters.get(name);
	}

	/**
	 * Vraća set imena privremenih parametara.
	 *
	 * @return set imena privremenih parametara
	 */
	public Set<String> getTemporaryParameterNames() {
		return Collections.unmodifiableSet(temporaryParameters.keySet());
	}

	/**
	 * Postavlja vrijednost privremenog parametra sa zadanim imenom na zadanu
	 * vrijednost.
	 *
	 * @param name
	 *            ime parametra
	 * @param value
	 *            vrijednost parametra
	 */
	public void setTemporaryParameter(String name, String value) {
		temporaryParameters.put(name, value);
	}

	/**
	 * Briše privremeni parametar sa zadanim imenom.
	 *
	 * @param name
	 *            ime privremenog parametra
	 */
	public void removeTemporaryParameter(String name) {
		temporaryParameters.remove(name);
	}

	/**
	 * Postavlja statusni kod na zadanu vrijednost.
	 *
	 * @param statusCode
	 *            novi statusni kod
	 */
	public void setStatusCode(int statusCode) {
		if (headerGenerated) {
			throw new RuntimeException("Can not set status code after the header has been generated.");
		}
		this.statusCode = statusCode;
	}

	/**
	 * Postavlja statusni tekst.
	 *
	 * @param statusText
	 *            statusni tekst
	 */
	public void setStatusText(String statusText) {
		if (headerGenerated) {
			throw new RuntimeException("Can not set status text after the header has been generated.");
		}
		this.statusText = statusText;
	}

	/**
	 * Postavlja mime tip.
	 *
	 * @param mimeType
	 *            mime tip
	 */
	public void setMimeType(String mimeType) {
		if (headerGenerated) {
			throw new RuntimeException("Can not set mime type after the header has been generated.");
		}
		this.mimeType = mimeType;
	}

	/**
	 * Postavlja encoding.
	 *
	 * @param encoding
	 *            encoding
	 */
	public void setEncoding(String encoding) {
		if (headerGenerated) {
			throw new RuntimeException("Can not set encoding after the header has been generated.");
		}
		this.encoding = encoding;
	}

	/**
	 * Zapisuje zadano polje bajtova na {@link OutputStream} zadan u
	 * konstruktoru.
	 *
	 * @param data
	 *            polje bajtova koji će biti zapisani
	 * @throws IOException
	 *             ukoliko dođe do problema sa zapisivanjem
	 */
	public void write(byte[] data) throws IOException {
		if (!headerGenerated) {
			generateAndWriteHeader();
		}
		outputStream.write(data);
	}

	/**
	 * Zapisuje zadani tekst na {@link OutputStream} zadan u konstruktoru.
	 *
	 * @param text
	 *            tekst koji će biti zapisan
	 * @throws IOException
	 *             ukoliko dođe do problema sa zapisivanjem
	 */
	public void write(String text) throws IOException {
		if (!headerGenerated) {
			generateAndWriteHeader();
		}
		write(text.getBytes(charset));
	}

	/**
	 * Generira i zapisuje zaglavlje odgovora na zahtjev.
	 *
	 * @throws IOException
	 *             ukoliko dođe do problema sa zapisivanjem
	 */
	private void generateAndWriteHeader() throws IOException {
		charset = Charset.forName(encoding);

		StringBuilder headerB = new StringBuilder();
		headerB.append("HTTP/1.1 " + statusCode + " " + statusText + "\r\n");

		if (mimeType.startsWith("text/")) {
			headerB.append("Content-Type: " + mimeType + "; charset=" + encoding + "\r\n");
		} else {
			headerB.append("Content-Type: " + mimeType + "\r\n");
		}

		for (String line : additionalHeaderLines) {
			headerB.append(line + "\r\n");
		}

		for (RCCookie cookie : outputCookies) {
			headerB.append("Set-Cookie: " + cookie.name + "=\"" + cookie.value + "\"");
			if (cookie.domain != null) {
				headerB.append("; Domain=" + cookie.domain);
			}
			if (cookie.path != null) {
				headerB.append("; Path=" + cookie.path);
			}
			if (cookie.maxAge != null) {
				headerB.append("; Max-Age=" + cookie.maxAge);
			}
			if (cookie.isHttpOnly) {
				headerB.append("; HttpOnly");
			}
			headerB.append("\r\n");
		}
		headerB.append("\r\n");
		outputStream.write(headerB.toString().getBytes(StandardCharsets.ISO_8859_1));
		headerGenerated = true;
	}

	/**
	 * Klasa koja predstavlja jedan internetski kolačić.
	 *
	 * @author Alen Magdić
	 *
	 */
	public static class RCCookie {
		/** Ime kolačića. **/
		private String name;
		/** Vrijednost **/
		private String value;
		/**
		 * Adresa za koju vrijedi ovaj kolačić.
		 */
		private String domain;
		/**
		 * Putanja za koju vrijedi ovaj kolačić.
		 */
		private String path;
		/**
		 * Maksimalna starost.
		 */
		private Integer maxAge;
		/**
		 * Zastavica koja predstavlja je li kolačić HttpOnly.
		 */
		private boolean isHttpOnly;

		/**
		 * Konstruktor.
		 *
		 * @param name
		 *            ime kolačića
		 * @param value
		 *            vrijednost
		 * @param maxAge
		 *            maksimalna starost
		 * @param domain
		 *            adresa za koju vrijedi kolačić
		 * @param path
		 *            putanja za koju vrijedi kolačić
		 */
		public RCCookie(String name, String value, Integer maxAge, String domain, String path) {
			super();
			this.name = name;
			this.value = value;
			this.domain = domain;
			this.path = path;
			this.maxAge = maxAge;
		}

		/**
		 * Postavlja kolačić kao http-only ili kao ne-http-only.
		 *
		 * @param value
		 *            nova vrijednost zastavice http-only
		 */
		public void setHttpOnly(boolean value) {
			isHttpOnly = value;
		}

		/**
		 * Dohvaća ime kolačića.
		 *
		 * @return ime kolačića
		 */
		public String getName() {
			return name;
		}

		/**
		 * Dohvaća vrijednost kolačića.
		 *
		 * @return vrijednost kolačića
		 */
		public String getValue() {
			return value;
		}

		/**
		 * Dohvaća adresu za koju vrijedi kolačić.
		 *
		 * @return adresa za koju vrijedi kolačić
		 */
		public String getDomain() {
			return domain;
		}

		/**
		 * Dohvaća putanju za koju vrijedi kolačić.
		 *
		 * @return putanja za koju vrijedi kolačić
		 */
		public String getPath() {
			return path;
		}

		/**
		 * Dohvaća maksimalnu starost kolačića.
		 *
		 * @return maksimalna starost kolačića
		 */
		public Integer getMaxAge() {
			return maxAge;
		}

	}

	/**
	 * Dodaje zadani kolačić.
	 *
	 * @param rcCookie
	 *            kolačić
	 */
	public void addRCCookie(RCCookie rcCookie) {
		outputCookies.add(rcCookie);
	}
}
