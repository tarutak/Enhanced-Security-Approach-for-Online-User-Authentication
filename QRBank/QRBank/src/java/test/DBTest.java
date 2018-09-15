/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import constants.Constants;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author pankaj
 */
public class DBTest implements Constants {

    public static void main(String[] args) {
        try {
            Class.forName(className);
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("Success Connection");
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

}