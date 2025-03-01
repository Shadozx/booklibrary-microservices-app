package com.shadoww.api.dto.request.book;


import java.util.List;

public class ParseLinksRequest {
    private List<String> urls;

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }
}
