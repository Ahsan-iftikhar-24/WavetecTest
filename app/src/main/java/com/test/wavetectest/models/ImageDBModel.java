package com.test.wavetectest.models;

import com.google.gson.annotations.SerializedName;

public class ImageDBModel {
    @SerializedName("imgID")
    private int imgID;
    @SerializedName("photographerName")
    private String photographerName;
    @SerializedName("width")
    private String width;
    @SerializedName("height")
    private String height;
    @SerializedName("url")
    private String url;
    @SerializedName("pUrl")
    private String pUrl;
    @SerializedName("imgMed")
    private String imgMed;
    @SerializedName("imgLarge")
    private String imgLarge;


    public ImageDBModel(int imgID, String photographerName, String width, String height, String url, String pUrl, String imgMed, String imgLarge) {
        this.imgID = imgID;
        this.photographerName = photographerName;
        this.width = width;
        this.height = height;
        this.url = url;
        this.pUrl = pUrl;
        this.imgMed = imgMed;
        this.imgLarge = imgLarge;
    }

    public int getImgID() {
        return imgID;
    }

    public void setImgID(int imgID) {
        this.imgID = imgID;
    }

    public String getPhotographerName() {
        return photographerName;
    }

    public void setPhotographerName(String photographerName) {
        this.photographerName = photographerName;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getpUrl() {
        return pUrl;
    }

    public void setpUrl(String pUrl) {
        this.pUrl = pUrl;
    }

    public String getImgMed() {
        return imgMed;
    }

    public void setImgMed(String imgMed) {
        this.imgMed = imgMed;
    }

    public String getImgLarge() {
        return imgLarge;
    }

    public void setImgLarge(String imgLarge) {
        this.imgLarge = imgLarge;
    }
}
