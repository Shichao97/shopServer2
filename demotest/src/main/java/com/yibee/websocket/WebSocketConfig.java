package com.yibee.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

/**
 * <websocketConfiguration>
 **/
@Configuration //Mark it as the Spring configuration class
@EnableWebSocket //Enable WebSocket support
public class WebSocketConfig implements WebSocketConfigurer{

	@Autowired
	private MyWebSocketHandler myWebSocketHandler;
	@Autowired
	private TestHandler testHandler;
    // Register the message handler and map the connection address
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry)
    {
        // Register message handlers and add custom interceptors to support webSocket connection access
        registry.addHandler(myWebSocketHandler, "myHandler")
            .addInterceptors(new WebSocketHandshakeInterceptor()).setAllowedOrigins("*");
//        registry.addHandler(new MyWebSocketHander(), "myHandler")
//        .setAllowedOrigins("*");
        registry.addHandler(testHandler, "testHandler").addInterceptors(new HttpSessionHandshakeInterceptor()).setAllowedOrigins("*");

   
//        registry.addHandler(new MyWebSocketHander(), "/chatjs")
//                .addInterceptors(new WebSocketHandshakeInterceptor()).withSockJS();
    }
}