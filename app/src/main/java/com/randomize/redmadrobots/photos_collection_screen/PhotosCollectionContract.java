package com.randomize.redmadrobots.photos_collection_screen;

import com.randomize.redmadrobots.models.Photo;
import com.randomize.redmadrobots.models.collections.Collection;

import java.util.List;

public interface PhotosCollectionContract {

    interface Presenter {

        void onDestroy();

        void requestFirstData(long idCollection);

        void requestAddData(int countPage, long idCollection);
    }

    interface PhotosCollectionView {

        void showRecyclerView();

        void hideRecyclerView();

        void showProgress();

        void hideProgress();

        void showNetworkError();

        void hideNetworkError();

        void setDataToRecyclerView(List<Photo> photos);

        void addDataToRecyclerView(List<Photo> photos);

        void onResponseFailure(Throwable throwable);
    }

    interface GetNoticeIntractor {

        interface OnFinishedListener {

            void onFirstFinished(List<Photo> photos);

            void onFinished(List<Photo> photos);

            void onFailure(Throwable throwable);
        }

        void getFirstNoticeList(OnFinishedListener finishedListener, long idCollection);

        void getNoticeList(OnFinishedListener finishedListener, int pageCount, long idCollection);
    }
}
