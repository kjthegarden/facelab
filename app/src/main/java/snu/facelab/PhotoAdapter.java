package snu.facelab;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import snu.facelab.Photo;
import snu.facelab.R;
import snu.facelab.model.Picture;

class PhotoAdapter extends BaseAdapter {
    Context context;     // 현재 화면의 제어권자
    int layout;              // 한행을 그려줄 layout
    List<Picture> al;     // 다량의 데이터
    LayoutInflater inf; // 화면을 그려줄 때 필요
    private Bitmap image;
    private int visibleFlag = 0;
    private int allCheckFlag = 0;

    public PhotoAdapter(Context context, int layout, List<Picture> al) {
        this.context = context;
        this.layout = layout;
        this.al = al;
        this.inf = (LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void updateView(int flag) {
        visibleFlag = flag;
        notifyDataSetChanged();
    }

    public void allCheck(int flag) {
        allCheckFlag = flag;
        notifyDataSetChanged();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inf.inflate(layout, null);

        Log.d("check", "-------------------------------------");

        ImageView iv = convertView.findViewById(R.id.photoView);

        final Picture m = al.get(position);

        CheckBox checkbox = convertView.findViewById(R.id.checkBox1);
        checkbox.setVisibility(View.GONE);

        if (visibleFlag == 1) {
            checkbox.setVisibility(View.VISIBLE);
        }

        if (allCheckFlag == 1) {
            checkbox.setChecked(true);
            //Log.d("check", String.valueOf(checkbox.isChecked()));
        }
        if (allCheckFlag == 0) {
            checkbox.setChecked(false);
            Log.d("check", "unchecked");
        }

        Log.d("check", String.valueOf(checkbox.isChecked()));

        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    m.setChecked(true);
                } else {
                    m.setChecked(false);
                    Log.d("check", String.valueOf(allCheckFlag));
                }
            }
        });

        Uri imageUri = Uri.fromFile(new File(m.getPath()));
        Glide.with(context)
                .load(imageUri)
                .apply(new RequestOptions()
                        .centerCrop())
                .into(iv);
        return convertView;
    }
}