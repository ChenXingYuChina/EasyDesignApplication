package cn.edu.hebut.easydesign.HttpClient.Form;

public class BooleanField extends TextField {
    public BooleanField(String fieldName, boolean data) {
        super(fieldName, data?"true":"false");
    }
}
