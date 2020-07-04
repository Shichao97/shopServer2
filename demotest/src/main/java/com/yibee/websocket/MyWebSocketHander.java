package com.yibee.websocket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import com.yibee.MessageRepository;
import com.yibee.MyUtil;
import com.yibee.entity.Member;
import com.yibee.entity.Message;

import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * <消息处理中心>
 * <功能详细描述>
 * @author wzh
 * @version 2018-07-24 23:11
 * @see [相关类/方法] (可选)
 **/
@Component("webSocketHander")
//@EnableJpaRepositories
public class MyWebSocketHander extends AbstractWebSocketHandler{

    private Logger log = LogManager.getLogger(MyWebSocketHander.class);
    //@Resource
//    private static MessageService messageService;
//    @Autowired
//    public void setMessageService(MessageService messageService) {
//    	MyWebSocketHander.messageService = messageService;
//    }    
    //***websocket注入repository或bean必须static才行，否则为null
    private static MessageRepository repo;
    @Autowired
    public void setMessageService(MessageRepository repo) {
    	MyWebSocketHander.repo = repo;
    }    

    
    /**
     * 用来存放每个客户端对应的webSocket对象。
     */
    private static Map<String,WebSocketBeanSpring> webSocketMap;
    private static Map<String,WebSocketBeanSpring> userMap;

    static
    {
        // concurrent包的线程安全map
        webSocketMap = new ConcurrentHashMap<String, WebSocketBeanSpring>();
        userMap = new ConcurrentHashMap<String, WebSocketBeanSpring>();
    }

    // 服务器与客户端初次websocket连接成功执行
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        log.debug("websocket 连接成功......");

        // 连接成功当前对象放入websocket对象集合
        WebSocketBeanSpring bean = new WebSocketBeanSpring();
        bean.setSession(session);

        webSocketMap.put(session.getId(),bean);
        Member m = (Member)session.getAttributes().get(MyUtil.ATTR_LOGIN_NAME);
        String userName = null;
        if(m != null) {
        	userName = m.getUserName();
        	userMap.put(userName,bean);
        }
        
        long n = repo.count();
        
        log.info("客户端连接服务器session id :"+session.getId()+"，当前连接数：" + webSocketMap.size());

    }

    // 接受消息处理消息
    @Override
    public void handleMessage(WebSocketSession webSocketSession,
        WebSocketMessage<?> webSocketMessage)
        throws Exception
    {
        /*
		        获取客户端发送的消息,这里使用文件消息，也就是字符串进行接收
		        消息可以通过字符串，或者字节流进行接收
	        TextMessage String/byte[]接收均可以
	        BinaryMessage byte[]接收
        */
        log.info("客户端发送消息" + webSocketMessage.getPayload().toString());
        JSONObject jsonObject = JSONObject.fromObject(webSocketMessage.getPayload());
        String flag = jsonObject.getString("flag");
        Member sender = (Member)webSocketSession.getAttributes().get(MyUtil.ATTR_LOGIN_NAME);
        if(flag.equals("msg") && sender !=null) {
        	Long toId = jsonObject.getLong("toId");
        	if(toId > 0) {
	        	Message mes = new Message();
	        	mes.setContent(jsonObject.getString("content"));
	        	mes.setHasRead(0);
	        	mes.setReceiverId(jsonObject.getLong("toId"));
	        	mes.setSenderId(sender.getId());
	        	mes.setSenderName(sender.getUserName());
	        	mes.setId(0L);
	        	repo.save(mes);
        	}
        	jsonObject.put("fromId", sender.getId());
        	jsonObject.put("fromName", sender.getUserName());
        	jsonObject.remove("toId");
        	String toName = jsonObject.remove("toName").toString();
            TextMessage tm = new TextMessage(jsonObject.toString());
            this.sendMessage(toName, tm);
        }

        //WebSocketBeanSpring bean = userMap.get(toName);
        //Member receiver = null;
        //if(bean ==null) receiver = (Member)bean.getSession().getAttributes().get(MyUtil.ATTR_LOGIN_NAME);
        //this.batchSendMessage(message);


        
    }

    // 连接错误时触发
    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
        if(webSocketSession.isOpen()){
            webSocketSession.close();
        }

        log.debug("链接出错，关闭链接......");
        WebSocketBeanSpring bean = webSocketMap.remove(webSocketSession.getId());
        if(bean != null) {
        	String userName = (String) bean.getSession().getAttributes().get(MyUtil.ATTR_LAST_USER);
        	userMap.remove(userName);
        }
    }

    // 关闭websocket时触发
    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {

        log.debug("链接关闭......" + closeStatus.toString());
        //webSocketMap.remove(webSocketSession.getId());
        WebSocketBeanSpring bean = webSocketMap.remove(webSocketSession.getId());
        if(bean != null) {
        	String userName = (String) bean.getSession().getAttributes().get(MyUtil.ATTR_LAST_USER);
        	userMap.remove(userName);
        }

    }

    /**
     * 给所有在线用户发送消息（这里用的文本消息）
     * @param message
     */
    public void batchSendMessage(TextMessage message)
    {
        
        Set<Map.Entry<String, WebSocketBeanSpring>> setInfo =
            webSocketMap.entrySet();
        for (Map.Entry<String, WebSocketBeanSpring> entry : setInfo)
        {
            WebSocketBeanSpring bean = entry.getValue();
            try
            {
                bean.getSession().sendMessage(message);
            }
            catch (IOException e)
            {
                log.error(e.getMessage(),e);
            }
        }
    }

    public void sendToSessionId(String sessionId, TextMessage message)
    {
        WebSocketBeanSpring bean = webSocketMap.get(sessionId);
        try
        {
            bean.getSession().sendMessage(message);
        }
        catch (IOException e)
        {
            log.error(e.getMessage(), e);
        }
    }    

    public void sendMessage(String userName, TextMessage message)
    {
        WebSocketBeanSpring bean = userMap.get(userName);
        try
        {
            if(bean !=null) {
            	bean.getSession().sendMessage(message);
            }
        }
        catch (IOException e)
        {
            log.error(e.getMessage(), e);
        }
    }

}