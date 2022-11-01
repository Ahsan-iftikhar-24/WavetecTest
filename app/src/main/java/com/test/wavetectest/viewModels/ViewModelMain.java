package com.test.wavetectest.viewModels;

import android.app.ProgressDialog;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.test.wavetectest.models.PhotoModel;
import com.test.wavetectest.models.PhotosRoot;
import com.test.wavetectest.retrofit.ApiInterface;
import com.test.wavetectest.retrofit.RetrofitInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewModelMain extends ViewModel {

    private MutableLiveData<List<PhotoModel>> mutableLiveData;
    private MutableLiveData<String> errorCheck;


    public ViewModelMain(){
        mutableLiveData = new MutableLiveData<>();
        errorCheck = new MutableLiveData<>();

    }

    public MutableLiveData<List<PhotoModel>> getMutableLiveData() {
        return mutableLiveData;
    }

    public MutableLiveData<String> getErrorCheck() {
        return errorCheck;
    }

public void makeApiCall(String apiKey,int pageCount, int perPage){


    ApiInterface apiInterface = RetrofitInstance.getClient().create(ApiInterface.class);
    Call<PhotosRoot> call = apiInterface.getPhotos(apiKey,pageCount,perPage);
    call.enqueue(new Callback<PhotosRoot>() {
        @Override
        public void onResponse(Call<PhotosRoot> call, Response<PhotosRoot> response) {
            if(response.isSuccessful()){
                if(response.code() == 200){
                    PhotosRoot photosRoot = response.body();
                    if(photosRoot!=null){

                        List<PhotoModel> photoModels = photosRoot.getPhotosList();
                        mutableLiveData.setValue(photoModels);

                    }
                }
                else if(response.code() == 401){
                    errorCheck.setValue("401");
                    mutableLiveData.setValue(null);
                }
                else if(response.code() == 500){
                    errorCheck.setValue("500");
                    mutableLiveData.setValue(null);
                }
                else {
                    errorCheck.setValue("error");
                    mutableLiveData.setValue(null);
                }
            }
        }

        @Override
        public void onFailure(Call<PhotosRoot> call, Throwable t) {
            errorCheck.setValue("fail");
            mutableLiveData.setValue(null);
        }
    });

}


}
