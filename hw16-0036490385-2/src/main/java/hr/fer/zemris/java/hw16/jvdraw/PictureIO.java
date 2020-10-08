package hr.fer.zemris.java.hw16.jvdraw;

import java.awt.Color;
import java.awt.Point;
import java.util.List;

import hr.fer.zemris.java.hw16.jvdraw.JVDraw.NamesGenerator;
import hr.fer.zemris.java.hw16.jvdraw.geomobjects.Circle;
import hr.fer.zemris.java.hw16.jvdraw.geomobjects.FilledCircle;
import hr.fer.zemris.java.hw16.jvdraw.geomobjects.GeometricalObject;
import hr.fer.zemris.java.hw16.jvdraw.geomobjects.Line;
import hr.fer.zemris.java.hw16.jvdraw.modeling.DrawingModel;

/**
 * Klasa koja sadrži metode za pohranu i za čitanje .jvd datoteka.
 *
 * @author Alen Magdić
 *
 */
public class PictureIO {

	/**
	 * Na temelju zadanog modela, izgrađuje sadržaj .jvd datoteke.
	 *
	 * @param drawingModel
	 *            model za kojeg treba generirati sadržaj .jvd datoteke
	 * @return izgrađeni sadržaj .jvd datoteke za zadani model
	 */
	public static String getPictureContentAsText(DrawingModel drawingModel) {
		StringBuilder strB = new StringBuilder();
		for (int i = 0, n = drawingModel.getSize(); i < n; i++) {
			strB.append(String.format("%s%n", describeObject(drawingModel.getObject(i))));
		}
		return strB.toString();
	}

	/**
	 * Stvara tekstualni opis zadanog objekta, odnosno opis koji se koristi u
	 * kontekstu opisa čitave slike.
	 *
	 * @param object
	 *            objekt kojeg se opisuje
	 * @return tekstualni opisa zadanog objekta
	 */
	private static String describeObject(GeometricalObject object) {
		Color color = object.getColor();
		if (object instanceof Line) {
			Line line = (Line) object;
			return String.format("LINE %d %d %d %d %d %d %d", line.getStartPoint().x, line.getStartPoint().y,
					line.getEndPoint().x, line.getEndPoint().y, color.getRed(), color.getGreen(), color.getBlue());
		} else if (object instanceof FilledCircle) {
			FilledCircle circle = (FilledCircle) object;
			Color fColor = circle.getFillColor();
			return String.format("FCIRCLE %d %d %d %d %d %d %d %d %d", circle.getCenterPoint().x,
					circle.getCenterPoint().y, circle.getRadius(), color.getRed(), color.getGreen(), color.getBlue(),
					fColor.getRed(), fColor.getGreen(), fColor.getBlue());
		} else if (object instanceof Circle) {
			Circle circle = (Circle) object;
			return String.format("CIRCLE %d %d %d %d %d %d", circle.getCenterPoint().x, circle.getCenterPoint().y,
					circle.getRadius(), color.getRed(), color.getGreen(), color.getBlue());
		}
		return null;
	}

	/**
	 * Parsira zadani sadržaj .jvd datoteke i na temelju pročitanog generira
	 * geometrijske objekte i dodaje ih u zadani model. Za imenovanje kreiranih
	 * objekata koristi zadani generator imena.
	 *
	 * @param drawingModel
	 *            model u kojeg se dodaju generirani objekti
	 * @param text
	 *            pročitani sadržaj .jvd datoteke
	 * @param namesGenerator
	 *            generator imena geometrijskih objekata
	 * @return true ako je operacije uspješno izvršena
	 */
	public static boolean importTextContent(DrawingModel drawingModel, List<String> text,
			NamesGenerator namesGenerator) {
		try {
			for (String line : text) {
				String[] parts = line.trim().split(" ");

				if (parts[0].equals("LINE")) {
					int x0 = Integer.parseInt(parts[1]);
					int y0 = Integer.parseInt(parts[2]);
					int x1 = Integer.parseInt(parts[3]);
					int y1 = Integer.parseInt(parts[4]);
					int r = Integer.parseInt(parts[5]);
					int g = Integer.parseInt(parts[6]);
					int b = Integer.parseInt(parts[7]);
					Line ln = new Line(new Point(x0, y0), new Point(x1, y1), new Color(r, g, b));
					ln.setName(namesGenerator.generateNameFor(ln));
					drawingModel.add(ln);
				} else if (parts[0].equals("CIRCLE")) {
					int x = Integer.parseInt(parts[1]);
					int y = Integer.parseInt(parts[2]);
					int radius = Integer.parseInt(parts[3]);
					int r = Integer.parseInt(parts[4]);
					int g = Integer.parseInt(parts[5]);
					int b = Integer.parseInt(parts[6]);
					Circle circle = new Circle(new Point(x, y), radius, new Color(r, g, b));
					circle.setName(namesGenerator.generateNameFor(circle));
					drawingModel.add(circle);
				} else if (parts[0].equals("FCIRCLE")) {
					int x = Integer.parseInt(parts[1]);
					int y = Integer.parseInt(parts[2]);
					int radius = Integer.parseInt(parts[3]);
					int r1 = Integer.parseInt(parts[4]);
					int g1 = Integer.parseInt(parts[5]);
					int b1 = Integer.parseInt(parts[6]);
					int r2 = Integer.parseInt(parts[7]);
					int g2 = Integer.parseInt(parts[8]);
					int b2 = Integer.parseInt(parts[9]);
					FilledCircle circle = new FilledCircle(new Point(x, y), radius, new Color(r1, g1, b1),
							new Color(r2, g2, b2));
					circle.setName(namesGenerator.generateNameFor(circle));
					drawingModel.add(circle);
				} else {
					return false;
				}
			}
		} catch (RuntimeException ex) {
			return false;
		}
		return true;

	}
}
