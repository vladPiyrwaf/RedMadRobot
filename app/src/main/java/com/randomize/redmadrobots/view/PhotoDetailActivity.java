package com.randomize.redmadrobots.view;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.randomize.redmadrobots.R;
import com.squareup.picasso.Picasso;

public class PhotoDetailActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView txtDescription, txtWidth, txtHeight, txtFullSize;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        mToolbar = findViewById(R.id.toolbar_photo_detail);
        imageView = findViewById(R.id.image_detail_photo);
        txtDescription = findViewById(R.id.txt_detail_photo_description);
        txtWidth = findViewById(R.id.txt_detail_photo_width);
        txtHeight = findViewById(R.id.txt_detail_photo_height);
        txtFullSize = findViewById(R.id.txt_detail_photo_url_full_size);

        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        String urlImage = getIntent().getStringExtra("url_image");
        String description = getIntent().getStringExtra("description");
        int width = getIntent().getIntExtra("width", 0);
        int height= getIntent().getIntExtra("height", 0);
        String urlImageFullSize = getIntent().getStringExtra("url_full_image");

        Picasso.get().load(urlImage).into(imageView);

        txtDescription.setText(description);
        txtWidth.setText("Ширина: " + String.valueOf(width));
        txtHeight.setText("Высота: " + String.valueOf(height));
        txtFullSize.setText("Оригинал фото: " + urlImageFullSize);
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
