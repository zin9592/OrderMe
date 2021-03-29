package zin.orderme.title;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

// 타이틀 화면 
public class TitleMain extends Application {
	@Override
	public void start(Stage stage){
		try {
		Parent title = FXMLLoader.load(Class.forName("zin.orderme.title.TitleMain").getResource("title.fxml"));
		Scene scene = new Scene(title);
		stage.setTitle("메인화면");
		stage.setScene(scene);
		stage.show();
		} catch(Exception e) {
			System.out.println("titleMain 화면오류 : " + e.getMessage());
		}
	}
	@Override
	public void stop() {
		System.out.println("exit");
		System.exit(0);
	}
	
	public static void main(String[] args) {	
		launch(args);
	}
}
