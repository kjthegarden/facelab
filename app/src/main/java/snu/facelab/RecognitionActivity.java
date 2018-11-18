package snu.facelab;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.List;

import ch.zhaw.facerecognitionlibrary.Helpers.FileHelper;
import ch.zhaw.facerecognitionlibrary.Helpers.MatOperation;
import ch.zhaw.facerecognitionlibrary.PreProcessor.PreProcessorFactory;
import ch.zhaw.facerecognitionlibrary.Recognition.Recognition;
import ch.zhaw.facerecognitionlibrary.Recognition.RecognitionFactory;

public class RecognitionActivity extends AppCompatActivity {
    private static int PICK_IMAGE_REQUEST = 1;
    ImageView imgView;
    Thread thread;
    private PreProcessorFactory ppF;
    private Recognition rec;
    private ProgressBar progressBar;
    private static final String TAG = "From gallery";
    private FileHelper fh;
    private String path;

    static {
        if (!OpenCVLoader.initDebug()) {
            // Handle initialization error
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognition);

        Intent intent = getIntent();
        path = intent.getStringExtra("Path");

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(ProgressBar.VISIBLE);

        fh = new FileHelper();
        File folder = new File(fh.getFolderPath());
        if(folder.mkdir() || folder.isDirectory()){
            Log.i(TAG,"New directory for photos created");
        } else {
            Log.i(TAG,"Photos directory already existing");
        }

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        /*
        Mat mat = Imgcodecs.imread(path);
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGRA2RGBA);

        Mat processedImage = new Mat();
        mat.copyTo(processedImage);

        List<Mat> images = ppF.getProcessedImage(processedImage, PreProcessorFactory.PreprocessingMode.RECOGNITION);
        Rect[] faces = ppF.getFacesForRecognition();

        faces = MatOperation.rotateFaces(mat, faces, ppF.getAngleForRecognition());
        for(int i = 0; i<faces.length; i++){
            //MatOperation.drawRectangleAndLabelOnPreview(mat, faces[i], rec.recognize(images.get(i), ""), true);
            System.out.println(rec.recognize(images.get(i), ""));
        }
        */
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Mat mat = Imgcodecs.imread(path);
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGRA2RGBA);

        Mat processedImage = new Mat();
        mat.copyTo(processedImage);

        List<Mat> images = ppF.getProcessedImage(processedImage, PreProcessorFactory.PreprocessingMode.RECOGNITION);
        Rect[] faces = ppF.getFacesForRecognition();

        faces = MatOperation.rotateFaces(mat, faces, ppF.getAngleForRecognition());
        for(int i = 0; i<faces.length; i++){
            MatOperation.drawRectangleAndLabelOnPreview(mat, faces[i], rec.recognize(images.get(i), ""), true);
            System.out.println(rec.recognize(images.get(i), ""));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ppF = new PreProcessorFactory(getApplicationContext());
        final Handler handler = new Handler(Looper.getMainLooper());
        Thread t = new Thread(new Runnable() {
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                });
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String algorithm = sharedPref.getString("key_classification_method", getResources().getString(R.string.eigenfaces));
                //System.out.println("algorithm : "+ algorithm);
                rec = RecognitionFactory.getRecognitionAlgorithm(getApplicationContext(), Recognition.RECOGNITION, algorithm);
                //System.out.println("algorithm : "+ algorithm);

                /*final Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);*/

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        //startActivity(intent);
                    }
                });
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
