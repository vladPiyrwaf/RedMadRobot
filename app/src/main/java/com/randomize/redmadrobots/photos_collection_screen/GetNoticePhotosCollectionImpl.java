package com.randomize.redmadrobots.photos_collection_screen;


import com.randomize.redmadrobots.api.UnplashApi;
import com.randomize.redmadrobots.models.Photo;
import com.randomize.redmadrobots.network.NetworkService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetNoticePhotosCollectionImpl implements PhotosCollectionContract.GetNoticeIntractor {

    private static final String CLIENT_ID = "e1302c9b61d67d3011bfed17ff854fa7aa0426c2adbe9c9fd18528a073476682";
    private static final int PER_PAGE = 10;
    private static final int FIRST_PAGE = 1;

    private UnplashApi unplashApi;
    private Call<List<Photo>> call;

    @Override
    public void getFirstNoticeList(OnFinishedListener finishedListener, long idCollection) {

        unplashApi = NetworkService.getInstance().getJSONApi();
        call = unplashApi.getPhotosCollection(idCollection, FIRST_PAGE, PER_PAGE, CLIENT_ID);
        call.enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                finishedListener.onFirstFinished(response.body());
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                finishedListener.onFailure(t);
            }
        });

    }

    @Override
    public void getNoticeList(OnFinishedListener finishedListener, int pageCount, long idCollection) {

        unplashApi = NetworkService.getInstance().getJSONApi();
        call = unplashApi.getPhotosCollection(idCollection, pageCount, PER_PAGE, CLIENT_ID);
        call.enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                finishedListener.onFinished(response.body());
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                finishedListener.onFailure(t);
            }
        });
    }
}
