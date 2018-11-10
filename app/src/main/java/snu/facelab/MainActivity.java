/* Copyright 2016 Michael Sladoje and Mike Schälchli. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==============================================================================*/

package snu.facelab;

import android.app.Activity;
import android.content.Intent;
<<<<<<< HEAD
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import ch.zhaw.facerecognitionlibrary.Helpers.FileHelper;
//import ch.zhaw.facerecognitionlibrary.R;
import snu.facelab.R;

public class MainActivity extends Activity {
=======
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ArrayList<Person> al = new ArrayList<Person>();
    public static final String PERSON = "Person";
    private GridView gridView;
>>>>>>> a321a17fd5ec93864c4f5a1b7f3dd86d61e257d1

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

<<<<<<< HEAD
        Intent intent = getIntent();
        String training = intent.getStringExtra("training");
        if (training != null && !training.isEmpty()){
            Toast.makeText(getApplicationContext(), training, Toast.LENGTH_SHORT).show();
            intent.removeExtra("training");
        }

        double accuracy = intent.getDoubleExtra("accuracy", 0);
        if (accuracy != 0){
            Toast.makeText(getApplicationContext(), "The accuracy was " + accuracy * 100 + " %", Toast.LENGTH_LONG).show();
            intent.removeExtra("accuracy");
        }

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        Button callSettings = (Button)findViewById(R.id.button_settings);
        callSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), SettingsActivity.class));
            }
        });

        Button callMakeFolder = (Button)findViewById(R.id.button_makeFolder);
        callMakeFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), MakeFolderActivity.class));
            }
        });

        FileHelper fh = new FileHelper();

        Button callRecognition = (Button)findViewById(R.id.button_recognition_view);
        if(!((new File(fh.DATA_PATH)).exists())) callRecognition.setEnabled(false);
        callRecognition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), RecognitionActivity.class));
            }
        });
        /*
        Button callTraining = (Button)findViewById(R.id.button_recognition_training);
        if(fh.getTrainingList().length == 0) callTraining.setEnabled(false);
        callTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), TrainingActivity.class));
            }
        });
*/

        Button callFromGalleryView = (Button)findViewById(R.id.button_from_gallery);
        callFromGalleryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), FromGallery.class));
            }
        });


        Button callAbout = (Button)findViewById(R.id.button_about);
        callAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://github.com/Qualeams/Android-Face-Recognition-with-Deep-Learning-Test-Framework";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                try {
                    startActivity(intent);
                } catch (android.content.ActivityNotFoundException e) {
                    Log.e(getClass().getName(), null, e);
                    Toast.makeText(getApplicationContext(), "No browser found on the device to open the url", Toast.LENGTH_SHORT).show();
                }
            }
        });
=======
        Intent intent = new Intent(this, LoadingActivity.class);
        startActivity(intent);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // ListView
        //    1. 다량의 데이터
        //    2. Adapter (데이터와 view의 연결 관계를 정의)
        //    3. AdapterView (ListView)

        int [] person1 = new int [] {R.drawable.jungwon_1, R.drawable.jungwon_2, R.drawable.jungwon_3, R.drawable.jungwon_4, R.drawable.jungwon_5,
                R.drawable.jungwon_6, R.drawable.jungwon_7, R.drawable.jungwon_8, R.drawable.jungwon_9};
        int [] person2 = new int [] {R.drawable.yunsun_1, R.drawable.yunsun_2, R.drawable.yunsun_3, R.drawable.yunsun_4, R.drawable.yunsun_5,
                R.drawable.yunsun_6, R.drawable.yunsun_7, R.drawable.yunsun_8, R.drawable.yunsun_9};
        int [] person3 = new int [] {R.drawable.jooyun_1, R.drawable.jooyun_2, R.drawable.jooyun_3, R.drawable.jooyun_4, R.drawable.jooyun_5,
                R.drawable.jooyun_6, R.drawable.jooyun_7, R.drawable.jooyun_8, R.drawable.jooyun_9};

        al.add(new Person(R.drawable.jungwon, person1, "김정원"));
        al.add(new Person(R.drawable.yunsun, person2, "이윤선"));
        al.add(new Person(R.drawable.jooyun, person3, "이주연"));


        // adapter
        PersonAdapter adapter = new PersonAdapter(
                getApplicationContext(), // 현재 화면의 제어권자
                R.layout.person_thumbnail,             // 한행을 그려줄 layout
                al);                     // 다량의 데이터

        GridView lv = (GridView)findViewById(R.id.gridView);
        lv.setAdapter(adapter);

        // 이벤트 처리
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                GridView GridView = (GridView) adapterView;
                Person person = (Person) GridView.getItemAtPosition(position);
                Log.d("JO", "selected item => "+person.name);

                Intent i = new Intent(MainActivity.this, PersonPhotoActivity.class);
                i.putExtra(PERSON, person);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_add_person) {
            // Handle the camera action
            Intent i = new Intent(MainActivity.this, AddPersonActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_add_photo) {
            Intent i = new Intent(MainActivity.this, AddPhotoActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
>>>>>>> a321a17fd5ec93864c4f5a1b7f3dd86d61e257d1
    }
}
