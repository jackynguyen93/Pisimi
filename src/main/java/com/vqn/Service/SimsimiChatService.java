package com.vqn.Service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;

/**
 * Created by vqnguyen on 7/16/2017.
 */
@SuppressWarnings({"ALL", "deprecation"})
@Service
public class SimsimiChatService {
    @Autowired
    CrawlerService crawlerService;

    private String language = "vn";

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @SuppressWarnings("deprecation")
    public String chatWithSimsimi(String message) throws Exception{
        String cookie = crawlerService.getCookie();
        //noinspection deprecation
        @SuppressWarnings("deprecation") DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("http://simsimi.com/getRealtimeReq?lc="+ language +"&ft=1&normalProb=3&reqText="+ URLEncoder.encode(message, "UTF-8") + "&status=W&talkCnt=0");
        httpGet.setHeader("Cookie", cookie);
        HttpResponse response = httpClient.execute(httpGet);
        if (response.getStatusLine().getStatusCode() != 200) {
            cookie = crawlerService.getCookieFromSesimi();
            httpGet.abort();
            httpGet.setHeader("Cookie", cookie);
            response = httpClient.execute(httpGet);
        }
        String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
        JsonObject responseJson = new Gson().fromJson(responseString,JsonObject.class);
        return StringUtils.strip(responseJson.get("respSentence").toString(),"\"");
    }

    @SuppressWarnings("deprecation")
    public String chatWithSimsimi(String message, String language) throws Exception{
        String cookie = crawlerService.getCookie();
        //noinspection deprecation
        @SuppressWarnings("deprecation") DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("http://simsimi.com/getRealtimeReq?lc="+ language +"&ft=1&normalProb=3&reqText="+ URLEncoder.encode(message, "UTF-8") + "&status=W&talkCnt=0");
        httpGet.setHeader("Cookie", cookie);
        HttpResponse response = httpClient.execute(httpGet);
        if (response.getStatusLine().getStatusCode() != 200) {
            cookie = crawlerService.getCookieFromSesimi();
            httpGet.abort();
            httpGet.setHeader("Cookie", cookie);
            response = httpClient.execute(httpGet);
        }
        String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
        JsonObject responseJson = new Gson().fromJson(responseString,JsonObject.class);
        return StringUtils.strip(responseJson.get("respSentence").toString(),"\"");
    }
}
