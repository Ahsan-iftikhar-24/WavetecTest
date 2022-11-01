package com.test.wavetectest.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PhotosRoot {

    @SerializedName("photos")
    private List<PhotoModel> photosList;

    public PhotosRoot() {
    }

    public List<PhotoModel> getPhotosList() {
        return photosList;
    }

    public void setPhotosList(List<PhotoModel> photosList) {
        this.photosList = photosList;
    }
}
