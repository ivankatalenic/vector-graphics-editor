package ivankatalenic;

public class Point {

	private double x, y;

	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Point duplicate() {
		return new Point(x, y);
	}

	public Point translate(Point delta) {
		return translate(delta.getX(), delta.getY());
	}

	public Point translate(double x, double y) {
		return new Point(this.x + x, this.y + y);
	}

	/**
	 * Returns a new point representing a vector from the delta point to this point.
	 *
	 * @param delta A point representing the starting vector.
	 * @return A point representing vector difference.
	 */
	public Point difference(Point delta) {
		return new Point(x - delta.getX(), y - delta.getY());
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

}
