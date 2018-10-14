package org.teamm.opengallery;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    //UI
    ImageView image1, image2, image3, image4, image5;

    //constant
    final int PICTURE_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //UI
        image1 = (ImageView)findViewById(R.id.img1);
        image2 = (ImageView)findViewById(R.id.img2);
        image3 = (ImageView)findViewById(R.id.img3);
        image4 = (ImageView)findViewById(R.id.img4);
        image5 = (ImageView)findViewById(R.id.img5);

        Button btnImage = (Button)findViewById(R.id.btnImage);
        btnImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                //사진을 여러개 선택할수 있도록 한다
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),  PICTURE_REQUEST_CODE);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICTURE_REQUEST_CODE)
        {
            if (resultCode == RESULT_OK)
            {

                //기존 이미지 지우기
                image1.setImageResource(0);
                image2.setImageResource(0);
                image3.setImageResource(0);
                image4.setImageResource(0);
                image5.setImageResource(0);

                //ClipData 또는 Uri를 가져온다
                Uri uri = data.getData();
                ClipData clipData = data.getClipData();

                //이미지 URI 를 이용하여 이미지뷰에 순서대로 세팅한다.
                if(clipData!=null)
                {

                    for(int i = 0; i < 5; i++)
                    {
                        if(i<clipData.getItemCount()){
                            Uri urione =  clipData.getItemAt(i).getUri();
                            switch (i){
                                case 0:
                                    image1.setImageURI(urione);
                                    break;
                                case 1:
                                    image2.setImageURI(urione);
                                    break;
                                case 2:
                                    image3.setImageURI(urione);
                                    break;
                                case 3:
                                    image4.setImageURI(urione);
                                    break;
                                case 4:
                                    image5.setImageURI(urione);
                                    break;
                            }
                        }
                    }
                }
                else if(uri != null)
                {
                    image1.setImageURI(uri);
                }
            }
        }
    }
}