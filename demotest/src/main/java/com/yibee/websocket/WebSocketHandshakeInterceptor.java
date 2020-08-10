package com.yibee.websocket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import com.yibee.MyUtil;
import com.yibee.entity.Member;

import javax.servlet.http.HttpSession;

import java.util.Date;
import java.util.Map;

/**
 * <websocket communication intercepter>
 */
public class WebSocketHandshakeInterceptor extends HttpSessionHandshakeInterceptor
{
    
    private Logger log = LogManager.getLogger(WebSocketHandshakeInterceptor.class);

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
        ServerHttpResponse response,
        WebSocketHandler webSocketHandler, Map<String, Object> map)
        throws Exception
    {
 
    	// invoked before websocket handshake build, get httpsession
        if(request instanceof ServletServerHttpRequest)
        {
            ServletServerHttpRequest servletRequset = (ServletServerHttpRequest) request;
            HttpSession httpSession = servletRequset.getServletRequest().getSession(true);
            if (httpSession != null)
            {
                
                log.info("httpSession key：" + httpSession.getId());
        		Object o = httpSession.getAttribute(MyUtil.ATTR_LOGIN_NAME);
        		Member m =(Member)o;
        		if(m !=null) System.out.println("WebScoket login:"+m.getUserName());
            }
            else
            {
                log.warn("httpSession is null");
            }
        }

        boolean b =super.beforeHandshake(request,response,webSocketHandler,map);
        return b;
    }
    
    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest,
        ServerHttpResponse serverHttpResponse,
        WebSocketHandler webSocketHandler, Exception e)
    {
        // invoked after websocket handshake build
        log.info("websocket连接握手成功");
    }
}