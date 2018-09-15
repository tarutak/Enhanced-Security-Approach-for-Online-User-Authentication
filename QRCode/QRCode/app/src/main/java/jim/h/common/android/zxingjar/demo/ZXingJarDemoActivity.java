package jim.h.common.android.zxingjar.demo;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;
import jim.h.common.android.zxingjar.demo.constants.RestConstants;
import jim.h.common.android.zxingjar.demo.rest.JSONService;
import jim.h.common.android.zxingjar.demo.util.Util;
import jim.h.common.android.zxinglib.integrator.IntentIntegrator;
import jim.h.common.android.zxinglib.integrator.IntentResult;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.*;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.zxing.BarcodeFormat;

import com.google.zxing.WriterException;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


public class ZXingJarDemoActivity extends Activity implements RestConstants {
    private Handler  handler = new Handler();
    private TextView txtScanResult;
    String a[];
    TextView otp;
    EditText user_id_edittext;
    EditText otp_edittext;
    EditText imei;
    EditText password;
    SharedPreferences sharedPreferences;
    TelephonyManager telephonyManager;
    String abc;
    String encryptAbc;
    String a1;
    Button generate;
    ImageView imageQRCode;
    Button send_barcode;
    Bitmap bitmap;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplication());
        txtScanResult = (TextView) findViewById(R.id.scan_result);
        View btnScan = findViewById(R.id.scan_button);
        generate=(Button)findViewById(R.id.generate_barcode);
        send_barcode=(Button)findViewById(R.id.send_barcode);
        otp=(TextView)findViewById(R.id.otp);
        user_id_edittext=(EditText)findViewById(R.id.user_id_edittext);
        otp_edittext=(EditText)findViewById(R.id.user_otp_edittext);
        imei=(EditText)findViewById(R.id.imei_edittext);
        password=(EditText)findViewById(R.id.password_edittext);
        imageQRCode=(ImageView)findViewById(R.id.image);
        telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.getDeviceId();
        Log.d("IMEI", telephonyManager.getDeviceId());
        Toast.makeText(getApplication(),telephonyManager.getDeviceId(),Toast.LENGTH_LONG).show();
        // Scan button
        btnScan.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // set the last parameter to true to open front light if available
                IntentIntegrator.initiateScan(ZXingJarDemoActivity.this, R.layout.capture,
                        R.id.viewfinder_view, R.id.preview_view, true);
            }
        });
        generate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password.getText().toString().equals(""))
                {
                    toast("Please Enter the password.");
                }
                else
                {
                    String pas=password.getText().toString().trim();
                    abc =sharedPreferences.getString("userid", "")+","+a1+","+telephonyManager.getDeviceId()+","+pas;
                    encryptAbc=encryptAES(abc);
                    Log.d("QR DATA",abc);
                    Log.d("QR ENCODED DATA", encryptAbc);
                    WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                    Display display = manager.getDefaultDisplay();
                    Point point = new Point();
                    display.getSize(point);
                    int width = point.x;
                    int height = point.y;
                    int smallerDimension = width < height ? width : height;
                    smallerDimension = smallerDimension * 3/4;
                    QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(encryptAbc,
                            null,
                            Contents.Type.TEXT,
                            BarcodeFormat.QR_CODE.toString(),
                            smallerDimension);
                    try {
                        bitmap = qrCodeEncoder.encodeAsBitmap();
                        imageQRCode.setImageBitmap(bitmap);
                    } catch (WriterException e) {
                        Log.e("", Log.getStackTraceString(e));
                    }
                }

            }
        });
        send_barcode.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                File f=new File(Environment.getExternalStorageDirectory(),"temp.jpg");
                try {
                    f.createNewFile();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,bos);
                    byte[] bitmapdata = bos.toByteArray();
                    FileOutputStream fos = new FileOutputStream(f);
                    fos.write(bitmapdata);
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String a= getStringImage(f);
                registerUser(a);
            }
        });
    }
    private void toast(String message) {
        Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();
    }
    private String getStringImage(File file){
        try {
            FileInputStream fin = new FileInputStream(file);
            byte[] imageBytes = new byte[(int)file.length()];
            fin.read(imageBytes, 0, imageBytes.length);
            fin.close();
            return Base64.encodeToString(imageBytes, Base64.DEFAULT);
        } catch (Exception ex) {
            Log.e("", Log.getStackTraceString(ex));
            toast("Image Size is Too High to upload.");
        }
        return null;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IntentIntegrator.REQUEST_CODE:
                IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode,
                        resultCode, data);
                if (scanResult == null) {
                    return;
                }
                final String result = decryptAES(scanResult.getContents());
                if (result != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            txtScanResult.setText(result);
                            a=result.split(";");
                            a1=a[2];
                            Toast.makeText(getApplication(),a1,Toast.LENGTH_LONG).show();
                            Log.d("QR OTP", a1);
                            Log.d("QR NAME", a[0]);
                            otp.setText(a1);
                            user_id_edittext.setText(sharedPreferences.getString("userid", ""));
                            otp_edittext.setText(a1);
                            imei.setText(telephonyManager.getDeviceId());
                            String pas=password.getText().toString().trim();
                            /*abc =sharedPreferences.getString("userid", "")+","+a1+","+telephonyManager.getDeviceId()+","+pas;
                            encryptAbc=encryptAES(abc);
                            Log.d("QR DATA",abc);
                            Log.d("QR ENCODED DATA",encryptAbc);*/

                        }
                    });
                }
                break;
            default:
        }
    }
    private void registerUser(String username) {
        final JSONService service = new JSONService();
        AsyncTask<String, Void, Boolean> task = new AsyncTask<String, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(String... args) {
                String url = args[0].trim();
                String username = args[1].trim();
                HashMap<String, String> params = new HashMap<>();
                params.put("image", username);
                params.put("user_id",sharedPreferences.getString("userid",""));
                return service.getResponse(url, params);
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                if(aBoolean) {
                    // success registered
                    // sho home page
                    process(service.getJSONObject());
                } else {
                    try {
                        String response = service.getJSONObject().getString(STATUS);
                        toast("Failed :: " + response);
                    } catch (Exception ex) {
                        Log.e("", Log.getStackTraceString(ex));
                    }
                }
                super.onPostExecute(aBoolean);
            }
        };
        String url = Util.getUrl(this) + "image_upload.jsp";
        task.execute(url, username);
    }

    private void process(JSONObject object) {
        try {
           /* String userId = object.getString(USER_ID);
            toast("Registered success with user id : " + userId);*/
            toast("Image send to server Successfully");
        } catch (Exception ex) {
            Log.e("", Log.getStackTraceString(ex));
        }
    }
    public static String encryptAES(String text) {
        String enc = null;
        try {
            Cipher cipher = Cipher.getInstance("AES");
            SecretKeySpec spec = new SecretKeySpec("thebestsecretkey".getBytes("UTF-8"), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, spec);
            byte[] bytes = text.getBytes("UTF-8");
            byte[] raw = cipher.doFinal(bytes);
           // enc= Base64.encodeToString(raw,1);
            BASE64Encoder encoder = new BASE64Encoder();
            enc = encoder.encode(raw);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return enc;
    }

    public static String decryptAES(String text) {
        String enc = null;
        try {
            Cipher cipher = Cipher.getInstance("AES");
            SecretKeySpec spec = new SecretKeySpec("thebestsecretkey".getBytes("UTF-8"), "AES");
            cipher.init(Cipher.DECRYPT_MODE, spec);
            byte[] raw = new BASE64Decoder().decodeBuffer(text);
            enc = new String(cipher.doFinal(raw), "UTF-8");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return enc;
    }

}