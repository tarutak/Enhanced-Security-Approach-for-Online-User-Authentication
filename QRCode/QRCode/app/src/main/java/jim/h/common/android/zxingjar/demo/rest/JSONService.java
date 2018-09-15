package jim.h.common.android.zxingjar.demo.rest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import android.util.Log;

import javax.net.ssl.HttpsURLConnection;

import jim.h.common.android.zxingjar.demo.constants.RestConstants;


public class JSONService implements RestConstants {

	private JSONObject object = new JSONObject();

	public JSONObject getJSONObject() {
		return object;
	}

	private String getPostDataString(HashMap<String, String> params) {
		StringBuilder result = new StringBuilder();
		try {
			boolean first = true;
			for (Map.Entry<String, String> entry : params.entrySet()) {
				if (first)
					first = false;
				else
					result.append("&");
				result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
				result.append("=");
				result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
			}
		} catch (Exception ex) {
			Log.e("JSONService.java", Log.getStackTraceString(ex));
		}
		return result.toString();
	}

	public boolean getResponse(String requestURL, HashMap<String, String> params) {
        StringBuilder builder = new StringBuilder();
		try {
            URL url = new URL(requestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            if(params != null) {
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(params));
                writer.flush();
                writer.close();
                os.close();
            }
            int responseCode=conn.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    builder.append(line);
                }
            } else {
                builder.append("{status: \"Unable to connect to server "+responseCode+" \"}");
            }
		} catch (Exception ex) {
			Log.e("JSONService.java", Log.getStackTraceString(ex));
            builder.append("{status: \"Unable to connect to server. "+ex.getMessage()+"\"}");
		}
        try {
            if(builder.indexOf("<") != -1) {
                builder.delete(builder.indexOf("<"), builder.length());
            }
            Log.d("JSONService.java", builder.toString());
            if(builder.length() <= 0) {
                builder.append("{status: \"No Output From Server\".}");
            }
                object = new JSONObject(builder.toString());
                if (SUCCESS.equalsIgnoreCase(object.getString(STATUS))) {
                    return true;
                }
        } catch (Exception ex) {
            Log.e("JSONService.java", Log.getStackTraceString(ex));
        }
		return false;
	}

}