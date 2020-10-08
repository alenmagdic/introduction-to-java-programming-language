package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Collator;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SortOrder;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;

import hr.fer.zemris.java.hw11.jnotepadpp.local.FormLocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LJToolbar;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizableAction;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;

/**
 * This is an application for simple text editing. It supports only plain text
 * documents, so no font styles, sizes and other decorations are supported. It's
 * a multi-language app. There are three languages supported: English, German
 * and Croatian.
 *
 * The application enables having multiple documents opened at the same time,
 * i.e. it contains a tab pane that enables switching tabs where each tab
 * represents single document.
 *
 * The application offers tools like sorting selected lines, removing duplicate
 * lines, case switch, etc.
 *
 * @author Alen Magdić
 *
 */
public class JNotepadPP extends JFrame {
	private static final long serialVersionUID = 1L;
	/** Path of a new text document that has not been saved yet. **/
	private static final Path UNTITLED_PATH = Paths.get("");
	/** Icon representing a file that has been modified after last save. **/
	private ImageIcon modifiedIcon;
	/** Icon representing a file that has not been modified after last save. **/
	private ImageIcon unmodifiedIcon;
	/**
	 * A pane containing tabs where each tab is represented by {@link TabPanel}
	 * class.
	 **/
	private JTabbedPane tabPane;
	/**
	 * Status bar that contains info about document length, caret position and
	 * current time.
	 **/
	private StatusBar statusBar;
	/**
	 * A localization provider used to implement the localization of this
	 * application.
	 */
	private FormLocalizationProvider flp = new FormLocalizationProvider(LocalizationProvider.getInstance(), this);

	/**
	 * The starting point of the application.
	 *
	 * @param args
	 *            command line arguments
	 */
	public static void main(String[] args) {
		Path fileToOpen = args.length == 1 ? Paths.get(args[0]) : null;

		SwingUtilities.invokeLater(() -> new JNotepadPP(fileToOpen).setVisible(true));
	}

