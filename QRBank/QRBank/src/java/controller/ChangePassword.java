/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import commons.Database;
import static constants.Constants.className;
import static constants.Constants.password;
import static constants.Constants.url;
import static constants.Constants.username;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class ChangePassword extends HttpServlet {

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
            HttpSession session = (HttpSession) request.getSession();
            if(session.getAttribute("username") == null) {
                response.sendRedirect("index.jsp");
                return;
            }
            String username = session.getAttribute("username").toString().trim();
            String oldPassword = request.getParameter("oldPassword");
            String newPassword = request.getParameter("newPassword");
            String confirmPassword = request.getParameter("confirmPassword");
            if(oldPassword == null || newPassword == null || confirmPassword == null) {
                response.sendRedirect("ChangePassword.jsp");
                return;
            }
            if(oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                response.sendRedirect("ChangePassword.jsp");
                return;
            }
            if(!newPassword.equals(confirmPassword)) {
                response.sendRedirect("ChangePassword.jsp");
                return;
            }
            String where = "username='" + username + "' AND password='" + oldPassword + "'";
            ArrayList<ArrayList> records = Database.select("users", null, where, connection);
            if(records.isEmpty()) {
                response.sendRedirect("ChangePassword.jsp");
                return;
            }
            HashMap<String, Object> values = new HashMap<>();
            values.put("password", newPassword);
            if(Database.update("users", where, values, connection)) {
                System.out.println("Password update success.");
            }
            response.sendRedirect("ChangePassword.jsp");
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
