package com.test.wavetectest.models;

import com.google.gson.annotations.SerializedName;

public class PhotoModel {

    @SerializedName("id")
    private int id;
    @SerializedName("width")
    private int width;
    @SerializedName("height")
    private int height;
    @SerializedName("url")
    private String url;
    @SerializedName("photographer")
    private String photographer;
    @SerializedName("photographer_url")
    private String photographer_url;
    @SerializedName("alt")
    private String alt;

    @SerializedName("src")
    private ImagesModel images;


    public PhotoModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPhotographer() {
        return photographer;
    }

    public void setPhotographer(String photographer) {
        this.photographer = photographer;
    }

    public String getPhotographer_url() {
        return photographer_url;
    }

    public void setPhotographer_url(String photographer_url) {
        this.photographer_url = photographer_url;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public ImagesModel getImages() {
        return images;
    }

    public void setImages(ImagesModel images) {
        this.images = images;
    }
}
