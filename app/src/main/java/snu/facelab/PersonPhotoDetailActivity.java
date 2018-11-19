package snu.facelab;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import static snu.facelab.MainActivity.PERSON;

public class PersonPhotoDetailActivity extends AppCompatActivity {
    public static final String PHOTO = "Photo";
    private ImageView iv;
    private Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_photo_detail);

        Photo photo = (Photo) getIntent().getExtras().getSerializable(PHOTO);
        Log.d("test", String.valueOf(photo.getImg()));

        LinearLayout imgHolder = (LinearLayout) findViewById(R.id.person_photo_holder);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);

        iv = (ImageView) findViewById(R.id.person_photo_detail);
        image = BitmapFactory.decodeFile(photo.getImg());
        iv.setImageBitmap(image);
        iv.setLayoutParams(params);

        if (photo.getImg() != null) {
            imgHolder.removeAllViews();
        }
        imgHolder.addView(iv);
    }

}
