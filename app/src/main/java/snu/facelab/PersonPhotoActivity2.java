package snu.facelab;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import snu.facelab.helper.DatabaseHelper;
import snu.facelab.model.Picture;

import static snu.facelab.MainActivity.PERSON;

public class PersonPhotoActivity2 extends AppCompatActivity {

    private ImageView[] imgView=new ImageView[16];
    private Bitmap[] image=new Bitmap[16];

    // DatabaseHelper 객체
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_photo2);

        Person person = (Person) getIntent().getExtras().getSerializable(PERSON);
        setTitle(person.name);

        // DBHelper 객체 생성
        db = new DatabaseHelper(getApplicationContext());

        // get pictures from db
        List<Picture> pictures = new ArrayList<Picture>();

        pictures = db.getAllPicturesByName(person.name);

        int imgCount = pictures.size();
        System.out.println("image count: " + imgCount);

        for(int i=0; i<16; i++){
            image[i] = BitmapFactory.decodeFile(pictures.get(i).getPath());

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
                case 10:
                    imgView[i] = (ImageView) findViewById(R.id.imageView10);
                    break;
                case 11:
                    imgView[i] = (ImageView) findViewById(R.id.imageView11);
                    break;
                case 12:
                    imgView[i] = (ImageView) findViewById(R.id.imageView12);
                    break;
                case 13:
                    imgView[i] = (ImageView) findViewById(R.id.imageView13);
                    break;
                case 14:
                    imgView[i] = (ImageView) findViewById(R.id.imageView14);
                    break;
                case 15:
                    imgView[i] = (ImageView) findViewById(R.id.imageView15);
                    break;
            }
            imgView[i].setImageBitmap(image[i]);
        }

    }


}
