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


