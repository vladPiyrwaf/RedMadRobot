package com.randomize.redmadrobots.collections_screen;

import com.randomize.redmadrobots.models.collections.Collection;

import java.util.List;

public class CollectionsPresenterImpl implements
        CollectionsContract.Presenter, CollectionsContract.GetNoticeIntractor.OnFinishedListener {

    private CollectionsContract.CollectionView collectionView;
    private CollectionsContract.GetNoticeIntractor getNoticeIntractor;

    public CollectionsPresenterImpl(CollectionsContract.CollectionView collectionView,
                                    CollectionsContract.GetNoticeIntractor getNoticeIntractor) {
        this.collectionView = collectionView;
        this.getNoticeIntractor = getNoticeIntractor;
    }

    @Override
    public void onDestroy() {
        collectionView = null;
    }

    @Override
    public void requestFirstData() {
        if (collectionView != null) {
            collectionView.showRecyclerView();
            collectionView.showProgress();
            collectionView.hideNetworkError();
            getNoticeIntractor.getFirstNoticeList(this);

        }

    }
    @Override
    public void requestAddData(int countPage) {
        if (collectionView != null) {
            collectionView.showProgress();
            getNoticeIntractor.getNoticeList(this, countPage);
        }
    }

    @Override
    public void onFirstFinished(List<Collection> collections) {
        if (collectionView != null) {
            collectionView.setDataToRecyclerView(collections);
            collectionView.hideProgress();
        }
    }

    @Override
    public void onFinished(List<Collection> collections) {
        if (collectionView != null) {
            collectionView.addDataToRecyclerView(collections);
            collectionView.hideProgress();
        }
    }

    @Override
    public void onFailure(Throwable t) {
        if (collectionView != null) {
            collectionView.onResponseFailure(t);
        }
    }
}
