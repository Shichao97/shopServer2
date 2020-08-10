package com.yibee.websocket;

import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * <websocket information object>
 * <used to store socket linking info>
 **/
public class WebSocketBeanSpring
{
    
    private WebSocketSession session;
    
    /**
     * The count of error link
     */
    private AtomicInteger errorLinkCount = new AtomicInteger(0);
    
    public int getErrorLinkCount()
    {
        // thread safe, atomically increase the current value by 1. 
    	// Note: the returning value is that before increment
        return errorLinkCount.getAndIncrement();
    }
    
    public void cleanErrorNum()
    {
        // clean up the count
        errorLinkCount = new AtomicInteger(0);
    }

	public void setSession(WebSocketSession session) {
		this.session = session;
		
	}

	public WebSocketSession getSession() {
		
		return this.session;
	}
    
   // ignore get set method
}
