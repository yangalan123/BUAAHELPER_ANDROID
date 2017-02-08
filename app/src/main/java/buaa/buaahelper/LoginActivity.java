package buaa.buaahelper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private Button button;
    private EditText Username, Password;
    private ProgressDialog pdiaLog;
    private String username, password;

    private static SQLiteUtils SQLiteLink;

    public static void setSQLiteLink(SQLiteUtils link) {
        SQLiteLink = link;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        button = (Button) findViewById(R.id.LoginButton);
        Username = (EditText) findViewById(R.id.LoginUsername);
        Password = (EditText) findViewById(R.id.LoginPassword);


        testToken();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!ClientUtils.getLog_state()) {
            Toast.makeText(getApplicationContext(), "正在离线查看历史消息", Toast.LENGTH_LONG).show();
            //android.os.Process.killProcess(android.os.Process.myPid());    //获取PID
            //System.exit(0);
        }
    }

    private void testToken() {
        pdiaLog = ProgressDialog.show(this, "登录", "验证登录中...", true, false);
        TestTokenTask testTokenTask = new TestTokenTask();
        testTokenTask.execute();

    }

    class TestTokenTask extends AsyncTask<String, Void, StringBuffer> {
        boolean LoginFlag = true;

        @Override
        protected StringBuffer doInBackground(String... strings) {
            StringBuffer errormsg = new StringBuffer("");


            Boolean relogin = true;
            try {
                String t = SQLiteLink.GetLastUserNToken();
                if (t != null) {
                    JSONObject lastuser = new JSONObject(t);

                    //Log.d("TestAccess_token", "" + ClientUtils.TestToken(lastuser.getString("access_token")));
                    if (ClientUtils.TestToken(lastuser.getString("access_token"))) {
                        ClientUtils.setAccess_token(lastuser.getString("access_token"));
                        ClientUtils.setUser(lastuser.getString("user"));
                        ClientUtils.setLog_state(true);
                        relogin = false;
                    }
                }
            } catch (Exception e) {
                //e.printStackTrace();
            }

            LoginFlag = relogin;
            return errormsg;
        }

        @Override
        protected void onPostExecute(StringBuffer s) {
            pdiaLog.dismiss();

            if (LoginFlag) {
                Toast.makeText(getApplicationContext(), "Error:登录状态失效，请重新登录", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "恢复登录成功", Toast.LENGTH_LONG).show();
                attemptStartService();
                LoginActivity.this.finish();

            }
        }
    }


    private void attemptLogin() {
        pdiaLog = ProgressDialog.show(this, "登录", "请稍等，正在登录中...", true, false);
        username = Username.getText().toString();
        password = Password.getText().toString();
        LoginTask logintask = new LoginTask();
        logintask.execute();
    }

    class LoginTask extends AsyncTask<String, Void, StringBuffer> {
        boolean LoginFlag = true;

        @Override
        protected StringBuffer doInBackground(String... strings) {
            StringBuffer errormsg = new StringBuffer("");
            LoginFlag = DataUtils.TemptLogin(username, password, errormsg);
            return errormsg;
        }

        @Override
        protected void onPostExecute(StringBuffer s) {
            pdiaLog.dismiss();

            if (!LoginFlag) {
                StringBuffer tmp = null;
                if (s == null) tmp = new StringBuffer("UNKNOWN");
                else tmp = new StringBuffer(s.toString());
                Toast.makeText(getApplicationContext(), "Error:" + tmp.toString(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Login Successfully!", Toast.LENGTH_LONG).show();
                //Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                //intent.putExtra("Username",username);
                //intent.putExtra("Password",password);
                //startActivity(intent);
                attemptStartService();

                LoginActivity.this.finish();

            }
        }
    }

    private void attemptStartService() {
        if (ClientUtils.getLog_state()) {
            Log.d("Service","Start!");
            Intent intent = new Intent(LoginActivity.this, NotificationUpdateQueryService.class);
            LoginActivity.this.startService(intent);
        }
    }
}
