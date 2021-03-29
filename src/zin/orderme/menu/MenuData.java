package zin.orderme.menu;

// 메뉴에서 DB에서 가져온 메뉴내용을 저장하는 클래스
//Menu에서 MenuEdit으로 데이터를 보내기 위한 클래스
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
