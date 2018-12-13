package snu.facelab;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
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

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

import snu.facelab.helper.DatabaseHelper;
import snu.facelab.model.Picture;

public class DetailPhoto extends android.support.v4.app.Fragment {
    //implements DatePickerDialog.OnDateSetListener
    private ImageView iv;
    private Bitmap image;
    private String changeName;
    private long pic_id;
    private long name_id;
    private boolean toggleFlag = false;
    private Person person;
    private Picture photo;
    private List<Picture> photo_list;
    private List<Long> pic_id_list;
    private int photo_size;
    private int curr_index;


    // DatabaseHelper 객체
    DatabaseHelper db;

    public void getData(Person person, List<Picture> photo_list, int curr_index) {
        this.person = person;
        this.photo_list = photo_list;
        this.curr_index = curr_index;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        RelativeLayout Layout= (RelativeLayout) inflater.inflate(R.layout.detail_slider,container,false);
        Context context = container.getContext();
        db = new DatabaseHelper(context);

        photo = photo_list.get(curr_index);

        iv = Layout.findViewById(R.id.person_photo_detail);

        Uri imageUri = Uri.fromFile(new File(photo.getPath()));
        Glide.with(context)
                .load(imageUri)
                .into(iv);

        return Layout;

    }


}

