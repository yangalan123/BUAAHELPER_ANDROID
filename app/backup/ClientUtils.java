package buaa.buaahelper;

import android.app.Activity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Croxx on 2017/1/31.
 */

public class ClientUtils {

    private static Boolean log_state = false;
    private static String access_token = "";
    private static String user = "";
    private static SQLiteUtils SQLiteLink;


    public static void setSQLiteLink(SQLiteUtils link) {
        SQLiteLink = link;
    }

    public static String getUser() {
        return user;
    }

    public static void setUser(String u) {
        user = u;
    }

    public static String getAccess_token() {
        return access_token;
    }

    public static void setAccess_token(String token) {
        access_token = token;
    }


    public static Boolean getLog_state() {
        return log_state;
    }

    public static void setLog_state(Boolean b) {
        log_state = b;
    }


    public final static String LOG_SUCCEED = "LOG_SUCCEED";
    public final static String LOG_WRONG = "LOG_WRONG";

    public final static String STATE_DISCONNECTED = "STATE_DISCONNECTED";
    public final static String STATE_LOGOUT = "STATE_LOGOUT";

    public final static String STATE_BADSERVICE = "STATE_BADSERVICE";

    public final static String STATE_GOODSERVICE = "STATE_GOODSERVICE";


    public static String Login(String user, String password) {

        /**
         * 向服务器发送登录请求获取access_token
         * 同时将access_token存到数据库
         */

        String url = "https://www.ourbuaa.com/api/login";
        Map<String, String> params = new HashMap<>();
        params.put("user", user);
        params.put("password", password);

        //Log.d("CLient",user);
        //Log.d("CLient",password);

        String t = HttpsUtils.httpPost(url, params);

        JSONObject mJSONObject = new JSONObject();
        try {
            mJSONObject = new JSONObject(t);
            if (mJSONObject.has("errcode")) {
                //Log.d("errcode",mJSONObject.getString("errcode"));
                if (mJSONObject.getString("errcode").equals("0")) {
                    setAccess_token(mJSONObject.getString("access_token"));
                    setUser(user);
                    log_state = true;
                    if (SQLiteLink != null) SQLiteLink.Login(user, access_token);
                    //MainActivity.
                    Log.d("Client", "" + getLog_state());
                    return LOG_SUCCEED;
                } else return LOG_WRONG;
            } else return STATE_BADSERVICE;
        } catch (JSONException e) {
            e.printStackTrace();
            return STATE_DISCONNECTED;
        }
    }

    public static String GetUserInfo(){
        String url = "https://www.ourbuaa.com/api/user/info";

        if (log_state) {

            Map<String, String> params = new HashMap<>();
            params.put("access_token", getAccess_token());

            String t = HttpsUtils.httpPost(url, params);

            try {
                JSONObject mJSONObject = new JSONObject(t);
                if (mJSONObject.has("errcode"))
                    if (mJSONObject.getString("errcode").equals("0")) {
                        return mJSONObject.getString("user");
                    } else return STATE_BADSERVICE;
                else return STATE_BADSERVICE;
            } catch (JSONException e) {
                e.printStackTrace();
                return STATE_DISCONNECTED;
            }

        } else return STATE_LOGOUT;
    }


    public static String ListNotification() {

        String url = "https://www.ourbuaa.com/api/notification";

        if (log_state) {

            Map<String, String> params = new HashMap<>();
            params.put("access_token", getAccess_token());

            String t = HttpsUtils.httpPost(url, params);

            try {
                JSONObject mJSONObject = new JSONObject(t);
                if (mJSONObject.has("errcode"))
                    if (mJSONObject.getString("errcode").equals("0")) {
                        return mJSONObject.getString("notifications");
                    } else return STATE_BADSERVICE;
                else return STATE_BADSERVICE;
            } catch (JSONException e) {
                e.printStackTrace();
                return STATE_DISCONNECTED;
            }

        } else return STATE_LOGOUT;

    }


    public static String ListStaredNotification() {

        String url = "https://www.ourbuaa.com/api/notification/stared";

        if (log_state) {

            Map<String, String> params = new HashMap<>();
            params.put("access_token", getAccess_token());

            String t = HttpsUtils.httpPost(url, params);

            try {
                JSONObject mJSONObject = new JSONObject(t);
                if (mJSONObject.has("errcode"))
                    if (mJSONObject.getString("errcode").equals("0")) {
                        return mJSONObject.getString("notifications");
                    } else return STATE_BADSERVICE;
                else return STATE_BADSERVICE;
            } catch (JSONException e) {
                e.printStackTrace();
                return STATE_DISCONNECTED;
            }

        } else return STATE_LOGOUT;

    }

