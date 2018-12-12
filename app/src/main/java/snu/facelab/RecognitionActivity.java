package snu.facelab;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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

import ch.zhaw.facerecognitionlibrary.Helpers.FileHelper;
import ch.zhaw.facerecognitionlibrary.Helpers.MatName;
import ch.zhaw.facerecognitionlibrary.Helpers.MatOperation;
import ch.zhaw.facerecognitionlibrary.Helpers.PreferencesHelper;
import ch.zhaw.facerecognitionlibrary.PreProcessor.PreProcessorFactory;
import ch.zhaw.facerecognitionlibrary.Recognition.Recognition;
import ch.zhaw.facerecognitionlibrary.Recognition.RecognitionFactory;
import snu.facelab.helper.DatabaseHelper;
import snu.facelab.model.Picture;

public class RecognitionActivity extends AppCompatActivity {
    private static int PICK_IMAGE_REQUEST = 1;
    ImageView imgView;
    Thread thread;
    private PreProcessorFactory ppF;
    private Recognition rec;
    private ProgressBar progressBar;
    private static final String TAG = "From gallery";
    private FileHelper fh;
    //private String path;
    private ArrayList<String> path_list;
    TextView text_view;

    DatabaseHelper db;

    static {
        if (!OpenCVLoader.initDebug()) {
            // Handle initialization error
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognition);
        text_view = findViewById(R.id.rec_text);
        System.out.println("Recognition activity");

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = getIntent();
        path_list = intent.getStringArrayListExtra("Path");

    }

    @Override
    protected void onResume() {
        super.onResume();

        final Handler handler = new Handler(Looper.getMainLooper());
        thread = new Thread(new Runnable() {
            public void run() {
                if(!Thread.currentThread().isInterrupted()){
                    db = new DatabaseHelper(getApplicationContext());

                    fh = new FileHelper();
                    File folder = new File(fh.getFolderPath());
                    if(folder.mkdir() || folder.isDirectory()){
                        Log.i(TAG,"New directory for photos created");
                    } else {
                        Log.i(TAG,"Photos directory already existing");
                    }

                    for(int i=0; i<path_list.size(); i++){
                        final int ii = i+1;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                text_view.setText(ii+" / "+path_list.size()+" 인식중...");
                            }
                        });


                        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        String path = path_list.get(i);
                        File file = new File(path);
                        long last_modified = file.lastModified();

                        // convert to Date format
                        Date date_time = new Date(last_modified);

                        // convert to SimpleDateFormat
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        String simple_date = formatter.format(date_time);

                        // convert to yyyymmdd format
                        int date= Integer.parseInt(simple_date.replace("-", ""));

                        // creating and inserting pictures
                        Picture pic = new Picture(path, date, last_modified);
                        long pic_id = db.createPicture(pic);

                        Mat src = Imgcodecs.imread(path);
                        Imgproc.cvtColor(src, src, Imgproc.COLOR_BGRA2RGBA);

                        Mat mat = new Mat();
                        Mat processedImage = new Mat();

                        if(src.width()>1000){
                            Size sz = new Size(src.width()/4, src.height()/4);
                            Imgproc.resize(src, mat, sz);
                            mat.copyTo(processedImage);
                        }


                        else{
                            src.copyTo(mat);
                            mat.copyTo(processedImage);
                        }

                        ppF = new PreProcessorFactory(getApplicationContext());
                        final List<Mat> images = ppF.getProcessedImage(processedImage, PreProcessorFactory.PreprocessingMode.RECOGNITION);

                        if(images!=null) {
                            Rect[] faces = ppF.getFacesForRecognition();
                            faces = MatOperation.rotateFaces(mat, faces, ppF.getAngleForRecognition());
                            System.out.println("#of detected faces : " + faces.length);


                            String algorithm = sharedPref.getString("key_classification_method", getResources().getString(R.string.eigenfaces));
                            rec = RecognitionFactory.getRecognitionAlgorithm(getApplicationContext(), Recognition.RECOGNITION, algorithm);

                            for (int j = 0; j < faces.length; j++) {

                                String rec_name = rec.recognize(images.get(j), "");
                                System.out.println(j + " : " + rec_name);

                                if (rec_name != null) {
                                    // 폴더명에서 facelab 제외해서 name_id 구하기
                                    long name_id = Long.parseLong(rec_name.substring(7)) + 1;
                                    // Inserting name_id & picture_id pair
                                    long name_picture_id = db.createNamePicture(name_id, pic_id);
                                }
                            }
                        }

                    }
                    final Intent intent_return = new Intent(getApplicationContext(), MainActivity.class);
                    intent_return.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(intent_return);
                        }
                    });
                } else {
                    Thread.currentThread().interrupt();
                }
            }
        });
        thread.start();

    }


}
