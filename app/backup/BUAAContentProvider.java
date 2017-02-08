package buaa.buaahelper;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by alan_yang on 2017/1/31.
 */

public class BUAAContentProvider extends ContentProvider {
    //TODO: Accomplish all the methods here, all content in each method should be overwritten
    public static final String FAV = "FAV", Notice = "NOTICE",TRASH="TRASH";
    private List<CommonItemForList> ITEMS = new ArrayList<CommonItemForList>();
    String username, password, type;
    private boolean isfirst = true;
    private static SQLiteUtils SQLiteLink;
    private BUAA_RecyclerViewAdapter buaa_recyclerViewAdapter;

    public static void setSQLiteLink(SQLiteUtils link) {
        SQLiteLink = link;
    }

    public void setBuaa_recyclerViewAdapter(BUAA_RecyclerViewAdapter buaa_recyclerViewAdapter) {
        this.buaa_recyclerViewAdapter = buaa_recyclerViewAdapter;
    }

    public BUAA_RecyclerViewAdapter getBuaa_recyclerViewAdapter() {
        return buaa_recyclerViewAdapter;
    }

    BUAAContentProvider(String type) {
        super(null); // do not delete this part
        this.type = type;
        //TODO:(YCH,2017,2,8)增加type为TRASH（垃圾箱）的实现
        getInitialDataList();

    }

    @Override
    public int getDataSize() {
        return ITEMS.size();
    }

    public void ForceFreshDataList() {

        updateNotifications();

    }

    private void getInitialDataList() {

        updateNotifications();
        /** 初始化列表 */
        /*
        for (int i = 1; i <= 25; i++) {
            ITEMS.add(createCommonItemForList(i));
        }
        */
        return;
    }

    public List<CommonItemForList> getPreDataList() {
        /** 获取较新内容 */

        updateNotifications();


        List<CommonItemForList> newlist = new ArrayList<CommonItemForList>();
        //for (int i = 1; i <= 10; i++)
        //   newlist.add(createCommonItemForList(-1));
        return newlist;
    }

    public List<CommonItemForList> getPostDataList() {  //获取新的几个


        List<CommonItemForList> newlist = new ArrayList<CommonItemForList>();
        for (int i = 0; i <= 10; i++)
            newlist.add(createCommonItemForList(i, "title", "", 0));
        return newlist;

    }

    public void LoadPreData()  //keep it
    {
        if (!isfirst) {
            updateNotifications();
            //clear();
            //addAll(getPreDataList());
        }
        isfirst = false;
    }

    public void LoadPostData()  //keep it
    {
        if (ClientUtils.getLog_state())
            addMoreItems(25);
        else updateNotifications();
        //ITEMS.addAll(getPostDataList());
        //updateNotifications();
        //updateNotifications();
    }

    @Override
    public void addItem(CommonItemForList item) {
        ITEMS.add(item);
    }


    protected CommonItemForList createCommonItemForList(long id, String label, String imageURI, long timestamp) {

        Date time = new Date(timestamp);

        if (!type.equals(FAV))
            return new CommonItemForList(id, label, imageURI, time);
        return new CommonItemForList(id, label, imageURI, time);
    }

    public void deleteDataInList(int position) {
      ITEMS.remove(position);
    }
    @Override
    public List<CommonItemForList> getDataList() {
        return ITEMS;
    }

    @Override
    protected void clear() {
        ITEMS.clear();
    }

    @Override
    protected void addAll(List<CommonItemForList> list) {
        ITEMS.addAll(list);
    }


    public void updateNotifications() {

        BUAAContentProvider.UpdateNotificationsTask updateNotificationsTask = new BUAAContentProvider.UpdateNotificationsTask();
        updateNotificationsTask.execute();

    }

    class UpdateNotificationsTask extends AsyncTask<String, Void, StringBuffer> {


        @Override
        protected StringBuffer doInBackground(String... strings) {
            StringBuffer errormsg = new StringBuffer("");

            ClientUtils.FetchNewNotification();

            return errormsg;
        }

        @Override
        protected void onPostExecute(StringBuffer s) {

            ITEMS.clear();
            if(buaa_recyclerViewAdapter!=null)
            buaa_recyclerViewAdapter.notifyDataSetChanged();
            String t = "";
            t = SQLiteLink.GetNotificationsOrderByUpdateTime(0, 25, ClientUtils.getUser());
            switch (t) {
                case SQLiteUtils.SQL_NONE:
                    break;
                default:

                    try {
                        JSONArray mJSONArray = new JSONArray(t);

                        for (int i = 0; i < mJSONArray.length(); i++) {
                            JSONObject j = mJSONArray.getJSONObject(i);
                            if (!type.equals(FAV)) {
                                CommonItemForList c = createCommonItemForList(j.getLong("id"), j.getString("title"), "", j.getLong("updated_at"));
                                ITEMS.add(c);
                            } else {
                                if (j.getLong("star") == 1) {
                                    CommonItemForList c = createCommonItemForList(j.getLong("id"), j.getString("title"), "", j.getLong("updated_at"));
                                    ITEMS.add(c);
                                }
                            }
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }


        }
    }

    private void addMoreItems(int dsize) {
        if (!type.equals(FAV)) {
            String t = "";
            t = SQLiteLink.GetNotificationsOrderByUpdateTime(ITEMS.size(), dsize, ClientUtils.getUser());
            switch (t) {
                case SQLiteUtils.SQL_NONE:
                    break;
                default:

                    try {
                        JSONArray mJSONArray = new JSONArray(t);

                        for (int i = 0; i < mJSONArray.length(); i++) {

                            JSONObject j = mJSONArray.getJSONObject(i);
                            CommonItemForList c = createCommonItemForList(j.getLong("id"), j.getString("title"), "", j.getLong("updated_at"));
                            ITEMS.add(c);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }else{
            String t = "";
            t = SQLiteLink.GetFAVNotificationsOrderByUpdateTime(ITEMS.size(), dsize, ClientUtils.getUser());
            switch (t) {
                case SQLiteUtils.SQL_NONE:
                    break;
                default:

                    try {
                        JSONArray mJSONArray = new JSONArray(t);

                        for (int i = 0; i < mJSONArray.length(); i++) {

                            JSONObject j = mJSONArray.getJSONObject(i);
                            CommonItemForList c = createCommonItemForList(j.getLong("id"), j.getString("title"), "", j.getLong("updated_at"));
                            ITEMS.add(c);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }


        }

    }
}
