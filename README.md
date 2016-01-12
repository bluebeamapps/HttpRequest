# HttpRequest
A simple HTTP client for Android built on top of Volley

Convenience Http GET:
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
