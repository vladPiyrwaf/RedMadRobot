package com.randomize.redmadrobots.home_screen;

import com.randomize.redmadrobots.models.Photo;

public class HomePresenterImpl implements HomeContract.Presenter, HomeContract.GetNoticeIntractor.OnFinishedListener {

    private HomeContract.MainView mainView;
    private HomeContract.GetNoticeIntractor getNoticeIntractor;

    private Photo mPhoto;

    public HomePresenterImpl(HomeContract.MainView mainView, HomeContract.GetNoticeIntractor getNoticeIntractor) {
        this.mainView = mainView;
        this.getNoticeIntractor = getNoticeIntractor;
    }

    @Override
    public void onDestroy() {
        mainView = null;
    }

    @Override
    public void requestDataFromServer() {
        mainView.showProgress();
        mainView.hideNetworkError();
        getNoticeIntractor.getNoticePhoto(this);
    }

    @Override
    public void onFinished(Photo noticePhoto) {
        if (mainView != null){
            mPhoto = noticePhoto;
            mainView.hideNetworkError();
            mainView.setDataToImageView(noticePhoto);
            mainView.showTextView();
            mainView.showImageView();
            mainView.hideProgress();
        }
    }

    @Override
    public void onFailure(Throwable t) {
        if (mainView != null){
            mainView.onResponseFailure(t);
        }
    }

    public Photo getItemPhoto(){
        if (mPhoto != null){
            return mPhoto;
        }
        return null;
    }
}
