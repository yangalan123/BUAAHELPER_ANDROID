package buaa.buaahelper;

import android.util.Log;

/**
 * Created by alan_yang on 2017/1/31.
 */
// handle Login Authentication && fetch various kind of data
public class DataUtils {


    public static boolean TemptLogin(String Username, String Password, StringBuffer Errormsg) {
        //Errormsg: Suggest the msg to explain the type of errors, if success, leave it to null
        String msgConnectfail = null;
        Boolean flag = false;
        //Log.d("LOGIN",ClientUtils.Login(Username, Password));
        switch (ClientUtils.Login(Username, Password)) {
            case ClientUtils.LOG_SUCCEED:
                flag = true;
                break;
            case ClientUtils.LOG_WRONG:
                msgConnectfail = "账号或密码错误！";
                break;
            case ClientUtils.STATE_DISCONNECTED:
                msgConnectfail = "网络连接错误！";
                break;

        }
        //String msgConnectfail = "ConnectFailed";
        Errormsg.append(msgConnectfail);
        return flag;
    }

    //type can either be FAV or Notice
    public static BUAAContentProvider getContentProvider(String type) {
        return new BUAAContentProvider(type);
    }
}
