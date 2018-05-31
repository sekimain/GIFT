package com.example.mypar.gift.Structure;

/**
 * Created by watoz on 2018-05-17.
 */
public class Pdf {
    public Pdf() {
    }

    public Pdf(String filename, String url) {
        this.filename = filename;
        this.url = url;
    }

    public String getFilename() {
        return filename;
    }

    public String getUrl() {
        return url;
    }

    private String filename;
    private String url;


}