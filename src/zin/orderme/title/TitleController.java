package zin.orderme.title;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class TitleController implements Initializable {
	@FXML private Button btnEnter;

	@Override
	public void initialize(URL location, ResourceBundle resources){
		
		btnEnter.setOnAction(new EventHandler<ActionEvent>(){
	
			@Override
			public void handle(ActionEvent event) {
				try {
					handleBtnEnterAction(event);
				} catch (Exception e) {
					System.out.println("title 화면오류 : " + e.getMessage());
				}
			}
			
		});
	}
	// 메인화면 이동
	public void handleBtnEnterAction(ActionEvent event) throws Exception {
		Stage stage = (Stage)btnEnter.getScene().getWindow();
        Parent main = FXMLLoader.load(Class.forName("zin.orderme.main.MainController").getResource("main.fxml"));
		Scene scene = new Scene(main);
		stage.setScene(scene);
		stage.show();
	}
}
