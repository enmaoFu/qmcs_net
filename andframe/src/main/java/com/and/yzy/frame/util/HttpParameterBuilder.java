package com.and.yzy.frame.util;

import android.net.Uri;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by yzy on 2016/8/28.
 */
public class HttpParameterBuilder {
    private Map<String, RequestBody> params;

    public HttpParameterBuilder() {

        params = new HashMap<>();


    }

    public HttpParameterBuilder addParameter(String key, Object o) {
        if (o instanceof String) {

            RequestBody body = RequestBody.create(MediaType.parse("text/plain"), (String) o);
            params.put(key, body);
        } else if (o instanceof File) {

            RequestBody body = RequestBody.create(MediaType.parse("image/*"), (File) o);

            params.put(key + "\"; filename=\"" + ((File) o).getName() + "", body);
        } else if (o instanceof Integer) {
            RequestBody body = RequestBody.create(MediaType.parse("text/plain"), o + "");
            params.put(key, body);
        }

        return this;
    }


    public HttpParameterBuilder addFilesByUri(String key, List<Uri> uris) {

        for (int i = 0; i < uris.size(); i++) {
            File file = new File(uris.get(i).getPath());
            RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
            params.put(key + i + "\"; filename=\"" + file.getName() + "", body);
        }

        return this;
    }


    public Map<String, RequestBody> bulider() {

        return params;
    }
}
