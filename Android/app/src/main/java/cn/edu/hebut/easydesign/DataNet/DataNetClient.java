package cn.edu.hebut.easydesign.DataNet;

public class DataNetClient {
    private static String netAddress = "localhost:8081";
    private static DataNetClient client = new DataNetClient();
    private DataNetClient() {}
    public static DataNetClient getInstance() {
        return client;
    }
    
}
