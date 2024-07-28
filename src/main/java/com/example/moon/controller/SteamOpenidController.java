package com.example.moon.controller;

import jakarta.servlet.http.HttpServletRequest;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SteamOpenidController {

    @GetMapping("/steamLogin")
    public String login() {
        String url;
        url = "redirect:https://steamcommunity.com/openid/login";
        url = url + "?openid.ns=http://specs.openid.net/auth/2.0";
        url = url + "&openid.mode=checkid_setup";
        url = url + "&openid.return_to=http://localhost:8887/steamCallback";
        url = url + "&openid.realm=http://localhost:8887/steamCallback";
        url = url + "&openid.identity=http://specs.openid.net/auth/2.0/identifier_select";
        url = url + "&openid.claimed_id=http://specs.openid.net/auth/2.0/identifier_select";

        return url;
    }

    @GetMapping("/steamCallback")
    public String steamCallback(HttpServletRequest request) {
        System.out.println("正常进入callback");

        Object signed = request.getParameter("openid.signed");
        if (signed == null || "".equals(signed)) {
            return "";
        }
        StringBuilder uri = new StringBuilder("https://steamcommunity.com/openid/login?");
        CloseableHttpClient httpclient = HttpClients.createDefault();
        List<NameValuePair> params = new ArrayList<>();

        String[] signeds = signed.toString().split(",");
        for (String s : signeds) {
            String val = request.getParameter("openid." + s);
            params.add(new BasicNameValuePair("openid." + s, val == null ? "" : val));
        }
        params.add(new BasicNameValuePair("openid.mode", "check_authentication"));


        for (NameValuePair param : params) {
            uri.append(param.getName());
            uri.append("=");
            try {
                uri.append(URLEncoder.encode(param.getValue().toString(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            uri.append("&");
        }
        uri= new StringBuilder(uri.substring(0, uri.length() - 1));
        System.out.println(uri);

//        Request request1 = new Request.Builder().url(String.valueOf(uri)).build();
//        try (Response response = new OkHttpClient().newCall(request1).execute()) {
//            String string = response.body().string();
//            System.out.println(string);
//            return "/index";
//        }catch (Exception e) {
//            e.printStackTrace();
//        }

        try {
            HttpPost httppost = new HttpPost(String.valueOf(uri));

//            RequestConfig requestConfig = RequestConfig.custom()
//                    .setConnectTimeout(1000000).setSocketTimeout(1000000).build();
//            httppost.setConfig(requestConfig);

            HttpResponse response = httpclient.execute(httppost);
            System.out.println(response.toString());
            System.out.println("正常！");
        } catch (IOException e) {
            System.out.println("抛出异常");
            throw new RuntimeException(e);
        }

        return null;
    }
}


