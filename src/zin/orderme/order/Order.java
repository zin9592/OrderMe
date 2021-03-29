package zin.orderme.order;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

// OrderView의 속성들
public class Order {
	private StringProperty number;
	private StringProperty date;
	private StringProperty contents;
	
	public Order(String _number, String _date, String _contents) {
		number = new SimpleStringProperty(_number);
		date = new SimpleStringProperty(_date);
		contents = new SimpleStringProperty(_contents);
	}
	
    public StringProperty getNumberProperty() {
        return number;
    }
    public StringProperty getDateProperty() {
        return date;
    }
    public StringProperty getContentsProperty() {
        return contents;
    }
    
    public void setNumberProperty(String _number) {
        number.set(_number);
    }
    public void setDateProperty(String _date) {
       date.set(_date);
    }
    public void setContentsProperty(String _contents) {
        contents.set(_contents);
    }
}
