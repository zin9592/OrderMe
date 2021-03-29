package zin.orderme.main;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainController implements Initializable {
	@FXML
	private Button btnOrder;
	@FXML
	private Button btnMenu;
	@FXML
	private Button btnCheckURL;
	@FXML
	private Button btnUser;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		btnOrder.setOnAction(event -> handleBtnOrderAction(event));
		btnMenu.setOnAction(event -> handleBtnMenuAction(event));
		btnCheckURL.setOnAction(event -> handleBtnCheckUrlAction(event));
		btnUser.setOnAction(event -> handleBtnReadMeAction(event));
	}
	
	// 설명서
	public void handleBtnReadMeAction(ActionEvent event) {
		try {
			String path = System.getProperty("user.dir") + "/readme.txt";
			Desktop.getDesktop().edit(new File(path));
		} catch (IOException e) {

		}
	}
	
	// 주문현황 이동
	public void handleBtnOrderAction(ActionEvent event) {
		try {
			Stage stage = (Stage) btnOrder.getScene().getWindow();
			Parent order = FXMLLoader.load(Class.forName("zin.orderme.order.OrderController").getResource("order.fxml"));
			Scene scene = new Scene(order);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			System.out.println("Main Order 버튼오류 : " + e.getMessage());
		}
	}

	// 메뉴 이동
	public void handleBtnMenuAction(ActionEvent event) {
		try {
			Stage stage = (Stage) btnMenu.getScene().getWindow();
			Parent menu = FXMLLoader.load(Class.forName("zin.orderme.menu.MenuController").getResource("menu.fxml"));
			Scene scene = new Scene(menu);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			System.out.println("Main Menu 버튼오류 : " + e.getMessage());
		}
	}

	// URL 확인
	public void handleBtnCheckUrlAction(ActionEvent event) {
		try {
			// 다이얼로그 켜기
			Stage stage = (Stage) btnCheckURL.getScene().getWindow();

			Stage dialog = new Stage(StageStyle.UTILITY);
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(stage);
			dialog.setTitle("URL확인");

			Parent url = FXMLLoader.load(Class.forName("zin.orderme.main.MainController").getResource("ip_check.fxml"));
			Scene scene = new Scene(url);
			dialog.setScene(scene);
			dialog.setResizable(false);
			dialog.show();

			Button btnCan = (Button) url.lookup("#btnOk");
			btnCan.setOnAction(subEvent -> dialog.close());

			// 로컬ip 가져오기
			/*
			 * InetAddress ip = InetAddress.getLocalHost(); Label viewIp = (Label)
			 * parent.lookup("#ip"); viewIp.setText("컴퓨터 IP : " + ip.getHostAddress());
			 */

			// 외부ip 가져오기 출처 : https://codeday.me/ko/qa/20190310/37518.html
			URL getIp = new URL("http://checkip.amazonaws.com");
			BufferedReader in = new BufferedReader(new InputStreamReader(getIp.openStream()));
			String ip = in.readLine();
			Label viewIp = (Label) url.lookup("#ip");
			viewIp.setText("컴퓨터 IP : " + ip);

		} catch (Exception e) {
			System.out.println("Main CheckURL 버튼오류 : " + e.getMessage());
		}
	}
}

