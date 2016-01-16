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
