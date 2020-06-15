package com.yibee;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSendObj {
	
	private Properties props = new Properties(); //先运行声明的属性，再运行构造函数
    private String account = "shichaostats@outlook.com";
    private String password = "lYc196899";
    
    
	//use default outlook
	public MailSendObj() {
		// TODO Auto-generated constructor stub
		props.setProperty("mail.transport.protocol", "smtp");   // 使用的协议（JavaMail规范要求）        
        props.put("mail.smtp.host", "smtp.office365.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.starttls.enable","true");
        props.put("mail.smtp.auth", "true");    
		
	}
	
	public MailSendObj(String host, int port, boolean starttlsEnable, String account, String password) {
		props.setProperty("mail.transport.protocol", "smtp");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", ""+port);
		if(starttlsEnable) {
			props.put("mail.smtp.starttls.enable","true");
		}
		props.put("mail.smtp.auth", "true");  
		this.account = account;
		this.password = password;
	}
	
	public void setProperty(String name, String value) {
		props.setProperty(name,value);
	}
	
	public String getProperty(String name) {
		return props.getProperty(name);
	}
	
	public boolean sendOut(String sendMail, String sendName, String receiveMail, String receiveName, String subject, String content) {
		
		try {
			Session session = Session.getInstance(props);
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(sendMail, sendName, "UTF-8"));
			message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMail, receiveName, "UTF-8"));
			message.setSubject(subject, "UTF-8");
			message.setContent(content, "text/html;charset=UTF-8");
			message.setSentDate(new Date());
			message.saveChanges();
			Transport transport = session.getTransport();
	        transport.connect(account, password);
	        transport.sendMessage(message, message.getAllRecipients());
	        transport.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
		 
	}

}
