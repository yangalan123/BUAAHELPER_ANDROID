package buaa.buaahelper;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class NotificationUpdateQueryService extends Service {
    public NotificationUpdateQueryService() {
    }


    private NotificationManager notifyManager;

    @Override
    public void onCreate() {
        super.onCreate();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //flags = START_STICKY;
        //flags = Service.START_FLAG_REDELIVERY;


        HandlerThread testUpdateThread = new HandlerThread("TestUpdateThread");
        testUpdateThread.start();

        final Handler handler = new Handler(testUpdateThread.getLooper());
        Handler UIHandler = new Handler();
        final Context context = this;
        notifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                //设置小图标
                .setSmallIcon(R.mipmap.ic_launcher)
                //设置通知标题
                .setContentTitle("BUAAHelper")
                //设置通知内容
                .setContentText("新通知！");
        final NotificationCompat.Builder builder2 = new NotificationCompat.Builder(this)
                //设置小图标
                .setSmallIcon(R.mipmap.ic_launcher)
                //设置通知标题
                .setContentTitle("BUAAHelper正在运行");
        //设置通知内容
        //.setContentText("新通知！");
        //设置通知时间，默认为系统发出通知的时间，通常不用设置
        //.setWhen(System.currentTimeMillis());
        //通过builder.build()方法生成Notification对象,并发送通知,id=1
        //notifyManager.notify(2,builder2.build());
        startForeground(2, builder2.build());
        new Thread(new Runnable() {
            @Override
            public void run() {
                //notifyManager.notify(1, builder.build());

                if (ClientUtils.FetchNewNotification().equals(ClientUtils.STATE_GOODSERVICE)) {
                    Log.d("Service", "Get!");

                    notifyManager.notify(1, builder.build());

                } else {
                    Log.d("Service", "None!");
                }


                handler.postDelayed(this, 60000);

            }
        }).start();


        //return START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
