package com.randomize.redmadrobots.search_screen;

import com.randomize.redmadrobots.api.UnplashApi;
import com.randomize.redmadrobots.models.SearchResults;
import com.randomize.redmadrobots.network.NetworkService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetNoticeSearchPhotosImpl implements SearchContract.GetNoticeIntractor {

    private static final String CLIENT_ID = "e1302c9b61d67d3011bfed17ff854fa7aa0426c2adbe9c9fd18528a073476682";
    private static final int PER_PAGE = 10;
    private static final int FIRST_PAGE = 1;

    private UnplashApi unplashApi;
    private Call<SearchResults> call;

    @Override
    public void getFirstNoticeList(OnFinishedListener onFinishedListener, String query) {
         unplashApi = NetworkService.getInstance().getJSONApi();
         call = unplashApi.searchPhotos(query, FIRST_PAGE, PER_PAGE, null, CLIENT_ID);
         call.enqueue(new Callback<SearchResults>() {
             @Override
             public void onResponse(Call<SearchResults> call, Response<SearchResults> response) {
                 onFinishedListener.onFirstFinished(response.body());
             }

             @Override
             public void onFailure(Call<SearchResults> call, Throwable t) {
                onFinishedListener.onFailure(t);
             }
         });
    }

    @Override
    public void getNoticeList(OnFinishedListener onFinishedListener, String query, int countPage) {
        unplashApi = NetworkService.getInstance().getJSONApi();
        call = unplashApi.searchPhotos(query, countPage, PER_PAGE, null, CLIENT_ID);
        call.enqueue(new Callback<SearchResults>() {
            @Override
            public void onResponse(Call<SearchResults> call, Response<SearchResults> response) {
                onFinishedListener.onFinished(response.body());
            }

            @Override
            public void onFailure(Call<SearchResults> call, Throwable t) {
                onFinishedListener.onFailure(t);
            }
        });
    }
}
