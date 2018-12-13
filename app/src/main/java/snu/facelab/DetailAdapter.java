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

public class DetailAdapter extends FragmentPagerAdapter {
    //implements DatePickerDialog.OnDateSetListener
    private ImageView iv;
    private Bitmap image;
    private String changeName;
    private long pic_id;
    private long name_id;
    private boolean toggleFlag = false;
    private List<Picture> photo_list;
    private List<Long> pic_id_list;
    private int photo_size;
    private int curr_index;
    private Context context;
    private Person person;
    private Picture photo;

    Fragment cur_fragment = new Fragment();


    // DatabaseHelper 객체
    DatabaseHelper db;

    public DetailAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    public void getData(Person person, List<Picture> photo_list, int curr_index) {
//        this.name_id = name_id;
//        this.pic_id = pic_id;
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

    public Fragment getCurrentFragment() {
        return cur_fragment;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (getCurrentFragment() != object) {
            cur_fragment = ((Fragment) object);
        }
        super.setPrimaryItem(container, position, object);
    }
}


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_person_photo_detail);
////
////        // 전체화면
////        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
////
////        // No default action bar
////        //getSupportActionBar().hide();
//
//        Picture photo = (Picture) getIntent().getExtras().getSerializable(PHOTO);
//        person = (Person) getIntent().getExtras().getSerializable(PERSON);
//
//        // 제목 설정
//        TextView title = findViewById(R.id.date_title);
//        String dateAll = String.valueOf(photo.getDate());
//        String year = dateAll.substring(0,4);
//
//        String month = dateAll.substring(4, 6);
//        Integer month_int = Integer.valueOf(month);
//        month = String.valueOf(month_int);
//
//        String date = dateAll.substring(6, 8);
//        Integer date_int = Integer.valueOf(month);
//        date = String.valueOf(date_int);
//
//        String titleFormat = year + "년 " + month + "월 " + date + "일";
//
//
//        // last modified time
//        File file = new File(photo.getPath());
//        long last_modified = file.lastModified();
//
//        // convert to Date format
//        Date date_time = new Date(last_modified);
//
//        // convert to SimpleDateFormat
//        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
//        String simple_date = formatter.format(date_time);
//
//        // convert to yyyymmdd format
//        //int date_= Integer.parseInt(simple_date.replace("-", ""));
//        String subTitleFormat = simple_date;
//        title.setText(getFormattedLabelText(titleFormat, subTitleFormat));
//
//
//
//        db = new DatabaseHelper(getApplicationContext());
//
//        iv = findViewById(R.id.person_photo_detail);
//
//        Uri imageUri = Uri.fromFile(new File(photo.getPath()));
//        Glide.with(DetailAdapter.this)
//                .load(imageUri)
//                .into(iv);
//
//        name_id = db.getNameWithString(person.name).getId();
//        pic_id = db.getPictureIdByPath(photo.getPath());
//
//        final List<String> names = db.getAllNameByPicId(pic_id);
//
//
//        // 해시태그 sheet용 버튼
//        final FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                BottomSheetDialog bottomSheetDialog = BottomSheetDialog.getInstance();
//                bottomSheetDialog.getNames(names);
//                bottomSheetDialog.show(getSupportFragmentManager(),"bottomSheet");
//            }
//        });
//
//
//        final RelativeLayout backLayout = findViewById(R.id.photo_detail);
//        //final RelativeLayout titleLayout = findViewById(R.id.title_layout);
//        final Toolbar titleLayout = findViewById(R.id.toolbar_photo);
//        setSupportActionBar(titleLayout);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//
//        // 배경 클릭할 시 검은 바탕에 사진만 뜨기
//        backLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                toggleDisplay(backLayout, fab);
//            }
//        });
//
//        // 툴바 레이아웃 클릭 시 아무일 없도록
//        titleLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // do nothing
//            }
//        });
//
//
//        // 뒤로가기 버튼
//        AppCompatButton backBtn = findViewById(R.id.photo_back);
//        backBtn.setOnClickListener(new Button.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.photo_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.edit_date:
//                datePicker();
//                break;
//            case R.id.edit_name:
//                showNameDialog();
//                break;
//            case R.id.delete:
//                db.deleteNamePicture(pic_id, name_id);
//                Toast.makeText(getApplicationContext(), "Successfully deleted.", Toast.LENGTH_SHORT).show();
//
//                Intent i = new Intent(getApplicationContext(), PersonPhotoActivity.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                i.putExtra(PERSON, person);
//                startActivity(i);
//                finish();
//        }
//        return true;
//    }
//
//
//
//    /****** EDIT DATE *******/
//    public void datePicker(){
//        DatePickerFragment fragment = new DatePickerFragment();
//        fragment.show(getSupportFragmentManager(), "date");
//    }
//
//    // To receive a callback when the user sets the date.
//    @Override
//    public void onDateSet(DatePicker view, int year, int month, int day) {
//       // convert to yyyymmdd format
//        int date = year * 10000 + (month+1) * 100 + day;
//        db.changePictureDate(pic_id, date);
//        Toast.makeText(getApplicationContext(), "Successfully edited.", Toast.LENGTH_SHORT).show();
//
//        Intent i = new Intent(getApplicationContext(), PersonPhotoActivity.class);
//        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        i.putExtra(PERSON, person);
//        startActivity(i);
//    }
//
//    /****** EDIT NAME *******/
//    public void showNameDialog() {
//        WheelViewDialog dialog = new WheelViewDialog(this);
//        dialog.setTitle("Select name").setItems(createNameArrays()).setButtonText("OK").setDialogStyle(Color
//                .parseColor("#6699ff")).setCount(getNameWheelSize()).show();
//
//        dialog.setOnDialogItemClickListener(new WheelViewDialog.OnDialogItemClickListener() {
//            @Override
//            public void onItemClick(int position, String data) {
//                changeName = data;
//
//                // change name matched with the picture
//                int change_name_id = db.getNameWithString(changeName).getId();
//                db.changeNamePicture(pic_id, name_id, change_name_id);
//
//                Toast.makeText(getApplicationContext(), "Successfully edited.", Toast.LENGTH_SHORT).show();
//
//                Intent i = new Intent(getApplicationContext(), PersonPhotoActivity.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                i.putExtra(PERSON, person);
//                startActivity(i);
//            }
//        });
//
//    }
//
//    public int getNameWheelSize() {
//        db = new DatabaseHelper(getApplicationContext());
//        List<Name> names = db.getAllNames();
//        int names_size = names.size();
//
//        if(names_size % 2 ==0) names_size++;
//
//        return names_size;
//    }
//    public ArrayList<String> createNameArrays() {
//        db = new DatabaseHelper(getApplicationContext());
//        List<Name> names = db.getAllNames();
//        ArrayList<String> names_string= new ArrayList<>();
//
//        for(int i=0; i<names.size(); i++){
//            names_string.add(names.get(i).getName());
//        }
//        return names_string;
//    }
//
//    public void toggleDisplay(RelativeLayout backLayout, FloatingActionButton hashButton) {
//        toggleFlag = !toggleFlag;
//
//        if (toggleFlag) {
//            getSupportActionBar().hide();
//            backLayout.setBackgroundColor(Color.parseColor("#000000"));
//            hashButton.hide();
//        }
//        else {
//            getSupportActionBar().show();
//            backLayout.setBackgroundColor(getResources().getColor(R.color.photoBackground));
//            hashButton.show();
//
//        }
//
//    }
//
//    private Spanned getFormattedLabelText(String text, String subText) {
//        String fullText = String.format("%s\n%s", text, subText);
//
//        int fullTextLength = fullText.length();
//        int titleEnd = text.length();
//
//        SpannableStringBuilder s = new SpannableStringBuilder(fullText);
//
//        // Center align the text
//        AlignmentSpan alignmentSpan = new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER);
//        s.setSpan(alignmentSpan, 0, fullTextLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        // Make the title bold
//       // s.setSpan(new StyleSpan(Typeface.BOLD), 0, titleEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); //bold
//
//        // Make the subtext small
//        //int smallTextSize = DisplayUtil.getPixels(TypedValue.COMPLEX_UNIT_SP, 10);
//        s.setSpan(new AbsoluteSizeSpan(17), titleEnd + 1, fullTextLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); //bold
//        //s.setSpan(new ForegroundColorSpan(Color.GRAY), titleEnd + 1, fullTextLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        return s;
//    }
//}
//
