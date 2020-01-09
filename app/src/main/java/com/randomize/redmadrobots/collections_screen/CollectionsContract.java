package com.randomize.redmadrobots.collections_screen;

import com.randomize.redmadrobots.models.collections.Collection;

import java.util.List;

public interface CollectionsContract {

    interface Presenter {

        void onDestroy();

        void requestFirstData();

        void requestAddData(int countPage);
    }

    interface CollectionView {

        void showRecyclerView();

        void hideRecyclerView();

        void showProgress();

        void hideProgress();

        void showNetworkError();

        void hideNetworkError();

        void setDataToRecyclerView(List<Collection> collections);

        void addDataToRecyclerView(List<Collection> collections);

        void onResponseFailure(Throwable throwable);
    }

    interface GetNoticeIntractor {

        interface OnFinishedListener {

            void onFirstFinished(List<Collection> collections);

            void onFinished(List<Collection> collections);

            void onFailure(Throwable t);
        }

        void getFirstNoticeList(OnFinishedListener onFinishedListener);

        void getNoticeList(OnFinishedListener onFinishedListener, int pageCount);
    }
}
