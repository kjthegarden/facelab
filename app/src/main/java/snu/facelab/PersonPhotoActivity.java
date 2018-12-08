package snu.facelab;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;

import org.w3c.dom.Text;

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

        // Person 객체 받아오고 title 설정
        Person person = (Person) getIntent().getExtras().getSerializable(PERSON);
        String title = "<font color=#E6E6FA>#</font>" + person.name;
        TextView name = findViewById(R.id.personPhotoName);
        name.setText(Html.fromHtml(title));

        // DBHelper 객체 생성
        db = new DatabaseHelper(getApplicationContext());

        // ListView 위한 객체 설정
        ListView Lv = findViewById(R.id.listView);
        final ListAdapter adapter = new ListAdapter();

        // get pictures from db by date
        List<Picture> picture_month = new ArrayList<Picture>();
        List<Integer> months = new ArrayList<Integer>();

        months = db.getAllMonthsByName(person.name);

        int monthCount = months.size();

        for (int i = 0; i < monthCount; i++)
        {
            picture_month = db.getAllPicturesByNameAndMonth(person.name, months.get(i));
            adapter.addItem(months.get(i), picture_month, person);
        }

        Lv.setAdapter(adapter);

        // Add Picture
        final AppCompatButton btn_add_photo = findViewById(R.id.btn_add_photo);
        btn_add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImage();
            }
        });

        // Button for check & share
        final AppCompatButton btn_check = findViewById(R.id.btn_check);
        final AppCompatButton btn_cancel = findViewById(R.id.btn_cancel);
        btn_cancel.setVisibility(View.GONE);
        AppCompatButton btn_share = findViewById(R.id.btn_sns);
        btn_share.setVisibility(View.INVISIBLE);

        //Check
        btn_check.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkImage(adapter);
                btn_add_photo.setVisibility(View.GONE);
                btn_check.setVisibility(View.GONE);
                btn_cancel.setVisibility(View.VISIBLE);
            }
        });

        //Cancel
        btn_cancel.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopCheck(adapter);
                btn_add_photo.setVisibility(View.VISIBLE);
                btn_cancel.setVisibility(View.GONE);
                btn_check.setVisibility(View.VISIBLE);
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
        final ListAdapter a = adapter;
        a.updateView(1);

        AppCompatButton btn_share = findViewById(R.id.btn_sns);
        btn_share.setVisibility(View.VISIBLE);
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareImage(a);
            }
        });

    }

    // 이미지 공유 함수
    public void shareImage(ListAdapter adapter) {

        // 사진 절대 경로 받아오기
        adapter.notifyDataSetChanged();
        ArrayList<String> checked_paths = adapter.get_paths();
        Log.d("check", "checked: "+ checked_paths.toString());

        // 여러 개의 사진 공유
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND_MULTIPLE); //전송 메소드를 호출합니다. 여러개의 사진 Intent.ACTION_SEND_MULTIPLE
        intent.setType("image/jpg"); //jpg 이미지를 공유 하기 위해 Type을 정의합니다.

        ArrayList<Uri> files = new ArrayList<Uri>();

        int checked = checked_paths.size();
        String[] filesToSend = new String[checked];
        for (int i = 0; i < checked; i++) {
            filesToSend[i] = checked_paths.get(i);
        }

        adapter.reset_check();

        for(String path : filesToSend /* List of the files you want to send */) {
            File file = new File(path);
            Uri uri = FileProvider.getUriForFile(PersonPhotoActivity.this,
                    "snu.facelab.provider", file);
            files.add(uri);
        }

        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
        startActivity(Intent.createChooser(intent, "Choose")); //Activity를 이용하여 호출 합니다.

    }

    public void stopCheck(ListAdapter adapter) {
        final ListAdapter a = adapter;
        a.updateView(0);

        AppCompatButton btn_share = findViewById(R.id.btn_sns);
        btn_share.setVisibility(View.INVISIBLE);
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


