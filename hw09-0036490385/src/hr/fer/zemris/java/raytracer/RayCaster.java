package hr.fer.zemris.java.raytracer;

import java.util.List;

import hr.fer.zemris.java.raytracer.model.GraphicalObject;
import hr.fer.zemris.java.raytracer.model.IRayTracerProducer;
import hr.fer.zemris.java.raytracer.model.IRayTracerResultObserver;
import hr.fer.zemris.java.raytracer.model.LightSource;
import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.model.Ray;
import hr.fer.zemris.java.raytracer.model.RayIntersection;
import hr.fer.zemris.java.raytracer.model.Scene;
import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

/**
 * This program demonstrates a ray-caster. It shows a predefined scene
 * containing some spheres.
 *
 * @author Alen Magdiæ
 *
 */
public class RayCaster {

	/**
	 * The starting point of the program.
	 *
	 * @param args
	 *            command line arguments
	 */
	public static void main(String[] args) {
		RayTracerViewer.show(getIRayTracerProducer(), new Point3D(10, 0, 0), new Point3D(0, 0, 0),
				new Point3D(0, 0, 10), 20, 20);
	}

	/**
	 * Sets the color of a single pixel on screen. If there are no any
	 * intersections with scene objects (loooking from perspective of an
	 * observer), then the pixel color is set to black. Otherwise there are
	 * three components used to calculate the pixel color: ambient component,
	 * difusse component and reflection component.
	 *
	 * @param scene
	 *            a scene that is to be displayed on screen
	 * @param ray
	 *            a ray pointing from an eye (i.e. perspective of an observer)
	 *            to a scene
	 * @param rgb
	 *            an array used to store the determined color
	 */
	protected static void tracer(Scene scene, Ray ray, short[] rgb) {
		rgb[0] = 0;
		rgb[1] = 0;
		rgb[2] = 0;
		RayIntersection closest = findClosestIntersection(scene, ray);
		if (closest == null) {
			return;
		}

		determineColorFor(scene, ray, rgb, closest);
	}

	/**
	 * Determines the color of a single pixel on screen. There are three
	 * components used to calculate the pixel color: ambient component, difusse
	 * component and reflection component.
	 *
	 * @param scene
	 *            a scene that is to be displayed on screen
	 * @param ray
	 *            a ray pointing from an eye (i.e. perspective of an observer)
	 *            to a scene
	 * @param rgb
	 *            an array used to store the determined color
	 * @param intersection
	 *            the closest intersection of the specified ray with any of the
	 *            scene objects
	 */
	private static void determineColorFor(Scene scene, Ray ray, short[] rgb, RayIntersection intersection) {
		rgb[0] = 15;
		rgb[1] = 15;
		rgb[2] = 15;
		for (LightSource light : scene.getLights()) {
			Ray ray1 = Ray.fromPoints(light.getPoint(), intersection.getPoint());
			RayIntersection inters1 = findClosestIntersection(scene, ray1);
			if (inters1 == null) {
				continue;
			}
			if (inters1 == null || inters1 != null && inters1.getDistance()
					+ 1E-2 < distanceBetweenTwoPoints(light.getPoint(), intersection.getPoint())) {
				continue;
			}

			// difussion
			double scalarProduct = intersection.getNormal().normalize()
					.scalarProduct(ray1.direction.negate().normalize());
			scalarProduct = scalarProduct < 0 ? 0 : scalarProduct;
			rgb[0] += light.getR() * intersection.getKdr() * scalarProduct;
			rgb[1] += light.getG() * intersection.getKdg() * scalarProduct;
			rgb[2] += light.getB() * intersection.getKdb() * scalarProduct;

			// reflection
			Point3D intersToLight = light.getPoint().sub(intersection.getPoint()).normalize();
			Point3D reflectionVector = intersToLight.negate().sub(
					intersection.getNormal().scalarMultiply(2 * intersToLight.scalarProduct(intersection.getNormal())))
					.normalize();
			scalarProduct = reflectionVector.scalarProduct(ray.direction.negate().normalize());
			scalarProduct = scalarProduct < 0 ? 0 : scalarProduct;
			rgb[0] += light.getR() * intersection.getKrr() * scalarProduct;
			rgb[1] += light.getG() * intersection.getKrg() * scalarProduct;
			rgb[2] += light.getB() * intersection.getKrb() * scalarProduct;
		}

	}

