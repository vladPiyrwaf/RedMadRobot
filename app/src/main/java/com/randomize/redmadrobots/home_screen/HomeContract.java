package com.randomize.redmadrobots.home_screen;

import com.randomize.redmadrobots.models.Photo;

public interface HomeContract {

    interface Presenter {
        void onDestroy();

        void requestDataFromServer();
    }

    interface MainView {

        void showProgress();

        void hideProgress();

        void showImageView();

        void hideImageView();

        void showNetworkError();

        void hideNetworkError();

        void showTextView();

        void hideTextView();

        void setDataToImageView(Photo photo);

        void onResponseFailure(Throwable throwable);
    }

    interface GetNoticeIntractor {
        interface OnFinishedListener {
            void onFinished(Photo noticePhoto);

            void onFailure(Throwable t);
        }

        void getNoticePhoto(OnFinishedListener onFinishedListener);
    }
}

