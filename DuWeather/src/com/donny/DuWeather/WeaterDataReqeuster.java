package com.donny.DuWeather;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeaterDataReqeuster {

    public Map request(String city) throws IOException, JSONException {

        HashMap map = new HashMap();

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("location", city));
        params.add(new BasicNameValuePair("output", "json"));
        params.add(new BasicNameValuePair("ak", "HeNzq37TzcmRNg98bi830rFu"));

        String paramStr = URLEncodedUtils.format(params, "UTF-8");

        HttpGet request = new HttpGet("http://api.map.baidu.com/telematics/v3/weather_city?" + paramStr);

        DefaultHttpClient httpClient = new DefaultHttpClient();

        HttpResponse response = httpClient.execute(request);
        HttpEntity httpEntity = response.getEntity();

        InputStream inputStream = httpEntity.getContent();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }

        String result = outputStream.toString();

        JSONObject jsonObject = new JSONObject(result);
        JSONArray resultsJSONArray = jsonObject.optJSONArray("results");
        JSONObject cityJSONObject = resultsJSONArray.optJSONObject(0);

        String pm25 = cityJSONObject.optString("pm25");
        JSONArray weatherDataArray = cityJSONObject.optJSONArray("weather_data");
        JSONObject today = weatherDataArray.optJSONObject(0);

        String dayPictureUrl = today.optString("dayPictureUrl");

        map.put("pm25", pm25);
        map.put("dayPictureUrl", dayPictureUrl);

        return map;
    }
}
