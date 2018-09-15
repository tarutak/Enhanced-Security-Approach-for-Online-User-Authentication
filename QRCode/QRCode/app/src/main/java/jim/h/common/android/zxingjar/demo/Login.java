package jim.h.common.android.zxingjar.demo;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;

import jim.h.common.android.zxingjar.demo.constants.RestConstants;
import jim.h.common.android.zxingjar.demo.rest.JSONService;
import jim.h.common.android.zxingjar.demo.util.Util;

/**
 * Created by TanmayP on 02-03-2016.
 */
public class Login extends AppCompatActivity implements RestConstants {
    String logintext;
    EditText login;
    Button send;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zxing_jar_demo);
        send=(Button)findViewById(R.id.button_send);
        login=(EditText)findViewById(R.id.user_name_edittext);
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplication());
        editor=sharedPreferences.edit();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logintext=login.getText().toString().trim();
                login_user();
            }
        });
    }
    private void login_user() {
        if(logintext.length() <= 0) {
            login.setError(Html.fromHtml("<font color='red'>Your Employee ID</font>"));
            login.requestFocus();
            return;
        }

        final JSONService service = new JSONService();
        AsyncTask<String, Void, Boolean> task = new AsyncTask<String, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(String... args) {
                String url = args[0].trim();
                String username = args[1].trim();
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(USERNAME, username);
                return service.getResponse(url,params);
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                if(aBoolean)
                {
                    process(service.getJSONObject());
                    Intent i=new Intent(Login.this,ZXingJarDemoActivity.class);
                    startActivity(i);

                }
                else
                {
                    toast("Invalid Login Credentials.Please try again..");
                }
                super.onPostExecute(aBoolean);
            }
        };
        String url = Util.getUrl(this) + LOGIN;
        task.execute(url, logintext);
    }

    private void process(JSONObject object) {
        try {
            String user_name= object.getString(USERNAME);
            String userid=object.getString(USER_ID);
            toast("logged in user : " + user_name);
            editor.putString("username", user_name);
            editor.putString("userid", userid);
            editor.apply();
        } catch (Exception ex) {
            Log.e("", Log.getStackTraceString(ex));
        }
    }
    private void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.actionSettings) {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_settings);
            dialog.setCancelable(true);
            final EditText textIpAddress = (EditText) dialog.findViewById(R.id.textIpAddress);
            if(textIpAddress != null) {
                textIpAddress.setText(Util.getIp(this));
            }
            final EditText textPortNumber = (EditText) dialog.findViewById(R.id.textPortNumber);
            if(textPortNumber != null) {
                textPortNumber.setText(Util.getPort(this));
            }
            Button buttonSave = (Button) dialog.findViewById(R.id.buttonSave);
            buttonSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String ipAddress = textIpAddress.getText().toString().trim();
                    String portNumber = textPortNumber.getText().toString().trim();
                    if(ipAddress.isEmpty()) {
                        toast(Login.this, "Enter IP address of server.");
                        return;
                    }
                    if(portNumber.isEmpty()) {
                        toast(Login.this, "Enter port number of server.");
                        return;
                    }
                    Util.addToPreferences(Login.this, IP, ipAddress);
                    Util.addToPreferences(Login.this, PORT, portNumber);
                    toast(Login.this, "Settings saved success.");
                    dialog.dismiss();
                }
            });
            dialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}