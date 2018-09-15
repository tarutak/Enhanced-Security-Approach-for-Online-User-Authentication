package jim.h.common.android.zxingjar.demo.constants;

import android.content.SharedPreferences;

import java.text.SimpleDateFormat;

/**
 * Created by TanmayP on 02-02-2016.
 */

public interface RestConstants {

//    String SITE = "http://192.168.1.18:8080/QRBank/";
   //String SITE = "http://192.168.1.18:8081/QRCode/";
//    String SITE1="http://";
//    String SITE2=":8081/QRCode/";
    String STATUS = "status";
    String SUCCESS = "success";
    String USERNAME="username";
    String USER_ID="user_id";
    String IP = "ip";
    String PORT = "port";
    String WEB_SERVICES = "/QRBank/";

    String LOGIN="login.jsp";

    String charset = "UTF-8"; // or "ISO-8859-1"
    int w = 400, h = 400;
//    String projectPath = "D:\\OneDrive\\Colleges\\MIT COE\\QRBank/";
//    String projectPath = "D:\\Tribbianis\\Nirranjan\\QR/";
//    String uploads = projectPath + "web/uploads/";
    String ext = "png";
    SimpleDateFormat format = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss_aa");

}