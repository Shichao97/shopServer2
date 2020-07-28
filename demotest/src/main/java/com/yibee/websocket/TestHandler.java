package com.yibee.websocket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import net.sf.json.JSONObject;

@Component("TestHandler")
public class TestHandler extends AbstractWebSocketHandler{
	private Logger log = LogManager.getLogger(TestHandler.class);
	
	 @Override
	 public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		 log.info("afterConnectionEstablished");
		 WebSocketBeanSpring bean = new WebSocketBeanSpring();
	     bean.setSession(session);
	 }
	 
	 @Override
     public void handleMessage(WebSocketSession webSocketSession,
        WebSocketMessage<?> webSocketMessage)
        throws Exception{
		 log.info("handleMessage");
		 JSONObject jsonObject = JSONObject.fromObject(webSocketMessage.getPayload());
	     String flag = jsonObject.getString("flag");
	     TextMessage tm = new TextMessage(jsonObject.toString());
	     this.sendMessage(toId.toString(), tm);
	     webSocketSession.sendMessage(tm);
	     
	 }
	 
	// 连接错误时触发
    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
    	log.info("handleTransportError");
    }
    
    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
    	log.info("afterConnectionClosed");
    }

}