	/**
	 * Constructor.
	 */
	public JNotepadPP(Path fileToOpen) {
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setSize(800, 600);
		setTitle("JNotepad++");
		setLocationRelativeTo(null);

		initGUI();

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exitApplication();
			}

		});

		if (fileToOpen != null) {
			try {
				String fileContent = new String(Files.readAllBytes(fileToOpen), StandardCharsets.UTF_8);
				addDocumentToTabPane(fileToOpen, fileContent);
			} catch (IOException e1) {
			}
		}

	}

	/**
	 * Initializes the graphical user interface.
	 */
	private void initGUI() {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());

		tabPane = new JTabbedPane();
		tabPane.addChangeListener(e -> {
			TabPanel tab = getActiveTab();
			if (tab == null) {
				setActionsEnabled(false);
				setSelectionToolsEnabled(false);
				setTitle("JNotepad++");
			} else {
				setActionsEnabled(true);
				String path = tab.filePath == UNTITLED_PATH ? flp.getString("defFileName") : tab.filePath.toString();
				setTitle(path + " - JNotepad++");
				tab.add(statusBar, BorderLayout.PAGE_END);
				saveDocumentAction.setEnabled(tab.modified);
				statusBar.update();
				tab.textArea.requestFocus();
			}
		});
		setActionsEnabled(false);
		setSelectionToolsEnabled(false);
		// addDocumentToTabPane(UNTITLED_PATH, ""); //can optionally be included
		cp.add(tabPane, BorderLayout.CENTER);

		statusBar = new StatusBar();

		modifiedIcon = getIcon("icons\\redDisk.png");
		unmodifiedIcon = getIcon("icons\\greenDisk.png");
		refreshIcons();

		setAccelerators();
		createMenus();
		createToolbars();

		// status bar labels can not be implemented using LJLabel because they
		// need to get info about caret and currently active text in order to
		// update themselves
		flp.addLocalizationListener(() -> {
			statusBar.update();

			// refreshes default file names
			TabPanel actTab = getActiveTab();
			for (int i = 0, n = tabPane.getComponentCount(); i < n; i++) {
				TabPanel tab = (TabPanel) tabPane.getComponentAt(i);
				if (tab.filePath == UNTITLED_PATH) {
					tabPane.setTitleAt(i, flp.getString("defFileName"));
				}
				if (tab == actTab) {
					String path = tab.filePath == UNTITLED_PATH ? flp.getString("defFileName")
							: tab.filePath.toString();
					setTitle(path + " - JNotepad++");
				}
			}
		});
	}

	/**
	 * Enables or disables actions that can be executed only when there is at least
	 * one file opened, excluding actions that can be executed only when there is a
	 * part of text selected in the currently active tab.
	 *
	 * @param state
	 *            new state that is to be set to specific actions - true enables
	 *            them, false disables them
	 */
	private void setActionsEnabled(boolean state) {
		saveDocumentAction.setEnabled(state);
		saveAsDocumentAction.setEnabled(state);
		closeFileAction.setEnabled(state);
		cutFileAction.setEnabled(state);
		copyFileAction.setEnabled(state);
		pasteFileAction.setEnabled(state);
		statsAction.setEnabled(state);
	}

	/**
	 * Enables or disables actions that can be executed only when there is a part of
	 * text selected in the currently active tab.
	 *
	 * @param state
	 *            new state that to be set to specific actions - true enables them,
	 *            false disables them
	 */
	private void setSelectionToolsEnabled(boolean state) {
		invertCaseAction.setEnabled(state);
		toUppercaseAction.setEnabled(state);
		toLowercaseAction.setEnabled(state);
		deleteSelectedPartAction.setEnabled(state);
		ascendingSortAction.setEnabled(state);
		descendingSortAction.setEnabled(state);
		uniqueAction.setEnabled(state);
	}

	/**
	 * An encapsulation of a tab. It holds information about a tab like path to file
	 * that it represents and text area that is used to edit that file.
	 *
	 * @author Alen Magdić
	 *
	 */
	private class TabPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		/** Text area of a tab **/
		private JTextArea textArea;
		/** Path of file that is represented by this tab. **/
		private Path filePath;
		/**
		 * Representing that the file represented by this tab has been modified after
		 * last save
		 **/
		private boolean modified;

		/**
		 * Constructor.
		 *
		 * @param filePath
		 *            path to the file represented by this tab
		 * @param fileContent
		 *            content of the file represented by this tab
		 * @param statusBar
		 *            status bar that is to be put at the bottom of this tab
		 */
		public TabPanel(Path filePath, String fileContent, StatusBar statusBar) {
			this.filePath = filePath;

			textArea = new JTextArea();
			textArea.setText(fileContent);
			textArea.getDocument().addDocumentListener(new DocumentListener() {

				public void doChanges() {
					if (modified == false) {
						modified = true;
						refreshIcons();
					}
					modified = true;
					saveDocumentAction.setEnabled(true);
				}

				@Override
				public void removeUpdate(DocumentEvent e) {
					doChanges();
				}

				@Override
				public void insertUpdate(DocumentEvent e) {
					doChanges();
				}

				@Override
				public void changedUpdate(DocumentEvent e) {
					doChanges();
				}
			});
			textArea.addCaretListener(e -> {
				statusBar.update();

				if (textArea.getCaretPosition() - textArea.getCaret().getMark() != 0) {
					setSelectionToolsEnabled(true);
				} else {
					setSelectionToolsEnabled(false);
				}
			});

			setLayout(new BorderLayout());
			add(new JScrollPane(textArea), BorderLayout.CENTER);
			add(statusBar, BorderLayout.PAGE_END);

			setModified(false);
		}

		/**
		 * Sets the state of the 'modified' flag to the specified state and does
		 * neccessary updates on the application (like enabling/disabling document
		 * saving).
		 *
		 * @param state
		 *            new state
		 */
		public void setModified(boolean state) {
			modified = state;
			saveDocumentAction.setEnabled(state);
			refreshIcons();
		}

	}

	/**
	 * Gets the currently active tab.
	 *
	 * @return currently active tab
	 */
	private TabPanel getActiveTab() {
		if (tabPane.getSelectedIndex() == -1) {
			return null;
		}
		return (TabPanel) tabPane.getComponentAt(tabPane.getSelectedIndex());
	}

	/**
	 * Refreshes tab icons.
	 */
	public void refreshIcons() {
		for (int i = 0, n = tabPane.getTabCount(); i < n; i++) {
			TabPanel tab = (TabPanel) tabPane.getComponentAt(i);
			if (tab.modified) {
				tabPane.setIconAt(i, modifiedIcon);
			} else {
				tabPane.setIconAt(i, unmodifiedIcon);
			}
		}
	}

	/**
	 * Creates a new tab representing the specified file. If UNTITLED_PATH is put as
	 * the first argument, the new tab will be named using a default name.
	 *
	 * @param filePath
	 *            path to a file that is to be opened in a new tab
	 * @param fileContent
	 *            content of the specified file
	 */
	private void addDocumentToTabPane(Path filePath, String fileContent) {
		String name = filePath == UNTITLED_PATH ? flp.getString("defFileName") : filePath.getFileName().toString();
		TabPanel newTab = new TabPanel(filePath, fileContent, statusBar);
		if (filePath == UNTITLED_PATH) {
			newTab.setModified(true);
		}

		tabPane.add(name, newTab);
		tabPane.setSelectedComponent(newTab);
		newTab.textArea.requestFocus();
		tabPane.setToolTipTextAt(tabPane.getSelectedIndex(),
				filePath == UNTITLED_PATH ? flp.getString("defFileName") : filePath.toString());
		refreshIcons();
	}

	/**
	 * An encapsulation of a status bar. It contains information about the length of
	 * currently active document, length of selected text and position of the caret.
	 *
	 * @author Alen Magdić
	 *
	 */
	private class StatusBar extends JPanel {
		private static final long serialVersionUID = 1L;
		/**
		 * Label containing information about length of the currently active document.
		 **/
		private JLabel lengthLB;
		/**
		 * Label containing the following information about the currently active
		 * document: caret line, caret column, selection length.
		 **/
		private JLabel infoLB;
		/** Label containing current date and date. **/
		private JLabel timeLB;
		/** Part of the status bar aligned to the left. **/
		private JPanel leftPanel;
		/** A flag used to stop a thread that updates the time. **/
		private volatile boolean stopRequested;

		/**
		 * Constructor.
		 */
		public StatusBar() {
			lengthLB = new JLabel();
			infoLB = new JLabel();
			timeLB = new JLabel();

			leftPanel = new JPanel(new GridLayout(1, 2));
			leftPanel.add(lengthLB);
			leftPanel.add(infoLB);

			setLayout(new BorderLayout());
			add(leftPanel, BorderLayout.LINE_START);
			add(timeLB, BorderLayout.LINE_END);

			startClock();
		}

		/** Updates the status bar information. **/
		public void update() {
			TabPanel tab = getActiveTab();
			if (tab == null) {
				return;
			}

			Caret caret = tab.textArea.getCaret();
			String text = tab.textArea.getText();

			lengthLB.setText(flp.getString("length") + text.length());

			int ln = 1;
			int col = 1;
			for (int i = 0, n = caret.getDot(); i < n; i++) {
				char c = text.charAt(i);
				col++;
				if (c == '\n') {
					ln++;
					col = 1;
				}
			}

			int selLen = Math.abs(caret.getDot() - caret.getMark());

			infoLB.setText(String.format(flp.getString("stBarInfo"), ln, col, selLen));
		}

		/**
		 * Starts the clock.
		 */
		public void startClock() {
			Thread clockThread = new Thread(() -> {
				while (!stopRequested) {
					LocalDateTime time = LocalDateTime.now();
					DateTimeFormatter form = DateTimeFormatter.ofPattern("yyyy/MM/dd kk:mm:ss");

					SwingUtilities.invokeLater(() -> timeLB.setText(time.format(form)));
					try {
						Thread.sleep(1000);
					} catch (InterruptedException ignorable) {
					}
				}
			});
			clockThread.start();
		}

		/**
		 * Stops the clock.
		 *
		 */
		public void stopClock() {
			stopRequested = true;
		}
	}

	/**
	 * Loads the specified icon from memory.
	 *
	 * @param iconName
	 *            name or path to an icon that is to be loaded
	 * @return {@link ImageIcon} object created by loading the specified icon
	 */
	private ImageIcon getIcon(String iconName) {
		InputStream is = this.getClass().getResourceAsStream(iconName);
		if (is == null) {
			return null;
		}
		List<Byte> bytes = new ArrayList<>();

		while (true) {
			int b;
			try {
				b = is.read();
			} catch (IOException e) {
				return null;
			}
			if (b == -1) {
				break;
			}
			bytes.add((byte) b);
		}

		try {
			is.close();
		} catch (IOException e) {
			return null;
		}

		byte[] bytesArr = new byte[bytes.size()];
		for (int i = 0; i < bytesArr.length; i++) {
			bytesArr[i] = bytes.get(i);
		}
		return new ImageIcon(bytesArr);
	}

	/**
	 * An action that opens a new blank document.
	 */
	private Action newDocumentAction = new LocalizableAction("new", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			addDocumentToTabPane(UNTITLED_PATH, "");
		}
	};

	/**
	 * An action that asks user to choose a text document and opens it in a new tab.
	 */
	private Action openDocumentAction = new LocalizableAction("open", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser();
			fc.setDialogTitle(flp.getString("open_title"));

			if (fc.showOpenDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION) {
				return;
			}

			Path filePath = fc.getSelectedFile().toPath();

			if (!Files.isReadable(filePath)) {
				JOptionPane.showMessageDialog(JNotepadPP.this,
						String.format(flp.getString("fileNotReadable"), filePath.toString()), flp.getString("error"),
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			String text = null;
			try {
				text = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(JNotepadPP.this,
						String.format(flp.getString("errorLoadingFile"), filePath.toString()), flp.getString("error"),
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			addDocumentToTabPane(filePath, text);
		}
	};

	/**
	 * An action that saves the currently active document.
	 */
	private Action saveDocumentAction = new LocalizableAction("save", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			TabPanel tab = getActiveTab();

			if (tab.filePath == UNTITLED_PATH) {
				Path path = getFilePathForSaving();
				if (path == null) {
					return;
				}
				tab.filePath = path;
				tabPane.setTitleAt(tabPane.getSelectedIndex(), path.getFileName().toString());

			}

			saveActiveTabFile();
		}

	};

	/**
	 * An action that saves the currently active text document using a new name
	 * determined by user.
	 */
	private Action saveAsDocumentAction = new LocalizableAction("saveAs", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			TabPanel tab = getActiveTab();

			Path path = getFilePathForSaving();
			if (path == null) {
				return;
			}
			tab.filePath = path;
			tabPane.setTitleAt(tabPane.getSelectedIndex(), path.getFileName().toString());

			saveActiveTabFile();

		}
	};

	/**
	 * An action that opens a information message containing statistics of the
	 * currently active document.
	 */
	private Action statsAction = new LocalizableAction("stats", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			String text = getActiveTab().textArea.getText();

			String message = String.format(flp.getString("stats_msg"), text.length(), countNonBlankChars(text),
					countLines(text));
			JOptionPane.showConfirmDialog(JNotepadPP.this, message, flp.getString("stats"), JOptionPane.CLOSED_OPTION,
					JOptionPane.INFORMATION_MESSAGE);

		}

		/**
		 * Returns the number of lines contained in the specified string.
		 *
		 * @param text
		 *            a string containing text
		 * @return number of lines contained in the specified string
		 */
		private int countLines(String text) {
			if (text.length() == 0) {
				return 0;
			}
			int n = 1;
			for (char c : text.toCharArray()) {
				if (c == '\n') {
					n++;
				}
			}
			return n;
		}

		/**
		 * Counts number of characters in the specified text excluding space and
		 * characters '\n','\t','\r'.
		 *
		 * @param text
		 *            a string containing some text
		 * @return number of non blank characters in the specified text
		 */
		private int countNonBlankChars(String text) {
			int n = 0;
			for (char c : text.toCharArray()) {
				if (c != '\n' && c != ' ' && c != '\t' && c != '\r') {
					n++;
				}
			}
			return n;
		}
	};

	/**
	 * An action that closes the currently active document.
	 */
	private Action closeFileAction = new LocalizableAction("close", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			TabPanel tab = getActiveTab();
			closeFile(tab);
		}

	};

	/**
	 * Checks if there are any unsaved documents and then exits the application. If
	 * there are any unsaved documents, it asks user to decide to save or not to
	 * save that document, or to cancel the exiting of application.
	 */
	private void exitApplication() {
		TabPanel tab = getActiveTab();
		while (tab != null) {
			if (closeFile(tab) == false) {
				return;
			}
			tab = getActiveTab();
		}
		statusBar.stopClock();
		dispose();
	}

	/**
	 * An action that exits the application. It checks if there are any unsaved
	 * documents and then exits the application. If there are any unsaved documents,
	 * it asks user to decide to save or not to save that document, or to cancel the
	 * exiting of application.
	 */
	private Action exitAction = new LocalizableAction("exit", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			exitApplication();
		}
	};

	/**
	 * An action that deletes the selected part of the currently active document.
	 */
	private Action deleteSelectedPartAction = new LocalizableAction("delete", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			changeSelectedText(t -> "");

		}
	};

	/**
	 * An action that inverts case of the selected part of the currently active
	 * document.
	 */
	private Action invertCaseAction = new LocalizableAction("invCase", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			changeSelectedText(this::toggleText);
		}

		/**
		 * Creates a new string from the specified string by changing all lower case
		 * letters to uppercase and all upercase letters to lowercase letters.
		 *
		 * @param text
		 *            a string containing some text
		 * @return a string made by inverting letter cases of the specified string
		 */
		private String toggleText(String text) {
			StringBuilder sb = new StringBuilder(text.length());
			for (char c : text.toCharArray()) {
				if (Character.isUpperCase(c)) {
					sb.append(Character.toLowerCase(c));
				} else if (Character.isLowerCase(c)) {
					sb.append(Character.toUpperCase(c));
				} else {
					sb.append(c);
				}
			}
			return sb.toString();
		}
	};

	/**
	 * An action that sets all letters of the selected part of the currently active
	 * document to upper case.
	 */
	private Action toUppercaseAction = new LocalizableAction("toUpper", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			changeSelectedText(t -> t.toUpperCase());
		}
	};

	/**
	 * An action that sets all letters of the selected part of the currently active
	 * document to lower case.
	 */
	private Action toLowercaseAction = new LocalizableAction("toLower", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			changeSelectedText(t -> t.toLowerCase());
		}
	};

	/**
	 * An action that cuts the selected part of the currently active document.
	 */
	private Action cutFileAction = new LocalizableAction("cut", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JTextArea ta = getActiveTab().textArea;
			ta.getActionMap().get(DefaultEditorKit.cutAction).actionPerformed(e);

		}
	};

	/**
	 * An action that copies the selected part of the currently active document to
	 * clipboard.
	 */
	private Action copyFileAction = new LocalizableAction("copy", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JTextArea ta = getActiveTab().textArea;
			int pos = ta.getCaretPosition();
			ta.getActionMap().get(DefaultEditorKit.copyAction).actionPerformed(e);
			ta.setCaretPosition(pos);

		}
	};

	/**
	 * An action that pastes the text from clipboard to the currently active
	 * document.
	 */
	private Action pasteFileAction = new LocalizableAction("paste", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JTextArea ta = getActiveTab().textArea;
			ta.getActionMap().get(DefaultEditorKit.pasteAction).actionPerformed(e);

		}
	};

	/**
	 * An action that sorts ascending the selected lines of the currently active
	 * document using the current localization.
	 */
	private Action ascendingSortAction = new LocalizableAction("ascending", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			sortSelectedText(SortOrder.ASCENDING);
		}
	};

	/**
	 * An action that sorts descending the selected lines of the currently active
	 * document using the current localization.
	 */
	private Action descendingSortAction = new LocalizableAction("descending", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			sortSelectedText(SortOrder.DESCENDING);
		}
	};

	/**
	 * An action that removes duplicate lines from the selected lines of the
	 * currently active document.
	 */
	private Action uniqueAction = new LocalizableAction("unique", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			changeSelectedLines(lines -> {
				Set<String> set = new LinkedHashSet<>(Arrays.asList(lines));
				return set.toArray(new String[0]);
			});
		}
	};

	/**
	 * Sorts the selected lines of the currently active document using the specified
	 * order and using the current localization.
	 *
	 * @param order
	 *            order that is to be used for sorting
	 */
	private void sortSelectedText(SortOrder order) {
		changeSelectedLines(lines -> {
			Arrays.sort(lines, (a, b) -> {
				Collator collator = Collator.getInstance(LocalizationProvider.getInstance().getLocale());
				return order == SortOrder.ASCENDING ? collator.compare(a, b) : collator.compare(b, a);
			});
			return lines;
		});
	}

	/**
	 * Does the specified changes on the selected lines of the currently active
	 * document. The changes are specified by a function that takes an array of
	 * {@link String} objects and returns the modified array of {@link String}
	 * objects.
	 *
	 * @param transformator
	 *            a function that transforms the selected lines
	 */
	private void changeSelectedLines(Function<String[], String[]> transformator) {
		JTextArea editor = getActiveTab().textArea;
		Document doc = editor.getDocument();

		try {
			int start = Math.min(editor.getCaret().getDot(), editor.getCaret().getMark());
			start = editor.getLineStartOffset(editor.getLineOfOffset(start));
			int end = Math.max(editor.getCaret().getDot(), editor.getCaret().getMark());
			end = editor.getLineEndOffset(editor.getLineOfOffset(end));

			String text = doc.getText(start, end - start);
			String[] lines = text.split("\n");

			lines = transformator.apply(lines);

			StringBuilder sorted = new StringBuilder();
			for (String line : lines) {
				sorted.append(line);
				sorted.append("\n");
			}
			doc.remove(start, end - start);
			doc.insertString(start, sorted.toString(), null);
		} catch (BadLocationException ignorable) {
		}
	}

	/**
	 * Does the specified changes on the selected text of the currently active
	 * document. The changes are specified by a function that takes a {@link String}
	 * and returns the modified {@link String}.
	 *
	 * @param transformator
	 *            a function that transforms the selected text
	 */
	private void changeSelectedText(Function<String, String> transformator) {
		JTextArea editor = getActiveTab().textArea;
		Document doc = editor.getDocument();

		int offset = 0;
		int len = Math.abs(editor.getCaret().getDot() - editor.getCaret().getMark());
		offset = Math.min(editor.getCaret().getDot(), editor.getCaret().getMark());

		String text = null;
		try {
			text = doc.getText(offset, len);
			text = transformator.apply(text);
			doc.remove(offset, len);
			doc.insertString(offset, text, null);
		} catch (BadLocationException ignorable) {
		}

		editor.requestFocus();
		editor.setCaretPosition(offset);
		editor.moveCaretPosition(offset + text.length());
	}

	/**
	 * Saves the currently active document giving the user information if the
	 * operation succeeded or failed.
	 */
	private void saveActiveTabFile() {
		TabPanel tab = getActiveTab();
		try {
			Files.write(tab.filePath, tab.textArea.getText().getBytes(StandardCharsets.UTF_8));
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(JNotepadPP.this, flp.getString("saveFailed"), flp.getString("error"),
					JOptionPane.ERROR_MESSAGE);
		}

		tab.setModified(false);
		setTitle(tab.filePath.toString() + " - JNotepad++");

		JOptionPane.showMessageDialog(JNotepadPP.this, flp.getString("saveSuccess"), flp.getString("info_title"),
				JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Closes the specified tab. If the document that this tab represents has been
	 * modified but not saved, the method will ask user if he wants to save that
	 * document before closing. If there is an untitled document not containing any
	 * text, the tab will be closed without asking for confirmation.
	 *
	 * @param tab
	 *            a tab that is to be closed
	 * @return true if the tab closes, false if user cancels the operation of
	 *         closing
	 */
	private boolean closeFile(TabPanel tab) {
		if (tab.modified && !(tab.filePath == UNTITLED_PATH && tab.textArea.getText().length() == 0)) {
			int ans = JOptionPane.showConfirmDialog(JNotepadPP.this, flp.getString("saveConfirm"),
					flp.getString("conf_title"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (ans == JOptionPane.CANCEL_OPTION) {
				return false;
			} else if (ans == JOptionPane.YES_OPTION) {
				if (tab.filePath == UNTITLED_PATH) {
					Path path = getFilePathForSaving();
					if (path == null) {
						return false;
					}
					tab.filePath = path;
					tabPane.setTitleAt(tabPane.getSelectedIndex(), path.getFileName().toString());

				}

				saveActiveTabFile();
			}
		}

		tabPane.remove(tabPane.getSelectedIndex());
		return true;
	}

	/**
	 * Opens a {@link JFileChooser} so that the user can specify the path and the
	 * file name - i.e. the information used to save a document.
	 *
	 * @return path that is to be used to save a document
	 */
	private Path getFilePathForSaving() {
		while (true) {
			JFileChooser fc = new JFileChooser();
			fc.setDialogTitle(flp.getString("save_title"));
			if (fc.showSaveDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION) {
				JOptionPane.showMessageDialog(JNotepadPP.this, flp.getString("saveCanceled"),
						flp.getString("warning_title"), JOptionPane.WARNING_MESSAGE);
				return null;
			}

			if (Files.exists(fc.getSelectedFile().toPath())) {
				int ans = JOptionPane.showConfirmDialog(JNotepadPP.this, flp.getString("overwriteConfirm"),
						flp.getString("conf_title"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (ans == JOptionPane.YES_OPTION) {
					return fc.getSelectedFile().toPath();
				}
			}
		}
	}

	/**
	 * Sets the accelerator keys for various actions.
	 *
	 */
	private void setAccelerators() {
		newDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control N"));
		openDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control O"));
		saveDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S"));
		saveAsDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift pressed S"));
		closeFileAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control W"));
		statsAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control alt pressed S"));
		cutFileAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control X"));
		copyFileAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control C"));
		pasteFileAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control V"));
		exitAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift pressed X"));
		deleteSelectedPartAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control D"));
		invertCaseAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control F1"));
		toUppercaseAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control U"));
		toLowercaseAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control L"));
		ascendingSortAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control F2"));
		descendingSortAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control F3"));
		uniqueAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control F4"));
	}

	/**
	 * Creates application menubar containing submenus and menu items.
	 */
	private void createMenus() {
		/**
		 * An implementation of {@link LocalizableAction} that does nothing.
		 *
		 * @author Alen Magdić
		 *
		 */
		class EmptyLocalizableAction extends LocalizableAction {
			private static final long serialVersionUID = 1L;

			/**
			 * Constructor.
			 *
			 * @param key
			 *            the key used to get action name from the properties file specified
			 *            by the currently used language
			 * @param lp
			 *            a localization provider used to get the localized name of this
			 *            action (localized by language)
			 */
			public EmptyLocalizableAction(String key, ILocalizationProvider lp) {
				super(key, lp);
			}

			@Override
			public void actionPerformed(ActionEvent e) {
			}

		}

		JMenuBar menubar = new JMenuBar();

		/// FILE MENU///
		JMenu fileMenu = new JMenu(new EmptyLocalizableAction("file", flp));
		menubar.add(fileMenu);

		fileMenu.add(new JMenuItem(newDocumentAction));
		fileMenu.add(new JMenuItem(openDocumentAction));
		fileMenu.addSeparator();
		fileMenu.add(new JMenuItem(saveDocumentAction));
		fileMenu.add(new JMenuItem(saveAsDocumentAction));
		fileMenu.addSeparator();
		fileMenu.add(new JMenuItem(closeFileAction));
		fileMenu.addSeparator();
		fileMenu.add(new JMenuItem(exitAction));

		/// EDIT MENU///
		JMenu editMenu = new JMenu(new EmptyLocalizableAction("edit", flp));
		menubar.add(editMenu);

		editMenu.add(new JMenuItem(cutFileAction));
		editMenu.add(new JMenuItem(copyFileAction));
		editMenu.add(new JMenuItem(pasteFileAction));
		editMenu.add(new JMenuItem(deleteSelectedPartAction));

		/// STATS MENU///
		JMenu statsMenu = new JMenu(new EmptyLocalizableAction("stats", flp));
		menubar.add(statsMenu);
		statsMenu.add(new JMenuItem(statsAction));

		/// LANG MENU///
		JMenu langMenu = new JMenu(new EmptyLocalizableAction("languages", flp));
		menubar.add(langMenu);

		JMenuItem ge = new JMenuItem(new EmptyLocalizableAction("german", flp));
		JMenuItem eng = new JMenuItem(new EmptyLocalizableAction("english", flp));
		JMenuItem hr = new JMenuItem(new EmptyLocalizableAction("croatian", flp));

		eng.addActionListener(e -> LocalizationProvider.getInstance().setLanguage("en"));
		hr.addActionListener(e -> LocalizationProvider.getInstance().setLanguage("hr"));
		ge.addActionListener(e -> LocalizationProvider.getInstance().setLanguage("ge"));

		langMenu.add(eng);
		langMenu.add(ge);
		langMenu.add(hr);

		/// TOOLS MENU///
		JMenu tools = new JMenu(new EmptyLocalizableAction("tools", flp));
		menubar.add(tools);

		JMenu changeCase = new JMenu(new EmptyLocalizableAction("changeCase", flp));
		tools.add(changeCase);

		changeCase.add(new JMenuItem(toUppercaseAction));
		changeCase.add(new JMenuItem(toLowercaseAction));
		changeCase.add(new JMenuItem(invertCaseAction));

		JMenu sort = new JMenu(new EmptyLocalizableAction("sort", flp));
		tools.add(sort);

		sort.add(new JMenuItem(ascendingSortAction));
		sort.add(new JMenuItem(descendingSortAction));

		tools.add(new JMenuItem(uniqueAction));

		setJMenuBar(menubar);
	}

	/**
	 * Creates toolbars containing some actions, like opening a new blank document,
	 * saving a document, etc.
	 */
	private void createToolbars() {
		JToolBar toolbar = new LJToolbar("toolbarName", flp);
		toolbar.setFloatable(true);

		toolbar.add(new JButton(newDocumentAction));
		toolbar.add(new JButton(openDocumentAction));
		toolbar.add(new JButton(saveDocumentAction));
		toolbar.add(new JButton(saveAsDocumentAction));
		toolbar.add(new JButton(closeFileAction));
		toolbar.addSeparator();
		toolbar.add(new JButton(cutFileAction));
		toolbar.add(new JButton(copyFileAction));
		toolbar.add(new JButton(pasteFileAction));
		toolbar.add(new JButton(deleteSelectedPartAction));
		toolbar.add(new JButton(invertCaseAction));
		toolbar.addSeparator();
		toolbar.add(new JButton(statsAction));

		getContentPane().add(toolbar, BorderLayout.PAGE_START);
	}

}
