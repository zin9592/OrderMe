package zin.orderme.menu;

// �޴����� DB���� ������ �޴������� �����ϴ� Ŭ����
//Menu���� MenuEdit���� �����͸� ������ ���� Ŭ����
public class MenuData {
	public static String menuName = null;
	String name;
	String price;
	String src;
	
	MenuData(String _name, String _price, String _src){
		name = _name;
		price = _price;
		src = "file:///"+_src;
	}
}
