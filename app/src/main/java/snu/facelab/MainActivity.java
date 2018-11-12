package snu.facelab;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

                // Intent i = new Intent(MainActivity.this, PersonPhotoActivity.class);
                Intent i = new Intent(MainActivity.this, PersonPhotoActivity2.class);
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

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        if (id == R.id.nav_add_person) {
            // Handle the camera action
            Intent i = new Intent(MainActivity.this, AddPersonActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_add_photo) {
            Intent i = new Intent(MainActivity.this, AddPhotoActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {
            Intent i = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
