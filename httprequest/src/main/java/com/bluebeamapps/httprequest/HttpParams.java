package com.bluebeamapps.httprequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HttpParams {

    private Map<String, String> params = new HashMap<>();

    public void addParam(String paramName, String paramValue) {
        params.put(paramName, paramValue);
    }

    @Override
    public String toString() {
        String string = "";
        Set<Map.Entry<String, String>> entrySet = params.entrySet();

        if (entrySet.size() > 0) {
            string += "?";

            for (Map.Entry<String, String> entry : entrySet) {
                string += entry.getKey() + entry.getValue() + "&";
            }

            if (string.length() > 0) {
                string = string.substring(0, string.length() - 1);
            }
        }

        return string;
    }
}
