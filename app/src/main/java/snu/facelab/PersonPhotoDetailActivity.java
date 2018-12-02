package snu.facelab;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import com.wx.wheelview.widget.WheelViewDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import snu.facelab.model.Name;
import snu.facelab.model.Picture;

import snu.facelab.helper.DatabaseHelper;

public class PersonPhotoDetailActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    public static final String PHOTO = "Photo";
    private ImageView iv;
    private Bitmap image;
    private String changeName;
    private long pic_id;

    // DatabaseHelper 객체
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_photo_detail);

        Picture photo = (Picture) getIntent().getExtras().getSerializable(PHOTO);
        Log.d("test", String.valueOf(photo.getPath()));

        db = new DatabaseHelper(getApplicationContext());

        iv = (ImageView) findViewById(R.id.person_photo_detail);
        image = BitmapFactory.decodeFile(photo.getPath());
        iv.setImageBitmap(image);

        pic_id = db.getPictureIdByPath(photo.getPath());

        Button btnEditDate = (Button) findViewById(R.id.btn_edit_date);
        btnEditDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // select date from calendar
                datePicker(v);
            }
        });

        Button btnEditName = (Button) findViewById(R.id.btn_edit_name);
        btnEditName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // select name from wheel view dialog
                showNameDialog(v);
            }
        });

        Button btnDelete = (Button) findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.deleteNamePicture(pic_id);
                Toast.makeText(getApplicationContext(), "Successfully deleted.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /****** EDIT DATE *******/
    public void datePicker(View view){
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.show(getSupportFragmentManager(), "date");
    }

    // To receive a callback when the user sets the date.
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
       // convert to yyyymmdd format
        int date = year * 10000 + (month+1) * 100 + day;
        db.changePictureDate(pic_id, date);
        Toast.makeText(getApplicationContext(), "Successfully edited.", Toast.LENGTH_SHORT).show();
    }

    /****** EDIT NAME *******/
    public void showNameDialog(View view) {
        WheelViewDialog dialog = new WheelViewDialog(this);
        dialog.setTitle("Select name").setItems(createNameArrays()).setButtonText("OK").setDialogStyle(Color
                .parseColor("#6699ff")).setCount(getNameWheelSize()).show();

        dialog.setOnDialogItemClickListener(new WheelViewDialog.OnDialogItemClickListener() {
            @Override
            public void onItemClick(int position, String data) {
                changeName = data;

                // change name matched with the picture
                int change_name_id = db.getNameWithString(changeName).getId();
                db.changeNamePicture(pic_id, change_name_id);

                Toast.makeText(getApplicationContext(), "Successfully edited.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public int getNameWheelSize() {
        db = new DatabaseHelper(getApplicationContext());
        List<Name> names = db.getAllNames();
        int names_size = names.size();

        if(names_size % 2 ==0) names_size++;

        return names_size;
    }
    public ArrayList<String> createNameArrays() {
        db = new DatabaseHelper(getApplicationContext());
        List<Name> names = db.getAllNames();
        ArrayList<String> names_string= new ArrayList<>();

        for(int i=0; i<names.size(); i++){
            names_string.add(names.get(i).getName());
        }
        return names_string;
    }
}
