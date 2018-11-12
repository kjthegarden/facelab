package snu.facelab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;

import ch.zhaw.facerecognitionlibrary.Helpers.FileHelper;
import ch.zhaw.facerecognitionlibrary.Helpers.MatName;
import ch.zhaw.facerecognitionlibrary.Helpers.MatOperation;
import ch.zhaw.facerecognitionlibrary.PreProcessor.PreProcessorFactory;
import snu.facelab.helper.DatabaseHelper;
import snu.facelab.model.Name;
import snu.facelab.model.Picture;

public class OpenGalleryActivity extends AppCompatActivity {
    private PreProcessorFactory ppF;
    private FileHelper fh;
    private String folder;
    private String name;
    private int total;
    private ImageView[] imgView=new ImageView[10];
    //private Bitmap[] image=new Bitmap[10];
    public static final String IMAGES = "images";

    // DatabaseHelper 객체
    DatabaseHelper db;

    static {
        if (!OpenCVLoader.initDebug()) {
            // Handle initialization error
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_gallery);

        Intent intent = getIntent();
        folder = intent.getStringExtra("Folder");
        name = intent.getStringExtra("Name");

        fh = new FileHelper();
        total = 0;

        Intent newIntent = new Intent(this, AlbumSelectActivity.class);

        //newIntent.putExtra("Folder", folder);

        startActivityForResult(newIntent, Constants.REQUEST_CODE);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> image_list = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);

            // DBHelper 객체 생성
            db = new DatabaseHelper(getApplicationContext());

            // Array for pictures
            Picture pic[] = new Picture[10];
            long pic_ids[] = new long[10];

            for (int i = 0; i < 10; i++) {
                // Creating pictures
                pic[i] = new Picture(image_list.get(i).path);
                // Inserting pictures in db
                pic_ids[i] = db.createPicture(pic[i]);

                Mat mat = Imgcodecs.imread(image_list.get(i).path);
                Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGRA2RGBA);
                Mat imgCopy = new Mat();
                mat.copyTo(imgCopy);
                List<Mat> images = ppF.getCroppedImage(imgCopy);

                //image[i] = BitmapFactory.decodeFile(image_list.get(i).path);

                // Check that only 1 face is found. Skip if any or more than 1 are found.
                if (images != null && images.size() == 1) {
                    Mat img = images.get(0);
                    if (img != null) {
                        Rect[] faces = ppF.getFacesForRecognition();
                        //Only proceed if 1 face has been detected, ignore if 0 or more than 1 face have been detected
                        if ((faces != null) && (faces.length == 1)) {
                            faces = MatOperation.rotateFaces(mat, faces, ppF.getAngleForRecognition());
                            MatName m = new MatName(name + "_" + i, img);
                            String wholeFolderPath = fh.TRAINING_PATH + name;
                            new File(wholeFolderPath).mkdirs();
                            fh.saveMatToImage(m, wholeFolderPath + "/");
                            total++;
                        }

                        for (int j = 0; j < faces.length; j++) {
                            MatOperation.drawRectangleAndLabelOnPreview(mat, faces[j], String.valueOf(i), true);
                        }

                        for (int j = 0; j < faces.length; j++) {
                            MatOperation.drawRectangleOnPreview(mat, faces[j], true);
                        }
                    }
                }
            }

            // Creating name
            Name name1 = new Name(name);
            // Inserting name with pictures in db
            long name_id = db.createName(name1, pic_ids);

            Intent intent = new Intent(getApplicationContext(), TrainingActivity.class);
            intent.putExtra("Name", name);
            /*Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            if(total>0){
                intent.putExtra(name, total+"success");
            }*/
            startActivity(intent);
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        ppF = new PreProcessorFactory(this);
    }

}