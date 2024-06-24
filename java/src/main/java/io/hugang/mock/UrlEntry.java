package io.hugang.mock;


import lombok.Data;

@Data
public class UrlEntry {
    private String url;
    private String param;
    private String method;
    private int code;
    private String type;
    private Object data;
}