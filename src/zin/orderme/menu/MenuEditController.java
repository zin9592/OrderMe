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
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import zin.orderme.menu.MenuData;
import zin.orderme.utils.DBConnection;
import zin.orderme.utils.Utils;
import zin.orderme.utils.DBConnection.ConnectType;

public class MenuEditController implements Initializable {
	private String selectedMenuName = MenuData.menuName;
	@FXML
	private Button btnBack;
	@FXML
	private Button btnEdit;
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
		DBConnection db = new DBConnection();
		String strSQL = "SELECT * FROM menu WHERE name ='" + selectedMenuName + "'";
		ResultSet rs = db.getDBContents(ConnectType.Query, strSQL);
		try {
			while (rs.next()) {
				menuName.setText(rs.getString("name"));
				menuPrice.setText(rs.getString("price"));
				menuImg.setText(rs.getString("src"));
				menuContent.setText(rs.getString("detail"));
			}
		} catch (Exception e) {
			System.out.println("MenuEdit �ʱ�ȭ���� : " + e.getMessage());
		}

		btnBack.setOnAction(event -> handleBtnBackAction(event));
		btnEdit.setOnAction(event -> handleBtnEditAction(event));
		file.setOnAction(event -> handleFileAction(event));
	}

	public void handleBtnBackAction(ActionEvent event) {
		try {
			// ����ȭ������ ������
			Stage stage = (Stage) btnBack.getScene().getWindow();
			Parent menu = FXMLLoader.load(Class.forName("zin.orderme.menu.MenuController").getResource("menu.fxml"));
			Scene scene = new Scene(menu);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			System.out.println("MenuEdit Back ��ư���� : " + e.getMessage());
		}
	}

	public void handleBtnEditAction(ActionEvent event) {
		// �ֹ���� �����
		DBConnection db = new DBConnection();
		String strSQL = "DROP table order_list";
		db.getDBContents(ConnectType.Table, strSQL);

		try {
			//�޴��̸� ��������
			String strMenu = menuName.getText();
			String name = strMenu.replaceAll(" ", "");
			
			//����üũ
			db = new DBConnection();
			strSQL = "Select count(name) From menu Where name = '"+name+"'";
			ResultSet rs = db.getDBContents(ConnectType.Query, strSQL);
			rs.next();
			if(rs.getInt(1) != 0 || menuName.getText().equals("")) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("�޴��̸� Ȯ��");
				alert.setHeaderText("�޴� �̸��� ���ų� �ߺ� �Ǿ����ϴ�.");
				alert.showAndWait();
				return;
			}else if(menuPrice.getText().equals("")) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("���� �̼���");
				alert.setHeaderText("������ �Է����ּ���");
				alert.showAndWait();
				return;
			}else if(menuImg.getText().equals("")) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("�̹��� ��ξ���");
				alert.setHeaderText("����� �޴��� �̹����� �Է����ּ���");
				alert.showAndWait();
				return;
			}
			
			// �̹��� �����۾�
			String imageURL = menuImg.getText();
			String copyPath = System.getProperty("user.dir") +"/image/"+ Utils.imageName;
			copyPath = Utils.fileCopy(imageURL, copyPath);
			
			strSQL = "UPDATE menu SET name = '" + name + "', price = '" + menuPrice.getText()
					+ "', src = '" + copyPath + "', detail = '" + menuContent.getText() + "' WHERE name = '"
					+ selectedMenuName + "'";
			db.getDBContents(ConnectType.Update, strSQL);
			Utils.imageName = null;
			
			// ����ȭ������ ������
			Stage stage = (Stage) btnEdit.getScene().getWindow();
			Parent menu = FXMLLoader.load(Class.forName("zin.orderme.menu.MenuController").getResource("menu.fxml"));
			Scene scene = new Scene(menu);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			System.out.println("MenuEdit Edit ��ư���� : " + e.getMessage());
		}
	}

	public void handleFileAction(ActionEvent event) {
		try {
			Stage stage = (Stage) btnBack.getScene().getWindow();

			Stage dialog = new Stage(StageStyle.UTILITY);
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(stage);
			dialog.setTitle("����ã��");

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

		}
	}

}
