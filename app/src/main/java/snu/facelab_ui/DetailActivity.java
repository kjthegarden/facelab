package snu.facelab_ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static snu.facelab_ui.MainActivity.PERSON;

public class DetailActivity extends AppCompatActivity {
    ArrayList<Photo> al = new ArrayList<Photo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        Intent intent = getIntent();
        Person person = (Person) getIntent().getExtras().getSerializable(PERSON);
        setTitle(person.name);

        int imgCount = person.getImgs().length;
        //LinearLayout layout = (LinearLayout)findViewById(R.id.imageLayout);

        for(int i = 0; i < imgCount; i++)
        {
            al.add(new Photo(person.imgs[i], ""));
//            ImageView image = new ImageView(this);
//            image.setLayoutParams(new android.view.ViewGroup.LayoutParams(300,400));
//            image.setImageResource(person.imgs[i]);
//            image.setMaxHeight(20);
//            image.setMaxWidth(20);
//            image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//
//            // Adds the view to the layout
//            layout.addView(image);
        }

        Log.d("test", al.toString());

        // adapter
        PhotoAdapter adapter = new PhotoAdapter(
                getApplicationContext(), // 현재 화면의 제어권자
                R.layout.photo,             // 한행을 그려줄 layout
                al);                     // 다량의 데이터

        ListView lv = (ListView)findViewById(R.id.listView2);
        lv.setAdapter(adapter);

        Log.d("test", String.valueOf(adapter.getCount()));

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


class PhotoAdapter extends BaseAdapter {
    Context context;     // 현재 화면의 제어권자
    int layout;              // 한행을 그려줄 layout
    ArrayList<Photo> al;     // 다량의 데이터
    LayoutInflater inf; // 화면을 그려줄 때 필요

    public PhotoAdapter(Context context, int layout, ArrayList<Photo> al) {
        this.context = context;
        this.layout = layout;
        this.al = al;
        this.inf = (LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() { // 총 데이터의 개수를 리턴
        return al.size();
    }
    @Override
    public Object getItem(int position) { // 해당번째의 데이터 값
        return al.get(position);
    }
    @Override
    public long getItemId(int position) { // 해당번째의 고유한 id 값
        return position;
    }
    @Override // 해당번째의 행에 내용을 셋팅(데이터와 레이아웃의 연결관계 정의)
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inf.inflate(layout, null);

        ImageView iv = (ImageView)convertView.findViewById(R.id.photoView);
        //TextView tvName=(TextView)convertView.findViewById(R.id.tvName);

        Photo m = al.get(position);

        iv.setImageResource(m.getImg());
        //tvName.setText(m.name);
        return convertView;
    }
}


