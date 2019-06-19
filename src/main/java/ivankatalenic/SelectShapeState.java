package ivankatalenic;

import javafx.scene.input.KeyCode;

public class SelectShapeState implements State {

    private GUI gui;

    public SelectShapeState(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
        if (!ctrlDown) {
            for (GraphicalObject o : gui.dm.list()) {
                if (o.isSelected()) {
                    o.setSelected(false);
                    for (int i = 0; i < o.getNumberOfHotPoints(); i++) {
                        o.setHotPointSelected(i, false);
                    }
                }
            }
        }
        GraphicalObject go = gui.dm.findSelectedGraphicalObject(mousePoint);
        if (go == null) {
            return;
        }
        if (!go.isSelected()) {
            go.setSelected(true);
        }
        if (gui.dm.getSelectedObjects().size() == 1) {
            GraphicalObject o = gui.dm.getSelectedObjects().get(0);
            int index = gui.dm.findSelectedHotPoint(o, mousePoint);
            if (index != -1) {
                o.setHotPointSelected(index, true);
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
            int selectedIndex = gui.dm.findSelectedHotPoint(o, mousePoint);
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
