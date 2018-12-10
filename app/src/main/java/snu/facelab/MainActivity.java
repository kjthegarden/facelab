package snu.facelab;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import snu.facelab.helper.DatabaseHelper;
import snu.facelab.model.Name;
import snu.facelab.model.Picture;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ArrayList<Person> al = new ArrayList<Person>();
    public static final String PERSON = "Person";
    private GridView Gv;
    DatabaseHelper db;
    private String personImage;
    private List<Name> names;
    private List<Picture> pictures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // DBHelper 객체 생성
        db = new DatabaseHelper(getApplicationContext());

        names = db.getAllNames();
        setAlbumList();

        // 검색창 클릭 전까지는 키보드가 뜨지 않도록 함
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        // 검색 키보드
        final InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        // 검색창
        final EditText searchText = (EditText) findViewById(R.id.search_text);
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchText.clearFocus();
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchText.getWindowToken(), 0);

                    String text = searchText.getText().toString();
                    if(al!=null)  al.clear();
                    if(names!=null) names.clear();
                    if(pictures!=null) pictures.clear();

                    if(text!=null) {
                        names = db.getAllNamesWithKey(text);
                        setAlbumList();
                    }else {
                        names = db.getAllNames();
                        setAlbumList();
                    }
                    return true;
                }
                return false;
            }
        });

        // 이벤트 처리
        Gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                GridView GridView = (GridView) adapterView;
                Person person = (Person) GridView.getItemAtPosition(position);
                Log.d("JO", "selected item => " + person.name);

                Intent i = new Intent(MainActivity.this, PersonPhotoActivity.class);
                i.putExtra(PERSON, person);
                startActivity(i);
            }
        });
    }

    // 사진과 이름으로 앨범 목록 구성
    public void setAlbumList() {
        int personCount = names.size();
        for (int i = 0; i < personCount; i++) {
            String personName = names.get(i).getName();
            pictures = db.getAllPicturesByName(personName);
            personImage = pictures.get(0).getPath();
            al.add(new Person(personImage, personName));
        }

        // adapter
        PersonAdapter adapter = new PersonAdapter(
                getApplicationContext(), // 현재 화면의 제어권자
                R.layout.person_thumbnail,             // 한행을 그려줄 layout
                al);                     // 다량의 데이터

        // GridView
        //    1. 다량의 데이터
        //    2. Adapter (데이터와 view의 연결 관계를 정의)
        //    3. AdapterView (GridView)
        Gv = findViewById(R.id.gridView);
        Gv.setAdapter(adapter);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        if (id == R.id.nav_add_person) {
            // Handle the camera action
            Intent i = new Intent(MainActivity.this, AddPersonActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_auto_add) {
            Intent i = new Intent(MainActivity.this, AutoAddActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_tutorial) {
            Intent i = new Intent(MainActivity.this, TutorialActivity.class);
            startActivity(i);
        } /*else if (id == R.id.nav_manage) {
            Intent i = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/else if(id == R.id.nav_about) {
            Intent i = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void checkFirstRun() {

        final String PREFS_NAME = "MyPrefsFile";
        final String PREF_VERSION_CODE_KEY = "version_code";
        final int DOESNT_EXIST = -1;

        // Get current version code
        int currentVersionCode = BuildConfig.VERSION_CODE;

        // Get saved version code
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);

        // Check for first run or upgrade
        if (currentVersionCode == savedVersionCode) {

            // This is just a normal run
            Intent intent = new Intent(this, LoadingActivity.class);
            startActivity(intent);
            return;

        } else if (savedVersionCode == DOESNT_EXIST) {

            // TODO This is a new install (or the user cleared the shared preferences)
            Intent intent = new Intent(this, LoadingActivity.class);
            startActivity(intent);

        } else if (currentVersionCode > savedVersionCode) {

            // TODO This is an upgrade
            Intent intent = new Intent(this, LoadingActivity.class);
            startActivity(intent);
        }

        // Update the shared preferences with the current version code
        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply();
    }
}