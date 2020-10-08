package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Posjetitelj stabla dobivenog parsiranjem skripte.
 *
 * @author Alen Magdić
 *
 */
public interface INodeVisitor {
	/**
	 * Posjećuje čvor tipa {@link TextNode}.
	 *
	 * @param node
	 *            čvor tipa {@link EchoNode}
	 */
	public void visitTextNode(TextNode node);

	/**
	 * Posjećuje čvor tipa {@link ForLoopNode}.
	 *
	 * @param node
	 *            čvor tipa {@link EchoNode}
	 */
	public void visitForLoopNode(ForLoopNode node);

	/**
	 * Posjećuje čvor tipa {@link EchoNode}.
	 *
	 * @param node
	 *            čvor tipa {@link EchoNode}
	 */
	public void visitEchoNode(EchoNode node);

	/**
	 * Posjećuje čvor tipa {@link DocumentNode}.
	 *
	 * @param node
	 *            čvor tipa {@link DocumentNode}
	 */
	public void visitDocumentNode(DocumentNode node);
}