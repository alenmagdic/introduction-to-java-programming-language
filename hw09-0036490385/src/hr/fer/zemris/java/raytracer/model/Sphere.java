package hr.fer.zemris.java.raytracer.model;

/**
 * A class that represents a simple graphical object: a sphere.
 *
 * @author Alen Magdiæ
 *
 */
public class Sphere extends GraphicalObject {
	/** Sphere center **/
	private Point3D center;
	/** Sphere radius **/
	private double radius;
	/** Difusse component - red component **/
	private double kdr;
	/** Difusse component - green component **/
	private double kdg;
	/** Difusse component - blue component **/
	private double kdb;
	/** Reflective component - red component **/
	private double krr;
	/** Reflective component - green component **/
	private double krg;
	/** Reflective component - blue component **/
	private double krb;
	/** Reflective component - parameter n **/
	private double krn;

	/**
	 * Constructor. Takes center, radius and color components of the sphere.
	 *
	 * @param center
	 *            sphere center
	 * @param radius
	 *            sphere radius
	 * @param kdr
	 *            difusse component - red component
	 * @param kdg
	 *            difusse component - green component
	 * @param kdb
	 *            difusse component - blue component
	 * @param krr
	 *            reflective component - red component
	 * @param krg
	 *            reflective component - green component
	 * @param krb
	 *            reflective component - blue component
	 * @param krn
	 *            reflective component - parameter n
	 */
	public Sphere(Point3D center, double radius, double kdr, double kdg, double kdb, double krr, double krg, double krb,
			double krn) {
		super();
		this.center = center;
		this.radius = radius;
		this.kdr = kdr;
		this.kdg = kdg;
		this.kdb = kdb;
		this.krr = krr;
		this.krg = krg;
		this.krb = krb;
		this.krn = krn;
	}

	@Override
	public RayIntersection findClosestRayIntersection(Ray ray) {
		double a = 1;
		double b = 2 * ray.direction.scalarProduct(ray.start.sub(center));
		double c = Math.pow(ray.start.sub(center).norm(), 2) - radius * radius;
		double discriminant = b * b - 4 * a * c;

		if (discriminant < 0) {
			return null;
		}

		double t1 = (-b + Math.sqrt(discriminant)) / (2 * a);
		double t2 = (-b - Math.sqrt(discriminant)) / (2 * a);
		double t;
		if (t2 < 0) {
			t = t1;
		} else {
			t = t2;
		}

		Point3D point = ray.start.add(ray.direction.scalarMultiply(t));
		double distance = Math.sqrt(Math.pow(ray.start.x - point.x, 2) + Math.pow(ray.start.y - point.y, 2)
				+ Math.pow(ray.start.z - point.z, 2));

		return new RayAndSphereIntersection(point, distance, true);
	}

	/**
	 * An implementation of RayIntersection. Represents an intersection between
	 * a ray and a sphere.
	 *
	 * @author Alen Magdiæ
	 *
	 */
	private class RayAndSphereIntersection extends RayIntersection {

		/**
		 * Constructor.
		 *
		 * @param point
		 *            point of intersection
		 * @param distance
		 *            distance between the start of the ray and the point of
		 *            intersection
		 * @param outer
		 *            true if this is an intersection at which the ray enters
		 *            into the sphere
		 */
		protected RayAndSphereIntersection(Point3D point, double distance, boolean outer) {
			super(point, distance, outer);
		}

		@Override
		public Point3D getNormal() {
			return super.getPoint().sub(center);
		}

		@Override
		public double getKdr() {
			return kdr;
		}

		@Override
		public double getKdg() {
			return kdg;
		}

		@Override
		public double getKdb() {
			return kdb;
		}

		@Override
		public double getKrr() {
			return krr;
		}

		@Override
		public double getKrg() {
			return krg;
		}

		@Override
		public double getKrb() {
			return krb;
		}

		@Override
		public double getKrn() {
			return krn;
		}

	}
}
