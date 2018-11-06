package ch.zhaw.facerecognition.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.darsh.multipleimageselect.models.Image;

import org.opencv.core.Rect;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import org.slf4j.helpers.Util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import ch.zhaw.facerecognition.R;
import ch.zhaw.facerecognitionlibrary.Helpers.FileHelper;
import ch.zhaw.facerecognitionlibrary.Helpers.MatName;
import ch.zhaw.facerecognitionlibrary.Helpers.MatOperation;
import ch.zhaw.facerecognitionlibrary.PreProcessor.PreProcessorFactory;

public class PictureActivity extends AppCompatActivity {
    private PreProcessorFactory ppF;
    private FileHelper fh;
    private ImageView[] imgView=new ImageView[10];
    private Bitmap[] image=new Bitmap[10];
    private String folder;
    private String name;
    private Mat mat;

    static {
        if (!OpenCVLoader.initDebug()) {
            // Handle initialization error
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        Intent intent = getIntent();
        ArrayList<Image> image_list = intent.getParcelableArrayListExtra(OpenGalleryActivity.IMAGES);

        name = intent.getExtras().getString("Name");

        for(int i=0; i<1; i++){

            image[i] = BitmapFactory.decodeFile(image_list.get(i).path);
            String wholeFolderPath = fh.TRAINING_PATH + name + "/";
            saveImage(wholeFolderPath, image_list.get(i).path, "test_imge" +i );
            switch (i){
                case 0:
                    imgView[i] = (ImageView) findViewById(R.id.imageView0);
                    break;
                case 1:
                    imgView[i] = (ImageView) findViewById(R.id.imageView1);
                    break;
                case 2:
                    imgView[i] = (ImageView) findViewById(R.id.imageView2);
                    break;
                case 3:
                    imgView[i] = (ImageView) findViewById(R.id.imageView3);
                    break;
                case 4:
                    imgView[i] = (ImageView) findViewById(R.id.imageView4);
                    break;
                case 5:
                    imgView[i] = (ImageView) findViewById(R.id.imageView5);
                    break;
                case 6:
                    imgView[i] = (ImageView) findViewById(R.id.imageView6);
                    break;
                case 7:
                    imgView[i] = (ImageView) findViewById(R.id.imageView7);
                    break;
                case 8:
                    imgView[i] = (ImageView) findViewById(R.id.imageView8);
                    break;
                case 9:
                    imgView[i] = (ImageView) findViewById(R.id.imageView9);
                    break;
            }

            imgView[i].setImageBitmap(image[i]);

            //saveImage(wholeFolderPath, image[i], "test_image" + i);

/*            String filePath = image_list.get(i).path;
            System.out.println(filePath);

            mat = Imgcodecs.imread(filePath);
            Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGRA2RGBA);
            Mat imgCopy = new Mat();
            mat.copyTo(imgCopy);

            List<Mat> images = ppF.getCroppedImage(imgCopy);

            if (images != null && images.size() == 1) {
                Mat img = images.get(0);
                if (img != null) {
                    Rect[] faces = ppF.getFacesForRecognition();
                    //Only proceed if 1 face has been detected, ignore if 0 or more than 1 face have been detected
                    if ((faces != null) && (faces.length == 1)) {
                        faces = MatOperation.rotateFaces(mat, faces, ppF.getAngleForRecognition());
                        File dir = new File(wholeFolderPath);
                        dir.mkdirs();
                        String fname = "Image-" + "test_image" + i + ".png";
                        File file = new File(dir, fname);
                        if (file.exists()) file.delete();
                        try {
                            FileOutputStream out = new FileOutputStream(file);
                            MatName m = new MatName(fname, img);
                            fh.saveMatToImage(m, wholeFolderPath + "/");
                            //finalBitmap.compress(Bitmap.CompressFormat.PNG, 10, out);
                            out.flush();
                            out.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }*/

        }

        Button backToMenu = (Button)findViewById(R.id.btn_Back_To_Menu);
        backToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), MainActivity.class));
            }
        });
    }

    private void saveImage(String wholeFolderPath, String filePath, String image_name) {
        System.out.println(filePath);
        Mat mat = Imgcodecs.imread(filePath);
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGRA2RGBA);
        Mat imgCopy = new Mat();
        mat.copyTo(imgCopy);

        ppF = new PreProcessorFactory(getApplicationContext());
        List<Mat> images = ppF.getCroppedImage(imgCopy);
        if (images != null && images.size() == 1) {
            Mat img = images.get(0);
            if (img != null) {
                Rect[] faces = ppF.getFacesForRecognition();
                //Only proceed if 1 face has been detected, ignore if 0 or more than 1 face have been detected
                if ((faces != null) && (faces.length == 1)) {
                    faces = MatOperation.rotateFaces(mat, faces, ppF.getAngleForRecognition());
                    File dir = new File(wholeFolderPath);
                    dir.mkdirs();
                    System.out.println(name + "_" + image_name);
                    System.out.println(img);
                    MatName m = new MatName(name + "_" + image_name, img);

                    fh.saveMatToImage(m, wholeFolderPath + "/");
                    /*String fname = "Image-" + image_name + ".png";
                    File file = new File(dir, fname);
                    if (file.exists()) file.delete();
                    try {
                        FileOutputStream out = new FileOutputStream(file);
                        Bitmap bitmap = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888);
                        Utils.matToBitmap(img, bitmap);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

                        //finalBitmap.compress(Bitmap.CompressFormat.PNG, 10, out);
                        out.flush();
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    */
                }
            }
        }


    }
    @Override
    protected void onResume() {
        super.onResume();
        ppF = new PreProcessorFactory(getApplicationContext());
    }
/*
    private void saveImage(String wholeFolderPath, Bitmap finalBitmap, String image_name) {

        File dir = new File(wholeFolderPath);
        dir.mkdirs();
        String fname = "Image-" + image_name+ ".jpg";
        File file = new File(dir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 1, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

*/

}