package ivankatalenic;

import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.List;

public class SelectShapeState implements State {

	private GUI gui;

	public SelectShapeState(GUI gui) {
		this.gui = gui;
	}

	@Override
	public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
		if (!ctrlDown) {
			if (gui.dm.getSelectedObjects().size() == 1) {
				GraphicalObject go = gui.dm.getSelectedObjects().get(0);
				int index = gui.dm.findSelectedHotPoint(go, mousePoint);
				if (index != -1) {
					go.setHotPointSelected(index, true);
				} else {
					go.setSelected(false);
					gui.currentState = gui.idleState;
				}
			} else {
				for (GraphicalObject o : gui.dm.list()) {
					if (o.isSelected()) {
						o.setSelected(false);
					}
				}
				gui.currentState = gui.idleState;
			}
		} else {
			GraphicalObject go = gui.dm.findSelectedGraphicalObject(mousePoint);
			if (go != null && !go.isSelected()) {
				go.setSelected(true);
			}
		}
	}

	@Override
	public void mouseUp(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
		for (GraphicalObject o : gui.dm.list()) {
			for (int i = 0; i < o.getNumberOfHotPoints(); i++) {
				o.setHotPointSelected(i, false);
			}
		}
	}

	@Override
	public void mouseDragged(Point mousePoint) {
		if (gui.dm.getSelectedObjects().size() == 1) {
			GraphicalObject o = gui.dm.getSelectedObjects().get(0);
			int selectedIndex = -1;
			for (int i = 0; i < o.getNumberOfHotPoints(); i++) {
				if (o.isHotPointSelected(i)) {
					selectedIndex = i;
					break;
				}
			}
			if (selectedIndex == -1) {
				return;
			}
			o.setHotPoint(selectedIndex, mousePoint);
		}
	}

	@Override
	public void keyPressed(int keyCode) {
		if (keyCode == KeyCode.ESCAPE.getCode()) {
			for (GraphicalObject o : gui.dm.list()) {
				if (o.isSelected()) {
					o.setSelected(false);
				}
			}
			gui.currentState = gui.idleState;
		} else if (keyCode == KeyCode.UP.getCode()) {
			for (GraphicalObject o : gui.dm.getSelectedObjects()) {
				o.translate(new Point(0.0, -1.0));
			}
		} else if (keyCode == KeyCode.DOWN.getCode()) {
			for (GraphicalObject o : gui.dm.getSelectedObjects()) {
				o.translate(new Point(0.0, 1.0));
			}
		} else if (keyCode == KeyCode.LEFT.getCode()) {
			for (GraphicalObject o : gui.dm.getSelectedObjects()) {
				o.translate(new Point(-1.0, 0.0));
			}
		} else if (keyCode == KeyCode.RIGHT.getCode()) {
			for (GraphicalObject o : gui.dm.getSelectedObjects()) {
				o.translate(new Point(1.0, 0.0));
			}
		} else if (keyCode == KeyCode.PLUS.getCode()) {
			if (gui.dm.getSelectedObjects().size() == 1) {
				GraphicalObject o = gui.dm.getSelectedObjects().get(0);
				gui.dm.decreaseZ(o);
			}
		} else if (keyCode == KeyCode.MINUS.getCode()) {
			if (gui.dm.getSelectedObjects().size() == 1) {
				GraphicalObject o = gui.dm.getSelectedObjects().get(0);
				gui.dm.increaseZ(o);
			}
		} else if (keyCode == KeyCode.G.getCode()) {
			List<GraphicalObject> children = new ArrayList<>(gui.dm.getSelectedObjects().size());
			children.addAll(gui.dm.getSelectedObjects());
			for (GraphicalObject o : children) {
				if (o.isSelected()) {
					o.setSelected(false);
				}
				gui.dm.removeGraphicalObject(o);
			}
			gui.dm.addGraphicalObject(new CompositeShape(children));
		} else if (keyCode == KeyCode.U.getCode()) {
			if (gui.dm.getSelectedObjects().size() == 1) {
				GraphicalObject obj = gui.dm.getSelectedObjects().get(0);
				if (obj instanceof CompositeShape) {
					CompositeShape o = (CompositeShape) obj;
					gui.dm.removeGraphicalObject(o);
					for (GraphicalObject child : o.getChildren()) {
						gui.dm.addGraphicalObject(child);
						child.setSelected(true);
					}
				}
			}
		}
	}

	@Override
	public void keyReleased(int keyCode) {

	}

	@Override
	public void afterDraw(Renderer r, GraphicalObject go) {
		if (!go.isSelected()) {
			return;
		}
		Rectangle rect = go.getBoundingBox();
		Point topLeft = new Point(rect.getX(), rect.getY());
		Point bottomRight = topLeft.translate(rect.getWidth(), rect.getHeight());
		Point topRight = topLeft.translate(rect.getWidth(), 0.0);
		Point bottomLeft = topLeft.translate(0.0, rect.getHeight());
		r.drawLine(topLeft, topRight);
		r.drawLine(topRight, bottomRight);
		r.drawLine(bottomRight, bottomLeft);
		r.drawLine(bottomLeft, topLeft);
		if (gui.dm.getSelectedObjects().size() == 1) {
			for (int i = 0; i < go.getNumberOfHotPoints(); i++) {
				Point hotPoint = go.getHotPoint(i);
				final double RADIUS = 5.0;
				Point tl = hotPoint.translate(-RADIUS, -RADIUS);
				Point tr = hotPoint.translate(RADIUS, -RADIUS);
				Point br = hotPoint.translate(RADIUS, RADIUS);
				Point bl = hotPoint.translate(-RADIUS, RADIUS);
				r.drawLine(tl, tr);
				r.drawLine(tr, br);
				r.drawLine(br, bl);
				r.drawLine(bl, tl);
			}
		}
	}

	@Override
	public void afterDraw(Renderer r) {

	}

	@Override
	public void onLeaving() {

	}

}
