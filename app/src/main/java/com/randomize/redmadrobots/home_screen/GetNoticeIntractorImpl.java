package com.randomize.redmadrobots.home_screen;

import com.randomize.redmadrobots.api.UnplashApi;
import com.randomize.redmadrobots.models.Photo;
import com.randomize.redmadrobots.network.NetworkService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetNoticeIntractorImpl implements HomeContract.GetNoticeIntractor {

    private static final String CLIENT_ID = "e1302c9b61d67d3011bfed17ff854fa7aa0426c2adbe9c9fd18528a073476682";
    public static final String ORDER_BY = "daily";

    @Override
    public void getNoticePhoto(OnFinishedListener onFinishedListener) {
        UnplashApi unplashApi = NetworkService.getInstance().getJSONApi();

        Call<Photo> call = unplashApi.getRandomPhoto(1, 1, ORDER_BY, CLIENT_ID);

        call.enqueue(new Callback<Photo>() {
            @Override
            public void onResponse(Call<Photo> call, Response<Photo> response) {
                onFinishedListener.onFinished(response.body());
            }

            @Override
            public void onFailure(Call<Photo> call, Throwable t) {
                onFinishedListener.onFailure(t);
            }
        });
    }
}
