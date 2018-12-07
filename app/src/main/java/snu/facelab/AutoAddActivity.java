package snu.facelab;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
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
import ch.zhaw.facerecognitionlibrary.Helpers.MatOperation;
import ch.zhaw.facerecognitionlibrary.PreProcessor.PreProcessorFactory;
import ch.zhaw.facerecognitionlibrary.Recognition.Recognition;
import ch.zhaw.facerecognitionlibrary.Recognition.RecognitionFactory;
import snu.facelab.helper.DatabaseHelper;
import snu.facelab.model.Name;
import snu.facelab.model.Picture;

public class AutoAddActivity extends AppCompatActivity {


    //private ProgressBar progressBar;

    private static final String TAG = "From gallery";
    private FileHelper fh;
    private static int PICK_IMAGE_REQUEST = 1;
    ImageView imgView;
    Thread thread;
    private PreProcessorFactory ppF;
    private Recognition rec;
    //private ProgressBar progressBar;
    //private TextView tv;

    private String path;

    //public static final String IMAGES = "images";

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
        setContentView(R.layout.activity_auto_add);

        //progressBar = (ProgressBar)findViewById(R.id.progressBar2);
        //progressBar.setVisibility(ProgressBar.VISIBLE);

        fh = new FileHelper();
        File folder = new File(fh.getFolderPath());
        if(folder.mkdir() || folder.isDirectory()){
            Log.i(TAG,"New directory for photos created");
        } else {
            Log.i(TAG,"Photos directory already existing");
        }
        //SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        Intent newIntent = new Intent(this, AlbumSelectActivity.class);
        startActivityForResult(newIntent, Constants.REQUEST_CODE);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            final ArrayList<Image> image_list = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
            int size = image_list.size();
            System.out.println("add photo activity result image_list size :" + size);

            db = new DatabaseHelper(getApplicationContext());

            for (int i = 0; i < image_list.size(); i++) {

                // last modified time
                File file = new File(image_list.get(i).path);
                long last_modified = file.lastModified();

                // convert to Date format
                Date date_time = new Date(last_modified);

                // convert to SimpleDateFormat
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String simple_date = formatter.format(date_time);

                // convert to yyyymmdd format
                int date= Integer.parseInt(simple_date.replace("-", ""));

                // creating and inserting pictures
                Picture pic = new Picture(image_list.get(i).path, date, last_modified);
                long pic_id = db.createPicture(pic);

                Mat mat = Imgcodecs.imread(image_list.get(i).path);
                Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGRA2RGBA);
                Mat imgCopy = new Mat();
                mat.copyTo(imgCopy);

                final List<Mat> images = ppF.getProcessedImage(imgCopy, PreProcessorFactory.PreprocessingMode.RECOGNITION);

                if(images!=null){
                    Rect[] faces = ppF.getFacesForRecognition();

                    faces = MatOperation.rotateFaces(mat, faces, ppF.getAngleForRecognition());

                    for (int j = 0; j < faces.length; j++) {
                        String rec_name = rec.recognize(images.get(j), "");
                        System.out.println(j + " : " + rec_name);

                        if(rec_name!=null){
                            // 폴더명에서 facelab 제외해서 name_id 구하기
                            long name_id = Long.parseLong(rec_name.substring(7))+1;
                            // Inserting name_id & picture_id pair
                            long name_picture_id = db.createNamePicture(name_id, pic_id);
                        }
                    }
                }

            }
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();

        ppF = new PreProcessorFactory(getApplicationContext());
        final Handler handler = new Handler(Looper.getMainLooper());
        Thread t = new Thread(new Runnable() {
            public void run() {
                /*handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                });*/
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String algorithm = sharedPref.getString("key_classification_method", getResources().getString(R.string.eigenfaces));
                //System.out.println("algorithm : "+ algorithm);
                rec = RecognitionFactory.getRecognitionAlgorithm(getApplicationContext(), Recognition.RECOGNITION, algorithm);
                //System.out.println("algorithm : "+ algorithm);
                /*handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                    }
                });*/
            }
        });

        t.start();

        // Wait until Eigenfaces loading thread has finished
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}