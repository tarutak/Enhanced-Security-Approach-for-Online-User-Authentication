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
import static constants.Constants.url;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Random;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import util.EncDecUtil;
import util.QRCode;


public class GenerateQR extends HttpServlet implements Constants {

    private static Connection connection;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            System.out.println("Connecting to server using : " + url + " Username : " + username + " Password : " + password);
            Initializer.initializeResources(projectPath, className, url, username, password);
            connection = Initializer.getConnection();
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
        try {
            String user = request.getParameter("username");
            if(user == null) {
                user = "";
            }
            ArrayList<String> errors = new ArrayList<String>();
            if(user.length() <= 0) {
                errors.add("Enter username.");
            }
            if(!errors.isEmpty()) {
                request.setAttribute("errors", errors);
                RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
                dispatcher.forward(request, response);
                return;
            }
            HttpSession session = (HttpSession) request.getSession();
            session.setAttribute("username", user);
            ArrayList<ArrayList> records = Database.select("users", null, "username='" + user + "'", connection);
            if(records.isEmpty() || records.size() > 1) {
                errors.add("Invalid username.");
                request.setAttribute("errors", errors);
                RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
                dispatcher.forward(request, response);
                return;
            }
            String userId = records.get(0).get(0).toString().trim();
            String otp = generateOTP();
            session.setAttribute("otp", otp);
            session.setAttribute("user_id", userId);
            String qrCodeData = user + ";" + userId + ";" + otp;
            // encrypt here
            String imageFile = QRCode.createQRCode(EncDecUtil.encryptAES(qrCodeData));
            response.sendRedirect("qrcode.jsp?fileName=" + imageFile);
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
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

    public static String generateOTP() {
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