package ivankatalenic;

public class GeometryUtil {

	public static double length(Point a) {
		return Math.hypot(a.getX(), a.getY());
	}

	public static double distanceFromPoint(Point point1, Point point2) {
		return length(point2.difference(point1));
	}

	public static double scalarProduct(Point a, Point b) {
		return a.getX() * b.getX() + a.getY() * b.getY();
	}

	public static double crossProductNorm(Point a, Point b) {
		return Math.abs(a.getX() * b.getY() - a.getY() * b.getX());
	}

	public static double distanceFromLineSegment(Point s, Point e, Point p) {
		// Izračunaj koliko je točka P udaljena od linijskog segmenta određenog
		// početnom točkom S i završnom točkom E. Uočite: ako je točka P iznad/ispod
		// tog segmenta, ova udaljenost je udaljenost okomice spuštene iz P na S-E.
		// Ako je točka P "prije" točke S ili "iza" točke E, udaljenost odgovara
		// udaljenosti od P do početne/konačne točke segmenta.
		Point v = e.difference(s);
		double vLength = length(v);
		double lambda = scalarProduct(v, p.difference(s)) / (vLength * vLength);
		if (lambda < 0.0) {
			return distanceFromPoint(s, p);
		} else if (lambda > 1.0) {
			return distanceFromPoint(e, p);
		} else {
			return crossProductNorm(v, p.difference(s)) / vLength;
		}
	}

}
