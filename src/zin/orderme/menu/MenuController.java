package zin.orderme.menu;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import zin.orderme.menu.MenuData;
import zin.orderme.utils.DBConnection;
import zin.orderme.utils.DBConnection.ConnectType;
import zin.orderme.utils.Utils;

public class MenuController implements Initializable {
	@FXML
	private Button btnBack;
	@FXML
	private Button btnMenuAdd;

	@FXML
	private Label menu_name_1;
	@FXML
	private Label menu_name_2;
	@FXML
	private Label menu_name_3;
	@FXML
	private Label menu_name_4;

	@FXML
	private Label menu_price_1;
	@FXML
	private Label menu_price_2;
	@FXML
	private Label menu_price_3;
	@FXML
	private Label menu_price_4;

	@FXML
	private ImageView menu_img_1;
	@FXML
	private ImageView menu_img_2;
	@FXML
	private ImageView menu_img_3;
	@FXML
	private ImageView menu_img_4;

	@FXML
	private ImageView menu_del_1;
	@FXML
	private ImageView menu_del_2;
	@FXML
	private ImageView menu_del_3;
	@FXML
	private ImageView menu_del_4;

	@FXML
	private ImageView menu_edit_1;
	@FXML
	private ImageView menu_edit_2;
	@FXML
	private ImageView menu_edit_3;
	@FXML
	private ImageView menu_edit_4;

	// �޴� ȭ���̵�
	@FXML
	private ImageView btnLeft;
	@FXML
	private ImageView btnRight;
	@FXML
	private Slider menuSlider;

	private ArrayList<MenuData> menuList = new ArrayList<MenuData>(); // �޴� �ڷ� �����
	private int idx = 0; // �޴� ��ȣ

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		btnBack.setOnAction(event -> handleBtnBackAction(event));
		btnMenuAdd.setOnAction(event -> handleBtnMenuAddAction(event));

		menu_del_1.setOnMouseClicked(event -> handleBtnDelAction(event));
		menu_del_2.setOnMouseClicked(event -> handleBtnDelAction(event));
		menu_del_3.setOnMouseClicked(event -> handleBtnDelAction(event));
		menu_del_4.setOnMouseClicked(event -> handleBtnDelAction(event));

		menu_edit_1.setOnMouseClicked(event -> handleBtnEditAction(event));
		menu_edit_2.setOnMouseClicked(event -> handleBtnEditAction(event));
		menu_edit_3.setOnMouseClicked(event -> handleBtnEditAction(event));
		menu_edit_4.setOnMouseClicked(event -> handleBtnEditAction(event));

