package snu.facelab;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.ArrayList;

import static snu.facelab.MainActivity.PERSON;

class PersonAdapter extends BaseAdapter {
    Context context;     // 현재 화면의 제어권자
    int layout;              // 한행을 그려줄 layout
    ArrayList<Person> al;     // 다량의 데이터
    LayoutInflater inf; // 화면을 그려줄 때 필요
    private Bitmap image;

    public PersonAdapter(Context context, int layout, ArrayList<Person> al) {
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
        final Context context = parent.getContext();
        if (convertView == null)
            convertView = inf.inflate(layout, null);

        ImageView iv = (ImageView)convertView.findViewById(R.id.imageView1);
        TextView tvName=(TextView)convertView.findViewById(R.id.tvName);

       final  Person m = al.get(position);

        Uri imageUri;
        if(m.getMainImg()!=null){
            imageUri = Uri.fromFile(new File(m.getMainImg()));
        }else{
            imageUri =  Uri.parse("android.resource://snu.facelab/" + R.drawable.smile);
        }

        Glide.with(context)
                .load(imageUri)
                .apply(new RequestOptions()
                        .centerCrop())
                .into(iv);

        tvName.setText(m.name);

        // Edit Name
        AppCompatButton bt = convertView.findViewById(R.id.edit);
        bt.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, EditNameActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra(PERSON, m);
                context.startActivity(i);
            }
        });

        return convertView;
    }
}

