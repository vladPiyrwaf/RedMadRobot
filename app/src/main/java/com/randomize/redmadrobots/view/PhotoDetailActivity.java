package com.randomize.redmadrobots.view;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.randomize.redmadrobots.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoDetailActivity extends AppCompatActivity {

    @BindView(R.id.image_detail_photo) ImageView imageView;
    @BindView(R.id.txt_detail_photo_description) TextView txtDescription;
    @BindView(R.id.txt_detail_photo_width) TextView txtWidth;
    @BindView(R.id.txt_detail_photo_height) TextView txtHeight;
    @BindView(R.id.txt_detail_photo_url_full_size) TextView txtFullSize;
    @BindView(R.id.toolbar_photo_detail) Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        String urlImage = getIntent().getStringExtra("url_image");
        String description = getIntent().getStringExtra("description");
        int width = getIntent().getIntExtra("width", 0);
        int height = getIntent().getIntExtra("height", 0);
        String urlImageFullSize = getIntent().getStringExtra("url_full_image");

        Picasso.get().load(urlImage).into(imageView);

        if (description == null) {
            txtDescription.setVisibility(View.GONE);
        } else {
            txtDescription.setText(description);
        }
        txtWidth.setText(String.format(getString(R.string.width_detail_photo), String.valueOf(width)));
        txtHeight.setText(String.format(getString(R.string.height_detail_photo), String.valueOf(height)));
        txtFullSize.setText(String.format(getString(R.string.url_detail_photo), urlImageFullSize));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getWindow().setExitTransition(null);
            supportFinishAfterTransition();
        }
        return true;
    }
}
