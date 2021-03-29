package zin.orderme.menu;

import java.io.File;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import zin.orderme.utils.DBConnection;
import zin.orderme.utils.Utils;
import zin.orderme.utils.DBConnection.ConnectType;

public class MenuAddController implements Initializable {
	@FXML
	private Button btnBack;
	@FXML
	private Button btnAdd;
	@FXML
	private Button file;
	@FXML
	private TextField menuName;
	@FXML
	private TextField menuPrice;
	@FXML
	private TextField menuImg;
	@FXML
	private TextArea menuContent;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		btnBack.setOnAction(event -> handleBtnBackAction(event));
		btnAdd.setOnAction(event -> handleBtnAddAction(event));
		file.setOnAction(event -> handleFileAction(event));
	}

	public void handleBtnBackAction(ActionEvent event) {
		try {
			Stage stage = (Stage) btnBack.getScene().getWindow();
			Parent menu = FXMLLoader.load(Class.forName("zin.orderme.menu.MenuController").getResource("menu.fxml"));
			Scene scene = new Scene(menu);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			System.out.println("MenuAdd Back 버튼오류 : " + e.getMessage());
		}
	}

	public void handleBtnAddAction(ActionEvent event) {
		// 주문목록 재생성
		DBConnection db = new DBConnection();
		String strSQL = "DROP table order_list";
		db.getDBContents(ConnectType.Table, strSQL);
		
		try {
			//메뉴이름 공백제거
			String strMenu = menuName.getText();
			String name = strMenu.replaceAll(" ", "");
			
			//오류체크
			db = new DBConnection();
			strSQL = "Select count(name) From menu Where name = '"+name+"'";
			ResultSet rs = db.getDBContents(ConnectType.Query, strSQL);
			rs.next();
			if(rs.getInt(1) != 0 || menuName.getText().equals("")) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("메뉴이름 확인");
				alert.setHeaderText("메뉴 이름이 없거나 중복 되었습니다.");
				alert.showAndWait();
				return;
			}else if(menuPrice.getText().equals("")) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("가격 미설정");
				alert.setHeaderText("가격을 입력해주세요");
				alert.showAndWait();
				return;
			}else if(menuImg.getText().equals("")) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("이미지 경로없음");
				alert.setHeaderText("등록할 메뉴의 이미지를 입력해주세요");
				alert.showAndWait();
				return;
			}
			
			// 이미지 복사작업 System.getProperty("user.dir") + "/src/zin/orderme/image/" + 
			String imageURL = menuImg.getText();
			String copyPath = System.getProperty("user.dir") +"/image/"+ Utils.imageName;
			copyPath = Utils.fileCopy(imageURL, copyPath);
			
			//메뉴넣기
			strSQL = "Insert into menu(name, price, src, detail) values ('" + name + "', '"
					+ menuPrice.getText() + "', '" + copyPath + "', '" + menuContent.getText() + "')";
			db.getDBContents(ConnectType.Update, strSQL);
			
			Stage stage = (Stage) btnBack.getScene().getWindow();
			Parent menu = FXMLLoader.load(Class.forName("zin.orderme.menu.MenuController").getResource("menu.fxml"));
			Scene scene = new Scene(menu);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			System.out.println("MenuAdd Add 버튼오류 : " + e.getMessage());
		}
	}

	public void handleFileAction(ActionEvent event) {
		try {
			Stage stage = (Stage) btnBack.getScene().getWindow();

			Stage dialog = new Stage(StageStyle.UTILITY);
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(stage);
			dialog.setTitle("파일찾기");

			FileChooser fileChooser = new FileChooser();
			fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JPG", "*.jpg"),
					new FileChooser.ExtensionFilter("PNG", "*.png"), new FileChooser.ExtensionFilter("BMP", "*.bmp"));
			File selectedFile = fileChooser.showOpenDialog(stage);
			dialog.show();
			if (selectedFile != null) {
				menuImg.setText(selectedFile.getPath());
				Utils.imageName = selectedFile.getName();
				dialog.close();
			} else
				dialog.close();

		} catch (Exception e) {
			System.out.println("MenuAdd File 찾기오류 : " + e.getMessage());
		}
	}
}