    public static String ShowNotification(long id) {

        String url = "https://www.ourbuaa.com/api/notification/" + id;

        if (log_state) {

            Map<String, String> params = new HashMap<>();
            params.put("access_token", getAccess_token());

            String t = HttpsUtils.httpPost(url, params);

            try {
                JSONObject mJSONObject = new JSONObject(t);
                if (mJSONObject.has("errcode"))
                    if (mJSONObject.getString("errcode").equals("0")) {
                        return mJSONObject.getString("notification");
                    } else return STATE_BADSERVICE;
                else return STATE_BADSERVICE;
            } catch (JSONException e) {
                e.printStackTrace();
                return STATE_DISCONNECTED;
            }

        } else return STATE_LOGOUT;

    }

    public static String StarNotification(long id) {

        String url = "https://www.ourbuaa.com/api/notification/" + id + "/star";

        if (log_state) {

            Map<String, String> params = new HashMap<>();
            params.put("access_token", getAccess_token());

            String t = HttpsUtils.httpPost(url, params);

            try {
                JSONObject mJSONObject = new JSONObject(t);
                if (mJSONObject.has("errcode"))
                    if (mJSONObject.getString("errcode").equals("0")) {
                        SQLiteLink.StarNotification(id);
                        return STATE_GOODSERVICE;
                    } else return STATE_BADSERVICE;
                else return STATE_BADSERVICE;
            } catch (JSONException e) {
                e.printStackTrace();
                return STATE_DISCONNECTED;
            }

        } else return STATE_LOGOUT;

    }


    public static String UnstarNotification(long id) {

        String url = "https://www.ourbuaa.com/api/notification/" + id + "/unstar";

        if (log_state) {

            Map<String, String> params = new HashMap<>();
            params.put("access_token", getAccess_token());

            String t = HttpsUtils.httpPost(url, params);

            try {
                JSONObject mJSONObject = new JSONObject(t);
                if (mJSONObject.has("errcode"))
                    if (mJSONObject.getString("errcode").equals("0")) {
                        SQLiteLink.UnStarNotification(id);
                        return STATE_GOODSERVICE;
                    } else return STATE_BADSERVICE;
                else return STATE_BADSERVICE;
            } catch (JSONException e) {
                e.printStackTrace();
                return STATE_DISCONNECTED;
            }

        } else return STATE_LOGOUT;

    }

    public static Boolean TestToken(String access_token) {

        String url = "https://www.ourbuaa.com/api/notification";


        Map<String, String> params = new HashMap<>();
        params.put("access_token", access_token);

        String t = HttpsUtils.httpPost(url, params);

        try {
            JSONObject mJSONObject = new JSONObject(t);
            if (mJSONObject.has("errcode"))
                if (mJSONObject.getString("errcode").equals("0")) {
                    return true;
                }
            return false;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }


    }

    public static String FetchNewNotification() {

        //

        String msg = ListStaredNotification();
        if (msg.equals(STATE_BADSERVICE) || msg.equals(STATE_DISCONNECTED) || msg.equals(STATE_LOGOUT))
            return msg;
        else {
            String t = "";
            try {
                SQLiteLink.UnStarAllNotifications();
                JSONArray mJSONArray = new JSONArray(msg);
                for(int i=0;i<mJSONArray.length();i++){
                    JSONObject mJSONObject= mJSONArray.getJSONObject(i);
                    long id = mJSONObject.getLong("id");
                    SQLiteLink.StarNotification(id);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        msg = ListNotification();
        if (msg.equals(STATE_BADSERVICE) || msg.equals(STATE_DISCONNECTED) || msg.equals(STATE_LOGOUT))
            return msg;
        else {
            String t = "";
            try {
                JSONArray mJSONArray = new JSONArray(msg);
                for (int i = 0; i < mJSONArray.length(); i++) {
                    JSONObject mJSONObject = mJSONArray.getJSONObject(i);
                    long id = mJSONObject.getLong("id");
                    long updated_at = mJSONObject.getLong("updated_at");
                    if (SQLiteLink.TestNotificationUpdate(id, updated_at)) {
                        String tt = ShowNotification(id);
                        if (tt.equals(STATE_BADSERVICE) || tt.equals(STATE_DISCONNECTED) || tt.equals(STATE_LOGOUT))
                            return tt;
                        JSONObject noticifationJSON = new JSONObject(tt);
                        String owner = user;
                        //String owner = noticifationJSON.getString("owner");
                        String title = noticifationJSON.getString("title");
                        String author = noticifationJSON.getString("author");
                        String department = noticifationJSON.getString("department");
                        String content = noticifationJSON.getString("content");
                        String files = noticifationJSON.getString("files");
                        SQLiteLink.InsertNUpdateNotification(id, updated_at, owner, title, author, department, content, files);
                        t = STATE_GOODSERVICE;

                    }
                }

            } catch (JSONException e) {
                t = STATE_BADSERVICE;
                e.printStackTrace();
            }
            return t;
        }
    }


}

