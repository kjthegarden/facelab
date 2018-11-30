package snu.facelab;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import snu.facelab.helper.DatabaseHelper;
import snu.facelab.model.*;

import static snu.facelab.MainActivity.PERSON;

public class PersonPhotoActivity extends AppCompatActivity {
    //ArrayList<List<Picture>> pictures = new ArrayList<List<Picture>>();
    public static final String PHOTO = "Photo";
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_photo);

        //Intent intent = getIntent();
        Person person = (Person) getIntent().getExtras().getSerializable(PERSON);
        setTitle(person.name);

        // DBHelper 객체 생성
        db = new DatabaseHelper(getApplicationContext());

        // ListView 위한 객체 설정
        ListView Lv = findViewById(R.id.listView);
        ListAdapter adapter = new ListAdapter();

        // get pictures from db by date
        List<Picture> picture_date = new ArrayList<Picture>();
        List<Integer> dates = new ArrayList<Integer>();

        dates = db.getAllDatesByName(person.name);

        int dateCount = dates.size();

        for (int i = 0; i < dateCount; i++)
        {
            picture_date = db.getAllPicturesByNameAndDate(person.name, dates.get(i));
            adapter.addItem(dates.get(i), picture_date);
        }

        Lv.setAdapter(adapter);

        //Check
        FloatingActionButton btn_check = (FloatingActionButton)findViewById(R.id.btn_check);
        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkImage();
            }
        });

        // Share
        FloatingActionButton btn = (FloatingActionButton)findViewById(R.id.btn_sns);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareImage();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void checkImage() {

    }



    // 이미지 공유 함수
    public void shareImage() {

        // 사진 절대 경로 받아오기

        // 여러 개의 사진 공유
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND_MULTIPLE); //전송 메소드를 호출합니다. 여러개의 사진 Intent.ACTION_SEND_MULTIPLE
        intent.setType("image/jpg"); //jpg 이미지를 공유 하기 위해 Type을 정의합니다.

        ArrayList<Uri> files = new ArrayList<Uri>();

        String[] filesToSend={"/storage/emulated/0/DCIM/Camera/20181103_084637_HDR.jpg",
                "/storage/emulated/0/DCIM/Camera/20181101_192825.jpg"};

        for(String path : filesToSend /* List of the files you want to send */) {
            File file = new File(path);
            Uri uri = FileProvider.getUriForFile(PersonPhotoActivity.this,
                    "snu.facelab.provider", file);
            files.add(uri);
        }

        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
        startActivity(Intent.createChooser(intent, "Choose")); //Activity를 이용하여 호출 합니다.
    }
}


