package snu.facelab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

        // 전체화면
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = getIntent();
        folder = intent.getStringExtra("Folder");
        name = intent.getStringExtra("Name");

        fh = new FileHelper();
        total = 0;

        Intent newIntent = new Intent(this, AlbumSelectActivity.class);

        startActivityForResult(newIntent, Constants.REQUEST_CODE);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> image_list = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
            int image_list_size = image_list.size();
            if( image_list_size<10){
                Toast.makeText(getApplicationContext(), "Choose at least 10 pictures.", Toast.LENGTH_SHORT).show();
                Intent newIntent = new Intent(this, AlbumSelectActivity.class);

                startActivityForResult(newIntent, Constants.REQUEST_CODE);
            }else{
                // DBHelper 객체 생성
                db = new DatabaseHelper(getApplicationContext());

                // Array for pictures
                long pic_ids[] = new long[image_list_size];

                for (int i = 0; i < image_list_size; i++) {
                    // last modified time
                    File file = new File(image_list.get(i).path);
                    long last_modified = file.lastModified();

                    // convert to Date format
                    Date date_time = new Date(last_modified);
                    System.out.println(date_time);

                    // convert to SimpleDateFormat
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    String simple_date = formatter.format(date_time);
                    System.out.println(simple_date);

                    // convert to yyyymmdd format
                    int date= Integer.parseInt(simple_date.replace("-", ""));

                    // creating and inserting pictures
                    Picture pic = new Picture(image_list.get(i).path, date, last_modified);
                    pic_ids[i] = db.createPicture(pic);

                    Mat src = Imgcodecs.imread(file.getAbsolutePath());
                    Imgproc.cvtColor(src, src, Imgproc.COLOR_BGRA2RGBA);
                    //System.out.println("width" + src.width() + " height" + src.height());
                    Mat mat = new Mat();
                    Mat imgCopy = new Mat();

                    if(src.width()>1000){

                        Size sz = new Size(src.width()/4, src.height()/4);
                        Imgproc.resize(src, mat, sz);
                        mat.copyTo(imgCopy);
                    }
                    else{
                        src.copyTo(mat);
                        src.copyTo(imgCopy);
                    }

                    List<Mat> images = ppF.getCroppedImage(imgCopy);


                    // Check that only 1 face is found. Skip if any or more than 1 are found.
                    if (images != null && images.size() == 1) {
                        Mat img = images.get(0);
                        if (img != null) {
                            Rect[] faces = ppF.getFacesForRecognition();
                            //Only proceed if 1 face has been detected, ignore if 0 or more than 1 face have been detected
                            if ((faces != null) && (faces.length == 1)) {
                                faces = MatOperation.rotateFaces(mat, faces, ppF.getAngleForRecognition());
                                MatName m = new MatName(name + "_" + i, img);

                                // trainging 사진이 저장되는 경로
                                String wholeFolderPath = fh.TRAINING_PATH + "facelab" + db.getNameCount();

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

                // creating name
                Name name1 = new Name(name);
                // inserting name with pictures in db
                long name_id = db.createName(name1, pic_ids);

                Intent intent = new Intent(getApplicationContext(), TrainingActivity.class);
                intent.putExtra("FolderName", "facelab" + (db.getNameCount()-1));

                startActivity(intent);
            }
        }
        else {
            finish();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        ppF = new PreProcessorFactory(this);
    }

}