package com.yibee;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.lang.StringUtils;
//配置拦截路径
//@WebFilter(filterName = "loginFilter",urlPatterns = {"/goods/sell/*,/goods/buy/*,/member/upIcon,/member/edit*"})

@WebFilter(filterName = "commonFilter",urlPatterns = {"/goods/*","/member/*","/collect/*","/order/*"})
public class CommonFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;


		String url = request.getHeader("Origin");  
		System.out.println(url);
		if (!StringUtils.isEmpty(url)) {                          
			response.addHeader("Access-Control-Allow-Origin", url);                
			response.addHeader("Access-Control-Allow-Credentials", "true");                   
			response.addHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, PUT, OPTIONS");                
			response.addHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type");                   
		}
		
        Object o = request.getSession().getAttribute(MyUtil.ATTR_LOGIN_NAME);
//        Cookie[] cookies = request.getCookies();
//        if(o == null && this.getCookies(cookies, "userId").length()>0) {
//			Cookie c = new Cookie("userId", "");
//			Cookie c2 = new Cookie("username","");
//			c.setPath("/");
//			c2.setPath("/");
//			
//			response.addCookie(c);//添加到response中
//			response.addCookie(c2);        	
//        }
		
		filterChain.doFilter(servletRequest, servletResponse);
    }
    
	public String getCookies(Cookie[] cookies,String name){
    	//HttpServletRequest 装请求信息类
    	//HttpServletRespionse 装相应信息的类
    	// Cookie cookie=new Cookie("sessionId","CookieTestInfo");
    	//Cookie[] cookies = request.getCookies();
    	if(cookies != null){
	    	for(Cookie cookie : cookies){
	    		if(cookie.getName().equals(name)){
	    			return cookie.getValue();
	    		}
	    	}
    	}
    	return "";
    }

    @Override
    public void destroy() {

    }
}
