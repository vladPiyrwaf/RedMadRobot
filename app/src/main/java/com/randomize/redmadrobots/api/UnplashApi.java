package com.randomize.redmadrobots.api;

import com.randomize.redmadrobots.models.Photo;
import com.randomize.redmadrobots.models.SearchResults;
import com.randomize.redmadrobots.models.collections.Collection;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UnplashApi {


    @GET("photos/random")
    Call<Photo> getRandomPhoto(@Query("page") Integer page, @Query("per_page") Integer perPage,
                               @Query("order_by") String orderBy, @Query("client_id") String clientId);

    @GET("search/photos")
    Call<SearchResults> searchPhotos(@Query("query") String query,
                                     @Query("page") Integer page,
                                     @Query("per_page") Integer perPage,
                                     @Query("orientation") String orientation,
                                     @Query("client_id") String clientId);

    @GET("collections")
    Call<List<Collection>> getPhotoCollections(@Query("page") Integer page,
                                               @Query("per_page") Integer perPage,
                                               @Query("client_id") String clientId);

    @GET("collections/{id}/photos")
    Call<List<Photo>> getCollectionPhotos(@Path("id") long id,
                                          @Query("client_id") String clientId,
                                          @Query("page") int page,
                                          @Query("per_page") int perPage);

}
