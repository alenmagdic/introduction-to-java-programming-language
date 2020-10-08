package hr.fer.zemris.java.gui.prim;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * This program is a demonstration of {@link PrimListModel}. It shows two
 * {@link JList} components sharing the same {@link ListModel} with a button
 * that adds the next generated prime number to the lists.
 *
 * @author Alen Magdić
 *
 */
public class PrimDemo extends JFrame {
	private static final long serialVersionUID = 1L;

	/**
	 * The starting point of the program.
	 *
	 * @param args
	 *            command line arguments
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new PrimDemo().setVisible(true));
	}

	/**
	 * Constructor.
	 *
	 */
	public PrimDemo() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Prim demo");

		initGUI();

		pack();
		setLocationRelativeTo(null);
	}

	/**
	 * Initializes the graphical user interface.
	 */
	private void initGUI() {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());

		PrimListModel model = new PrimListModel();
		JList<Integer> list1 = new JList<>(model);
		JList<Integer> list2 = new JList<>(model);

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(list1),
				new JScrollPane(list2));
		splitPane.setResizeWeight(0.5);
		cp.add(splitPane, BorderLayout.CENTER);

		JButton nextB = new JButton("Next");
		cp.add(nextB, BorderLayout.PAGE_END);

		nextB.addActionListener(e -> {
			model.next();
		});

	}

}
