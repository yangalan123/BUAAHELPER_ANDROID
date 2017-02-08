package buaa.buaahelper;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailActivity extends Activity implements View.OnClickListener {


    private static SQLiteUtils SQLiteLink;

    public static void setSQLiteLink(SQLiteUtils link) {
        SQLiteLink = link;
    }

    protected long id;
    private boolean FAV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(android.R.style.Theme_Light_NoTitleBar);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_detail);

        TextView textView_Detail_title = (TextView) findViewById(R.id.Detail_title);
        TextView textView_Detail_updateat = (TextView) findViewById(R.id.Detail_updateat);
        TextView textView_Detail_content = (TextView) findViewById(R.id.Detail_content);
        final ImageButton Fav = (ImageButton) findViewById(R.id.fav);
        ImageButton getNext, getLast, ReturnToMain;
        getNext = (ImageButton) findViewById(R.id.getNext);
        getLast = (ImageButton) findViewById(R.id.getBefore);

        RelativeLayout Bottom_bar = (RelativeLayout) findViewById(R.id.bottom_bar_detail_act);

        ReturnToMain = (ImageButton) findViewById(R.id.goback);
        ReturnToMain.setOnClickListener(this);
        //Button button = (Button) findViewById(R.id.back);
        //button.setOnClickListener(this);
        Bundle bundle = getIntent().getExtras();
        //BUAAItemTouchHelperCallback buaaItemTouchHelperCallback = NoticeListFragment.buaaItemTouchHelperCallback;
        //textView.setText("Position:" + bundle.getInt("Position") + " "+"ID:"+bundle.getLong("ID")+" ");
        id = bundle.getLong("ID");

        String t = SQLiteLink.GetNotificationByID(id);

        try {
            JSONObject j = new JSONObject(t);

            textView_Detail_title.setText(j.getString("title"));
            textView_Detail_updateat.setText(SQLiteUtils.TimeStamp2Time(j.getLong("updated_at")));
            textView_Detail_content.setText(Html.fromHtml(j.getString("content")));
            if (j.getLong("star") == 1) {
                Fav.setImageResource(R.drawable.ic_star_black_24dp);
                FAV = true;
            } else {
                Fav.setImageResource(R.mipmap.ic_star_border_black_24dp);
                FAV = false;
            }

            /**
             * j.put("id", cursor.getLong(0));
             j.put("updated_at", cursor.getLong(1));
             j.put("title", cursor.getString(2));
             j.put("author", cursor.getString(3));
             j.put("content", cursor.getString(4));
             j.put("files", cursor.getString(5));
             j.put("star", cursor.getLong(6));
             *
             * */


        } catch (JSONException e) {
            e.printStackTrace();
        }

        //TODO:由于这次讨论的结果是不做上下翻页，这里就留作以后用好了，就是hasMoreXXX方法不用写了（暂时）

        if (!hasMoreBeforeNotification()) getLast.setVisibility(View.GONE);
        if (!hasMoreNextNotification()) getNext.setVisibility(View.GONE);

        if (hasMoreNextNotification() && hasMoreBeforeNotification()) {
            Bottom_bar.setBackgroundColor(0XEEEEEE);
        }
        //TODO:完成以下三个监听事件，从上往下：上一个，下一个，收藏（前两个暂时不写）

        getLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasMoreBeforeNotification()) {

                }
            }
        });

        getNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasMoreNextNotification()) {

                }
            }
        });

        Fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!FAV) {
                    Fav.setImageResource(R.drawable.ic_star_black_24dp);
                    FAV = true;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ClientUtils.StarNotification(id);
                        }
                    }).start();
                    //第一次点击，收藏操作
                } else {
                    Fav.setImageResource(R.mipmap.ic_star_border_black_24dp);
                    FAV = false;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ClientUtils.UnstarNotification(id);
                        }
                    }).start();
                    //第二次点击，取消收藏

                }
            }
        });


    }

    @Override
    public void onClick(View view) {
        this.finish();
    }

    private boolean hasMoreBeforeNotification()  //TODO：前面是否还有（暂时不写）
    {
        return false;
    }

    private boolean hasMoreNextNotification()  //TODO:后面是否还有（暂时不写）
    {
        return false;
    }
}
