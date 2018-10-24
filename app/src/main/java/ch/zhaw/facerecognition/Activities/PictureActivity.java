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

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import ch.zhaw.facerecognition.R;
import ch.zhaw.facerecognitionlibrary.Helpers.FileHelper;

public class PictureActivity extends AppCompatActivity {
    private FileHelper fh;
    private ImageView[] imgView=new ImageView[10];
    private Bitmap[] image=new Bitmap[10];
    private String folder;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        Intent intent = getIntent();
        ArrayList<Image> image_list = intent.getParcelableArrayListExtra(OpenGalleryActivity.IMAGES);

        name = intent.getExtras().getString("Name");

        for(int i=0; i<10; i++){
            image[i] = BitmapFactory.decodeFile(image_list.get(i).path);
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
            String wholeFolderPath = fh.TRAINING_PATH + name + "/";
            saveImage(wholeFolderPath, image[i], "test_image" + i);
        }

        Button backToMenu = (Button)findViewById(R.id.btn_Back_To_Menu);
        backToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), MainActivity.class));
            }
        });
    }

    private void saveImage(String wholeFolderPath, Bitmap finalBitmap, String image_name) {

        File dir = new File(wholeFolderPath);
        dir.mkdirs();
        String fname = "Image-" + image_name+ ".jpg";
        File file = new File(dir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}