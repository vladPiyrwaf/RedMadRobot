package com.randomize.redmadrobots.collections_screen;

import com.randomize.redmadrobots.api.UnplashApi;
import com.randomize.redmadrobots.models.collections.Collection;
import com.randomize.redmadrobots.network.NetworkService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetNoticeInstractorCollectionImpl implements CollectionsContract.GetNoticeIntractor {

    private static final String CLIENT_ID = "e1302c9b61d67d3011bfed17ff854fa7aa0426c2adbe9c9fd18528a073476682";
    private static final int PER_PAGE = 10;
    private static final int FIRST_PAGE = 1;

    private UnplashApi unplashApi;
    private Call<List<Collection>> call;

    @Override
    public void getFirstNoticeList(OnFinishedListener onFinishedListener) {
        unplashApi = NetworkService.getInstance().getJSONApi();

        call = unplashApi.getCollections(FIRST_PAGE, PER_PAGE, CLIENT_ID);

        call.enqueue(new Callback<List<Collection>>() {
            @Override
            public void onResponse(Call<List<Collection>> call, Response<List<Collection>> response) {
                onFinishedListener.onFirstFinished(response.body());
            }

            @Override
            public void onFailure(Call<List<Collection>> call, Throwable t) {
                onFinishedListener.onFailure(t);
            }
        });

    }

    @Override
    public void getNoticeList(OnFinishedListener onFinishedListener, int pageCount) {

        unplashApi = NetworkService.getInstance().getJSONApi();

        call = unplashApi.getCollections(pageCount, PER_PAGE, CLIENT_ID);

        call.enqueue(new Callback<List<Collection>>() {
            @Override
            public void onResponse(Call<List<Collection>> call, Response<List<Collection>> response) {
                onFinishedListener.onFinished(response.body());
            }

            @Override
            public void onFailure(Call<List<Collection>> call, Throwable t) {
                onFinishedListener.onFailure(t);
            }
        });

    }
}
