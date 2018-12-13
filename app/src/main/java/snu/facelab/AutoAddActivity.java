package snu.facelab;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.andremion.louvre.Louvre;
import com.andremion.louvre.home.GalleryActivity;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ch.zhaw.facerecognitionlibrary.Helpers.FileHelper;

public class AutoAddActivity extends AppCompatActivity {
    private static final int LOUVRE_REQUEST_CODE = 0;
    private List<Uri> mSelection;
    private static final String TAG = "From gallery";
    private FileHelper fh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        fh = new FileHelper();
        File folder = new File(fh.getFolderPath());
        if(folder.mkdir() || folder.isDirectory()){
            Log.i(TAG,"New directory for photos created");
        } else {
            Log.i(TAG,"Photos directory already existing");
        }

        Louvre.init(AutoAddActivity.this)
                .setRequestCode(LOUVRE_REQUEST_CODE)
                .setMaxSelection(100)
                .setSelection((List<Uri>)mSelection)
                .setMediaTypeFilter(Louvre.IMAGE_TYPE_JPEG, Louvre.IMAGE_TYPE_PNG)
                .open();

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
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

            int size = mSelection.size();
            //System.out.println("add photo activity result image_list size :" + size);

            ArrayList<String> path_list = new ArrayList<String>(size);

            for (int i = 0; i < size; i++) {
                String filePath = mSelection.get(i).getPath();
                path_list.add(filePath);
            }

            Intent intent = new Intent(getApplicationContext(), RecognitionActivity.class);
            intent.putStringArrayListExtra("Path", path_list);
            startActivity(intent);
        }
        else {
            finish();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

}