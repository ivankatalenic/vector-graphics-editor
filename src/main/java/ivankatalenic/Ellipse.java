package ivankatalenic;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Ellipse extends AbstractGraphicalObject {

    private static final double DIVS_CONST = 2.0;

    public Ellipse(Point right, Point down) {
        super();
        hotPoints = new Point[] {right, down};
        hotPointsSelected = new boolean[hotPoints.length];
        for (int i = 0; i < hotPoints.length; i++) {
            hotPointsSelected[i] = false;
        }
    }

    public Ellipse() {
        this(new Point(20.0, 0.0), new Point(0.0, 20.0));
    }

    @Override
    public double selectionDistance(Point mousePoint) {
        double rx = hotPoints[0].getX() - hotPoints[1].getX();
        double ry = hotPoints[1].getY() - hotPoints[0].getY();
        double rxa = rx >= 0 ? rx : -rx;
        double rya = ry >= 0 ? ry : -ry;
        double outerRadius = rxa > rya ? rxa : rya;
        double centerX = hotPoints[1].getX();
        double centerY = hotPoints[0].getY();
        double distance = GeometryUtil.distanceFromPoint(new Point(centerX, centerY), mousePoint) - outerRadius;
        return distance >= 0 ? distance : 0;
    }

    @Override
    public void translate(Point delta) {
        for (int i = 0; i < hotPoints.length; i++) {
            hotPoints[i] = hotPoints[i].translate(delta);
        }
        notifyListeners();
    }

    @Override
    public Rectangle getBoundingBox() {
        double rx = hotPoints[0].getX() - hotPoints[1].getX();
        double ry = hotPoints[1].getY() - hotPoints[0].getY();
        rx = rx >= 0 ? rx : -rx;
        ry = ry >= 0 ? ry : -ry;
        double centerX = hotPoints[1].getX();
        double centerY = hotPoints[0].getY();
        return new Rectangle(centerX - rx, centerY - ry,
                2 * rx, 2 * ry);
    }

    @Override
    public void documentChange() {

    }

    @Override
    public void render(Renderer r) {
        double rx = hotPoints[0].getX() - hotPoints[1].getX();
        double ry = hotPoints[1].getY() - hotPoints[0].getY();
        double centerX = hotPoints[1].getX();
        double centerY = hotPoints[0].getY();
        double rxa = rx >= 0 ? rx : -rx;
        double rya = ry >= 0 ? ry : -ry;
        double maxSide = rxa > rya ? rxa : rya;
        int renderDivs = (int) (maxSide * DIVS_CONST);
        if (renderDivs < 2) {
            return;
        }
        double t = (2.0 * Math.PI) / renderDivs;
        List<Point> points = new ArrayList<>(renderDivs);
        for (int n = 0; n < renderDivs; n++) {
            points.add(new Point(
                    centerX + rx * Math.cos(n * t),
                    centerY + ry * Math.sin(n * t)));
        }
        r.fillPolygon(points.toArray(new Point[1]));
    }

    @Override
    public String getShapeName() {
        return "Ellipse";
    }

    @Override
    public GraphicalObject duplicate() {
        return new Ellipse(hotPoints[0].duplicate(), hotPoints[1].duplicate());
    }

    @Override
    public void load(Stack<GraphicalObject> stack, String data) {
        throw new Error("Not implemented!");
    }

    @Override
    public void save(List<String> rows) {
        throw new Error("Not implemented!");
    }

}
