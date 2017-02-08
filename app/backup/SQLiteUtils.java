package buaa.buaahelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by Croxx on 2017/1/31.
 */

public class SQLiteUtils {

    public final static String SQL_NONE = "SQL_NONE";


    private class DatabaseHelper extends SQLiteOpenHelper {

        //类没有实例化,是不能用作父类构造器的参数,必须声明为静态

        private static final String name = "HelperDB"; //数据库名称

        private static final int version = 1; //数据库版本

        private DatabaseHelper(Context context) {

            //第三个参数CursorFactory指定在执行查询时获得一个游标实例的工厂类,设置为null,代表使用系统默认的工厂类

            super(context, name, null, version);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            final String SQL_TABLE_INIT_usertokens = "CREATE TABLE IF NOT EXISTS usertokens("
                    + "updated_at INTEGER PRIMARY KEY NOT NULL,"
                    + "user VARCHAR(50) NOT NULL,"
                    + "access_token VARCHAR(100) NOT NULL"
                    + ")";
            final String SQL_TABLE_INIT_notification = "CREATE TABLE IF NOT EXISTS notifications("
                    + "id INTEGER NOT NULL,"
                    + "updated_at INTEGER NOT NULL,"
                    + "owner VARCHAR(50) NOT NULL,"
                    + "title VARCHAR(50),"
                    + "author VARCHAR(50),"
                    + "department VARCHAR(50),"
                    + "content VARCHAR(2000),"
                    + "files VARCHAR(500),"
                    + "star INTEGER DEFAULT 0,"
                    + "show INTEGER DEFAULT 1,"
                    + "UNIQUE(id,owner)"
                    + ")";

            db.execSQL(SQL_TABLE_INIT_usertokens);
            db.execSQL(SQL_TABLE_INIT_notification);

            Log.d("SQLite", "SQLite Online !");
            Log.d("TimeStamp", "" + GetTimeStamp());

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

    private DatabaseHelper mDatabaseHelper;
    private SQLiteDatabase db;

    private static SQLiteUtils instance;

    private SQLiteUtils(Context context) {

        mDatabaseHelper = new DatabaseHelper(context);
        db = mDatabaseHelper.getWritableDatabase();

    }

    public static synchronized SQLiteUtils getInstance(Context context) {
        if (instance == null) {
            instance = new SQLiteUtils(context);
        }
        return instance;
    }

    public void Login(String user, String access_token) {

        final String SQL_DATA_INSERT_Login = "INSERT INTO usertokens(updated_at,user,access_token)values(?,?,?)";
        db.execSQL(SQL_DATA_INSERT_Login, new Object[]{GetTimeStamp(), user, access_token});

        Log.d("TimeStamp", "" + GetTimeStamp());

        Cursor cursor = db.rawQuery("SELECT * FROM usertokens", null);

        while (cursor.moveToNext()) {

            Log.d("SQLite", " updated_at : " + cursor.getInt(0) + " user : " + cursor.getString(1) + " access_token : " + cursor.getString(2));

        }

        cursor.close();

    }

    public String GetLastUserNToken() {

        final String SQL_DATA_QUERY_LASTUserNToken = " SELECT user,access_token FROM usertokens WHERE ( updated_at = (SELECT MAX(updated_at) FROM usertokens))";

        Log.d("TimeStamp", "" + GetTimeStamp());

        Cursor cursor = db.rawQuery(SQL_DATA_QUERY_LASTUserNToken, null);

        if (cursor.getCount() == 0) {
            cursor.close();
            return null;
        } else {

            String t = "";
            while (cursor.moveToNext()) {

                t = "{\"user\":\"" + cursor.getString(0) + "\",\"access_token\":\"" + cursor.getString(1) + "\"}";
                Log.d("SQLite", t);

            }
            cursor.close();

            return t;
        }
    }

    public Boolean TestNotificationUpdate(long id, long local_at) {

        final String SQL_DATA_QUERY_UpdatedatById = "SELECT updated_at FROM notifications WHERE id = ?";

        Log.d("TimeStamp", "" + GetTimeStamp());

        Cursor cursor = db.rawQuery(SQL_DATA_QUERY_UpdatedatById, new String[]{"" + id});

        if (cursor.getCount() == 0) {
            cursor.close();
            return true;
        } else {

            while (cursor.moveToNext()) {
                long server_at = cursor.getLong(0);
                if (server_at > local_at) {
                    cursor.close();
                    return true;
                }
            }
            cursor.close();
            return false;

        }


    }

    public void InsertNUpdateNotification(long id, long updated_at, String owner, String title, String author, String department, String content, String files) {
        final String SQL_DATA_REPLACEINTO_Notification = "REPLACE INTO notifications (id,updated_at,owner,title,author,content,files)VALUES(?,?,?,?,?,?,?)";
        try {
            db.execSQL(SQL_DATA_REPLACEINTO_Notification, new Object[]{id, updated_at, owner, title, author, content, files});
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String GetNotificationsOrderByUpdateTime(int from, int size, String owner) {

        final String SQL_DATA_QUERY_LASTUserNToken = "SELECT id,updated_at,title,author,content,files,star FROM notifications WHERE (owner= ? AND show=1)ORDER BY updated_at DESC LIMIT ? OFFSET ?";

        Cursor cursor = db.rawQuery(SQL_DATA_QUERY_LASTUserNToken, new String[]{owner, "" + size, "" + from});
        if (cursor.getCount() == 0) {
            cursor.close();
            return SQL_NONE;

        } else {

            JSONArray mJSONArray = new JSONArray();


            while (cursor.moveToNext()) {

                JSONObject j = new JSONObject();
                try {
                    j.put("id", cursor.getLong(0));
                    j.put("updated_at", cursor.getLong(1));
                    j.put("title", cursor.getString(2));
                    j.put("author", cursor.getString(3));
                    j.put("content", cursor.getString(4));
                    j.put("files", cursor.getString(5));
                    j.put("star", cursor.getLong(6));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mJSONArray.put(j);


            }
            cursor.close();
            return mJSONArray.toString();

        }

    }
    public String GetFAVNotificationsOrderByUpdateTime(int from, int size, String owner) {

        final String SQL_DATA_QUERY_LASTUserNToken = "SELECT id,updated_at,title,author,content,files,star FROM notifications WHERE (owner= ? AND show=1 AND star=1)ORDER BY updated_at DESC LIMIT ? OFFSET ?";

        Cursor cursor = db.rawQuery(SQL_DATA_QUERY_LASTUserNToken, new String[]{owner, "" + size, "" + from});
        if (cursor.getCount() == 0) {
            cursor.close();
            return SQL_NONE;

        } else {

            JSONArray mJSONArray = new JSONArray();


            while (cursor.moveToNext()) {

                JSONObject j = new JSONObject();
                try {
                    j.put("id", cursor.getLong(0));
                    j.put("updated_at", cursor.getLong(1));
                    j.put("title", cursor.getString(2));
                    j.put("author", cursor.getString(3));
                    j.put("content", cursor.getString(4));
                    j.put("files", cursor.getString(5));
                    j.put("star", cursor.getLong(6));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mJSONArray.put(j);


            }
            cursor.close();
            return mJSONArray.toString();

        }

    }

    public String GetNotificationByID(long id) {

        final String SQL_DATA_QUERY_LASTUserNToken = "SELECT id,updated_at,title,author,content,files,star FROM notifications WHERE id= ? ";

        Cursor cursor = db.rawQuery(SQL_DATA_QUERY_LASTUserNToken, new String[]{"" + id});
        if (cursor.getCount() == 0) {
            cursor.close();
            return SQL_NONE;

        } else {

            JSONObject j = new JSONObject();

            while (cursor.moveToNext()) {
                try {
                    j.put("id", cursor.getLong(0));
                    j.put("updated_at", cursor.getLong(1));
                    j.put("title", cursor.getString(2));
                    j.put("author", cursor.getString(3));
                    j.put("content", cursor.getString(4));
                    j.put("files", cursor.getString(5));
                    j.put("star", cursor.getLong(6));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            cursor.close();
            return j.toString();

        }

    }

    public static void HideNotificationByID(long id) {
    }

    public static void SeekNotificationByID(long id) {
    }

    public void UnStarAllNotifications(){
        final String SQL_DATA_UPDATE = "UPDATE notifications SET star=0 WHERE star=1";
        db.execSQL(SQL_DATA_UPDATE);
    }

    public void StarNotification(long id) {
        final String SQL_DATA_UPDATE = "UPDATE notifications SET star=1 WHERE id=?";
        db.execSQL(SQL_DATA_UPDATE, new Object[]{"" + id});
    }

    public void UnStarNotification(long id) {
        final String SQL_DATA_UPDATE = "UPDATE notifications SET star=0 WHERE id=?";
        db.execSQL(SQL_DATA_UPDATE, new Object[]{"" + id});
    }


    public void LogAllNotifications() {

        final String SQL_DATA_QUERY_LASTUserNToken = "SELECT id,updated_at,owner,title,author,content,files,star FROM notifications ORDER BY updated_at";


        Cursor cursor = db.rawQuery(SQL_DATA_QUERY_LASTUserNToken, null);

        if (cursor.getCount() == 0) {
            cursor.close();

        } else {


            while (cursor.moveToNext()) {

                String t = "";
                t += "id : " + cursor.getString(0);
                t += "updated_at : " + cursor.getString(1);
                t += "owner : " + cursor.getString(2);
                t += "title : " + cursor.getString(3);
                t += "author : " + cursor.getString(4);
                t += "content : " + cursor.getString(5);
                t += "files : " + cursor.getString(6);
                t += "star : " + cursor.getString(7);
                Log.d("SQLite", t);

            }
            cursor.close();

        }


    }


    public static long GetTimeStamp() {
        long timestamp = System.currentTimeMillis() / 1000;//精确到秒
        //String  str=String.valueOf(timestamp);
        return timestamp;
    }

    public static String TimeStamp2Time(long timeLong) {
        String time = "" + timeLong;
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;

    }

}
