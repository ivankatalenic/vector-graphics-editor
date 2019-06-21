package ivankatalenic;

import java.util.ArrayList;
import java.util.List;

public class EraserState implements State {

	private GUI gui;
	private List<GraphicalObject> deleteList;

	public EraserState(GUI gui) {
		this.gui = gui;
		deleteList = new ArrayList<>();
	}

	@Override
	public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {

	}

	@Override
	public void mouseUp(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
		for (GraphicalObject o : deleteList) {
			gui.dm.removeGraphicalObject(o);
		}
		gui.currentState = gui.idleState;
	}

	@Override
	public void mouseDragged(Point mousePoint) {
		gui.renderer.drawLine(mousePoint, mousePoint);
		for (GraphicalObject o : gui.dm.list()) {
			if (o.selectionDistance(mousePoint) < 1e-3) {
				if (!deleteList.contains(o)) {
					deleteList.add(o);
				}
			}
		}
	}

	@Override
	public void keyPressed(int keyCode) {

	}

	@Override
	public void keyReleased(int keyCode) {

	}

	@Override
	public void afterDraw(Renderer r, GraphicalObject go) {

	}

	@Override
	public void afterDraw(Renderer r) {

	}

	@Override
	public void onLeaving() {

	}
}
