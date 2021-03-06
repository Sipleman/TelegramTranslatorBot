package com.ilias.translator_bot.TranslatorAPI;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FreeGoogleTranslator implements Translator {
    private final String USER_AGENT = "Mozilla/5.0";

    private final String stringForFormatRequest
            = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=%s&tl=%s&dt=t&q=%s";
    private final String GoogleUrl
            = "http://translate.google.com/translate_a/";

    public String requestTranslation(String text, Object from, Object to) throws IOException{
        String encodedText = URLEncoder.encode(text, "UTF-8");
        String params = String.format(stringForFormatRequest, from, to, encodedText);
        System.out.println(params
        );
        HttpResponse<String> response = null;
        try {
            response = Unirest.get(params)
                    .header("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.186 Mobile Safari/537.36")
                    .header("Accept-Encoding", "gzip, deflate, br")
                    .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                    .asString();
            System.out.println(URLEncoder.encode(response.getBody(), "UTF-8"));
        }catch (Exception e){
            e.printStackTrace();
        }
        if(response == null) return "hz";
        return castResult(response.getBody());

    }
    private String castResult(String result) {
        Pattern pat = Pattern.compile("\"(.*?)\"");
        List<String> allMatches = new ArrayList();
        Matcher matcher = pat.matcher(result);

        while(matcher.find()) {
            allMatches.add(matcher.group().replace("\"", ""));
        }

        return (String)allMatches.get(0);
    }
}
