package cn.edu.hebut.easydesign.DataNetTest;

import org.junit.Assert;
import org.junit.Test;

import cn.edu.hebut.easydesign.DataNet.DataNetClient;

public class DataNetUnitTest {
    @Test
    public void TestConnect() {
        DataNetClient client = DataNetClient.getInstance();
        client.PutData(new byte[]{1,2,3,4,5});
        try {
            client.Process(0xffffffffffffL);
            client.CheckFinish((byte)'f');
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
