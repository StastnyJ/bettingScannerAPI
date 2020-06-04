package com.bettingScanner.api.services;

import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.bettingScanner.api.requests.Request;

public class EmailingService {
    private final static String from = "robotscanenr@gmail.com";
    private final static String fromPwd = "Asdfgh1234";
    private final static String host = "smtp.gmail.com";
    private final static String port = "465";

    public static void SendEmail(String email, String subject, String body) {
        try {
            MimeMessage message = getMimeMessage();
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message.setSubject(subject);
            message.setContent(body, "text/html");
            Transport.send(message);
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    public static void NotifyFounds(List<Request> reqs, String email) {
        StringBuilder body = new StringBuilder();
        body.append("<p>Scanning service found one or more keywords on the following websites:</p><br/><ul>");
        reqs.stream().forEach(req -> body.append(String.format("<li><b>%s:</b> <a href=\"%s\">%s</a></li>",
                req.getKeyword(), req.getDisplayUrl(), req.getDisplayUrl())));
        body.append("</ul>");
        SendEmail(email, "Scanning service found one or more keywords", body.toString());
    }

    public static void testEmail(String email) {
        SendEmail(email, "Test email", "<b>This is test email</b>");
    }

    private static MimeMessage getMimeMessage() {
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, fromPwd);
            }
        });
        // session.setDebug(true);
        return new MimeMessage(session);
    }
}