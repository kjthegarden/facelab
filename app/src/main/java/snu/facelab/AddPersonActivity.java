package snu.facelab;

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.io.File;

import com.andremion.louvre.Louvre;
import com.andremion.louvre.home.GalleryActivity;

import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import ch.zhaw.facerecognitionlibrary.Helpers.FileHelper;
import ch.zhaw.facerecognitionlibrary.Helpers.MatName;
import ch.zhaw.facerecognitionlibrary.Helpers.MatOperation;
import ch.zhaw.facerecognitionlibrary.PreProcessor.PreProcessorFactory;
import snu.facelab.helper.DatabaseHelper;
import snu.facelab.model.Name;
import snu.facelab.model.Picture;

public class AddPersonActivity extends AppCompatActivity {
    private static final int LOUVRE_REQUEST_CODE = 0;

    private List<Uri> mSelection;
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
        setContentView(R.layout.activity_add_person);

        // 전체화면
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        fh = new FileHelper();
        total = 0;

        AppCompatButton btnImage = findViewById(R.id.btn_Start);
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get name for folder name
                EditText txt_Folder_Name = (EditText) findViewById(R.id.txt_Name);
                name = txt_Folder_Name.getText().toString();

                // choose multiple pictures
                /*Intent intent = new Intent(v.getContext(), OpenGalleryActivity.class);
                intent.putExtra("Name", name);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);*/

                if (isNameAlreadyUsed(new FileHelper().getTrainingList(), name)) {
                    Toast.makeText(getApplicationContext(), "This name is already used. Please choose another one.", Toast.LENGTH_SHORT).show();
                }
                else if (name.matches("")) {
                    Toast.makeText(getApplicationContext(), "Please enter a name.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Louvre.init(AddPersonActivity.this)
                            .setRequestCode(LOUVRE_REQUEST_CODE)
                            .setMaxSelection(10)
                            .setSelection((List<Uri>)mSelection)
                            .setMediaTypeFilter(Louvre.IMAGE_TYPE_JPEG)
                            .open();
                }

            }
        });

        AppCompatButton btnCancel = findViewById(R.id.add_cancel);
        btnCancel.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private boolean isNameAlreadyUsed(File[] list, String name) {
        boolean used = false;
        if (list != null && list.length > 0) {
            for (File person : list) {
                // The last token is the name --> Folder name = Person name
                String[] tokens = person.getAbsolutePath().split("/");
                final String foldername = tokens[tokens.length - 1];
                if (foldername.equals(name)) {
                    used = true;
                    break;
                }
            }
        }
        return used;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //noinspection unchecked
        mSelection = (List<Uri>) getLastCustomNonConfigurationInstance();
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return mSelection;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOUVRE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            mSelection = GalleryActivity.getSelection(data);

            int image_list_size = mSelection.size();
            if( image_list_size<10){
                Toast.makeText(getApplicationContext(), "Choose at least 10 pictures.", Toast.LENGTH_SHORT).show();
                Louvre.init(AddPersonActivity.this)
                        .setRequestCode(LOUVRE_REQUEST_CODE)
                        .setMaxSelection(10)
                        .setSelection((List<Uri>)mSelection)
                        .setMediaTypeFilter(Louvre.IMAGE_TYPE_JPEG)
                        .open();
            }else{
                // DBHelper 객체 생성
                db = new DatabaseHelper(getApplicationContext());

                // Array for pictures
                long pic_ids[] = new long[image_list_size];

                for (int i = 0; i < image_list_size; i++) {
                    // last modified time
                    String filePath = mSelection.get(i).getPath();
                    System.out.println(filePath);
                    File file = new File(filePath);
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
                    Picture pic = new Picture(filePath, date, last_modified);
                    pic_ids[i] = db.createPicture(pic);

                    Mat src = Imgcodecs.imread(file.getAbsolutePath());
                    Imgproc.cvtColor(src, src, Imgproc.COLOR_BGRA2RGBA);

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

                                //트레이닝 폴더에 이미지 저장할 때 이름 맞는지??
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