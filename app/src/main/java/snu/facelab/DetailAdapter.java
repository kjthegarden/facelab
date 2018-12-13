package snu.facelab;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wx.wheelview.widget.WheelViewDialog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import snu.facelab.helper.DatabaseHelper;
import snu.facelab.layout.BottomSheetDialog;
import snu.facelab.model.Name;
import snu.facelab.model.Picture;

public class DetailAdapter extends FragmentStatePagerAdapter {
    private List<Picture> photo_list;
    private int photo_size;
    private int curr_index;
    private Context context;
    private Person person;
    private boolean toggleFlag;

    Fragment cur_fragment = new Fragment();


    // DatabaseHelper 객체
    DatabaseHelper db;

    public DetailAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    public void getData(Person person, List<Picture> photo_list, int curr_index) {
        this.person = person;
        this.photo_list = photo_list;
        this.curr_index = curr_index;

        db = new DatabaseHelper(context);
        photo_size = photo_list.size();

    }

    @Override
    public Fragment getItem(int position) {
        if (position < 0 || photo_size <= position)
            return null;

        cur_fragment = new DetailPhoto();
        ((DetailPhoto) cur_fragment).getData(person, photo_list, position);

        return cur_fragment;
    }
    @Override
    public int getCount() {
        return photo_size;
    }

    public void toggle(boolean toggleFlag) {
        this.toggleFlag = toggleFlag;
        ((DetailPhoto) cur_fragment).toggle(this.toggleFlag);
    }
}
