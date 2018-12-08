package snu.facelab.helper;

import snu.facelab.model.Name;
import snu.facelab.model.Picture;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = DatabaseHelper.class.getName();

    // Database Version
    private static final int DATABASE_VERSION = 6;

    // Database Name
    private static final String DATABASE_NAME = "picturesManager";

    // Table Names
    private static final String TABLE_NAME = "names";
    private static final String TABLE_PICTURE = "pictures";
    private static final String TABLE_NAME_PICTURE = "name_pictures";

    // Common column names
    private static final String KEY_ID = "id";

    // NAMES Table - column names
    private static final String KEY_NAME = "name";

    // PICTURES Table - column names
    private static final String KEY_PATH = "path";
    private static final String KEY_DATE = "date";
    private static final String KEY_MONTH = "month";
    private static final String KEY_DATE_TIME = "date_time";

    // NAME_PICTURES Table - column names
    private static final String KEY_NAME_ID = "name_id";
    private static final String KEY_PICTURE_ID = "picture_id";

    // Table Create Statements
    // Name table create statement
    private static final String CREATE_TABLE_NAME = "CREATE TABLE " + TABLE_NAME
            + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT"+ ")";

    // Picture table create statement
    private static final String CREATE_TABLE_PICTURE = "CREATE TABLE " + TABLE_PICTURE
            + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_PATH + " TEXT," + KEY_DATE + " INTEGER,"
            + KEY_MONTH +" INTEGER, "+ KEY_DATE_TIME+" INTEGER"+ ")";

    // Name_Picture table create statement
    private static final String CREATE_TABLE_NAME_PICTURE = "CREATE TABLE " + TABLE_NAME_PICTURE
            + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME_ID + " INTEGER," + KEY_PICTURE_ID + " INTEGER"+ ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_NAME);
        db.execSQL(CREATE_TABLE_PICTURE);
        db.execSQL(CREATE_TABLE_NAME_PICTURE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PICTURE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PICTURE);

        // create new tables
        onCreate(db);
    }

    // ------------------------ "names" table methods ----------------//

    /**
     * Creating a name
     */
    public long createName(Name name, long[] picture_ids) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name.getName());

        // insert row
        long name_id = db.insert(TABLE_NAME, null, values);

        // assigning pictures to name
        for (long picture_id : picture_ids) {
            createNamePicture(name_id, picture_id);
        }

        return name_id;
    }

    /**
     * get single name
     */
    public Name getNameWithId(long name_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + KEY_ID + " = " + name_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Name name = new Name();
        name.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        name.setName((c.getString(c.getColumnIndex(KEY_NAME))));

        return name;
    }

    /**
     * get single name
     */
    public Name getNameWithString(String rec_name) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + KEY_NAME + " = '" + rec_name+ "'";

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Name name = new Name();
        name.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        name.setName((c.getString(c.getColumnIndex(KEY_NAME))));

        return name;
    }

    /**
     * getting all names
     * */
    public List<Name> getAllNames() {
        List<Name> names = new ArrayList<Name>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Name name = new Name();
                name.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                name.setName((c.getString(c.getColumnIndex(KEY_NAME))));

                // adding to name list
                names.add(name);
            } while (c.moveToNext());
        }

        return names;
    }

    /**
     * getting name count
     */
    public int getNameCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /**
     * Updating a name
     */
    public int updateName(Name name) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name.getName());

        // updating row
        return db.update(TABLE_NAME, values, KEY_ID + " = ?",
                new String[] { String.valueOf(name.getId()) });
    }

    /**
     * Deleting a name
     */
    public void deleteName(long name_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?",
                new String[] { String.valueOf(name_id) });
    }

    // ------------------------ "pictures" table methods ----------------//

    /**
     * Creating Picture
     */
    public long createPicture(Picture picture) {
        Long picture_id = getPictureIdByPath(picture.getPath());

        if(picture_id==null){
            System.out.println(picture_id);
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_PATH, picture.getPath());
            values.put(KEY_DATE, picture.getDate());
            values.put(KEY_MONTH, picture.getMonth());
            values.put(KEY_DATE_TIME, picture.getDateTime());

            // insert row
            picture_id = db.insert(TABLE_PICTURE, null, values);
        }

        return picture_id;
    }

    /**
     * getting all pictures
     * */
    public List<Picture> getAllPictures() {
        List<Picture> pictures = new ArrayList<Picture>();
        String selectQuery = "SELECT  * FROM " + TABLE_PICTURE;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Picture p = new Picture();
                p.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                p.setPath(c.getString(c.getColumnIndex(KEY_PATH)));
                p.setDate(c.getInt((c.getColumnIndex(KEY_DATE))));
                p.setDate(c.getInt((c.getColumnIndex(KEY_MONTH))));
                p.setDateTime(c.getLong(c.getColumnIndex(KEY_DATE_TIME)));

                // adding to pictures list
                pictures.add(p);
            } while (c.moveToNext());
        }
        return pictures;
    }

    /**
     * getting picture id by path
     * */
    public Long getPictureIdByPath(String path) {
        List<Picture> pictures = new ArrayList<Picture>();
        String selectQuery = "SELECT  *" + " FROM " + TABLE_PICTURE +" WHERE " + KEY_PATH + " = '" + path+ "'";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null && c.moveToFirst()){
            return c.getLong(c.getColumnIndex(KEY_ID));
        }else{
            return null;
        }
    }

    /**
     * getting all dates under single name
     * */
    public List<Integer> getAllDatesByName(String name) {
        List<Integer> dates = new ArrayList<Integer>();

        String selectQuery = "SELECT  DISTINCT" + " pc." + KEY_DATE
                +" FROM " + TABLE_NAME + " nm, " + TABLE_PICTURE + " pc, " + TABLE_NAME_PICTURE + " np"
                +" WHERE nm." + KEY_NAME + " = '" + name + "'"
                + " AND pc." + KEY_ID + " = " + "np." + KEY_PICTURE_ID
                + " AND nm." + KEY_ID + " = " + "np." + KEY_NAME_ID
                + " ORDER BY pc." + KEY_DATE + " DESC";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                // adding to picture list
                dates.add(c.getInt((c.getColumnIndex(KEY_DATE))));
            } while (c.moveToNext());
        }
        return dates;
    }

    /**
     * getting all picture under single name and single date
     * */
    public List<Picture> getAllPicturesByNameAndDate(String name, Integer date) {
        List<Picture> pictures = new ArrayList<Picture>();

        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " nm, " + TABLE_PICTURE + " pc, " + TABLE_NAME_PICTURE + " np"
                +" WHERE nm." + KEY_NAME + " = '" + name + "'"
                + " AND pc." + KEY_ID + " = " + "np." + KEY_PICTURE_ID
                + " AND nm." + KEY_ID + " = " + "np." + KEY_NAME_ID
                + " AND pc." + KEY_DATE + " = '" + date + "'"
                + " ORDER BY pc." + KEY_DATE_TIME + " DESC";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Picture pic = new Picture();
                pic.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                pic.setPath((c.getString(c.getColumnIndex(KEY_PATH))));

                // adding to picture list
                pictures.add(pic);
            } while (c.moveToNext());
        }
        return pictures;
    }

    /**
     * getting all months under single name
     * */
    public List<Integer> getAllMonthsByName(String name) {
        List<Integer> months = new ArrayList<Integer>();

        String selectQuery = "SELECT  DISTINCT" + " pc." + KEY_MONTH
                +" FROM " + TABLE_NAME + " nm, " + TABLE_PICTURE + " pc, " + TABLE_NAME_PICTURE + " np"
                +" WHERE nm." + KEY_NAME + " = '" + name + "'"
                + " AND pc." + KEY_ID + " = " + "np." + KEY_PICTURE_ID
                + " AND nm." + KEY_ID + " = " + "np." + KEY_NAME_ID
                + " ORDER BY pc." + KEY_DATE + " DESC";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                // adding to picture list
                months.add(c.getInt((c.getColumnIndex(KEY_MONTH))));
            } while (c.moveToNext());
        }
        return months;
    }

    /**
     * getting all picture under single name and single date
     * */
    public List<Picture> getAllPicturesByNameAndMonth(String name, Integer month) {
        List<Picture> pictures = new ArrayList<Picture>();

        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " nm, " + TABLE_PICTURE + " pc, " + TABLE_NAME_PICTURE + " np"
                +" WHERE nm." + KEY_NAME + " = '" + name + "'"
                + " AND pc." + KEY_ID + " = " + "np." + KEY_PICTURE_ID
                + " AND nm." + KEY_ID + " = " + "np." + KEY_NAME_ID
                + " AND pc." + KEY_MONTH + " = '" + month + "'"
                + " ORDER BY pc." + KEY_DATE_TIME + " DESC";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Picture pic = new Picture();
                pic.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                pic.setPath((c.getString(c.getColumnIndex(KEY_PATH))));

                // adding to picture list
                pictures.add(pic);
            } while (c.moveToNext());
        }
        return pictures;
    }

    /**
     * getting all picture under single name
     * */
    public List<Picture> getAllPicturesByName(String name) {
        List<Picture> pictures = new ArrayList<Picture>();

        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " nm, " + TABLE_PICTURE + " pc, " + TABLE_NAME_PICTURE + " np"
                +" WHERE nm." + KEY_NAME + " = '" + name + "'"
                + " AND pc." + KEY_ID + " = " + "np." + KEY_PICTURE_ID
                + " AND nm." + KEY_ID + " = " + "np." + KEY_NAME_ID
                + " ORDER BY pc." + KEY_DATE_TIME + " DESC";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Picture pic = new Picture();
                pic.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                pic.setPath((c.getString(c.getColumnIndex(KEY_PATH))));

                // adding to picture list
                pictures.add(pic);
            } while (c.moveToNext());
        }
        return pictures;
    }

    public void changePictureDate(long id, int date) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DATE, date);

        db.update(TABLE_PICTURE, values, KEY_ID + " =?", new String[]{String.valueOf(id)});

    }

    // ------------------------ "name_pictures" table methods ----------------//

    /**
     * Creating name_pictures
     */
    public long createNamePicture(long name_id, long picture_id) {
        Long id = getPictureIdByNameIdAndPicId(name_id, picture_id);

        if(id==null){
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_NAME_ID, name_id);
            values.put(KEY_PICTURE_ID, picture_id);

            id = db.insert(TABLE_NAME_PICTURE, null, values);
        }

        return id;
    }

    /**
     * getting id by name_id and pic_id
     * */
    public Long getPictureIdByNameIdAndPicId(long name_id, long pic_id) {
        List<Picture> pictures = new ArrayList<Picture>();
        String selectQuery = "SELECT  *" + " FROM " + TABLE_NAME_PICTURE +" WHERE " + KEY_NAME_ID + " = '" + name_id+ "'"+ " AND "+KEY_PICTURE_ID + " = '" + pic_id+ "'";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null && c.moveToFirst()){
            return c.getLong(c.getColumnIndex(KEY_ID));
        }else{
            return null;
        }
    }

    /**
     * Deleting a name picture
     */
    public void deleteNamePicture(long pic_id, long name_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_PICTURE, KEY_PICTURE_ID + " =? AND " + KEY_NAME_ID + " =?",
                new String[]{String.valueOf(pic_id), String.valueOf(name_id)});
    }

    public void changeNamePicture(long pic_id, long name_id, long change_name_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME_ID, change_name_id);
        values.put(KEY_PICTURE_ID, pic_id);

        db.update(TABLE_NAME_PICTURE, values, KEY_PICTURE_ID + " =? AND " + KEY_NAME_ID + " =?",
                new String[]{String.valueOf(pic_id), String.valueOf(name_id)});

    }


    /**
     * getting names by pic_id
     * */
    public List<String> getAllNameByPicId(long pic_id) {
        List<String> names = new ArrayList<String>();

        String selectQuery = "SELECT DISTINCT nm." + KEY_NAME +
                " FROM " + TABLE_NAME + " nm, " + TABLE_PICTURE + " pc, " + TABLE_NAME_PICTURE + " np"
                +" WHERE pc." + KEY_ID + " = " + "np." + KEY_PICTURE_ID
                + " AND nm." + KEY_ID + " = " + "np." + KEY_NAME_ID
                + " AND pc." + KEY_ID +" = "+ pic_id;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                // adding to names list
                names.add(c.getString(c.getColumnIndex(KEY_NAME)));
            } while (c.moveToNext());
        }
        return names;
    }

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}