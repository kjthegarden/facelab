package snu.facelab_ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<Person> al = new ArrayList<Person>();
    public static final String PERSON = "Person";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ListView
        //    1. 다량의 데이터
        //    2. Adapter (데이터와 view의 연결 관계를 정의)
        //    3. AdapterView (ListView)

        int [] person1 = new int [] {R.drawable.jungwon_1, R.drawable.jungwon_2, R.drawable.jungwon_3, R.drawable.jungwon_4, R.drawable.jungwon_5,
                                     R.drawable.jungwon_6, R.drawable.jungwon_7, R.drawable.jungwon_8, R.drawable.jungwon_9};
        int [] person2 = new int [] {R.drawable.yunsun_1, R.drawable.yunsun_2, R.drawable.yunsun_3, R.drawable.yunsun_4, R.drawable.yunsun_5,
                                     R.drawable.yunsun_6, R.drawable.yunsun_7, R.drawable.yunsun_8, R.drawable.yunsun_9};
        int [] person3 = new int [] {R.drawable.jooyun_1, R.drawable.jooyun_2, R.drawable.jooyun_3, R.drawable.jooyun_4, R.drawable.jooyun_5,
                                     R.drawable.jooyun_6, R.drawable.jooyun_7, R.drawable.jooyun_8, R.drawable.jooyun_9};

        al.add(new Person(R.drawable.jungwon, person1, "Jungwon Kim"));
        al.add(new Person(R.drawable.yunsun, person2, "Yunsun Lee"));
        al.add(new Person(R.drawable.jooyun, person3, "Jooyun Lee"));


        // adapter
        MainAdapter adapter = new MainAdapter(
                getApplicationContext(), // 현재 화면의 제어권자
                R.layout.row,             // 한행을 그려줄 layout
                al);                     // 다량의 데이터

        ListView lv = (ListView)findViewById(R.id.listView1);
        lv.setAdapter(adapter);

        // 이벤트 처리
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ListView listView = (ListView) adapterView;
                Person person = (Person) listView.getItemAtPosition(position);
                Log.d("JO", "selected item => "+person.name);

                Intent i = new Intent(MainActivity.this, DetailActivity.class);
                i.putExtra(PERSON, person);
                startActivity(i);
            }
        });

    }
}

class MainAdapter extends BaseAdapter {
    Context context;     // 현재 화면의 제어권자
    int layout;              // 한행을 그려줄 layout
    ArrayList<Person> al;     // 다량의 데이터
    LayoutInflater inf; // 화면을 그려줄 때 필요
    public MainAdapter(Context context, int layout, ArrayList<Person> al) {
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

        ImageView iv = (ImageView)convertView.findViewById(R.id.imageView1);
        TextView tvName=(TextView)convertView.findViewById(R.id.tvName);

        Person m = al.get(position);

        iv.setImageResource(m.mainImg);
        tvName.setText(m.name);
        return convertView;
    }
}

