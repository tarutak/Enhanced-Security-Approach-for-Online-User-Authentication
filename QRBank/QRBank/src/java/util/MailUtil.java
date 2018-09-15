/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import constants.Constants;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class MailUtil implements Constants {

    static String SMTP_HOST_NAME = "smtp.gmail.com";
    static int SMTP_HOST_PORT = 465;

    public static boolean sendEmail(String title, String mssg, String emailid) {
        try {
            Properties props = new Properties();

            props.put("mail.transport.protocol", "smtps");
            props.put("mail.smtps.host", SMTP_HOST_NAME);
            props.put("mail.smtps.auth", "true");
            props.put("mail.smtps.quitwait", "false");

            Session mailSession = Session.getDefaultInstance(props);
            mailSession.setDebug(true);
            Transport transport = mailSession.getTransport();

            MimeMessage message = new MimeMessage(mailSession);
            message.setSubject(title);
            message.setContent(mssg, "text/plain");

            message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailid));

            transport.connect(SMTP_HOST_NAME, SMTP_HOST_PORT, SMTP_AUTH_USER, SMTP_AUTH_PWD);

            transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
            transport.close();
            return true;
        } catch (Exception ert) {
            System.out.println("\n Email Sending Error\n No Internet connection.");
        }
        return false;
    }

}