package snu.facelab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;

import java.util.ArrayList;

import ch.zhaw.facerecognitionlibrary.Helpers.FileHelper;

public class AddPersonPreviewActivity extends AppCompatActivity {
    private FileHelper fh;
    private String folder;
    private String subfolder;
    private String name;
    private TextView textView;
    private ImageView imgView;
    public static final String IMAGES = "images";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person_preview);

        Intent newIntent = new Intent(this, AlbumSelectActivity.class);
        startActivityForResult(newIntent, Constants.REQUEST_CODE);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            //The array list has the image paths of the selected images
            ArrayList<Image> images = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
            Log.d("STATE", images.get(0).path.toString());

            Intent launchResult = new Intent(this, AddPersonActivity.class);
            launchResult.putExtra(IMAGES, images);
            startActivity(launchResult);

        }

        //File imgFile = new File(images.get(0).path)

//        Intent intent = getIntent();
//        folder = intent.getStringExtra("Folder");
//        if(folder.equals("Test")){
//            subfolder = intent.getStringExtra("Subfolder");
//        }
//        name = intent.getStringExtra("Name");
//
//        fh = new FileHelper();
    }
}
