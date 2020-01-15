package cn.edu.hebut.easydesign.HttpClient;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class MyCookieJar implements CookieJar {
    private Map<String, List<Cookie>> cookieJar = new HashMap<>();
    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        cookieJar.put(url.host(), cookies);
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> list = cookieJar.get(url.host());
        if (list != null) {
            return list;
        }
        return new Vector<>();
    }

    Cookie DeleteCookie(HttpUrl url, String name) {
        List<Cookie> list = cookieJar.get(url.host());
        if (list != null) {
            for (Cookie c : list) {
                if (c.name().equals(name)) {
                    list.remove(c);
                    return c;
                }
            }
        }
        return null;
    }
    Cookie GetCookie(HttpUrl url, String name) {
        List<Cookie> list = cookieJar.get(url.host());
        long t = new Date().getTime();
        if (list != null) {
            for (Cookie c : list) {
                if (c.name().equals(name) && c.expiresAt() > t) {
                    return c;
                }
            }
        }
        return null;
    }
    boolean SetCookie(HttpUrl url, Cookie cookie) {
        long t = new Date().getTime();
        if (cookie.expiresAt() <= t) {
            return DeleteCookie(url, cookie.name()) != null;
        }
        List<Cookie> list = cookieJar.get(url.host());
        if (list != null) {
            for (int i = 0; i < list.size(); i ++) {
                if (list.get(i).name().equals(cookie.name())) {
                    list.set(i, cookie);
                    return true;
                }
            }
            list.add(cookie);
        } else {
            list = new Vector<>();
            list.add(cookie);
            cookieJar.put(url.host(), list);
        }
        return true;
    }
}
