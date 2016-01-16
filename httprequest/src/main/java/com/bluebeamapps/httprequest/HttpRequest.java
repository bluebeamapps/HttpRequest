/*
 * Copyright (C) 2016 Bluebeamapps.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bluebeamapps.httprequest;

import android.content.Context;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private static final String TAG = "HttpRequest";

    private static RequestQueue sQueue;
    private static String sDefaultHost;
    private static Map<String, String> sDefaultHeaders;

    private String host = "";
    private String path = "";
    private String params = "";
    private String body = "";
    private String method;
    private Map<String, String> headers;
    private OnHttpRequestFinishedListener listener;
    private BBAStringRequest request;

    public interface OnHttpRequestFinishedListener {
        void onHttpRequestSuccess(int statusCode, String data);
        void onHttpRequestError(int statusCode, String data);
    }

    /** Convenience function for the http method GET. */
    public static HttpRequest get(Context context, String host, String path,
                                  OnHttpRequestFinishedListener listener) {
        return new HttpRequest(context.getApplicationContext())
                .host(host)
                .path(path)
                .listener(listener)
                .get();
    }

    /** Convenience function for the http method POST. */
    public static HttpRequest post(Context context, String host, String path, String body,
                                   OnHttpRequestFinishedListener listener) {
        return new HttpRequest(context.getApplicationContext())
                .host(host)
                .path(path)
                .body(body)
                .listener(listener)
                .post();
    }

    /** Convenience function for the http method PUT. */
    public static HttpRequest put(Context context, String host, String path, String body,
                                   OnHttpRequestFinishedListener listener) {
        return new HttpRequest(context.getApplicationContext())
                .host(host)
                .path(path)
                .body(body)
                .listener(listener)
                .put();
    }

    /** Sets the default host.
     * <p>The default host wil be used on every request unless a host is supplied through the
     * method host() on the request object.</p> */
    public static void setDefaultHost(String defaultHost) {
        sDefaultHost = defaultHost;
    }

    /** Sets the default Http headers.
     * <p>The default Http headers will be used on every request unless an Http header is supplied
     * through the method headers() on the request object. </p>*/
    public static void setDefaultHeaders(Map<String, String> defaultHeaders) {
        sDefaultHeaders = defaultHeaders;
    }

    /** Instantiates a new Http request object. */
    public HttpRequest(Context context) {
        if (sQueue == null) {
            sQueue = Volley.newRequestQueue(context.getApplicationContext());
        }

        if (sDefaultHeaders == null) {
            sDefaultHeaders = new HashMap<>();
            sDefaultHeaders.put("Content-Type", "application/json");
        }

        this.host = sDefaultHost;
        this.headers = sDefaultHeaders;
    }

    /** Sets the host of the request. */
    public HttpRequest host(String host) {
        this.host = host;
        return this;
    }

    /** Sets the path of the request. */
    public HttpRequest path(String path) {
        if (path != null && !path.isEmpty()) {
            this.path = path;
        } else {
            this.path = "";
        }

        return this;
    }

    public HttpRequest params(String params) {
        if (params != null) {
            this.params = params;
        } else {
            this.params = "";
        }

        return this;
    }

    public HttpRequest params(HttpParams params) {
        if (params != null) {
            this.params = params.toString();
        } else {
            this.params = "";
        }

        return this;
    }

    public HttpRequest body(String body) {
        if (body != null) {
            this.body = body;
        } else {
            this.body = "";
        }

        return this;
    }

    public HttpRequest body(Object body) {
        if (body != null) {
            this.body = body.toString();
        } else {
            this.body = "";
        }

        return this;
    }

    public HttpRequest headers(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public HttpRequest method(String method) {
        if (method != null && !method.isEmpty()) {
            this.method = method;
        } else {
            throw new IllegalArgumentException(TAG + ": Method must not be null or empty");
        }

        return this;
    }

    public HttpRequest listener(OnHttpRequestFinishedListener listener) {
        this.listener = listener;
        return this;
    }

    public HttpRequest get() {
        method = "GET";
        send();

        return this;
    }

    public HttpRequest post() {
        method = "POST";
        send();

        return this;
    }

    public HttpRequest put() {
        method = "PUT";
        send();
        return this;
    }

    public HttpRequest delete() {
        method = "DELETE";
        send();

        return this;
    }

    public HttpRequest send() {
        if (host == null || host.isEmpty()) {
            throw new IllegalArgumentException(TAG + ": Host cannot be null or empty");
        }

        int method = parseMethod(this.method);
        final OnHttpRequestFinishedListener listener = this.listener;

        String url = host + "/" + path + getEncodedParams();

        BBAStringRequest request = new BBAStringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (listener != null) {
                    int statusCode = HttpRequest.this.request.getStatusCode();
                    listener.onHttpRequestSuccess(statusCode, response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

                if (listener != null) {
                    int statusCode = -1;
                    String data = "";

                    if (error.networkResponse != null) {
                        statusCode = error.networkResponse.statusCode;
                        data = getNetworkResponseData(error.networkResponse);
                    }

                    listener.onHttpRequestError(statusCode, data);
                }
            }
        });

        request.setHeaders(headers);
        request.setBody(body);

        sQueue.add(request);
        this.request = request;

        return this;
    }

    public void cancelRequest() {
        if (request != null) {
            request.cancel();
        }
    }

    private int parseMethod(String method) {
        method = method.toUpperCase();

        switch (method) {
            case "GET":
                return Request.Method.GET;

            case "POST":
                return Request.Method.POST;

            case "PUT":
                return Request.Method.PUT;

            case "DELETE":
                return Request.Method.DELETE;

            case "OPTIONS":
                return Request.Method.OPTIONS;

            case "PATCH":
                return Request.Method.PATCH;

            case "HEAD":
                return Request.Method.HEAD;

            default:
                throw new IllegalArgumentException(TAG + ": Http method " + method + " is not supported");
        }
    }

    private String getEncodedParams() {
        try {
            return URLEncoder.encode(params, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    private String getNetworkResponseData(NetworkResponse response) {
        String data = "";

        if (response.data != null) {
            try {
                data = new String(response.data, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return data;
    }
}
