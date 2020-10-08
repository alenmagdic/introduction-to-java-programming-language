package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;

/**
 * Klasa koja predstavlja čvor FOR petlje. FOR petlja sadrži naziv varijable te
 * najviše 3, a najmanje dva argumenta modelirana klasom Element.
 *
 * @author Alen Magdić
 *
 */
public class ForLoopNode extends Node {
	/**
	 * Varijabla FOR petlje
	 */
	private ElementVariable variable;
	/** Početni izraz **/
	private Element startExpression;
	/** Završni izraz **/
	private Element endExpression;
	/** Izraz koji predstavlja korak iteracije **/
	private Element stepExpression;

	/**
	 * Konstruktor. Prima argumente FOR petlje.
	 *
	 * @param variable
	 *            varijabla petlje
	 * @param startExpression
	 *            početni izraz
	 * @param endExpression
	 *            završni izraz
	 * @param stepExpression
	 *            izraz koji predstavlja korak iteracije
	 */
	public ForLoopNode(ElementVariable variable, Element startExpression, Element endExpression,
			Element stepExpression) {
		super();
		this.variable = variable;
		this.startExpression = startExpression;
		this.endExpression = endExpression;
		this.stepExpression = stepExpression;
	}

	/**
	 * Vraća varijablu petlje.
	 *
	 * @return varijabla petlje
	 */
	public ElementVariable getVariable() {
		return variable;
	}

	/**
	 * Vraća početni izraz.
	 *
	 * @return početni izraz
	 */
	public Element getStartExpression() {
		return startExpression;
	}

	/**
	 * Vraća završni izraz.
	 *
	 * @return završni izraz
	 */
	public Element getEndExpression() {
		return endExpression;
	}

	/**
	 * Vraća izraz koji predstavlja korak iteracije.
	 *
	 * @return izraz koji predstavlja korak iteracije
	 */
	public Element getStepExpression() {
		return stepExpression;
	}

}
