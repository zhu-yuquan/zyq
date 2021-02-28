package com.zyq.frechwind.pub.bean;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * 发件人账号密码
 */
public class MailAuthenticator extends Authenticator {

    public static String USERNAME = "";
    public static String PASSWORD = "";

    public MailAuthenticator() {
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(USERNAME, PASSWORD);
    }
}
