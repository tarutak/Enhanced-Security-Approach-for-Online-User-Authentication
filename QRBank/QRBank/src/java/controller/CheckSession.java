/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import commons.Database;
import commons.Initializer;
import constants.Constants;
import static constants.Constants.className;
import static constants.Constants.password;
//import static constants.Constants.projectPath;
import static constants.Constants.url;
import static constants.Constants.username;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Arrays;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import util.EncDecUtil;
import util.QRCode;


@WebServlet(name = "CheckSession", urlPatterns = {"/CheckSession"})
public class CheckSession extends HttpServlet implements Constants {

    private static Connection connection;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            Class.forName(className);
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        try {
            HttpSession httpSession = (HttpSession) request.getSession();
            if(httpSession.getAttribute("username") == null || httpSession.getAttribute("otp") == null) {
                writer.println("<font color='red'>No username in session.</font>");
                return;
            }
            String username = httpSession.getAttribute("username").toString().trim();
            String sessionOTP = httpSession.getAttribute("otp").toString().trim();
            String userId_ = httpSession.getAttribute("user_id").toString().trim();
            File file = new File(session, userId_ + ".jpg");
            System.out.println("++ Authentication file should be :: " + file.getAbsolutePath());
            if(!file.exists()) {
                writer.println("<font color='red'>Waiting for authentication...</font>");
                return;
            }
            String qrData = QRCode.readQRCode(file.getAbsolutePath());
            if(qrData == null || qrData.length() <= 0) {
                writer.println("<font color='red'>No data in QR Code.</font>");
                return;
            }
            file.delete();
            String originalData = EncDecUtil.decryptAES(qrData);
            String[] values = originalData.split(",");
            if(values.length < 4) {
                writer.println("<font color='red'>Invalid parameters :: " + Arrays.asList(values) + "</font>");
                return;
            }
            String userId = values[0];
            String otp = values[1];
            String imei = values[2];
            String password = values[3];
            if(!sessionOTP.equals(otp)) {
                writer.println("<font color='red'>Invalid OTP found.</font>");
                return;
            }
            String where = "userId=" + userId + " AND password='" + password + "' AND imei_number='" + imei + "'";
            ArrayList<ArrayList> records = Database.select("users", null, where, connection);
            if(records.isEmpty() || records.size() != 1) {
                writer.println("<font color='red'>Unauthorized access.</font>");
                return;
            }
            writer.println("success");
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}