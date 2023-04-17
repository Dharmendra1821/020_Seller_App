package standalone.eduqfix.qfixinfo.com.app.seller_app.activity;


import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.util.TouchImageView;

public class ImageZoomActivity extends AppCompatActivity {
    TouchImageView touchImageView;
    ImageView close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_zoom);


        touchImageView = findViewById(R.id.touchimageview);
        close          = findViewById(R.id.zoom_close);

        String largeimage = getIntent().getStringExtra("imagevalue");

        Glide.with(ImageZoomActivity.this).load(largeimage).into(touchImageView);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}
