package snu.facelab;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import ch.zhaw.facerecognitionlibrary.Helpers.FileHelper;
import ch.zhaw.facerecognitionlibrary.Helpers.MatName;
import ch.zhaw.facerecognitionlibrary.Helpers.PreferencesHelper;
import ch.zhaw.facerecognitionlibrary.PreProcessor.PreProcessorFactory;
import ch.zhaw.facerecognitionlibrary.Recognition.Recognition;
import ch.zhaw.facerecognitionlibrary.Recognition.RecognitionFactory;
import snu.facelab.helper.DatabaseHelper;

public class TrainingActivity extends Activity {
    private static final String TAG = "Training";
    TextView progress;
    Thread thread;
    Integer[] statusBtn = {R.id.rec_1, R.id.rec_2, R.id.rec_3, R.id.rec_4, R.id.rec_5,
                           R.id.rec_6, R.id.rec_7, R.id.rec_8, R.id.rec_9, R.id.rec_10};
    Button[] btn = new Button[10];

    private String folder_name;

    static {
        if (!OpenCVLoader.initDebug()) {
            // Handle initialization error
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        // 전체화면
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = getIntent();
        folder_name = intent.getStringExtra("FolderName");

//        progress = (TextView) findViewById(R.id.progressText);
//        progress.setMovementMethod(new ScrollingMovementMethod());
        for (int i = 0; i < 10; i++) {
            btn[i] = findViewById(statusBtn[i]);
            btn[i].setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();

        final Handler handler = new Handler(Looper.getMainLooper());
        thread = new Thread(new Runnable() {
            public void run() {
                if(!Thread.currentThread().isInterrupted()){
                    PreProcessorFactory ppF = new PreProcessorFactory(getApplicationContext());
                    PreferencesHelper preferencesHelper = new PreferencesHelper(getApplicationContext());
                    String algorithm = "TensorFlow with SVM or KNN";

                    FileHelper fileHelper = new FileHelper();
                    fileHelper.createDataFolderIfNotExsiting();
                    final File[] persons = fileHelper.getTrainingList();

                    if (persons.length > 0) {
                        Recognition rec = RecognitionFactory.getRecognitionAlgorithm(getApplicationContext(), Recognition.TRAINING, algorithm);

                        for (File person : persons) {
                            if (person.isDirectory()) {
                                // The last token is the name --> Folder name = Person name
                                String[] tokens = person.getAbsolutePath().split("/");

                                final String foldername = tokens[tokens.length - 1];

                                if (foldername.equals(folder_name)) {
                                    File[] files = person.listFiles();
                                    int counter = 1;
                                    for (File file : files) {
                                        if (FileHelper.isFileAnImage(file)) {
                                            Mat imgRgb = Imgcodecs.imread(file.getAbsolutePath());
                                            Imgproc.cvtColor(imgRgb, imgRgb, Imgproc.COLOR_BGRA2RGBA);
                                            Mat processedImage = new Mat();
                                            imgRgb.copyTo(processedImage);

                                            List<Mat> images = ppF.getProcessedImage(processedImage, PreProcessorFactory.PreprocessingMode.RECOGNITION);
                                            if (images == null || images.size() > 1) {
                                                // More than 1 face detected --> cannot use this file for training
                                                continue;
                                            } else {
                                                processedImage = images.get(0);
                                            }
                                            if (processedImage.empty()) {
                                                continue;
                                            }

                                            MatName m = new MatName("processedImage", processedImage);
                                            fileHelper.saveMatToImage(m, FileHelper.DATA_PATH);

                                            rec.addImage(processedImage, foldername, false);

                                            final int counterPost = counter-1;

                                            if (counterPost < 10) {
                                                btn[counterPost].post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        btn[counterPost].setVisibility(View.VISIBLE);
                                                    }
                                                });
                                            }

                                            counter++;
                                        }
                                    }
                                }
                            }
                        }
                        final Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        if (rec.train()) {
                            intent.putExtra("training", "Training successful");
                        } else {
                            intent.putExtra("training", "Training failed");
                        }
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(intent);
                            }
                        });
                    } else {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        });
        thread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        thread.interrupt();
    }

    @Override
    protected void onStop() {
        super.onStop();
        thread.interrupt();
    }
}