package hr.fer.zemris.java.raytracer;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

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
 * containing some spheres. The difference between this and {@link RayCaster} is
 * that this program uses multiple threads in order to make the process of
 * calculation faster.
 *
 * @author Alen Magdiæ
 *
 */
public class RayCasterParallel {

	/**
	 * The starting point of the program.
	 *
	 * @param args
	 *            command line arguments
	 */
	public static void main(String[] args) {
		RayTracerViewer.show(getIRayTracerProducer(), new Point3D(10, 10, 0), new Point3D(0, 0, 0),
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
	 * This class represents a single job that is to be done by one of the
	 * threads.
	 *
	 * @author Alen Magdiæ
	 *
	 */
	private static class CalculationJob extends RecursiveAction {
		/** Serial version UID **/
		private static final long serialVersionUID = 1L;
		/**
		 * Size of the job that is to be done directly, not by delegating to new
		 * threads. If yMax - yMin + 1 <= TRESHOLD, then the job is to be done
		 * directly.
		 */
		private static final int TRESHOLD = 20;
		/**
		 * Minimum y coordinate that is included in this job.
		 *
		 */
		private int yMin;
		/**
		 * Maximum y coordinate that is included in this job.
		 */
		private int yMax;
		/** Display width **/
		private int width;
		/** Display height **/
		private int height;
		/** Width of the frame included in the display. **/
		private double horizontal;
		/** Height of the frame included in the display. **/
		private double vertical;
		/**
		 * Corner of the frame included in the display.
		 */
		private Point3D screenCorner;
		/**
		 * x-axis in the scene.
		 */
		private Point3D xAxis;
		/**
		 * y-axis in the scene.
		 */
		private Point3D yAxis;
		/**
		 * Position of an observer.
		 */
		private Point3D eye;
		/**
		 * A scene that is to be displayed.
		 */
		private Scene scene;
		/**
		 * An array of red components of pixel colors.
		 */
		private short[] red;
		/**
		 * An array of green components of pixel colors.
		 */
		private short[] green;
		/**
		 * An array of blue components of pixel colors.
		 */
		private short[] blue;

		/**
		 * Constructor.
		 *
		 * @param yMin
		 *            minimum y coordinate that is included in this job.
		 * @param yMax
		 *            maximum y coordinate that is included in this job
		 * @param width
		 *            display width
		 * @param height
		 *            display height
		 * @param horizontal
		 *            width of the frame included in the display
		 * @param vertical
		 *            height of the frame included in the display
		 * @param screenCorner
		 *            corner of the frame included in the display
		 * @param xAxis
		 *            x-axis in the scene
		 * @param yAxis
		 *            y-axis in the scene
		 * @param eye
		 *            position of an observer
		 * @param scene
		 *            a scene that is to be displayed.
		 * @param red
		 *            an array of red components of pixel colors
		 * @param green
		 *            an array of green components of pixel colors
		 * @param blue
		 *            an array of blue components of pixel colors
		 */
		public CalculationJob(int yMin, int yMax, int width, int height, double horizontal, double vertical,
				Point3D screenCorner, Point3D xAxis, Point3D yAxis, Point3D eye, Scene scene, short[] red,
				short[] green, short[] blue) {
			super();
			this.yMin = yMin;
			this.yMax = yMax;
			this.width = width;
			this.height = height;
			this.horizontal = horizontal;
			this.vertical = vertical;
			this.screenCorner = screenCorner;
			this.xAxis = xAxis;
			this.yAxis = yAxis;
			this.eye = eye;
			this.scene = scene;
			this.red = red;
			this.green = green;
			this.blue = blue;
		}

		@Override
		protected void compute() {
			if (yMax - yMin + 1 <= TRESHOLD) {
				computeDirect();
				return;
			}
			invokeAll(
					new CalculationJob(yMin, yMin + (yMax - yMin) / 2, width, height, horizontal, vertical,
							screenCorner, xAxis, yAxis, eye, scene, red, green, blue),
					new CalculationJob(yMin + (yMax - yMin) / 2, yMax, width, height, horizontal, vertical,
							screenCorner, xAxis, yAxis, eye, scene, red, green, blue));
		}

		/**
		 * Computes directly the colors of the pixels that are to be computed as
		 * a part of this job.
		 */
		private void computeDirect() {
			short[] rgb = new short[3];
			int offset = width * yMin;
			for (int y = yMin; y < yMax; y++) {
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

		}

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
				System.out.println("Starting calculation...");

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

				ForkJoinPool pool = new ForkJoinPool();
				pool.invoke(new CalculationJob(0, height, width, height, horizontal, vertical, screenCorner, xAxis,
						yAxis, eye, scene, red, green, blue));
				pool.shutdown();

				System.out.println("Calculations done...");
				observer.acceptResult(red, green, blue, requestNo);
				System.out.println("Observer has been called...");
			}

		};
	}
}
