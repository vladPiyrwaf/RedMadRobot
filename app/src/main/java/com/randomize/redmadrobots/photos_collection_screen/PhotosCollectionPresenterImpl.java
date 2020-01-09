package com.randomize.redmadrobots.photos_collection_screen;

import com.randomize.redmadrobots.models.Photo;

import java.util.List;

public class PhotosCollectionPresenterImpl
        implements PhotosCollectionContract.Presenter, PhotosCollectionContract.GetNoticeIntractor.OnFinishedListener {

    private PhotosCollectionContract.PhotosCollectionView photosCollectionView;
    private PhotosCollectionContract.GetNoticeIntractor getNoticeIntractor;

    public PhotosCollectionPresenterImpl(PhotosCollectionContract.PhotosCollectionView photosCollectionView, PhotosCollectionContract.GetNoticeIntractor getNoticeIntractor) {
        this.photosCollectionView = photosCollectionView;
        this.getNoticeIntractor = getNoticeIntractor;
    }

    @Override
    public void onDestroy() {
        photosCollectionView = null;
    }

    @Override
    public void requestFirstData(long idCollection) {
        if (photosCollectionView != null) {
            photosCollectionView.showRecyclerView();
            photosCollectionView.showProgress();
            photosCollectionView.hideNetworkError();
            getNoticeIntractor.getFirstNoticeList(this, idCollection);
        }

    }

    @Override
    public void requestAddData(int countPage, long idCollection) {
        if (photosCollectionView != null) {
            photosCollectionView.showProgress();
            getNoticeIntractor.getNoticeList(this, countPage, idCollection);
        }
    }

    @Override
    public void onFirstFinished(List<Photo> photos) {
        if (photosCollectionView != null) {
            photosCollectionView.setDataToRecyclerView(photos);
            photosCollectionView.hideProgress();
        }
    }

    @Override
    public void onFinished(List<Photo> photos) {
        if (photosCollectionView != null) {
            photosCollectionView.addDataToRecyclerView(photos);
            photosCollectionView.hideProgress();
        }

    }

    @Override
    public void onFailure(Throwable throwable) {
        if (photosCollectionView != null) {
            photosCollectionView.onResponseFailure(throwable);
        }
    }
}
