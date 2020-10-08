package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Node koji sadrži tekstualni podatak.
 *
 * @author Alen Magdić
 *
 */
public class TextNode extends Node {
	/** Tekstualni podatak **/
	private String text;

	/** Konstruktor. Prima referencu na tekstualni podatak. **/
	public TextNode(String text) {
		super();
		this.text = text;
	}

	/** Vraća pohranjeni tekstualni podatak **/
	public String getText() {
		return text;
	}

	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitTextNode(this);
	}
}
