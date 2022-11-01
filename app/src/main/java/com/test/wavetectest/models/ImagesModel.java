package com.test.wavetectest.models;

import com.google.gson.annotations.SerializedName;

public class ImagesModel {

    @SerializedName("original")
    private String original;
    @SerializedName("medium")
    private String medium;
    @SerializedName("small")
    private String small;
    @SerializedName("large")
    private String large;

    public ImagesModel() {
    }


    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }
}
