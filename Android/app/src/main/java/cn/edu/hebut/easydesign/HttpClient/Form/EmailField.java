package cn.edu.hebut.easydesign.HttpClient.Form;

public class EmailField extends TextField {
    public EmailField(String fieldName, String data) throws Exception {
        super(fieldName, data);
        check();
    }
    private void check() throws Exception {
        if (data == null) {
            throw new IllegalArgumentException();
        }
    }
}
