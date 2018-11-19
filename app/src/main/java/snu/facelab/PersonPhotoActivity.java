package snu.facelab;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import snu.facelab.helper.DatabaseHelper;
import snu.facelab.model.Picture;

import static snu.facelab.MainActivity.PERSON;

public class PersonPhotoActivity extends AppCompatActivity {
    ArrayList<Photo> al = new ArrayList<Photo>();
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

        // get pictures from db
        List<Picture> pictures = new ArrayList<Picture>();

        pictures = db.getAllPicturesByName(person.name);

        int imgCount = pictures.size();

        for(int i = 0; i < imgCount; i++)
        {
           al.add(new Photo(pictures.get(i).getPath()));
        }


        Log.d("test", al.toString());

        // adapter
        PhotoAdapter adapter = new PhotoAdapter(
                getApplicationContext(), // 현재 화면의 제어권자
                R.layout.photo,             // 한행을 그려줄 layout
                al);                     // 다량의 데이터

        GridView Gv = findViewById(R.id.gridView2);
        Gv.setAdapter(adapter);

        // 이벤트 처리
        Gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                GridView GridView = (GridView) adapterView;
                Photo photo = (Photo) GridView.getItemAtPosition(position);

                Intent i = new Intent(PersonPhotoActivity.this, PersonPhotoDetailActivity.class);
                i.putExtra(PHOTO, photo);
                startActivity(i);
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
}


