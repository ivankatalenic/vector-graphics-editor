package ivankatalenic;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class App extends Application {

	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage stage) {
		List<GraphicalObject> objects = new ArrayList<>();

		objects.add(new LineSegment());
		objects.add(new Ellipse());

		GUI gui = new GUI(stage, objects);
	}

}
