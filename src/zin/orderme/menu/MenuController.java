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

	// 메뉴 화면이동
	@FXML
	private ImageView btnLeft;
	@FXML
	private ImageView btnRight;
	@FXML
	private Slider menuSlider;

	private ArrayList<MenuData> menuList = new ArrayList<MenuData>(); // 메뉴 자료 저장소
	private int idx = 0; // 메뉴 번호

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
			// 메뉴 화면에 생성하기
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
					menu_price_1.setText(menuList.get(idx * 4 + i).price + "원");
					img = new Image((menuList.get(idx * 4 + i).src == "") ? "" : menuList.get(idx * 4 + i).src);
					menu_img_1.setImage(img);
					break;
				case 1:
					menu_name_2.setText(menuList.get(idx * 4 + i).name);
					menu_price_2.setText(menuList.get(idx * 4 + i).price + "원");
					img = new Image((menuList.get(idx * 4 + i).src == "") ? "" : menuList.get(idx * 4 + i).src);
					menu_img_2.setImage(img);
					break;
				case 2:
					menu_name_3.setText(menuList.get(idx * 4 + i).name);
					menu_price_3.setText(menuList.get(idx * 4 + i).price + "원");
					img = new Image((menuList.get(idx * 4 + i).src == "") ? "" : menuList.get(idx * 4 + i).src);
					menu_img_3.setImage(img);
					break;
				case 3:
					menu_name_4.setText(menuList.get(idx * 4 + i).name);
					menu_price_4.setText(menuList.get(idx * 4 + i).price + "원");
					img = new Image((menuList.get(idx * 4 + i).src == "") ? "" : menuList.get(idx * 4 + i).src);
					menu_img_4.setImage(img);
					break;
				}
			}
			rs.close();

			// 슬라이더 설정하기
			menuSlider.setMax((int) menuList.size() / 4);
			menuSlider.valueProperty().addListener(new ChangeListener<Number>() {
				@Override
				public void changed(ObservableValue<? extends Number> arg, Number oldNumber, Number newNumber) {
					idx = newNumber.intValue();
					handleNextAction(null);
				}
			});
		} catch (Exception e) {
			System.out.println("Menu Init 오류 : " + e.getMessage());
		}
	}

	// Delete버튼 이벤트처리
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
			
			// 다이얼로그 켜기
			Stage stage = (Stage) btnBack.getScene().getWindow();

			Stage dialog = new Stage(StageStyle.UTILITY);
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(stage);
			dialog.setTitle("삭제확인");

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
			System.out.println("Menu Delete 버튼오류 : " + e.getMessage());
		}
	}

	// DB내용 삭제 메소드
	public void dbDelete(MouseEvent event, Stage dialog) {
		// 주문목록 재생성
		DBConnection db = new DBConnection();
		String strSQL = "DROP table order_list";
		db.getDBContents(ConnectType.Table, strSQL);
		try {
			// DB에서 해당내용 삭제
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

			// 페이지 새로고침
			Stage stage = (Stage) btnBack.getScene().getWindow();
			Parent order = FXMLLoader.load(Class.forName("zin.orderme.menu.MenuController").getResource("menu.fxml"));
			Scene scene = new Scene(order);
			stage.setScene(scene);
			stage.show();

			// 다이얼로그 끄기
			dialog.close();

			// 슬라이더 맥스치 재설정
			menuSlider.setMax((int) menuList.size() / 4);
		} catch (Exception e) {
			System.out.println("Menu DB삭제 오류 : " + e.getMessage());
		}
	}

	// Edit버튼 이벤트처리
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
			System.out.println("Menu Edit 버튼오류 : " + e.getMessage());
		}
	}

	// Add버튼 이벤트처리
	public void handleBtnMenuAddAction(ActionEvent event) {
		try {
			Stage stage = (Stage) btnMenuAdd.getScene().getWindow();
			Parent add = FXMLLoader.load(Class.forName("zin.orderme.menu.MenuAddController").getResource("menu_add.fxml"));
			Scene scene = new Scene(add);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			System.out.println("Menu Add 버튼오류 : " + e.getMessage());
		}
	}

	// Back버튼 이벤트처리
	public void handleBtnBackAction(ActionEvent event) {
		try {
			Stage stage = (Stage) btnBack.getScene().getWindow();
			Parent main = FXMLLoader.load(Class.forName("zin.orderme.main.MainController").getResource("main.fxml"));
			Scene scene = new Scene(main);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			System.out.println("Menu Back 버튼오류 : " + e.getMessage());
		}
	}

	// Next버튼 이벤트처리
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
						menu_price_1.setText(menuList.get(idx * 4 + i).price + "원");
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
						menu_price_2.setText(menuList.get(idx * 4 + i).price + "원");
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
						menu_price_3.setText(menuList.get(idx * 4 + i).price + "원");
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
						menu_price_4.setText(menuList.get(idx * 4 + i).price + "원");
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
			System.out.println("Menu Next 버튼오류 : " + e.getMessage());
		}
	}
}
