package snu.facelab;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.util.List;

import snu.facelab.helper.DatabaseHelper;
import snu.facelab.model.Picture;

public class DetailPhoto extends android.support.v4.app.Fragment {
    private PhotoView iv;
    private Person person;
    private Picture photo;
    private List<Picture> photo_list;
    private int curr_index;
    private boolean toggleFlag;


    // DatabaseHelper 객체
    DatabaseHelper db;

    public void getData(Person person, List<Picture> photo_list, int curr_index) {
        this.person = person;
        this.photo_list = photo_list;
        this.curr_index = curr_index;
        //this.toggleFlag = toggleFlag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void toggle(boolean toggleFlag) {
        this.toggleFlag = toggleFlag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final RelativeLayout Layout= (RelativeLayout) inflater.inflate(R.layout.detail_slider,container,false);
        Context context = container.getContext();
        db = new DatabaseHelper(context);

        photo = photo_list.get(curr_index);

        iv = Layout.findViewById(R.id.person_photo_detail);

        Uri imageUri = Uri.fromFile(new File(photo.getPath()));
        Glide.with(context)
                .load(imageUri)
                .into(iv);


        final RelativeLayout backLayout = Layout.findViewById(R.id.photo_detail);

        if (toggleFlag) {
            backLayout.setBackgroundColor(Color.parseColor("#000000"));
        }
        else {
            backLayout.setBackgroundColor(getResources().getColor(R.color.photoBackground));
        }

        // 배경 클릭할 시 검은 바탕에 사진만 뜨기
//        backLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //toggleDisplay(backLayout);
//            }
//        });


        return Layout;

    }

    public void toggleDisplay(RelativeLayout backLayout) {
        toggleFlag = !toggleFlag;

        if (toggleFlag) {
            backLayout.setBackgroundColor(Color.parseColor("#000000"));
        }
        else {
            backLayout.setBackgroundColor(getResources().getColor(R.color.photoBackground));

        }

    }


}