		btnLeft.setOnMouseClicked(event -> handleNextAction(event));
		btnRight.setOnMouseClicked(event -> handleNextAction(event));
		try {
			DBConnection db = new DBConnection();
			String strSQL = "create table menu("
					+ "idx int primary key auto_increment, "
					+ "name VARCHAR(10) not null, "
					+ "price varchar(10) not null, "
					+ "src varchar(100) not null, "
					+ "detail text not null"
					+ ") DEFAULT CHARSET=utf8 COLLATE utf8_general_ci";
			db.getDBContents(ConnectType.Table, strSQL);
		}catch(Exception e) {
			
		}
		
		
		try {
			// �޴� ȭ�鿡 �����ϱ�
			DBConnection db = new DBConnection();
			String strSQL = "SELECT * FROM menu";
			ResultSet rs = db.getDBContents(ConnectType.Query, strSQL);

			while (rs.next()) {
				menuList.add(new MenuData(rs.getString("name"), rs.getString("price"), rs.getString("src")));
			}

			for (int i = 0; i < ((menuList.size() - idx * 4 < 4) ? menuList.size() - idx * 4 : 4); ++i) {
				Image img;
				switch (i) {
				case 0:
					menu_name_1.setText(menuList.get(idx * 4 + i).name);
					menu_price_1.setText(menuList.get(idx * 4 + i).price + "��");
					img = new Image((menuList.get(idx * 4 + i).src == "") ? "" : menuList.get(idx * 4 + i).src);
					menu_img_1.setImage(img);
					break;
				case 1:
					menu_name_2.setText(menuList.get(idx * 4 + i).name);
					menu_price_2.setText(menuList.get(idx * 4 + i).price + "��");
					img = new Image((menuList.get(idx * 4 + i).src == "") ? "" : menuList.get(idx * 4 + i).src);
					menu_img_2.setImage(img);
					break;
				case 2:
					menu_name_3.setText(menuList.get(idx * 4 + i).name);
					menu_price_3.setText(menuList.get(idx * 4 + i).price + "��");
					img = new Image((menuList.get(idx * 4 + i).src == "") ? "" : menuList.get(idx * 4 + i).src);
					menu_img_3.setImage(img);
					break;
				case 3:
					menu_name_4.setText(menuList.get(idx * 4 + i).name);
					menu_price_4.setText(menuList.get(idx * 4 + i).price + "��");
					img = new Image((menuList.get(idx * 4 + i).src == "") ? "" : menuList.get(idx * 4 + i).src);
					menu_img_4.setImage(img);
					break;
				}
			}
			rs.close();

			// �����̴� �����ϱ�
			menuSlider.setMax((int) menuList.size() / 4);
			menuSlider.valueProperty().addListener(new ChangeListener<Number>() {
				@Override
				public void changed(ObservableValue<? extends Number> arg, Number oldNumber, Number newNumber) {
					idx = newNumber.intValue();
					handleNextAction(null);
				}
			});
		} catch (Exception e) {
			System.out.println("Menu Init ���� : " + e.getMessage());
		}
	}

	// Delete��ư �̺�Ʈó��
	public void handleBtnDelAction(MouseEvent event) {
		try {
			String menuName = "";

			if (event.getSource().equals(menu_del_1)) {
				menuName = menu_name_1.getText();
			} else if (event.getSource().equals(menu_del_2)) {
				menuName = menu_name_2.getText();
			} else if (event.getSource().equals(menu_del_3)) {
				menuName = menu_name_3.getText();
			} else if (event.getSource().equals(menu_del_4)) {
				menuName = menu_name_4.getText();
			};
			
			if (menuName.equals("-"))
				return;
			
			// ���̾�α� �ѱ�
			Stage stage = (Stage) btnBack.getScene().getWindow();

			Stage dialog = new Stage(StageStyle.UTILITY);
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(stage);
			dialog.setTitle("����Ȯ��");

			Parent popup = FXMLLoader.load(Class.forName("zin.orderme.menu.MenuController").getResource("popup_del.fxml"));
			Scene scene = new Scene(popup);
			dialog.setScene(scene);
			dialog.setResizable(false);
			dialog.show();

			Button btnDel = (Button) popup.lookup("#btnDel");
			btnDel.setOnAction(subEvent -> dbDelete(event, dialog));

			Button btnCan = (Button) popup.lookup("#btnCan");
			btnCan.setOnAction(subEvent -> dialog.close());

		} catch (Exception e) {
			System.out.println("Menu Delete ��ư���� : " + e.getMessage());
		}
	}

	// DB���� ���� �޼ҵ�
	public void dbDelete(MouseEvent event, Stage dialog) {
		// �ֹ���� �����
		DBConnection db = new DBConnection();
		String strSQL = "DROP table order_list";
		db.getDBContents(ConnectType.Table, strSQL);
		try {
			// DB���� �ش系�� ����
			String selectedMenuName = "";
			if (event.getSource().equals(menu_del_1)) {
				selectedMenuName = menu_name_1.getText();
			} else if (event.getSource().equals(menu_del_2)) {
				selectedMenuName = menu_name_2.getText();
			} else if (event.getSource().equals(menu_del_3)) {
				selectedMenuName = menu_name_3.getText();
			} else if (event.getSource().equals(menu_del_4)) {
				selectedMenuName = menu_name_4.getText();
			};
			strSQL = "DELETE FROM menu WHERE name = '" + selectedMenuName + "'";
			db.getDBContents(ConnectType.Update, strSQL);

			// ������ ���ΰ�ħ
			Stage stage = (Stage) btnBack.getScene().getWindow();
			Parent order = FXMLLoader.load(Class.forName("zin.orderme.menu.MenuController").getResource("menu.fxml"));
			Scene scene = new Scene(order);
			stage.setScene(scene);
			stage.show();

			// ���̾�α� ����
			dialog.close();

			// �����̴� �ƽ�ġ �缳��
			menuSlider.setMax((int) menuList.size() / 4);
		} catch (Exception e) {
			System.out.println("Menu DB���� ���� : " + e.getMessage());
		}
	}

	// Edit��ư �̺�Ʈó��
	public void handleBtnEditAction(MouseEvent event) {
		String menuName = "";

		if (event.getSource().equals(menu_edit_1)) {
			menuName = menu_name_1.getText();
		} else if (event.getSource().equals(menu_edit_2)) {
			menuName = menu_name_2.getText();
		} else if (event.getSource().equals(menu_edit_3)) {
			menuName = menu_name_3.getText();
		} else if (event.getSource().equals(menu_edit_4)) {
			menuName = menu_name_4.getText();
		}
		;
		if (menuName.equals("-"))
			return;

		MenuData.menuName = menuName;

		try {
			Stage stage = (Stage) btnBack.getScene().getWindow();
			Parent edit = FXMLLoader.load(Class.forName("zin.orderme.menu.MenuEditController").getResource("menu_edit.fxml"));
			Scene scene = new Scene(edit);
			stage.setScene(scene);
			stage.setTitle(menuName);
			stage.show();
		} catch (Exception e) {
			System.out.println("Menu Edit ��ư���� : " + e.getMessage());
		}
	}

	// Add��ư �̺�Ʈó��
	public void handleBtnMenuAddAction(ActionEvent event) {
		try {
			Stage stage = (Stage) btnMenuAdd.getScene().getWindow();
			Parent add = FXMLLoader.load(Class.forName("zin.orderme.menu.MenuAddController").getResource("menu_add.fxml"));
			Scene scene = new Scene(add);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			System.out.println("Menu Add ��ư���� : " + e.getMessage());
		}
	}

	// Back��ư �̺�Ʈó��
	public void handleBtnBackAction(ActionEvent event) {
		try {
			Stage stage = (Stage) btnBack.getScene().getWindow();
			Parent main = FXMLLoader.load(Class.forName("zin.orderme.main.MainController").getResource("main.fxml"));
			Scene scene = new Scene(main);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			System.out.println("Menu Back ��ư���� : " + e.getMessage());
		}
	}

	// Next��ư �̺�Ʈó��
	public void handleNextAction(MouseEvent event) {
		try {
			if (event == null) {
			} else if (event.getSource().equals(btnLeft) && idx > 0) {
				--idx;
				menuSlider.setValue(idx);
			} else if (event.getSource().equals(btnRight) && (idx + 1) * 4 < menuList.size()) {
				++idx;
				menuSlider.setValue(idx);
			}

			for (int i = 0; i < 4; ++i) {
				Image img;
				switch (i) {
				case 0:
					if (menuList.size() - idx * 4 > 0) {
						menu_name_1.setText(menuList.get(idx * 4 + i).name);
						menu_price_1.setText(menuList.get(idx * 4 + i).price + "��");
						img = new Image((menuList.get(idx * 4 + i).src == "") ? "" : menuList.get(idx * 4 + i).src);
						menu_img_1.setImage(img);
					} else {
						menu_name_1.setText(Utils.NONE);
						menu_price_1.setText(Utils.NONE);
						img = new Image(Utils.IMAGEPATH);
						menu_img_1.setImage(img);
					}
					break;
				case 1:
					if (menuList.size() - idx * 4 > 1) {
						menu_name_2.setText(menuList.get(idx * 4 + i).name);
						menu_price_2.setText(menuList.get(idx * 4 + i).price + "��");
						img = new Image((menuList.get(idx * 4 + i).src == "") ? "" : menuList.get(idx * 4 + i).src);
						menu_img_2.setImage(img);
					} else {
						menu_name_2.setText(Utils.NONE);
						menu_price_2.setText(Utils.NONE);
						img = new Image(Utils.IMAGEPATH);
						menu_img_2.setImage(img);
					}
					break;
				case 2:
					if (menuList.size() - idx * 4 > 2) {
						menu_name_3.setText(menuList.get(idx * 4 + i).name);
						menu_price_3.setText(menuList.get(idx * 4 + i).price + "��");
						img = new Image((menuList.get(idx * 4 + i).src == "") ? "" : menuList.get(idx * 4 + i).src);
						menu_img_3.setImage(img);
					} else {
						menu_name_3.setText(Utils.NONE);
						menu_price_3.setText(Utils.NONE);
						img = new Image(Utils.IMAGEPATH);
						menu_img_3.setImage(img);
					}
					break;
				case 3:
					if (menuList.size() - idx * 4 > 3) {
						menu_name_4.setText(menuList.get(idx * 4 + i).name);
						menu_price_4.setText(menuList.get(idx * 4 + i).price + "��");
						img = new Image((menuList.get(idx * 4 + i).src == "") ? "" : menuList.get(idx * 4 + i).src);
						menu_img_4.setImage(img);
					} else {
						menu_name_4.setText(Utils.NONE);
						menu_price_4.setText(Utils.NONE);
						img = new Image(Utils.IMAGEPATH);
						menu_img_4.setImage(img);
					}
					break;
				}
			}
		} catch (Exception e) {
			System.out.println("Menu Next ��ư���� : " + e.getMessage());
		}
	}
}
