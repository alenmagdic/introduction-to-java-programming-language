package hr.fer.zemris.java.gui.calc;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.Border;

import hr.fer.zemris.java.gui.layouts.CalcLayout;
import hr.fer.zemris.java.gui.layouts.RCPosition;

/**
 * This program is an implementation of a simple calculator that supports only
 * the basic operations with one or two operands. So, no complex expression are
 * supported. In addition to classic simple calculator, it contains a stack
 * which can store some values or results that can be popped when needed. It
 * contains a checkbox that inverts some operations, for example it inverts x^n
 * to nth-root of x.
 *
 * @author Alen Magdić
 *
 */
public class Calculator extends JFrame {
	private static final long serialVersionUID = 1L;
	/** A label that displays result **/
	private JLabel displayLB;
	/**
	 * A checkbox that when selected inverts some operations like x^n to
	 * nth-root of x.
	 **/
	private JCheckBox inversionCB;
	/**
	 * If set to true, the next input will override the content of display
	 * label.
	 */
	private boolean clearBeforeNewInput = true;
	/**
	 * Currently selected binary operation.
	 */
	private String binaryOperation;
	/**
	 * A map that associates string representation of a binary operation to a
	 * strategy that executes that operation.
	 **/
	private static final Map<String, BiFunction<Double, Double, Double>> MAP_OPER_TO_STRATEGY;
	/** The first operand of an operation. **/
	private double firstOperand;

	/** Button that executes the selected operation, i.e. button '=' **/
	private JButton resultBT;
	/** Button that clears the content of the display label. **/
	private JButton clrBT;
	/** Button that calculates 1/x. **/
	private JButton reciprBT;
	/** Button that calculates sin(x) or arcsin(x). **/
	private JButton sinusBT;
	/** Button that calculates cos(x) or arccos(x). **/
	private JButton cosinusBT;
	/** Button that calculates log(x) or 10^x. **/
	private JButton logBT;
	/** Button that calculates ln(x) or e^x. **/
	private JButton lnBT;
	/** Button that calculates tan(x) or arctan(x). **/
	private JButton tanBT;
	/** Button that calculates x^n or nth-root of x. **/
	private JButton powBT;
	/** Button that calculates ctg(x) or arcctg(x). **/
	private JButton ctgBT;
	/**
	 * Button that selects sign of the input number. When clicked, inverts the
	 * current sign of the input number.
	 **/
	private JButton signBT;
	/** Button that inputs a decimal point. **/
	private JButton dotBT;
	/** Addition button. **/
	private JButton plusBT;
	/** Subtraction button. **/
	private JButton minusBT;
	/** Multiplication button. **/
	private JButton multBT;
	/** Division button. **/
	private JButton divBT;
	/**
	 * Button that restarts all input, including input of the first operand, of
	 * the operation and clears the stack.
	 **/
	private JButton restartBT;
	/**
	 * Button that pushes the number that is currently on display to the
	 * calculator stack .
	 **/
	private JButton pushBT;
	/**
	 * Button that pops a number from the calculator stack.
	 */
	private JButton popBT;
	/** An array containing the buttons used to input a digit. **/
	private JButton[] numButtons;
	/** Calculator stack. **/
	private Stack<String> stack = new Stack<>();

	static {
		MAP_OPER_TO_STRATEGY = new HashMap<>();
		MAP_OPER_TO_STRATEGY.put("+", (a, b) -> a + b);
		MAP_OPER_TO_STRATEGY.put("-", (a, b) -> a - b);
		MAP_OPER_TO_STRATEGY.put("*", (a, b) -> a * b);
		MAP_OPER_TO_STRATEGY.put("/", (a, b) -> a / b);
		MAP_OPER_TO_STRATEGY.put("x^n", (a, b) -> Math.pow(a, b));
		MAP_OPER_TO_STRATEGY.put("nth-rt", (a, b) -> Math.pow(a, 1 / b));
	}

	/**
	 * The starting point of the program.
	 *
	 * @param args
	 *            command line arguments
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new Calculator().setVisible(true));
	}

	/**
	 * Constructor.
	 */
	public Calculator() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Calculator");

		initGUI();

