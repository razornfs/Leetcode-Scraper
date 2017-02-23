package com.scraper;

public class Code {
    private String content;
    private String language;

    public Code(String content, String language) {
        this.content = content;
        this.language = language;
    }

    public String getContent() {
        return content;
    }

    public String getLanguage() {
        return language;
    }
}
