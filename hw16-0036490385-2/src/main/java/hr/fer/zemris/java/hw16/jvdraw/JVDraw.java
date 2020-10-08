package hr.fer.zemris.java.hw16.jvdraw;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import hr.fer.zemris.java.hw16.jvdraw.colormanagament.ColorInfoLabel;
import hr.fer.zemris.java.hw16.jvdraw.colormanagament.JColorArea;
import hr.fer.zemris.java.hw16.jvdraw.geomobjects.Circle;
import hr.fer.zemris.java.hw16.jvdraw.geomobjects.FilledCircle;
import hr.fer.zemris.java.hw16.jvdraw.geomobjects.GeometricalObject;
import hr.fer.zemris.java.hw16.jvdraw.geomobjects.Line;
import hr.fer.zemris.java.hw16.jvdraw.modeling.DrawingModel;
import hr.fer.zemris.java.hw16.jvdraw.modeling.DrawingObjectListModel;
import hr.fer.zemris.java.hw16.jvdraw.modeling.JVDrawingModel;
import hr.fer.zemris.java.hw16.jvdraw.mousedrawers.CircleDrawer;
import hr.fer.zemris.java.hw16.jvdraw.mousedrawers.FilledCircleDrawer;
import hr.fer.zemris.java.hw16.jvdraw.mousedrawers.LineDrawer;
import hr.fer.zemris.java.hw16.jvdraw.mousedrawers.MouseDrawer;
import hr.fer.zemris.java.hw16.jvdraw.propertiespanels.CirclePropertiesPanel;
import hr.fer.zemris.java.hw16.jvdraw.propertiespanels.FCirclePropertiesPanel;
import hr.fer.zemris.java.hw16.jvdraw.propertiespanels.LinePropertiesPanel;
import hr.fer.zemris.java.hw16.jvdraw.propertiespanels.ObjectPropertiesPanel;

/**
 * Aplikacija za crtanje geometrijskih oblika. Omogućuje crtanje linija,
 * kružnica i krugova. Omogućuje odabir boja te opcije za naknadnu izmjenu bilo
 * kojeg nacrtanog objekta. Sadrži opciju za pohranu i učitavanje jvd datoteke
 * koja pohranjuje podatke o nacrtanim objektima. Sadrži opciju exportanja u
 * jpg,png i gif format.
 *
 * @author Alen Magdić
 *
 */
public class JVDraw extends JFrame {
	private static final long serialVersionUID = 1L;
	/**
	 * Platno na koje se crta.
	 */
	private JDrawingCanvas drawingCanvas;
	/**
	 * Komponenta koja omogućuje odabir boje za crtanje.
	 */
	private JColorArea fgColorArea = new JColorArea(Color.RED);
	/**
	 * Komponenta koja omogućuje odabir pozadinske boje za crtanje.
	 */
	private JColorArea bgColorArea = new JColorArea(Color.BLACK);
	/**
	 * Grupa gumbova za crtanje geometrijskih oblika.
	 */
	private ButtonGroup geomObjectsButtons;
	/**
	 * Objekt koji generira imena nacrtanih objekata.
	 */
	private NamesGenerator objectsNamesGenerator = new NamesGenerator();
	/**
	 * Lista alata za crtanje, npr. alat za crtanje linije.
	 */
	private final List<DrawingTool> DRAWING_TOOLS;
	/**
	 * Mapira ime alata na sam objekt alata.
	 */
	private final Map<String, DrawingTool> MAP_TOOL_NAME_TO_TOOL;
	/**
	 * Mapira klasu geometrijskog objekta na njemu odgovarajući properties panel.
	 */
	private final Map<Class<? extends GeometricalObject>, ObjectPropertiesPanel> PROPERTIES_PANELS;
	/**
	 * Putanja trenutne datoteke.
	 */
	private Path currentFile;
	/**
	 * Zastavica koja označava ima li nespremljenih promjena.
	 */
	private boolean unsavedChanges;

	{
		DRAWING_TOOLS = new ArrayList<>();
		DRAWING_TOOLS.add(new DrawingTool("Line", new LineDrawer(fgColorArea)));
		DRAWING_TOOLS.add(new DrawingTool("Circle", new CircleDrawer(fgColorArea)));
		DRAWING_TOOLS.add(new DrawingTool("Filled circle", new FilledCircleDrawer(fgColorArea, bgColorArea)));

		MAP_TOOL_NAME_TO_TOOL = new HashMap<>();
		for (DrawingTool tool : DRAWING_TOOLS) {
			MAP_TOOL_NAME_TO_TOOL.put(tool.toolName, tool);
		}

		PROPERTIES_PANELS = new HashMap<>();
		PROPERTIES_PANELS.put(Circle.class, new CirclePropertiesPanel());
		PROPERTIES_PANELS.put(FilledCircle.class, new FCirclePropertiesPanel());
		PROPERTIES_PANELS.put(Line.class, new LinePropertiesPanel());
	}

