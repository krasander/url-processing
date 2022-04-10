package com.example.urlprocessing.thread;

import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JsoupWrapper {

    public String getDocumentBody(final String url) throws IOException {
        return Jsoup.connect(url).get().body().toString();
    }
}
