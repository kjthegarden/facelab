package snu.facelab;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;

import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import ch.zhaw.facerecognitionlibrary.Helpers.FileHelper;
import ch.zhaw.facerecognitionlibrary.Helpers.MatName;
import ch.zhaw.facerecognitionlibrary.Helpers.PreferencesHelper;
import ch.zhaw.facerecognitionlibrary.PreProcessor.PreProcessorFactory;
import ch.zhaw.facerecognitionlibrary.Recognition.Recognition;
import ch.zhaw.facerecognitionlibrary.Recognition.RecognitionFactory;

public class AddPersonActivity extends AppCompatActivity {
    private FileHelper fh;
    private String folder;
    private String subfolder;
    private String name;
    private TextView textView;
    private ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);

        OpenCVLoader.initDebug();

        Intent intent = getIntent();
        ArrayList<Image> image_list = intent.getParcelableArrayListExtra(AddPersonPreviewActivity.IMAGES);
        Bitmap image1 = BitmapFactory.decodeFile(image_list.get(0).path);
        imgView = (ImageView) findViewById(R.id.imageView);
        imgView.setImageBitmap(image1);

        PreProcessorFactory ppF = new PreProcessorFactory(getApplicationContext());
        PreferencesHelper preferencesHelper = new PreferencesHelper(getApplicationContext());
        String algorithm = preferencesHelper.getClassificationMethod();
        FileHelper fileHelper = new FileHelper();
        Recognition rec = RecognitionFactory.getRecognitionAlgorithm(getApplicationContext(), Recognition.TRAINING, algorithm);

        Mat imgRgb = Imgcodecs.imread(image_list.get(0).path);
        Imgproc.cvtColor(imgRgb, imgRgb, Imgproc.COLOR_BGRA2RGBA);
        Mat processedImage = new Mat();
        imgRgb.copyTo(processedImage);
        List<Mat> images = ppF.getProcessedImage(processedImage, PreProcessorFactory.PreprocessingMode.RECOGNITION);
        if (images == null || images.size() > 1) {
            // More than 1 face detected --> cannot use this file for training
            //continue;
        } else {
            processedImage = images.get(0);
        }
        if (processedImage.empty()) {
            //continue;
        }

        MatName m = new MatName("processedImage", processedImage);
        fileHelper.saveMatToImage(m, FileHelper.DATA_PATH);
        String name = "temp";
        rec.addImage(processedImage, name, false);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            //The array list has the image paths of the selected images
            ArrayList<Image> images = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
            Log.d("STATE", images.get(0).path.toString());

            Intent launchResult = new Intent(this, AddPersonActivity.class);
            launchResult.putExtra("images", images);


//            Bitmap image1 = BitmapFactory.decodeFile(images.get(0).path);
//            imgView = (ImageView) findViewById(R.id.imageView);
//            imgView.setImageBitmap(image1);
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
