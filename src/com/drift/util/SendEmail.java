package com.drift.util;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeUtility;

public final class SendEmail {
	
	private static final String SMTP_HOST = "smtp.163.com";
	private static final String USER = "driftlove000@163.com";
	private static final String PASSWORD = "admin_Drift";

	public static void send(String to, String subject, String content) {
		
		// Sanity test
		if(to==null || to.isEmpty()) {
			System.err.println("TO should not be empty!");
			return;
		}
		
		if((subject==null)) {
			subject = "";
		}
		if(content==null) {
			content = "";
		}
		
		if(subject.isEmpty() && content.isEmpty()) {
			System.err.println("subject and content should not be both empty!");
			return;
		}
		
		Properties props = new Properties();
		props.put("mail.smtp.host", SMTP_HOST);	//ָ��SMTP������
		props.put("mail.smtp.auth", "true");	//ָ���Ƿ���ҪSMTP��֤
		
		try {
			// Step 1: Configure the Mail Session
			Session mailSession = Session.getDefaultInstance(props);
			mailSession.setDebug(true);
			
			// Step 2: Construct the Message
			Message message = new MimeMessage(mailSession);
			message.setFrom(new InternetAddress(USER));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			//StringBuffer sb = new StringBuffer("=?UTF-8?B?");
			//sb.append(subject);
			//sb.append("?=");
			message.setSubject(MimeUtility.encodeText(subject, "utf-8", "B"));
			//message.setSubject(sb.toString());
			message.setSentDate(new Date());
			message.setContent(content, "text/html;charset=utf-8");
			
			// Step 3: Now send the Message
			Transport transport = mailSession.getTransport("smtp");
			transport.connect(SMTP_HOST, USER, PASSWORD);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	
/*	public static void main(String[] args) {
		String to = "0518121@fudan.edu.cn";
		String subject = "��л��ע��Ư��ƿӦ��";
		String content = "�������������������˺�<a href='http://driftlove.duapp.com'>����</a>";
		SendEmail.send(to, subject, content);
		System.out.println("��ϲ���ʼ����ͳɹ���");
	}*/
}
