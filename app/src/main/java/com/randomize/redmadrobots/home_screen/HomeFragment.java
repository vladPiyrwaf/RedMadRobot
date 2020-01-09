package com.randomize.redmadrobots.home_screen;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.randomize.redmadrobots.R;
import com.randomize.redmadrobots.models.Photo;
import com.randomize.redmadrobots.view.PhotoDetailActivity;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomeFragment extends Fragment implements HomeContract.MainView, View.OnClickListener {

    @BindView(R.id.image_daily) ImageView imageView;
    @BindView(R.id.progress_home_screen) ProgressBar progressBar;
    @BindView(R.id.txt_home) TextView txtHome;
    @BindView(R.id.network_error_view) ConstraintLayout mNetworkErrorView;
    @BindView(R.id.swipe_container_home) SwipeRefreshLayout swipeRefreshLayout;

    private HomePresenterImpl presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.bind(this, view);

        imageView.setOnClickListener(this);

        presenter = new HomePresenterImpl(this, new GetNoticeIntractorImpl());
        presenter.requestDataFromServer();

        swipeRefreshLayout.setOnRefreshListener(() ->{
            presenter.requestDataFromServer();
            swipeRefreshLayout.setRefreshing(false);
        });

        return view;
    }

    @Override
    public void setDataToImageView(Photo photo) {
        Picasso.get().load(photo.getUrls().getRegular()).into(imageView);
    }

    @Override
    public void onResponseFailure(Throwable throwable) {
        showNetworkError();
        hideImageView();
        hideProgress();
        hideTextView();
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showImageView() {
        imageView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideImageView() {
        imageView.setVisibility(View.GONE);
    }

    @Override
    public void showNetworkError() {
        mNetworkErrorView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNetworkError() {
        mNetworkErrorView.setVisibility(View.GONE);
    }

    @Override
    public void showTextView() {
        txtHome.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideTextView() {
        txtHome.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public void onClick(View view) {
        Photo photo = presenter.getItemPhoto();
        Intent intent = new Intent(getActivity(), PhotoDetailActivity.class)
                .putExtra("url_image", photo.getUrls().getSmall())
                .putExtra("description", photo.getDescription())
                .putExtra("width", photo.getWidth())
                .putExtra("height", photo.getHeight())
                .putExtra("url_full_image", photo.getUrls().getFull());
        startActivity(intent);
    }
}
