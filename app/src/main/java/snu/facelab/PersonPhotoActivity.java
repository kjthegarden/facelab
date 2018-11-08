package snu.facelab;

import android.content.Context;
import android.content.Intent;
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

import static snu.facelab.MainActivity.PERSON;

public class PersonPhotoActivity extends AppCompatActivity {
    ArrayList<Photo> al = new ArrayList<Photo>();
    public static final String PHOTO = "Photo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_photo);

        Intent intent = getIntent();
        Person person = (Person) getIntent().getExtras().getSerializable(PERSON);
        setTitle(person.name);

        int imgCount = person.getImgs().length;

        for(int i = 0; i < imgCount; i++)
        {
            al.add(new Photo(person.imgs[i], ""));
        }

        Log.d("test", al.toString());

        // adapter
        PhotoAdapter adapter = new PhotoAdapter(
                getApplicationContext(), // 현재 화면의 제어권자
                R.layout.photo,             // 한행을 그려줄 layout
                al);                     // 다량의 데이터

        ListView lv = (ListView)findViewById(R.id.listView2);
        lv.setAdapter(adapter);

        // 이벤트 처리
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ListView ListView = (ListView) adapterView;
                Photo photo = (Photo) ListView.getItemAtPosition(position);

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