	/**
	 * Calculates the distance between the specified points.
	 *
	 * @param point1
	 *            the first point
	 * @param point2
	 *            the second point
	 * @return the distance between the first and the second point
	 */
	private static double distanceBetweenTwoPoints(Point3D point1, Point3D point2) {
		double distance = Math.sqrt(
				Math.pow(point1.x - point2.x, 2) + Math.pow(point1.y - point2.y, 2) + Math.pow(point1.z - point2.z, 2));
		return distance;
	}

	/**
	 * Finds the closest intersection between the specified ray and any object
	 * in the specified scene. If there are no any intersections for the
	 * specified ray, null is returned.
	 *
	 * @param scene
	 *            a scene that is to be displayed on screen
	 * @param ray
	 *            a ray whose closest intersection is to be found
	 * @return the closest intersection between the specified ray and any object
	 *         in the specified scene
	 */
	private static RayIntersection findClosestIntersection(Scene scene, Ray ray) {
		List<GraphicalObject> objects = scene.getObjects();
		RayIntersection closest = null;
		for (GraphicalObject o : objects) {
			RayIntersection inters = o.findClosestRayIntersection(ray);
			if (inters != null && (closest == null || closest.getDistance() > inters.getDistance())) {
				closest = inters;
			}
		}
		return closest;
	}

	/**
	 * Gets an instance of implementation of {@link IRayTracerProducer} that
	 * does the whole process of calculating colors for each screen pixel.
	 *
	 * @return an instance of implementation of {@link IRayTracerProducer}
	 */
	private static IRayTracerProducer getIRayTracerProducer() {
		return new IRayTracerProducer() {
			@Override
			public void produce(Point3D eye, Point3D view, Point3D viewUp, double horizontal, double vertical,
					int width, int height, long requestNo, IRayTracerResultObserver observer) {
				System.out.println("Starting calculations...");

				short[] red = new short[width * height];
				short[] green = new short[width * height];
				short[] blue = new short[width * height];

				Point3D yAxis = viewUp.normalize()
						.sub(view.sub(eye).normalize()
								.scalarMultiply(view.sub(eye).normalize().scalarProduct(viewUp.normalize())))
						.normalize();
				Point3D xAxis = view.sub(eye).normalize().vectorProduct(yAxis).normalize();
				Point3D screenCorner = view.sub(xAxis.scalarMultiply(horizontal / 2))
						.add(yAxis.scalarMultiply(vertical / 2));

				Scene scene = RayTracerViewer.createPredefinedScene();
				short[] rgb = new short[3];
				int offset = 0;
				for (int y = 0; y < height; y++) {
					for (int x = 0; x < width; x++) {
						Point3D screenPoint = screenCorner.add(xAxis.scalarMultiply(x * horizontal / (width - 1)))
								.sub(yAxis.scalarMultiply(y * vertical / (height - 1)));
						Ray ray = Ray.fromPoints(eye, screenPoint);
						tracer(scene, ray, rgb);
						red[offset] = rgb[0] > 255 ? 255 : rgb[0];
						green[offset] = rgb[1] > 255 ? 255 : rgb[1];
						blue[offset] = rgb[2] > 255 ? 255 : rgb[2];
						offset++;
					}
				}

				System.out.println("Calculations done...");
				observer.acceptResult(red, green, blue, requestNo);
				System.out.println("Observer has been called...");
			}
		};
	}

}
