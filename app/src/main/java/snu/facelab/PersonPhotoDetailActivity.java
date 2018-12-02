package snu.facelab;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;
import com.wx.wheelview.widget.WheelViewDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import snu.facelab.model.Name;
import snu.facelab.model.Picture;

import snu.facelab.helper.DatabaseHelper;

public class PersonPhotoDetailActivity extends AppCompatActivity {
    public static final String PHOTO = "Photo";
    private ImageView iv;
    private Bitmap image;
    private String changeName;

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

        final long pic_id = db.getPictureIdByPath(photo.getPath());

        Button btnEdit = (Button) findViewById(R.id.btn_edit);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // select name from wheel view dialog
                showDialog(v, pic_id);
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

    public void showDialog(View view, final long id) {
        WheelViewDialog dialog = new WheelViewDialog(this);
        dialog.setTitle("Select Name").setItems(createNameArrays()).setButtonText("OK").setDialogStyle(Color
                .parseColor("#6699ff")).setCount(getNameWheelSize()).show();


        dialog.setOnDialogItemClickListener(new WheelViewDialog.OnDialogItemClickListener() {
            @Override
            public void onItemClick(int position, String data) {
                changeName = data;

                // change name matched with the picture
                int change_name_id = db.getNameWithString(changeName).getId();
                db.changeNamePicture(id, change_name_id);
                System.out.println("changeNamePicture end");

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
