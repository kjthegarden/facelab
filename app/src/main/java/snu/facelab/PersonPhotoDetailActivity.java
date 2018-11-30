package snu.facelab;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import snu.facelab.model.Picture;

import snu.facelab.helper.DatabaseHelper;

public class PersonPhotoDetailActivity extends AppCompatActivity {
    public static final String PHOTO = "Photo";
    private ImageView iv;
    private Bitmap image;

    // DatabaseHelper 객체
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_photo_detail);

        db = new DatabaseHelper(getApplicationContext());

        Button btnEdit = (Button) findViewById(R.id.btn_edit);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Photo edit.", Toast.LENGTH_SHORT).show();
            }
        });

        Button btnDelete = (Button) findViewById(R.id.btn_delete);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // db.deleteNamePicture(long id);
                Toast.makeText(getApplicationContext(), "Photo deleted.", Toast.LENGTH_SHORT).show();

            }
        });

        Picture photo = (Picture) getIntent().getExtras().getSerializable(PHOTO);
        Log.d("test", String.valueOf(photo.getPath()));

        LinearLayout imgHolder = (LinearLayout) findViewById(R.id.person_photo_holder);
        // LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, 429);

        iv = (ImageView) findViewById(R.id.person_photo_detail);
        image = BitmapFactory.decodeFile(photo.getPath());
        iv.setImageBitmap(image);
        iv.setLayoutParams(params);

        if (photo.getPath() != null) {
            imgHolder.removeAllViews();
        }
        imgHolder.addView(iv);
    }
}
