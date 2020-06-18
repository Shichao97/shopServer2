package com.yibee.websocket;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

public class ChatTextHandler extends AbstractWebSocketHandler {
	 
	 @Override
	 protected void handleTextMessage(WebSocketSession session,
	   TextMessage message) throws Exception {
	  session.sendMessage(new TextMessage("xxxx"));
	 }
}