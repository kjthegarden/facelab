package snu.facelab;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import snu.facelab.helper.DatabaseHelper;
import snu.facelab.model.*;

import static snu.facelab.MainActivity.PERSON;

public class PersonPhotoActivity extends AppCompatActivity {
    public static final String PHOTO = "Photo";
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_photo);

        Person person = (Person) getIntent().getExtras().getSerializable(PERSON);
        setTitle(person.name);

        // DBHelper 객체 생성
        db = new DatabaseHelper(getApplicationContext());

        // ListView 위한 객체 설정
        ListView Lv = findViewById(R.id.listView);
        final ListAdapter adapter = new ListAdapter();

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

        // Add Picture
        FloatingActionButton btn_add_photo = (FloatingActionButton) findViewById(R.id.btn_add_photo);
        btn_add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImage();
            }
        });

        //Check
        FloatingActionButton btn_check = (FloatingActionButton)findViewById(R.id.btn_check);
        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkImage(adapter);
            }
        });

        // Share
        FloatingActionButton btn = (FloatingActionButton)findViewById(R.id.btn_sns);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareImage(adapter);
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

    public void checkImage(ListAdapter adapter) {
        adapter.updateView(1);

    }

    // 이미지 공유 함수
    public void shareImage(ListAdapter adapter) {

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

    // 새로운 사진 추가하기
    public void addImage() {
        Intent newIntent = new Intent(this, AlbumSelectActivity.class);
        startActivityForResult(newIntent, Constants.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            final ArrayList<Image> image_list = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
            int size = image_list.size();
            System.out.println("add photo activity result image_list size :" + size);

            db = new DatabaseHelper(getApplicationContext());

            for (int i = 0; i < image_list.size(); i++) {

                // last modified time
                File file = new File(image_list.get(i).path);
                long last_modified = file.lastModified();

                // convert to Date format
                Date date_time = new Date(last_modified);

                // convert to SimpleDateFormat
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String simple_date = formatter.format(date_time);

                // convert to yyyymmdd format
                int date = Integer.parseInt(simple_date.replace("-", ""));

                // creating and inserting pictures
                Picture pic = new Picture(image_list.get(i).path, date, last_modified);
                long pic_id = db.createPicture(pic);

                // Get name_id with name
                Person person = (Person) getIntent().getExtras().getSerializable(PERSON);
                Name name = db.getNameWithString(person.name);
                long name_id = name.getId();
                // Inserting name_id & picture_id pair
                long name_picture_id = db.createNamePicture(name_id, pic_id);

            }
        }
    }
}


