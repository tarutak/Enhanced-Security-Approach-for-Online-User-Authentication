/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package constants;

import java.text.SimpleDateFormat;


public interface Constants {

    String charset = "UTF-8"; // or "ISO-8859-1"
    int w = 400, h = 400;
    String projectPath = "/SOFTWARES/NETBEANS_PROJECTS/BE/RSCOE/QRBank/";
    // Change setting in http://accounts.google.com/security to Allow less secure apps: ON
    // enter here your gmail username and password
    static String SMTP_AUTH_USER = "taktaru@gmail.com";
    static String SMTP_AUTH_PWD = "zxcvbnm@123";
    String uploads = projectPath + "web/uploads/";
    String session = projectPath + "web/session/";
    String ext = "png";
    SimpleDateFormat format = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss_aa");
    String className = "com.mysql.jdbc.Driver";
    String url = "jdbc:mysql://localhost:3306/bank";
    String username = "root";
    String password = "password";
    String UTF_8 = "UTF-8";
    String KEY = "thebestsecretkey";
    String AES = "AES";

}