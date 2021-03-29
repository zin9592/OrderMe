package zin.orderme.order;

import java.net.URL;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import zin.orderme.utils.DBConnection;
import zin.orderme.utils.DBConnection.ConnectType;

public class OrderController implements Initializable {
	@FXML
	private Button btnBack;
	@FXML
	private Button test;

	@FXML
	private TableView<Order> orderView;
	@FXML
	private TableColumn<Order, String> orderNumber;
	@FXML
	private TableColumn<Order, String> orderDate;
	@FXML
	private TableColumn<Order, String> orderContent;

	private Timer work = new Timer();
	private Task job = new Task();
	private ObservableList<Order> myList = FXCollections.observableArrayList();
	private Object oldValue;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		btnBack.setOnAction(event -> handleBtnBackAction(event));
		test.setOnAction(event -> handleBtnTestAction(event));
		orderView.setOnMouseClicked(event -> handleTableSelectedAction(event));
		/*
		 * setCellValueFactory �޼���� �� �÷��� ���� ���� ���� �� �ִ� Callback �������̽��� ����Ѵ�.
		 * PropertyValueFactory Ŭ������ Callback �������̽��� �����ϰ� ������ �� �÷��� ���� cell value
		 * factory�� ���ǰ� �ִ�. PropertyValueFactory�� �����ڿ��� �÷����� �Ķ���ͷ� �־����µ�, call �޼��尡
		 * ȣ��Ǹ� �� �÷��� �ش��ϴ� �÷� ���� ��ȯ�Ѵ�.
		 */
		orderNumber.setCellValueFactory(cellData -> cellData.getValue().getNumberProperty());
		orderDate.setCellValueFactory(cellData -> cellData.getValue().getDateProperty());
		orderContent.setCellValueFactory(cellData -> cellData.getValue().getContentsProperty());

		// �ֹ���� ���̺��� ���� �� �ֹ���� ���̺� ����
		DBConnection db = new DBConnection();
		try {
			String menuSQL = "create table order_list (	"
					+ "idx int primary key auto_increment, "
					+ "price int(11), "
					+ "order_record TEXT, "
					+ "complete_flag tinyint(1) default '0',"
					+ "order_date DATETIME"
					+ ") DEFAULT CHARSET=utf8 COLLATE utf8_general_ci";
			db.getDBContents(ConnectType.Table, menuSQL);
		} catch (Exception e) {
			System.out.println("table not changed..");
		}
		work.scheduleAtFixedRate(job, (long) 5000, (long) 5000);
	}

	public void handleBtnBackAction(ActionEvent event) {
		try {
			work.cancel();
			Stage stage = (Stage) btnBack.getScene().getWindow();
			Parent main = FXMLLoader.load(Class.forName("zin.orderme.main.MainController").getResource("main.fxml"));
			Scene scene = new Scene(main);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {

		}
	}

	// �׽�Ʈ ��ǲ
	public void handleBtnTestAction(ActionEvent event) {
		try {
			Random rand = new Random();
			String[] menuNames = {"����","������","�Ľ�Ÿ","�ſ��Ľ�Ÿ"};
			int rntCount = rand.nextInt(menuNames.length);
			int rntCount2;
			do {
				rntCount2 = rand.nextInt(menuNames.length);
			}while(rntCount == rntCount2);
			
			
			String selectedMenu = menuNames[rntCount];
			String selectedMenu2 = menuNames[rntCount2];
			
			DBConnection db = new DBConnection();
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currentTime = sdf.format(date);

			String menuSQL = "insert into order_list(price, order_record, order_date) values ("
					+ rand.nextInt(30000) + ", "
					+ "'"+ selectedMenu + rand.nextInt(3) + "�� " + selectedMenu2 + rand.nextInt(3) + "�� ',"
					+ "'"+ currentTime + "')";

			/*
			String menuSQL = "insert into order_list(idx, price, order_record, order_date) values ("
					+ "100, " +
					+ rand.nextInt(30000) + ", "
					+ "'123123232dasdasdsadsdssdadasdas',"
					+ "'"+ currentTime + "')";*/
			
			db.getDBContents(ConnectType.Update, menuSQL);
			System.out.println("���� �߰��Ϸ�");
		} catch (Exception e) {

		}
	}
	// �ֹ����� �ڼ��� ����
	public void handleTableSelectedAction(MouseEvent event) {
		if(orderView.getSelectionModel().getSelectedItem() != null) {
			if(event.getPickResult().getIntersectedNode().equals(oldValue)) {
				String idx = orderView.getSelectionModel().getSelectedItem().getNumberProperty().getValue();
				// ���̾�α� ����
				try {
					Stage stage = (Stage) btnBack.getScene().getWindow();
	
					Stage dialog = new Stage(StageStyle.UTILITY);
					dialog.initModality(Modality.NONE);
					dialog.initOwner(stage);
					dialog.setTitle("�ֹ�ȭ��");
	
					Parent popup = FXMLLoader.load(Class.forName("zin.orderme.order.OrderController").getResource("orderList.fxml"));
					Scene scene = new Scene(popup);
					dialog.setScene(scene);
					dialog.setResizable(false);
					dialog.show();
					
					String strList = "";	//�󼼳���
					try {
						DBConnection dbOrder = new DBConnection();
						String strOrderSQL = "select * from order_list where idx = '"+idx+"'";
						ResultSet rsOrder = dbOrder.getDBContents(ConnectType.Query, strOrderSQL);
						rsOrder.next();
						strList = rsOrder.getString("order_record");
						strList = strList.replaceAll(" ", "\n");
						
					}catch(Exception e) {
						System.out.println("contents error >>> "+e.getMessage());
					}
					
					TextArea list = (TextArea) popup.lookup("#list");
					list.setText(strList);
	
					Button btnCom = (Button) popup.lookup("#btnComplete");
					btnCom.setOnAction(subEvent -> orderComplete(event, dialog, idx));
	
					Button btnCan = (Button) popup.lookup("#btnCancle");
					btnCan.setOnAction(subEvent -> dialog.close());
					
					
					orderView.getSelectionModel().clearSelection();
	                oldValue = null;
				}catch(Exception e) {
					
				}
			}else {
				oldValue = event.getPickResult().getIntersectedNode();
			}
		}
	};
	//�ֹ��Ϸᰡ �Ǹ� ���¸� 1�� ����� ��ư
	public void orderComplete(MouseEvent event, Stage dialog, String idx) {
		try {
			DBConnection db = new DBConnection();
			String menuSQL = "update order_list set complete_flag = '1' where idx = '"+idx+"'";
			db.getDBContents(ConnectType.Update, menuSQL);
			dialog.close();
		} catch (Exception e) {

		}
	}
	
	
	private class Task extends TimerTask {
		@Override
		public void run() {
			try {
				System.out.println("This Frame is work!");
				myList.clear();
				// Į���߰�
				try {
					DBConnection dbOrder = new DBConnection();
					String strOrderSQL = "select * from order_list";
					ResultSet rsOrder = dbOrder.getDBContents(ConnectType.Query, strOrderSQL);

					while (rsOrder.next()) {
						String contents = rsOrder.getString("order_record");
						if(contents.length() > 20) {
							contents.substring(0,20);
							contents += "...";
						}
						myList.add(new Order(rsOrder.getString("idx"), rsOrder.getString("order_date"), contents));
					}
					orderView.setItems(myList);

					rsOrder.close();
				} catch (Exception e) {
					System.out.println("col add error.." + e.getMessage());
				}
			} catch (Exception e) {

			}
		}
	}
	
}
