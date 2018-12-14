package snu.facelab;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.andremion.louvre.Louvre;
import com.andremion.louvre.home.GalleryActivity;

import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ch.zhaw.facerecognitionlibrary.Helpers.FileHelper;
import ch.zhaw.facerecognitionlibrary.Helpers.MatName;
import ch.zhaw.facerecognitionlibrary.Helpers.MatOperation;
import ch.zhaw.facerecognitionlibrary.PreProcessor.PreProcessorFactory;
import snu.facelab.helper.DatabaseHelper;
import snu.facelab.model.Name;
import snu.facelab.model.Picture;

public class EditNameActivity extends AppCompatActivity {
    public static final String PERSON = "Person";

    private Person person;
    private String name;
    private int total;
    public static final String IMAGES = "images";

    // DatabaseHelper 객체
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_name);

        // 전체화면
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // DBHelper 객체 생성
        db = new DatabaseHelper(getApplicationContext());

        // Get Information
        person = (Person) getIntent().getExtras().getSerializable(PERSON);
        Name pre_name = db.getNameWithString(person.name);
        final int name_id = pre_name.getId();

        AppCompatButton btnEdit = findViewById(R.id.btn_Edit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get name for folder name
                EditText txt_Folder_Name = (EditText) findViewById(R.id.txt_Name);
                name = txt_Folder_Name.getText().toString();

                Name name_set = new Name(name);
                name_set.setId(name_id);


                if (db.getNameWithString(name).getName()!=null) {
                    Toast.makeText(getApplicationContext(), "This name is already used. Please choose another one.", Toast.LENGTH_SHORT).show();
                }
                else if (name.matches("")) {
                    Toast.makeText(getApplicationContext(), "Please enter a name.", Toast.LENGTH_SHORT).show();
                }
                else {
                    db.updateName(name_set);
                    Toast.makeText(getApplicationContext(), "Successfully edited!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }

            }
        });

        AppCompatButton btnCancel = findViewById(R.id.edit_cancel);
        btnCancel.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}