		pack();
		setLocationRelativeTo(null);

	}

	/**
	 * Initializes the graphical user interface.
	 */
	private void initGUI() {
		Container cp = getContentPane();
		cp.setLayout(new CalcLayout(3));

		displayLB = new JLabel("0 ");
		resultBT = new JButton("=");
		clrBT = new JButton("clr");
		reciprBT = new JButton("1/x");
		sinusBT = new JButton("sin");
		logBT = new JButton("log");
		cosinusBT = new JButton("cos");
		lnBT = new JButton("ln");
		tanBT = new JButton("tan");
		powBT = new JButton("x^n");
		ctgBT = new JButton("ctg");
		divBT = new JButton("/");
		restartBT = new JButton("res");
		multBT = new JButton("*");
		pushBT = new JButton("push");
		minusBT = new JButton("-");
		popBT = new JButton("pop");
		signBT = new JButton("+/-");
		dotBT = new JButton(".");
		plusBT = new JButton("+");
		inversionCB = new JCheckBox("Inv");
		numButtons = new JButton[10];
		for (int num = 0; num < 10; num++) {
			numButtons[num] = new JButton(Integer.toString(num));
		}

		addComponentsToCP();

		Color bgColor = new Color(114, 159, 207);
		Border border = BorderFactory.createLineBorder(Color.BLUE);
		for (Component comp : cp.getComponents()) {
			comp.setBackground(bgColor);
			comp.setForeground(Color.BLACK);
			comp.setFont(new Font(comp.getFont().getFontName(), Font.BOLD, 20));
			if (comp != displayLB) {
				Dimension compPrefSize = comp.getPreferredSize();
				comp.setPreferredSize(
						new Dimension((int) (compPrefSize.width * 1.2), (int) (compPrefSize.width * 1.2)));
			}

		}

		displayLB.setBackground(Color.YELLOW);
		displayLB.setBorder(border);
		displayLB.setOpaque(true);
		displayLB.setHorizontalAlignment(SwingConstants.RIGHT);
		displayLB.setFont(new Font(displayLB.getFont().getFontName(), Font.BOLD, 30));
		inversionCB.setOpaque(true);

		addActionListeners();

	}

	/**
	 * Adds action listeners to all components of the calculator.
	 */
	private void addActionListeners() {
		ActionListener typer = event -> {
			if (clearBeforeNewInput) {
				displayLB.setText("");
				clearBeforeNewInput = false;
			}

			String cmd = event.getActionCommand();
			if (cmd.equals("0") && displayLB.getText().trim().equals("0")) {
				return;
			}
			if (cmd.equals(".") && displayLB.getText().indexOf(".") != -1) {
				return;
			}

			displayLB.setText(displayLB.getText().trim() + cmd + " ");

			if (displayLB.getText().trim().equals(".")) {
				displayLB.setText("0. ");
			}
		};

		for (JButton numBT : numButtons) {
			numBT.addActionListener(typer);
		}

		dotBT.addActionListener(typer);

		signBT.addActionListener(event -> {
			String currentText = displayLB.getText();
			try {
				Double.parseDouble(currentText);
			} catch (NumberFormatException e) {
				return;
			}

			if (currentText.startsWith("-")) {
				displayLB.setText(currentText.substring(1));
			} else {
				displayLB.setText("-" + currentText);
			}
		});

		clrBT.addActionListener(event -> {
			displayLB.setText("0 ");
			clearBeforeNewInput = true;
		});

		inversionCB.addActionListener(this::invertOperations);

		reciprBT.addActionListener(e -> execUnaryOperation(x -> 1 / x));
		sinusBT.addActionListener(e -> execUnaryOperation(x -> inversionCB.isSelected() ? Math.asin(x) : Math.sin(x)));
		cosinusBT
				.addActionListener(e -> execUnaryOperation(x -> inversionCB.isSelected() ? Math.acos(x) : Math.cos(x)));
		logBT.addActionListener(
				e -> execUnaryOperation(x -> inversionCB.isSelected() ? Math.pow(10, x) : Math.log10(x)));
		lnBT.addActionListener(
				e -> execUnaryOperation(x -> inversionCB.isSelected() ? Math.pow(Math.E, x) : Math.log(x)));
		tanBT.addActionListener(e -> execUnaryOperation(x -> inversionCB.isSelected() ? Math.atan(x) : Math.tan(x)));
		ctgBT.addActionListener(
				e -> execUnaryOperation(x -> inversionCB.isSelected() ? Math.atan(1 / x) : 1 / Math.tan(x)));

		ActionListener binOperationListener = event -> {
			executeBinOperation();
			clearBeforeNewInput = true;
			binaryOperation = event.getActionCommand();
		};
		plusBT.addActionListener(binOperationListener);
		minusBT.addActionListener(binOperationListener);
		divBT.addActionListener(binOperationListener);
		multBT.addActionListener(binOperationListener);
		powBT.addActionListener(binOperationListener);

		resultBT.addActionListener(event -> {
			executeBinOperation();
			binaryOperation = null;
			clearBeforeNewInput = true;
		});

		restartBT.addActionListener(event -> {
			firstOperand = 0;
			binaryOperation = null;
			clearBeforeNewInput = true;
			displayLB.setText("0 ");
			stack.clear();
		});

		pushBT.addActionListener(e -> {
			String currentText = displayLB.getText();
			try {
				Double.parseDouble(currentText);
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "Can not add a message to stack!");
				return;
			}
			stack.push(displayLB.getText());
		});

		popBT.addActionListener(e -> {
			if (stack.size() == 0) {
				JOptionPane.showMessageDialog(this, "The stack is empty!");
				return;
			}

			displayLB.setText(stack.pop());
		});

	}

	/**
	 * Executes the specified unary operation. The operation is specified by a
	 * strategy that does the calculation.
	 *
	 * @param operationCalculator
	 *            a strategy used to do calculation
	 */
	private void execUnaryOperation(Function<Double, Double> operationCalculator) {
		clearBeforeNewInput = true;
		double num;
		try {
			num = Double.parseDouble(displayLB.getText().trim());
		} catch (NumberFormatException ex) {
			displayLB.setText("Invalid input ");
			return;
		}
		double result = operationCalculator.apply(num);

		if (Double.isInfinite(result) || Double.isNaN(result)) {
			displayLB.setText("Invalid input ");
		} else {
			displayLB.setText(Double.toString(result) + " ");
		}

	}

	/**
	 * A method that is called whenever the state of "Inv" checkbox changes. It
	 * sets the appropriate names to buttons representing operations that can be
	 * inverted. For example, if this method is called when "Inv" has been
	 * checked, label on button that calculates sin(x) will be changed from sin
	 * -> asin, because when inverted, this button does a calculation of arcus
	 * sinus instead of sinus.
	 *
	 * @param event
	 *            the event of changing the state of "Inv" checkbox
	 */
	private void invertOperations(ActionEvent event) {
		if (!inversionCB.isSelected()) {
			sinusBT.setText("sin");
			cosinusBT.setText("cos");
			logBT.setText("log");
			lnBT.setText("ln");
			tanBT.setText("tan");
			powBT.setText("x^n");
			ctgBT.setText("ctg");
		} else {
			sinusBT.setText("asin");
			cosinusBT.setText("acos");
			logBT.setText("10^x");
			lnBT.setText("e^x");
			tanBT.setText("atan");
			powBT.setText("nth-rt");
			ctgBT.setText("actg");
		}
	}

	/**
	 * Adds all components to the content pane.
	 */
	private void addComponentsToCP() {
		Container cp = this.getContentPane();

		cp.add(displayLB, new RCPosition(1, 1));
		cp.add(resultBT, new RCPosition(1, 6));
		cp.add(clrBT, new RCPosition(1, 7));
		cp.add(reciprBT, new RCPosition(2, 1));
		cp.add(sinusBT, new RCPosition(2, 2));
		cp.add(logBT, new RCPosition(3, 1));
		cp.add(cosinusBT, new RCPosition(3, 2));
		cp.add(lnBT, new RCPosition(4, 1));
		cp.add(tanBT, new RCPosition(4, 2));
		cp.add(powBT, new RCPosition(5, 1));
		cp.add(ctgBT, new RCPosition(5, 2));
		cp.add(divBT, new RCPosition(2, 6));
		cp.add(restartBT, new RCPosition(2, 7));
		cp.add(multBT, new RCPosition(3, 6));
		cp.add(pushBT, new RCPosition(3, 7));
		cp.add(minusBT, new RCPosition(4, 6));
		cp.add(popBT, new RCPosition(4, 7));
		cp.add(signBT, new RCPosition(5, 4));
		cp.add(dotBT, new RCPosition(5, 5));
		cp.add(plusBT, new RCPosition(5, 6));
		cp.add(inversionCB, new RCPosition(5, 7));
		cp.add(numButtons[0], new RCPosition(5, 3));
		cp.add(numButtons[1], new RCPosition(4, 3));
		cp.add(numButtons[2], new RCPosition(4, 4));
		cp.add(numButtons[3], new RCPosition(4, 5));
		cp.add(numButtons[4], new RCPosition(3, 3));
		cp.add(numButtons[5], new RCPosition(3, 4));
		cp.add(numButtons[6], new RCPosition(3, 5));
		cp.add(numButtons[7], new RCPosition(2, 3));
		cp.add(numButtons[8], new RCPosition(2, 4));
		cp.add(numButtons[9], new RCPosition(2, 5));

	}

	/**
	 * Executes a binary operation that has been specified by user through GUI
	 * interaction.
	 */
	private void executeBinOperation() {
		double first = firstOperand;
		double second;
		try {
			second = Double.parseDouble(displayLB.getText().trim());
		} catch (NumberFormatException ex) {
			displayLB.setText("Invalid input ");
			firstOperand = Double.NaN;
			return;
		}

		double result = 0;

		if (binaryOperation == null) {
			firstOperand = second;
			return;
		}

		result = MAP_OPER_TO_STRATEGY.get(binaryOperation).apply(first, second);

		if (second == 0 && binaryOperation.equals("/")) {
			displayLB.setText("Cannot divide by zero ");
		} else if (Double.isNaN(result) || Double.isInfinite(result)) {
			displayLB.setText("Invalid input ");
		} else {
			displayLB.setText(Double.toString(result) + " ");
		}

		firstOperand = result;
	}
}
