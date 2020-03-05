package cn.edu.hebut.easydesign.HttpClient;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import cn.edu.hebut.easydesign.HttpClient.Form.Form;
import okhttp3.Cookie;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Client {
    public static final String netAddress = "192.168.31.216";
//    public static final String netAddress = "175.24.76.161";

    private static Client instance = new Client();
    private Client(){
        client = new OkHttpClient.Builder().readTimeout(3, TimeUnit.SECONDS).cookieJar(cookieJar).build();
    }
    public static Client getInstance() {
        return instance;
    }
    public OkHttpClient client;
    private HttpUrl host = new HttpUrl.Builder().scheme("http").host(netAddress).build();
    private MyCookieJar cookieJar = new MyCookieJar();

    public void setCookie(Cookie cookie) {
        cookieJar.SetCookie(host, cookie);
    }
    public Cookie DeleteCookie(String name) {
        return cookieJar.DeleteCookie(host, name);
    }
    public Cookie GetCookie(String name) {
        return cookieJar.GetCookie(host, name);
    }

    /* if like http://host:port/path...?xx=xx&xx=xx
    url will like path...?xx=xx&xx=xx
    it will block the thread so don't use it on UI thread
    */
    public Response GetFromHost(String url) throws Exception {
        return client.newCall(new Request.Builder().url(host.url() + url).build()).execute();
    }

    /*
    full url call for the resources in other host
     */
    public Response Get(String url) throws Exception {
        return client.newCall(new Request.Builder().url(url).build()).execute();
    }

    /*
    full url call for the resources in other host
     */
    public Response Get(URL url) throws Exception {
        return client.newCall(new Request.Builder().url(url).build()).execute();
    }

    public Response PostToHost(String url, Form form) throws Exception {
//        Log.i("ED", "PostToHost: " + host.url());
        return client.newCall(new Request.Builder().
                url(host.url() + url).
                method("POST", form.parse()).
                build()).execute();
    }

    // tools if required
    public static int GetStatusCode(Response response) {
        return response.code();
    }
}
