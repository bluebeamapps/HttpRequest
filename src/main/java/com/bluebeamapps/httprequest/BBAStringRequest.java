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

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class BBAStringRequest extends Request<String> {

    private static final String BODY_CHARSET = "utf-8";
    private static final String BODY_CONTENT_TYPE =
            String.format("application/json; charset=%s", BODY_CHARSET);

    private int statusCode = -1;
    private Response.Listener<String> listener;
    private Map<String, String> headers;
    private String body;

    public BBAStringRequest(int method, String url, Response.Listener<String> listener,
                            Response.ErrorListener errorListener) {
        super(method, url, errorListener);

        this.listener = listener;

        //Default header.
        headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setBody(JSONObject body) {
        if (body == null) {
            this.body = null;
        } else {
            this.body = body.toString();
        }
    }

    public void setBody(Map<String, String> body) {
        if (body == null) {
            this.body = null;
        } else {
            this.body = body.toString();
        }
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        try {
            return body == null ? null : body.getBytes(BODY_CHARSET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getBodyContentType() {
        return BODY_CONTENT_TYPE;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        statusCode = response.statusCode;

        try {
            String responseString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));

            return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(String response) {
        if (listener != null) {
            listener.onResponse(response);
        }
    }
}
