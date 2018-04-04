package com.ilias.translator_bot.TranslatorAPI;

import com.jafregle.http.HttpClient;
import com.jafregle.http.HttpMethod;
import com.jafregle.http.HttpResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FreeGoogleTranslator implements Translator {
    private final String stringForFormatRequest
            = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=%s&tl=%s&dt=t&q=%s";
    private final String GoogleUrl
            = "http://translate.google.com/translate_a/";

    public String requestTranslation(String text, Object from, Object to) throws IOException{
        String encodedText = URLEncoder.encode(text, "UTF-8");
        String params = String.format(stringForFormatRequest, from, to, encodedText);
        HttpResponse response = (new HttpClient()).request(HttpMethod.GET, params);

        return castResult(response.asString());
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
