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
import static constants.Constants.url;
import static constants.Constants.username;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Random;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import util.MailUtil;


public class RecoverServlet extends HttpServlet implements Constants {

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
        try {
            Class.forName(className);
            Connection connection = DriverManager.getConnection(url, username, password);
            String email = request.getParameter("email");
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            if(username == null || password == null || email == null) {
                response.sendRedirect("RecoverAccount.jsp");
                return;
            }
            String where = "username='" + username + "' AND password='" + password + "'";
            ArrayList<ArrayList> records = Database.select("users", null, where, connection);
            if(records.isEmpty()) {
                response.sendRedirect("RecoverAccount.jsp");
                return;
            }
            String otp = generateOTP();
            HttpSession session = (HttpSession) request.getSession();
            session.setAttribute("otp", otp);
            session.setAttribute("username", username);
            System.out.println("++++++++++  OTP :: " + otp);
            if(MailUtil.sendEmail("You OTP", "Your OTP for account recovery is " + otp, email)) {
//                response.sendRedirect("ConfirmOTP.jsp");
            } else {
//                response.sendRedirect("RecoverAccount.jsp");
            }
            response.sendRedirect("ConfirmOTP.jsp");
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

    public String generateOTP() {
        StringBuilder otp = new StringBuilder();
        try {
            String rawData = "0987654321";
            Random random = new Random();
            for (int i = 0; i < 5; i++) {
                otp.append(rawData.charAt(random.nextInt(rawData.length())));
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return otp.toString();
    }

}