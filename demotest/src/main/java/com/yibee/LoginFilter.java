package com.yibee;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.lang.StringUtils;
//配置拦截路径
//@WebFilter(filterName = "loginFilter",urlPatterns = {"/goods/sell/*,/goods/buy/*,/member/upIcon,/member/edit*"})

@WebFilter(filterName = "loginFilter",urlPatterns = {"/goods/sell/*","/goods/buy/*","/member/upIcon","/member/edit*"})
public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //Part part = request.getPart("photo");
        //String id = request.getParameter("id");
        //System.out.println("TestFilter,"+request.getRequestURI());
        
        Object o = request.getSession().getAttribute(MyUtil.ATTR_LOGIN_NAME);
        boolean b = request.getRequestURI().startsWith("/goods/search");

		String url = request.getHeader("Origin");  
		System.out.println(url);
		if (!StringUtils.isEmpty(url)) {                          
			response.addHeader("Access-Control-Allow-Origin", url);                
			response.addHeader("Access-Control-Allow-Credentials", "true");                   
			response.addHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, PUT, OPTIONS");                
			//response.addHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type");                   
		}
		
        String method = request.getMethod();
		//OPTIONS是预请求，一律放行。
        if(!b && o == null && !method.equals("OPTIONS")) {
        	//response.sendError(604, "未登录用户，限制访问。");
    		response.setStatus(604);
    		response.setContentType("text/html");
    		response.setContentLength(0);
    		response.flushBuffer();
    		response.getWriter().close();
        }
        //执行
        else filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
