package snu.facelab;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import snu.facelab.model.Picture;

class ListAdapter extends BaseAdapter {
    Context context; // 현재 화면의 제어권자
    int layout; // 한행을 그려줄 layout
    ArrayList<PersonGrid> al = new ArrayList<PersonGrid>(); // 다량의 데이터
    LayoutInflater inf; // 화면을 그려줄 때 필요
    public static final String PHOTO = "Photo";

    public ListAdapter() {

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
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.photo_gridview, parent, false);
        }

        PersonGrid Pg = al.get(position);

        TextView Tv = convertView.findViewById(R.id.gridTitle);
        Tv.setText(Pg.getDate().toString());

        // adapter
        PhotoAdapter adapter = new PhotoAdapter(
                context, // 현재 화면의 제어권자
                R.layout.photo, // 한행을 그려줄 layout
                Pg.getPhotos()); // 다량의 데이터

        GridView Gv = convertView.findViewById(R.id.gridView2);
        Log.d("test", Gv.toString());
        Gv.setAdapter(adapter);

        // 이벤트 처리
        Gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                GridView GridView = (GridView) adapterView;
                Picture photo = (Picture) GridView.getItemAtPosition(position);

                Intent i = new Intent(context, PersonPhotoDetailActivity.class);
                i.putExtra(PHOTO, photo);
                context.startActivity(i);
            }
        });

        return convertView;
    }

    public void addItem(Integer date, List<Picture> photos) {
        PersonGrid Pg = new PersonGrid(date, photos);
        al.add(Pg);
    }
}
