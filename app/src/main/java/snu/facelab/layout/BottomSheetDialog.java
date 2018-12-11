package snu.facelab.layout;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;

import java.util.List;

import snu.facelab.Person;
import snu.facelab.PersonPhotoActivity;
import snu.facelab.R;
import snu.facelab.helper.DatabaseHelper;

import static snu.facelab.PersonPhotoDetailActivity.PERSON;

public class BottomSheetDialog extends BottomSheetDialogFragment implements View.OnClickListener{

    public static BottomSheetDialog getInstance() {
        return new BottomSheetDialog();
    }

    public void getNames(List<String> names) {
        this.names = names;
    }

    private static List<String> names;

    // DatabaseHelper 객체
    DatabaseHelper db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_dialog, container,false);

        int count = names.size();
        FlexboxLayout layout = view.findViewById(R.id.bottom_sheet);

        // 동적으로 해시태그 추가
       for (int i=0; i<count; i++){
            TextView tag = new TextView(view.getContext());
            final String name = names.get(i);
            String hashName = "#" + name;
            tag.setText(hashName);
            tag.setTextSize(30);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);
            param.setMargins(0,0,30,20);
            tag.setLayoutParams(param);
            tag.setTextColor(getResources().getColor(R.color.hashtag));
            tag.setClickable(true);


            tag.setOnClickListener(new TextView.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String personName = name;
                    Person person = new Person(name);

                    Intent i = new Intent(v.getContext(), PersonPhotoActivity.class);
                    //i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.putExtra(PERSON, person);
                    startActivity(i);
                }
            });

            layout.addView(tag);
        }
        return view;
    }

    @Override
    public void onClick(View view) {

    }
}