	/**
	 * Metoda od koje počinje izvođenje programa.
	 *
	 * @param args
	 *            ulazni argumenti
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new JVDraw().setVisible(true));
	}

	/**
	 * Konstruktor.
	 */
	public JVDraw() {
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setSize(800, 600);
		setTitle("JVDraw");
		setLocationRelativeTo(null);

		initGui();

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exitApplication();
			}

		});
	}

	/**
	 * Inicijalizira grafičko korisničko sučelje.
	 */
	private void initGui() {
		Container cp = getContentPane();

		ColorInfoLabel bottomColorInfo = new ColorInfoLabel(fgColorArea, bgColorArea);
		cp.add(bottomColorInfo, BorderLayout.PAGE_END);

		JToolBar toolbar = new JToolBar();
		getContentPane().add(toolbar, BorderLayout.PAGE_START);
		toolbar.add(fgColorArea);
		toolbar.addSeparator();
		toolbar.add(bgColorArea);
		toolbar.addSeparator();

		geomObjectsButtons = new ButtonGroup();
		for (DrawingTool tool : DRAWING_TOOLS) {
			String geomObjectName = tool.toolName;
			JToggleButton geomObjectB = new JToggleButton(geomObjectName);
			geomObjectB.setActionCommand(geomObjectName);
			geomObjectsButtons.add(geomObjectB);
			toolbar.add(geomObjectB);
		}

		DrawingModel drawingModel = new JVDrawingModel();
		drawingCanvas = new JDrawingCanvas(drawingModel);
		setupDrawingProcedure(drawingCanvas);
		cp.add(drawingCanvas, BorderLayout.CENTER);

		createListOfObjects();

		createMenus();
	}

	/**
	 * Stvara menije.
	 */
	private void createMenus() {
		JMenuBar menubar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		menubar.add(fileMenu);

		JMenuItem openFileItem = new JMenuItem("Open");
		openFileItem.addActionListener(e -> {
			openFile();
		});

		JMenuItem saveFileItem = new JMenuItem("Save");
		saveFileItem.addActionListener(e -> {
			saveFile();
		});

		JMenuItem saveAsFileItem = new JMenuItem("Save as");
		saveAsFileItem.addActionListener(e -> {
			saveFileAs();
		});

		JMenuItem exportFileItem = new JMenuItem("Export");
		exportFileItem.addActionListener(e -> {
			exportFile();
		});

		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(e -> {
			exitApplication();
		});

		fileMenu.add(openFileItem);
		fileMenu.add(saveFileItem);
		fileMenu.add(saveAsFileItem);
		fileMenu.add(exportFileItem);
		fileMenu.add(exitItem);

		setJMenuBar(menubar);
	}

	/**
	 * Exporta crtež kao jpg,png ili gif sliku.
	 */
	private void exportFile() {
		Path exportPath = getFilePath("Export file",
				new FileNameExtensionFilter("Image Files (jpg,png,gif)", "jpg", "png", "gif"));
		if (!exportPath.toString().contains(".")) {
			JOptionPane.showMessageDialog(this, "Unable to export file without extension.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		String ext = exportPath.toString().substring(exportPath.toString().lastIndexOf(".") + 1);

		if (!ext.equals("jpg") && !ext.equals("png") && !ext.equals("gif")) {
			JOptionPane.showMessageDialog(this, "Unsupported file extension selected.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		Rectangle boundingBox = determineBoundingBox();
		BufferedImage image = new BufferedImage(boundingBox.width, boundingBox.height, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g = image.createGraphics();

		drawPicture(g, boundingBox);

		g.dispose();
		try {
			ImageIO.write(image, ext, exportPath.toFile());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Image export failed.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		JOptionPane.showMessageDialog(this, "The file has been succesfuly exported!", "Export succesful",
				JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Određuje {@link Rectangle} koji omeđuje sve nacrtane objekte.
	 *
	 * @return {@link Rectangle} koji omeđuje sve nacrtane objekte
	 */
	private Rectangle determineBoundingBox() {
		DrawingModel model = drawingCanvas.getDrawingModel();
		List<Integer> x0 = new ArrayList<>();
		List<Integer> x1 = new ArrayList<>();
		List<Integer> y0 = new ArrayList<>();
		List<Integer> y1 = new ArrayList<>();
		for (int i = 0, n = model.getSize(); i < n; i++) {
			Rectangle rect = model.getObject(i).getBoundingBox();
			x0.add(rect.x);
			x1.add(rect.x + rect.width);
			y0.add(rect.y);
			y1.add(rect.y + rect.height);
		}

		int x = Collections.min(x0);
		int y = Collections.min(y0);
		int width = Collections.max(x1) - x;
		int height = Collections.max(y1) - y;
		return new Rectangle(x, y, width, height);
	}

	/**
	 * Crta kreiranu sliku na način da bude izrezana tako da nema nepotrebnih
	 * praznih dijelova slike.
	 *
	 * @param g
	 *            {@link Graphics2D} objekt koji se koristi za crtanje
	 * @param boundingBox
	 *            {@link Rectangle} koji omeđuje sve nacrtane objekte
	 */
	private void drawPicture(Graphics2D g, Rectangle boundingBox) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, boundingBox.width, boundingBox.height);
		DrawingModel model = drawingCanvas.getDrawingModel();

		for (int i = 0, n = model.getSize(); i < n; i++) {
			GeometricalObject obj = model.getObject(i);
			obj.move(-boundingBox.x, -boundingBox.y);
			obj.draw(g);
			obj.move(boundingBox.x, boundingBox.y);
		}
	}

	/**
	 * Traži od korisnika da definira gdje i pod kojim imenom želi spremiti datoteku
	 * te ju sprema.
	 */
	private void saveFileAs() {
		currentFile = getFilePathForSaving();
		if (currentFile == null) {
			return;
		}
		saveFile();
	}

	/**
	 * Sprema datoteku na disk.
	 */
	private void saveFile() {
		if (currentFile == null) {
			saveFileAs();
			return;
		}

		try {
			Files.write(currentFile, PictureIO.getPictureContentAsText(drawingCanvas.getDrawingModel()).getBytes());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Save failed. It is not clear what is the current content of the file.",
					"Saving faileds", JOptionPane.ERROR_MESSAGE);
		}

		JOptionPane.showMessageDialog(this, "The file has been saved succesfuly!", "Saving succesful",
				JOptionPane.INFORMATION_MESSAGE);
		unsavedChanges = false;
	}

	/**
	 * Traži od korisnika da odabere datoteku koju želi otvoriti te ju otvara.
	 * Očekuje se unos .jvd datoteke, ali metoda će pokušati pročitati bilo koju
	 * datoteku koju korisnik zada.
	 */
	private void openFile() {
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("Open file");
		fc.setFileFilter(new FileNameExtensionFilter("JVDraw Files", "jvd"));

		if (fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
			return;
		}

		Path filePath = fc.getSelectedFile().toPath();

		if (!Files.isReadable(filePath)) {
			JOptionPane.showMessageDialog(this, "The selected file is not readable!", "File not readable",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		List<String> text = null;
		try {
			text = Files.readAllLines(filePath);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "There has been a problem loading the file.", "Loading failed",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		boolean importSuccess = PictureIO.importTextContent(drawingCanvas.getDrawingModel(), text,
				objectsNamesGenerator);
		if (!importSuccess) {
			JOptionPane.showMessageDialog(this,
					"Unable to load from the selected file because the content of the file is not correct.",
					"Loading failed", JOptionPane.ERROR_MESSAGE);
			return;
		}

		currentFile = filePath;

	}

	/**
	 * Traži od korisnika odabir gdje i pod kojim imenom spremiti datoteku.
	 *
	 * @return odabrana putanja za spremanje
	 */
	private Path getFilePathForSaving() {
		return getFilePath("Save file", new FileNameExtensionFilter("JVDraw Files", "jvd"));
	}

	/**
	 * Traži od korisnika odabir putanje.
	 *
	 * @param dialogTitle
	 *            naslov dijaloga za odabir putanje
	 * @param filter
	 *            filter koji se postavlja na dijalog za odabir putanje
	 * @return odabrana putanja
	 */
	private Path getFilePath(String dialogTitle, FileFilter filter) {
		while (true) {
			JFileChooser fc = new JFileChooser();
			fc.setDialogTitle(dialogTitle);
			fc.setFileFilter(filter);
			if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
				return null;
			}

			if (Files.exists(fc.getSelectedFile().toPath())) {
				int ans = JOptionPane.showConfirmDialog(this,
						"There already is a file with the selected path. Do you want to overwrite?", "Warning",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (ans == JOptionPane.YES_OPTION) {
					return fc.getSelectedFile().toPath();
				} else {
					continue;
				}
			}
			return fc.getSelectedFile().toPath();
		}
	}

	/**
	 * Stvara {@link JList} komponentu koja je zadužena za izlistavanja svih
	 * kreiranih geometrijskih objekata.
	 */
	private void createListOfObjects() {
		JList<GeometricalObject> geomObjectsList = new JList<>(
				new DrawingObjectListModel(drawingCanvas.getDrawingModel()));
		getContentPane().add(new JScrollPane(geomObjectsList), BorderLayout.LINE_END);
		geomObjectsList.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					openPropertyDialog(geomObjectsList.getSelectedValue());
				}

			}
		});
		geomObjectsList.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_DELETE) {
					JVDraw.this.drawingCanvas.getDrawingModel().remove(geomObjectsList.getSelectedValue());
				}
			}
		});

	}

	/**
	 * Otvara dijalog za izmjenu svojstava zadanog objekta
	 *
	 * @param object
	 *            objekt čija se svojstva žele izmijeniti
	 */
	private void openPropertyDialog(GeometricalObject object) {
		ObjectPropertiesPanel panel = PROPERTIES_PANELS.get(object.getClass());
		panel.setObject(object.getCopyOfObject());
		int option = JOptionPane.showConfirmDialog(this, panel, "Properties", JOptionPane.OK_CANCEL_OPTION);
		if (option == JOptionPane.OK_OPTION) {
			if (!object.equals(panel.getObject())) {
				unsavedChanges = true;
			}
			object.copyValuesFromObject(panel.getObject());
		}
		repaint();
	}

	/**
	 * Definira proceduru crtanja mišem.
	 *
	 * @param drawCanvas
	 *            platno na koje se crta
	 */
	private void setupDrawingProcedure(JDrawingCanvas drawCanvas) {
		drawCanvas.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				ButtonModel selectedButtonModel = geomObjectsButtons.getSelection();
				if (selectedButtonModel == null) {
					return;
				}

				MouseDrawer drawer = MAP_TOOL_NAME_TO_TOOL.get(selectedButtonModel.getActionCommand()).drawer;
				drawer.mouseClicked(e.getPoint());

				if (drawer.isDone()) {
					GeometricalObject createdObj = drawer.getCreatedObject();
					createdObj.setName(JVDraw.this.objectsNamesGenerator.generateNameFor(createdObj));
					JVDraw.this.drawingCanvas.getDrawingModel().add(createdObj);
					drawer.restart();
					unsavedChanges = true;
				}
			}
		});

		drawCanvas.addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseMoved(MouseEvent e) {
				ButtonModel selectedButtonModel = geomObjectsButtons.getSelection();
				if (selectedButtonModel == null) {
					return;
				}

				MouseDrawer drawer = MAP_TOOL_NAME_TO_TOOL.get(selectedButtonModel.getActionCommand()).drawer;
				JVDraw.this.drawingCanvas.setTempObject(drawer.getTempVersion(e.getPoint()));
			}

		});

	}

	/**
	 * Provjerava ima li nespremljenih izmjena te ako ima, nudi mogućnost spremanja
	 * izmjena prije izlaska. Ako nema nespremljenih izmjena, izlazi iz aplikacije.
	 */
	private void exitApplication() {
		if (unsavedChanges) {
			int ans = JOptionPane.showConfirmDialog(this, "Do you want to save file before closing?",
					"Save before closing", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (ans == JOptionPane.CANCEL_OPTION) {
				return;
			} else if (ans == JOptionPane.YES_OPTION) {
				saveFile();
				return;
			} else {
				dispose();
			}
		} else {
			dispose();
		}
	}

	/**
	 * Klasa koja pamti sva imena koja je generirala te na temelju toga stvara ime
	 * za svaki novi geometrijski objekt.
	 *
	 * @author Alen Magdić
	 *
	 */
	public static class NamesGenerator {
		/**
		 * Pohranjuje koliko je puta generirano ime za određeni tip objekta.
		 */
		private Map<String, Integer> typesToLastIndex = new HashMap<>();

		/**
		 * Generira ime zadanog objekta.
		 *
		 * @param object
		 *            objekt kojeg treba imenovati
		 * @return ime zadanog objekta
		 */
		public String generateNameFor(GeometricalObject object) {
			String typeName = object.getTypeName();
			if (typesToLastIndex.get(typeName) == null) {
				typesToLastIndex.put(typeName, 1);
			} else {
				typesToLastIndex.put(typeName, typesToLastIndex.get(typeName) + 1);
			}
			return typeName + " " + typesToLastIndex.get(typeName);
		}
	}

	/**
	 * Klasa koja predstavlja alat za crtanje nekog geometrijskog objekta, npr. za
	 * crtanje linije.
	 *
	 * @author Alen Magdić
	 *
	 */
	private static class DrawingTool {
		/**
		 * Ime alata.
		 *
		 */
		private String toolName;
		/**
		 * Objekt koji se koristi za crtanje objekta mišem.
		 */
		private MouseDrawer drawer;

		/**
		 * Konstruktor.
		 *
		 * @param toolName
		 *            ime alata
		 * @param drawer
		 *            objekt koji se koristi za crtanje objekta mišem
		 */
		public DrawingTool(String toolName, MouseDrawer drawer) {
			super();
			this.toolName = toolName;
			this.drawer = drawer;
		}

	}
}
