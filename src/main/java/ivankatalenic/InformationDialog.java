package ivankatalenic;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class InformationDialog {

	private Stage stage;
	private String message;

	public InformationDialog(Stage stage, String message) {
		this.stage = stage;
		this.message = message;
	}

	public void show() {
		var p = new Stage(StageStyle.UTILITY);
		BorderPane box = new BorderPane();
		Label label = new Label(message);
		label.setPadding(new Insets(5.0));
		Button okButton = new Button("OK");
		okButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				p.hide();
			}
		});
		box.setCenter(label);
		box.setBottom(okButton);
		BorderPane.setAlignment(okButton, Pos.CENTER);
		box.setPadding(new Insets(10.0));
		p.setTitle("Information");
		p.initOwner(stage);
		p.initModality(Modality.WINDOW_MODAL);
		p.setScene(new Scene(box));
		p.showAndWait();
	}

}
