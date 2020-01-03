package cn.edu.hebut.easydesign.DataNet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.LinkedList;

public class DataNetClient {
    private static final String serverHostName = "localhost";
    private static final int serverPort = 8081;
    private static DataNetClient client = new DataNetClient();
    private DataNetClient() {
        dataList = new LinkedList<>();
    }
    public static DataNetClient getInstance() {
        if (client != null) {
            return client;
        }
        synchronized (DataNetClient.class) {
            if (client != null) {
                client = new DataNetClient();
            }
        }
        return client;
    }
    private Socket con = null;
    private LinkedList<byte[]> dataList;
    private InputStream inputStream = null;
    private OutputStream outputStream = null;
    private void connect(long id) throws Exception {
        if (con == null){
            con = new Socket();
        } else if (!con.isClosed() && con.getKeepAlive()) {
            return;
        }
        con.connect(new InetSocketAddress(serverHostName, 8081));
        inputStream = con.getInputStream();
        outputStream = con.getOutputStream();
        outputStream.write(new byte[]{(byte)id, (byte)(id >> 8), (byte)(id >> 16), (byte)(id >> 24),
                (byte)(id >> 32), (byte)(id >> 40), (byte)(id >> 48), (byte)(id >> 56)});
        int start = inputStream.read();
        System.out.println(String.valueOf((char) start));
        if ((byte)start != 's') {
            throw new Exception();
        }
    }

    public void PutData(byte[] data) {
        dataList.add(data);
    }

    public void Clear() throws IOException {
        dataList.clear();
        con.close();
    }

    public void Process(long id) throws Exception {
        byte[] data = dataList.peek();
        if (data == null) {
            throw new Exception();
        }
        connect(id);
        while (data != null) {
//            System.out.println(data.length);
            outputStream.write(new byte[]{(byte)(data.length), (byte)(data.length >> 8), (byte)(data.length >> 16), (byte)(data.length >> 24)});
            outputStream.write(data);
            dataList.remove();
            data = dataList.peek();
        }
    }

    public boolean CheckFinish(byte key) throws Exception {
        return 'f' == inputStream.read();
    }
}
