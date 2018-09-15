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
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class UpdateImei extends HttpServlet implements Constants {

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
            String imei = request.getParameter("imei");
            if(imei == null) {
                response.sendRedirect("RecoverAccount.jsp");
                return;
            }
            HttpSession session = (HttpSession) request.getSession();
            if(session.getAttribute("username") == null) {
                response.sendRedirect("RecoverAccount.jsp");
                return;
            }
            String username = session.getAttribute("username").toString().trim();
            String where = "username='" + username + "'";
            HashMap<String, Object> values = new HashMap<>();
            values.put("imei_number", imei);
            if(Database.update("users", where, values, connection)) {
                System.out.println("Success");
                response.sendRedirect("index.jsp");
            } else {
                response.sendRedirect("RecoverAccount.jsp");
            }
            session.invalidate();
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
