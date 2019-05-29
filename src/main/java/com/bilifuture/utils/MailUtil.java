package com.bilifuture.utils;

import java.io.File;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * javamail
 * @author KingHenry
 *
 */
public class MailUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(MailUtil.class);
	
    private MimeMessage message;
    private Session session;
    private Transport transport;

    private String mailHost = "";
    private String sender_username = "";
    private String sender_password = "";

    /**
     * 初始化方法
     * @param host smtp服务器
     * @param sender_user 发件人邮箱地址
     * @param sender_pwd 发件人邮箱密码
     */
    public MailUtil(String host,String sender_user, String sender_pwd) {
    	Properties properties = new Properties();
        this.mailHost = host;
        this.sender_username = sender_user;
        this.sender_password = sender_pwd;
        properties.put("mail.smtp.host", this.mailHost);   
        properties.put("mail.smtp.auth", "true");   

        session = Session.getInstance(properties);
        session.setDebug(true);// 开启后有调试信息
        message = new MimeMessage(session);
    }

    /**
     * 最简单发送邮件
     * 
     * @param subject
     *            邮件主题
     * @param sendHtml
     *            邮件内容
     * @param receiveUser
     *            收件人地址
     * @param attachment
     *            附件
     */
    public void doSendHtmlEmail(String subject, String sendHtml, String receiveUser, File attachment) {
        try {
            // 发件人
            InternetAddress from = new InternetAddress(sender_username);
            message.setFrom(from);

            // 收件人
            InternetAddress address = new InternetAddress(receiveUser);
            message.setRecipient(Message.RecipientType.TO, address);

            // 邮件主题
            message.setSubject(subject);

            // 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
            Multipart multipart = new MimeMultipart();
            
            // 添加邮件正文
            BodyPart contentPart = new MimeBodyPart();
            contentPart.setContent(sendHtml, "text/html;charset=UTF-8");
            multipart.addBodyPart(contentPart);
            
            // 添加附件的内容
            if (attachment != null) {
                BodyPart attachmentBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(attachment);
                attachmentBodyPart.setDataHandler(new DataHandler(source));
                
                //MimeUtility.encodeWord可以避免文件名乱码
                attachmentBodyPart.setFileName(MimeUtility.encodeWord(attachment.getName()));
                multipart.addBodyPart(attachmentBodyPart);
            }
            
            // 将multipart对象放到message中
            message.setContent(multipart);
            // 保存邮件
            message.saveChanges();

            transport = session.getTransport("smtp");
            // smtp验证，就是你用来发邮件的邮箱用户名密码
            transport.connect(mailHost, sender_username, sender_password);
            // 发送
            transport.sendMessage(message, message.getAllRecipients());

            logger.info("send success!");
        } catch (Exception e) {
            logger.error("发送邮件失败：" + e.fillInStackTrace());
        } finally {
            if (transport != null) {
                try {
                    transport.close();
                } catch (MessagingException e) {
                    logger.equals("关闭发送邮件transport失败："+e.fillInStackTrace());
                }
            }
        }
    }

    
    /**
     * 群发邮件
     * 
     * @param subject 邮件主题
     * @param sendHtml 邮件内容
     * @param receiveUser 收件人地址 {"xxx@escmbc.com.cn","xxx@escmbc.com.cn"}
     * @param attachment 附件
     */
    public void doSendHtmlEmail(String subject, String sendHtml, String[] receiveUser, File attachment) {
        try {
            // 发件人
            InternetAddress from = new InternetAddress(sender_username);
            message.setFrom(from);

            // 收件人
            int len = receiveUser.length;    
            InternetAddress address[]=new InternetAddress[len];    
            for (int i = 0; i < receiveUser.length; i++) {  
                address[i]=new InternetAddress(receiveUser[i]);   
            }  
            message.setRecipients(Message.RecipientType.TO, address);

            // 邮件主题
            message.setSubject(subject);

            // 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
            Multipart multipart = new MimeMultipart();
            
            // 添加邮件正文
            BodyPart contentPart = new MimeBodyPart();
            contentPart.setContent(sendHtml, "text/html;charset=UTF-8");
            multipart.addBodyPart(contentPart);
            
            // 添加附件的内容
            if (attachment != null) {
                BodyPart attachmentBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(attachment);
                attachmentBodyPart.setDataHandler(new DataHandler(source));
                
                // 网上流传的解决文件名乱码的方法，其实用MimeUtility.encodeWord就可以很方便的搞定
                // 这里很重要，通过下面的Base64编码的转换可以保证你的中文附件标题名在发送时不会变成乱码
                //sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
                //messageBodyPart.setFileName("=?GBK?B?" + enc.encode(attachment.getName().getBytes()) + "?=");
                
                //MimeUtility.encodeWord可以避免文件名乱码
                attachmentBodyPart.setFileName(MimeUtility.encodeWord(attachment.getName()));
                multipart.addBodyPart(attachmentBodyPart);
            }
            
            // 将multipart对象放到message中
            message.setContent(multipart);
            // 保存邮件
            message.saveChanges();

            transport = session.getTransport("smtp");
            // smtp验证，就是你用来发邮件的邮箱用户名密码
            transport.connect(mailHost, sender_username, sender_password);
            // 发送
            transport.sendMessage(message, message.getAllRecipients());

            logger.info("send success!");
        } catch (Exception e) {
            logger.error("发送邮件失败：" + e.fillInStackTrace());
        } finally {
            if (transport != null) {
                try {
                    transport.close();
                } catch (MessagingException e) {
                    logger.equals("关闭发送邮件transport失败："+e.fillInStackTrace());
                }
            }
        }
    }
    
    
    /**
     * 发送邮件
     * 实现抄送、密送
     * @param subject 主题
     * @param sendHtml 文本
     * @param receiveUser 收件人 {"xxx@escmbc.com.cn","xxx@escmbc.com.cn"};
     * @param receiveUserCS 抄送{"xxx@escmbc.com.cn","xxx@escmbc.com.cn"};
     * @param receiveUserMS 密送{"xxx@escmbc.com.cn","xxx@escmbc.com.cn"};
     * @param attachment 附件
     */
    public void doSendHtmlEmail(String subject, String sendHtml, String[] receiveUser,String[] receiveUserCS,String[] receiveUserMS, File attachment) {
        try {
            // 发件人
            message.setFrom(new InternetAddress(sender_username));

            // 收件人
            if(receiveUser.length > 0){
            	InternetAddress address[] = new InternetAddress().parse(getMailList(receiveUser));    
            	message.setRecipients(Message.RecipientType.TO, address);
            }
            //抄送
            if(receiveUserCS.length > 0){
            	InternetAddress addressCS[] = new InternetAddress().parse(getMailList(receiveUserCS));    
            	message.setRecipients(Message.RecipientType.CC, addressCS);
            }
            //密送
            if(receiveUserMS.length > 0){
            	InternetAddress addressMS[] = new InternetAddress().parse(getMailList(receiveUserMS));    
            	message.setRecipients(Message.RecipientType.BCC, addressMS);
            }

            // 邮件主题
            message.setSubject(subject);

            // 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
            Multipart multipart = new MimeMultipart();
            
            // 添加邮件正文
            BodyPart contentPart = new MimeBodyPart();
            contentPart.setContent(sendHtml, "text/html;charset=UTF-8");
            multipart.addBodyPart(contentPart);
            
            // 添加附件的内容
            if (attachment != null) {
                BodyPart attachmentBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(attachment);
                attachmentBodyPart.setDataHandler(new DataHandler(source));
                
                // 网上流传的解决文件名乱码的方法，其实用MimeUtility.encodeWord就可以很方便的搞定
                // 这里很重要，通过下面的Base64编码的转换可以保证你的中文附件标题名在发送时不会变成乱码
                //sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
                //messageBodyPart.setFileName("=?GBK?B?" + enc.encode(attachment.getName().getBytes()) + "?=");
                
                //MimeUtility.encodeWord可以避免文件名乱码
                attachmentBodyPart.setFileName(MimeUtility.encodeWord(attachment.getName()));
                multipart.addBodyPart(attachmentBodyPart);
            }
            
            // 将multipart对象放到message中
            message.setContent(multipart);
            // 保存邮件
            message.saveChanges();

            transport = session.getTransport("smtp");
            // smtp验证，就是你用来发邮件的邮箱用户名密码
            transport.connect(mailHost, sender_username, sender_password);
            // 发送
            transport.sendMessage(message, message.getAllRecipients());

            logger.info("send success!");
        } catch (Exception e) {
            logger.error("发送邮件失败：" + e.fillInStackTrace());
        } finally {
            if (transport != null) {
                try {
                    transport.close();
                } catch (MessagingException e) {
                    logger.equals("关闭发送邮件transport失败："+e.fillInStackTrace());
                }
            }
        }
    }
    
    /**
     * 发送邮件
     * 实现多个附件
     * @param subject 主题
     * @param sendHtml 文本
     * @param receiveUser 收件人 {"xxx@escmbc.com.cn","xxx@escmbc.com.cn"};
     * @param receiveUserCS 抄送{"xxx@escmbc.com.cn","xxx@escmbc.com.cn"};
     * @param receiveUserMS 密送{};
     * @param fileList 附件 String[] fileList = new String[2]; 
     *  fileList[0] = "D:\\xxx.jpg"; 
     * fileList[1] = "D:\\xxx.txt"; 
     */
    public void doSendHtmlEmail(String subject, String sendHtml, String[] receiveUser,String[] receiveUserCS,String[] receiveUserMS, String fileList[]) {
        try {
            // 发件人
            message.setFrom(new InternetAddress(sender_username));

            // 收件人
            if(receiveUser.length > 0 && receiveUser != null){
            	InternetAddress address[] = new InternetAddress().parse(getMailList(receiveUser));    
            	message.setRecipients(Message.RecipientType.TO, address);
            }
            //抄送
            if(receiveUserCS.length > 0 && receiveUserCS != null){
            	InternetAddress addressCS[] = new InternetAddress().parse(getMailList(receiveUserCS));    
            	message.setRecipients(Message.RecipientType.CC, addressCS);
            }
            //密送
            if(receiveUserMS.length > 0 && receiveUserMS != null){
            	InternetAddress addressMS[] = new InternetAddress().parse(getMailList(receiveUserMS));    
            	message.setRecipients(Message.RecipientType.BCC, addressMS);
            }

            // 邮件主题
            message.setSubject(subject);

            // 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
            Multipart multipart = new MimeMultipart();
            
            // 添加邮件正文
            BodyPart contentPart = new MimeBodyPart();
            contentPart.setContent(sendHtml, "text/html;charset=UTF-8");
            multipart.addBodyPart(contentPart);
            
            // 添加附件的内容
            if (fileList != null && fileList.length > 0) {
            	for(int index = 0;index <  fileList.length;index++){
            		MimeBodyPart attachmentBodyPart = new MimeBodyPart();
            		FileDataSource source = new FileDataSource(fileList[index]);
            		attachmentBodyPart.setDataHandler(new DataHandler(source));
            		attachmentBodyPart.setFileName(MimeUtility.encodeWord(source.getName()));
            		multipart.addBodyPart(attachmentBodyPart);
            	}
            }
            
            // 将multipart对象放到message中
            message.setContent(multipart);
            // 保存邮件
            message.saveChanges();

            transport = session.getTransport("smtp");
            // smtp验证，就是你用来发邮件的邮箱用户名密码
            transport.connect(mailHost, sender_username, sender_password);
            // 发送
            transport.sendMessage(message, message.getAllRecipients());

            logger.info("send success!");
        } catch (Exception e) {
            logger.error("发送邮件失败：" + e.fillInStackTrace());
        } finally {
            if (transport != null) {
                try {
                    transport.close();
                } catch (MessagingException e) {
                    logger.equals("关闭发送邮件transport失败："+e.fillInStackTrace());
                }
            }
        }
    }
    
    private static String getMailList(String[] mailArray) { 

    	StringBuffer toList = new StringBuffer(); 
    	int length = mailArray.length; 
    	if (mailArray != null && length < 2) { 
    		toList.append(mailArray[0]); 
    	} else { 
	    	for (int i = 0; i < length; i++) { 
		    	toList.append(mailArray[i]); 
		    	if (i != (length - 1)) { 
		    		toList.append(","); 
		    	} 
	    	} 
    	} 
    	return toList.toString(); 
    } 
    
    public static void main(String[] args) {
    	MailUtil se = new MailUtil("smtp.escmbc.com.cn", "yt_00@escmbc.com.cn", "123456");
    	String content = "javamail测试";
    	String subject = "javamail测试";
    	String[] toMailArray = {"yt_11@escmbc.com.cn","yt_22@escmbc.com.cn"};  
    	File affix = new File("D:\\MM\\CONTENTFILE\\11331383040305398.jpg");
    	String[] toMailArrayCS = {"yt_33@escmbc.com.cn"}; 
    	String[] toMailArrayMS = {"yt_44@escmbc.com.cn"}; 
    	String[] arrArchievList = new String[2]; 
        arrArchievList[0] = "D:\\MM\\CONTENTFILE\\11331383040305398.jpg"; 
        arrArchievList[1] = "D:\\MM\\PROPACKAG\\13_1484_PROPACKAG.jpg"; 
    	
    	//邮件群发
        //se.doSendHtmlEmail(subject, content, toMailArray, affix);
        //抄送
        //se.doSendHtmlEmail(subject, content, toMailArray,toMailArrayCS,toMailArrayMS, affix);
        //多个附件
        se.doSendHtmlEmail(subject, content, toMailArray,toMailArrayCS,toMailArrayMS, arrArchievList);
    }
}

