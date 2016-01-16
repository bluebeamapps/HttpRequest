package com.bluebeamapps;

import android.test.AndroidTestCase;

import com.bluebeamapps.httprequest.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.CountDownLatch;

/**
 * Created by brunoaguiar on 1/11/16.
 */
public class HttpRequestTests extends AndroidTestCase {

    public void testConvenienceHttpRequestGet() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        String host = "http://www.mocky.io/v2";
        String path = "56945888110000731483a72f";

        HttpRequest.get(getContext(), host, path, new HttpRequest.OnHttpRequestFinishedListener() {
            @Override
            public void onHttpRequestSuccess(int statusCode, String data) {
                assertTrue(statusCode != -1 && data != null && !data.isEmpty());
                signal.countDown();
            }

            @Override
            public void onHttpRequestError(int statusCode, String data) {
                assertTrue(statusCode != -1);
                signal.countDown();
            }
        });

        signal.await();
    }

    public void testConvenienceHttpRequestPost() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        String host = "http://httpbin.org";
        String path = "post";
        final JSONObject body = new JSONObject();

        try {
            body.put("message", "Hello World");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRequest.post(getContext(), host, path, body.toString(), new HttpRequest.OnHttpRequestFinishedListener() {
            @Override
            public void onHttpRequestSuccess(int statusCode, String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    assertEquals(jsonObject.getString("data"), body.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    signal.countDown();
                }
            }

            @Override
            public void onHttpRequestError(int statusCode, String data) {
                assertTrue(statusCode != -1);
                signal.countDown();
            }
        });

        signal.await();
    }

    public void testHttpRequestGet() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        String host = "http://www.mocky.io/v2";
        String path = "56945888110000731483a72f";

        new HttpRequest(getContext())
                .host(host)
                .path(path)
                .listener(new HttpRequest.OnHttpRequestFinishedListener() {
                    @Override
                    public void onHttpRequestSuccess(int statusCode, String data) {
                        assertTrue(statusCode != -1 && data != null && !data.isEmpty());
                        signal.countDown();
                    }

                    @Override
                    public void onHttpRequestError(int statusCode, String data) {
                        assertTrue(statusCode != -1);
                        signal.countDown();
                    }
                })
                .get();

        signal.await();
    }
}
