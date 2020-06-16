package com.yibee;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import net.coobird.thumbnailator.Thumbnails;

public class MyUtil {
	public static final String ATTR_LOGIN_NAME="loginUser";
	
	private static final String slat = "&%5123***&&%%$$#@";
	public static String encrypt(String dataStr) {
		try {
			dataStr = dataStr + slat;
			MessageDigest m = MessageDigest.getInstance("MD5");
			m.update(dataStr.getBytes("UTF8"));
			byte s[] = m.digest();
			String result = "";
			for (int i = 0; i < s.length; i++) {
				result += Integer.toHexString((0x000000FF & s[i]) | 0xFFFFFF00).substring(6);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}
	
	
	private static Properties cofprops = null;
	public static Properties getConfProperties() throws IOException{
		if(cofprops == null) {
			
			Properties p = new Properties();
			ClassLoader cl = MyUtil.class.getClassLoader();
			InputStream in = cl.getResourceAsStream("my.properties");
			p.load(in);
			in.close();
			cofprops = p;
			return cofprops;
		
		}else {
			return cofprops;
		}

		
	}
	public static void manageImage(int sizeOf,String oriPath,String afterPath) throws IOException {
        File originalImg = new File(oriPath);   
        File thumbnailImg = new File(afterPath);
        Thumbnails.of(originalImg)
                .size(sizeOf, sizeOf)    
                .outputQuality(0.8f)    
                .toFile(thumbnailImg);
    }
}