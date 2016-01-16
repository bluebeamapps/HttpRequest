# HttpRequest
A simple HTTP client for Android built on top of Volley

Usage
----

Http GET:
```java
        String host = "http://www.mocky.io/v2";
        String path = "56945888110000731483a72f";

        HttpRequest.get(getContext(), host, path, new HttpRequest.OnHttpRequestFinishedListener() {
            @Override
            public void onHttpRequestSuccess(int statusCode, String data) {
                //Handle successful response.
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    Log.d("HttpRequest", jsonObject.getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onHttpRequestError(int statusCode, String data) {
                //Handle error.
            }
        });
```

Http POST:
```java
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
                    Log.d("HttpRequest", jsonObject.getString("data"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onHttpRequestError(int statusCode, String data) {
                assertTrue(statusCode != -1);
                signal.countDown();
            }
        });
```

You can also instantiate a request object and send the request:
```java
        String host = "http://www.mocky.io/v2";
        String path = "56945888110000731483a72f";

        //Use .headers() and .params() to set your headers and params.
        new HttpRequest(getContext())
                .host(host)
                .path(path)
                .listener(new HttpRequest.OnHttpRequestFinishedListener() {
                    @Override
                    public void onHttpRequestSuccess(int statusCode, String data) {
                        //Handle successful response.
                        try {
                            JSONObject jsonObject = new JSONObject(data);
                            Log.d("HttpRequest", jsonObject.getString("message"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onHttpRequestError(int statusCode, String data) {

                    }
                })
                .get();
```


A default host and header can be set using static methods.

When the default host is set, you do not need to supply a host to your requests. If you do supply a host however, the default host will not be used for that single request.

It is not necessary to supply an http header. The default header sets the field "Content-Type" to "application/json".
If you do supply a header to the request, the header will be used for that single request instead of the default header.

Example:
```java
HttpRequest.setDefaultHost("http://www.yourhost.com");
HttpRequest.setDefaultHeaders(new HashMap<String,String>());
```
