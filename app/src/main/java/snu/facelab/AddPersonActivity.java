package snu.facelab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

import ch.zhaw.facerecognitionlibrary.Helpers.FileHelper;

public class AddPersonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);

        Button btn_Start = (Button)findViewById(R.id.btn_Start);
        btn_Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText txt_Name = (EditText)findViewById(R.id.txt_Name);
                String name = txt_Name.getText().toString();
                Intent intent = new Intent(v.getContext(), notUsing_AddPersonPreviewActivity.class);
                intent.putExtra("Name", name);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


                if(isNameAlreadyUsed(new FileHelper().getTrainingList(), name)){
                    Toast.makeText(getApplicationContext(), "This name is already used. Please choose another one.", Toast.LENGTH_SHORT).show();
                } else {
                    intent.putExtra("Folder", "Training");
                    startActivity(intent);
                }
            }
        });
    }

    private boolean isNameAlreadyUsed(File[] list, String name){
        boolean used = false;
        if(list != null && list.length > 0){
            for(File person : list){
                // The last token is the name --> Folder name = Person name
                String[] tokens = person.getAbsolutePath().split("/");
                final String foldername = tokens[tokens.length-1];
                if(foldername.equals(name)){
                    used = true;
                    break;
                }
            }
        }
        return used;
    }


}
