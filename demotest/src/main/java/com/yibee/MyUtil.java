package com.yibee;


import java.awt.Rectangle;
import java.awt.image.BufferedImage;
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

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.Thumbnails.Builder;
import net.coobird.thumbnailator.geometry.Positions;

public class MyUtil {
	public static final String ATTR_LOGIN_NAME="loginUser";
	public static final String ATTR_LAST_USERID="lastUserId";
	
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
	public static void main(String[] args) {
		
	}
	public static void resizeImage(int sizeOf,String oriPath,String afterPath) throws IOException {
        resizeImage(new Rectangle(0,0,sizeOf,sizeOf),oriPath,afterPath);
    }
	
	public static void resizeImage(Rectangle rect,String fromPic,String toPic)throws IOException {
        File fromFile = new File(fromPic);   

        Thumbnails.of(fromFile)
                .size(rect.width, rect.height)    
                .outputQuality(1f)    
                .toFile(toPic);
	}	
	
	public static void resizeCenterRegionImage(Rectangle rect,String fromPic,String toPic)throws IOException {
		BufferedImage image = ImageIO.read(new File(fromPic));  
		Builder<BufferedImage> builder = null;  
		  
		int imageWidth = image.getWidth();  
		int imageHeitht = image.getHeight();  
		if ((float)rect.width / rect.height != (float)imageWidth / imageHeitht) {  
		    if ((float)rect.width / rect.height > (float)imageWidth / imageHeitht) {  
		        image = Thumbnails.of(fromPic).height(300).asBufferedImage();  
		    } else {  
		        image = Thumbnails.of(fromPic).width(400).asBufferedImage();  
		    }  
		    builder = Thumbnails.of(image).sourceRegion(Positions.CENTER, 400, 300).size(400, 300);  
		} else {  
		    builder = Thumbnails.of(image).size(400, 300);  
		}  
		builder.outputFormat("jpg").toFile(toPic);  
	}
}