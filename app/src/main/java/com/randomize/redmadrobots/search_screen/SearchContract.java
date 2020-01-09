package com.randomize.redmadrobots.search_screen;

import com.randomize.redmadrobots.models.Photo;
import com.randomize.redmadrobots.models.SearchResults;

import java.util.List;

public interface SearchContract {

    interface Presenter {

        void onDestroy();

        void requestFirstData(String query);

        void requestAddData(String query, int countPage);
    }

    interface SearchView {
        void showRecyclerView();

        void hideRecyclerView();

        void showProgress();

        void hideProgress();

        void showNetworkError();

        void hideNetworkError();

        void setDataToRecyclerView(List<Photo> photos);

        void setTotalPhoto(int totalPhoto);

        void addDataToRecyclerView(List<Photo> photos);

        void onResponseFailure(Throwable throwable);

        void showNoResultView();

        void hideNoResultView();
    }

    interface GetNoticeIntractor {

        interface OnFinishedListener {

            void onFirstFinished(SearchResults results);

            void onFinished(SearchResults results);

            void onFailure(Throwable throwable);
        }

        void getFirstNoticeList(OnFinishedListener onFinishedListener, String query);

        void getNoticeList(OnFinishedListener onFinishedListener, String query, int countPage);
    }
}
