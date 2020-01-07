package com.randomize.redmadrobots.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.randomize.redmadrobots.R;
import com.randomize.redmadrobots.api.NetworkService;
import com.randomize.redmadrobots.models.Photo;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private static final String CLIENT_ID = "e1302c9b61d67d3011bfed17ff854fa7aa0426c2adbe9c9fd18528a073476682";

    private ImageView imageView;
    private Photo photo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        imageView = view.findViewById(R.id.image_daily);
        imageView.setOnClickListener(this);
        setRequestRandomPhoto();

        return view;
    }

        private void setRequestRandomPhoto(){

        NetworkService.getInstance()
                .getJSONApi()
                .getRandomPhoto(1,1,"daily", CLIENT_ID)
                .enqueue(new Callback<Photo>() {
                    @Override
                    public void onResponse(Call<Photo> call, Response<Photo> response) {
                        photo = response.body();
                        Picasso.get().load(photo.getUrls().getSmall()).into(imageView);
                        Toast.makeText(getActivity(), photo.getId(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Photo> call, Throwable t) {

                    }
                });
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getActivity(), PhotoDetailActivity.class)
                .putExtra("url_image", photo.getUrls().getSmall())
                .putExtra("description", photo.getDescription())
                .putExtra("width", photo.getWidth())
                .putExtra("height", photo.getHeight())
                .putExtra("url_full_image", photo.getUrls().getFull());
        startActivity(intent);
    }
}
