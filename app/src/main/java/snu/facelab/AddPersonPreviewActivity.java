package snu.facelab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;

import java.io.File;
import java.util.ArrayList;

import ch.zhaw.facerecognitionlibrary.Helpers.FileHelper;
import ch.zhaw.facerecognitionlibrary.Helpers.MatName;
import ch.zhaw.facerecognitionlibrary.Helpers.MatOperation;
import ch.zhaw.facerecognitionlibrary.PreProcessor.PreProcessorFactory;

public class AddPersonPreviewActivity extends AppCompatActivity {
    private FileHelper fh;
    private String folder;
    private String subfolder;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person_preview);

        Intent intent = getIntent();
        folder = intent.getStringExtra("Folder");
        if(folder.equals("Test")){
            subfolder = intent.getStringExtra("Subfolder");
        }
        name = intent.getStringExtra("Name");

        fh = new FileHelper();

        Intent newIntent = new Intent(this, AlbumSelectActivity.class);
        startActivityForResult(newIntent, Constants.REQUEST_CODE);
    }


//    public void onButtonClicked(View view) {
//        Intent intent = new Intent(this, AlbumSelectActivity.class);
//        //set limit on number of images that can be selected, default is 10
//        //intent.putExtra(Constants.INTENT_EXTRA_LIMIT, numberOfImagesToSelect);
//        startActivityForResult(intent, Constants.REQUEST_CODE);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            //The array list has the image paths of the selected images
            ArrayList<Image> images = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
        }
    }
}
