package hr.fer.zemris.java.custom.scripting.exec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantDouble;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantInteger;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;
import hr.fer.zemris.java.custom.scripting.elems.ElementOperator;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Klasa zadužena za izvođenje skripte.
 *
 * @author Alen Magdić
 *
 */
public class SmartScriptEngine {
	/** Korijen stabla dobivenog parsiranjem skripte. **/
	private DocumentNode documentNode;
	/** Kontekst zahtjeva. **/
	private RequestContext requestContext;
	/** Multi-stog korišten za pohranu vrijednosti varijabli. **/
	private ObjectMultistack multistack = new ObjectMultistack();

	/**
	 * Posjetitelj stabla dobivenog parsiranjem. Obilaskom stabla vrši izvođenje
	 * zadane skripte.
	 *
	 */
	private INodeVisitor visitor = new INodeVisitor() {

		@Override
		public void visitTextNode(TextNode node) {
			if (node.getText().isEmpty()) {
				return;
			}
			try {
				requestContext.write(node.getText());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public void visitForLoopNode(ForLoopNode node) {
			String varName = node.getVariable().asText();
			String endingValue = node.getEndExpression().asText();
			String stepValue = node.getStepExpression().asText();
			multistack.push(varName, new ValueWrapper(node.getStartExpression().asText()));

			while (true) {
				if (multistack.peek(varName).numCompare(endingValue) > 0) {
					break;
				}
				acceptChildren(node);

				ValueWrapper val = multistack.pop(varName);
				val.add(stepValue);
				multistack.push(varName, val);
			}
			multistack.pop(varName);
		}

		@Override
		public void visitEchoNode(EchoNode node) {
			Stack<Object> tempStack = new Stack<>();
			Element[] elements = node.getElements();
			for (int i = 1; i < elements.length; i++) {
				Element el = elements[i];
				if (el instanceof ElementConstantDouble || el instanceof ElementConstantInteger
						|| el instanceof ElementString) {
					tempStack.push(el.asText());
				} else if (el instanceof ElementVariable) {
					tempStack.push(multistack.peek(el.asText()).getValue());
				} else if (el instanceof ElementOperator) {
					Object op1 = tempStack.pop();
					Object op2 = tempStack.pop();
					Object res = calculateOperation(op1, op2, el.asText());
					tempStack.push(res);
				} else if (el instanceof ElementFunction) {
					FunctionExecutions.execute(el.asText(), tempStack, requestContext);
				}
			}

			List<Object> remainingElements = new ArrayList<>();
			while (!tempStack.isEmpty()) {
				remainingElements.add(tempStack.pop());
			}
			for (int i = remainingElements.size() - 1; i >= 0; i--) {
				try {
					requestContext.write(remainingElements.get(i).toString());
				} catch (IOException e) {
					throw new RuntimeException("Unable to write output.");
				}
			}
		}

		/**
		 * Pomoćna metoda koja vrši izračun zadane operacije sa zadanim
		 * operandima. Podržane operacije su +,-,*,/.
		 *
		 * @param op1
		 *            prvi operand
		 * @param op2
		 *            drugi operand
		 * @param operator
		 *            operator +,-,*,/
		 * @return rezultat izvođenja operacije
		 */
		private Object calculateOperation(Object op1, Object op2, String operator) {
			ValueWrapper op1Wrapper = new ValueWrapper(op1);
			switch (operator) {
			case "+":
				op1Wrapper.add(op2);
				break;
			case "-":
				op1Wrapper.subtract(op2);
				break;
			case "*":
				op1Wrapper.multiply(op2);
				break;
			case "/":
				op1Wrapper.divide(op2);
				break;
			default:
				throw new UnsupportedOperationException("Operator '" + operator + "' is not supported.");
			}
			return op1Wrapper.getValue();
		}

		@Override
		public void visitDocumentNode(DocumentNode node) {
			acceptChildren(node);
		}

		/**
		 * Pomoćna metoda koja poziva metodu accept nad djecom zadanog čvora.
		 *
		 * @param node
		 *            čvor čiju je djecu potrebno pozvati
		 */
		private void acceptChildren(Node node) {
			for (int i = 0, n = node.numberOfChildren(); i < n; i++) {
				node.getChild(i).accept(this);
			}
		}

	};

	/**
	 * Konstruktor.
	 *
	 * @param documentNode
	 *            vrh stabla dobivenog parsiranjem skripte
	 * @param requestContext
	 *            kontekst zahtjeva odnosno objekt koji vrši ispis izlaza
	 *            skripte zajedno sa prikladnim zaglavljem
	 */
	public SmartScriptEngine(DocumentNode documentNode, RequestContext requestContext) {
		this.documentNode = documentNode;
		this.requestContext = requestContext;
	}

	/**
	 * Metoda koja pokreće izršavanje skripte.
	 */
	public void execute() {
		documentNode.accept(visitor);
	}
}
