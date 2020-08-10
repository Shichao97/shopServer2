package com.yibee.websocket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import com.yibee.MessageController;
import com.yibee.MessageRepository;
import com.yibee.MyUtil;
import com.yibee.entity.CountMessage;
import com.yibee.entity.Member;
import com.yibee.entity.Message;

import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * <Message access center>
 * <functions detailed description>
 **/
@Component
//@EnableJpaRepositories
public class MyWebSocketHandler extends AbstractWebSocketHandler{

    private Logger log = LogManager.getLogger(MyWebSocketHandler.class);
    //@Resource
//    private static MessageService messageService;
//    @Autowired
//    public void setMessageService(MessageService messageService) {
//    	MyWebSocketHander.messageService = messageService;
//    }    
//    private static MessageRepository repo;
//    @Autowired
//    public void setMessageRepository(MessageRepository repo) {
//    	MyWebSocketHandler.repo = repo;
//    }    

    @Autowired
    private MessageRepository repo;
    
    /**
     * Used to store webSocket objects for each user
     */
    private static Map<String,WebSocketBeanSpring> webSocketMap;
    private static Map<String,WebSocketBeanSpring> userMap;

    static
    {
        // concurrent package thread safe map
        webSocketMap = new ConcurrentHashMap<String, WebSocketBeanSpring>();
        userMap = new ConcurrentHashMap<String, WebSocketBeanSpring>();
    }

    // executed when the server and client build connection successfully the first time
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        log.debug("websocket connect success......");

        // The current object is put into the WebSocket object collection
        WebSocketBeanSpring bean = new WebSocketBeanSpring();
        bean.setSession(session);

        webSocketMap.put(session.getId(),bean);
        Member m = (Member)session.getAttributes().get(MyUtil.ATTR_LOGIN_NAME);
        String sId = null;
        if(m != null) {
        	
        	
        	sId = m.getId().toString();
        	WebSocketBeanSpring oldBean = userMap.get(sId);
        	if(oldBean != null) {//logout is another is login
	        	JSONObject jsonObject = new JSONObject();
	        	jsonObject.put("fromId", 1);
	        	jsonObject.put("fromName", "<System>");
	        	jsonObject.put("flag","logout");
	        	this.sendMessage(sId, new TextMessage(jsonObject.toString()));
	        	userMap.remove(sId);
	        	//this.sendSysMessage(m.getId(), "You are forced to logout because the same username login at other place");
        	}
        	userMap.put(sId,bean);
        }
        
        //long n = repo.count();
        
        log.info("The client connects to the server session id :"+session.getId()+"，connections now：" + webSocketMap.size());

    }

    public void sendSysMessage(Long toId,String content) {
    	Long sysId = 1L;
    	String sysName="<System>";
    	Message msg = new Message();
    	msg.setContent(content);
    	msg.setNotRead(1);
    	msg.setToId(toId);
    	msg.setFromId(sysId);
    	msg.setSendTime(new Date());
    	msg.setId(0L);
    	repo.save(msg);
    	long msgId = repo.findLastInsertId();    	
    	if(msgId>0L) {
    		JSONObject jsonObject = new JSONObject();
    		jsonObject.put("toId", toId);
        	jsonObject.put("fromId", sysId);
        	jsonObject.put("fromName", sysName);
        	jsonObject.put("msgId",msgId);
        	jsonObject.put("flag","msg");
        	jsonObject.put("content",content);

            TextMessage tm = new TextMessage(jsonObject.toString());
            this.sendMessage(toId.toString(), tm);
            
    	}
    }
    
    /*
	    Gets the message sent by the client, which is received using a file message, which is a string
	    Messages can be received via a string or byte stream
	    TextMessage can be received by String/byte[]
	    BinaryMessage can be received by byte[]
	*/
    @Override
    public void handleMessage(WebSocketSession webSocketSession,
        WebSocketMessage<?> webSocketMessage)
        throws Exception
    {
        log.info("client end send message" + webSocketMessage.getPayload().toString());
        JSONObject jsonObject = JSONObject.fromObject(webSocketMessage.getPayload());
        String flag = jsonObject.getString("flag");
        Member sender = (Member)webSocketSession.getAttributes().get(MyUtil.ATTR_LOGIN_NAME);
        if(flag.equals("msg") && sender !=null) {
        	Long toId = jsonObject.getLong("toId");
        	Long msgId = 0L;
        	if(toId > 0) {
	        	Message msg = new Message();
	        	msg.setContent(jsonObject.getString("content"));
	        	msg.setNotRead(1);
	        	msg.setToId(jsonObject.getLong("toId"));
	        	msg.setFromId(sender.getId());
	        	msg.setSendTime(new Date());
	        	msg.setId(0L);
	        	repo.save(msg);
	        	msgId = repo.findLastInsertId();
        	}
        	if(msgId.longValue()>0L) {
	        	jsonObject.put("fromId", sender.getId());
	        	jsonObject.put("fromName", sender.getUserName());
	        	jsonObject.put("msgId",msgId);
	        	jsonObject.put("sendTime",new Date().getTime());
	            TextMessage tm = new TextMessage(jsonObject.toString());
	            this.sendMessage(toId.toString(), tm);
	            //give message back to yourself
	            webSocketSession.sendMessage(tm);
        	}
        }
        else if(flag.equals("msg_new") && sender !=null) {//Main Message handshake
        	this.sendAllCountMessages(flag,webSocketSession,sender.getId());
    		jsonObject.put("flag","msg_new_end");
    		TextMessage tm = new TextMessage(jsonObject.toString());
    		webSocketSession.sendMessage(tm);
        }
        else if(flag.equals("msg_init") && sender !=null) {//MainPanel init
        	Long toId = jsonObject.getLong("toId");
        	this.sendHistoryMsg(flag,webSocketSession,sender.getId(),toId);
        	//send end of init
    		jsonObject.put("flag","msg_inited");
    		TextMessage tm = new TextMessage(jsonObject.toString());
    		webSocketSession.sendMessage(tm);
        }
        else if(flag.equals("msg_read") && sender !=null) {
        	Long id = jsonObject.getLong("msgId");

        	repo.readMessageById(id);
        	webSocketSession.sendMessage(webSocketMessage);
        }
        else if(flag.equals("msg_readAll") && sender !=null) {
        	Long toId = sender.getId();
        	Long fromId = jsonObject.getLong("otherId");

        	//TODO uncomment the next line
        	repo.readAllByToAndFrom(toId,fromId);
        	webSocketSession.sendMessage(webSocketMessage);
        }
    }

    
    private void sendAllCountMessages(String flag,WebSocketSession webSocketSession,
    		Long toId) throws Exception
    {
    	List<CountMessage> list = repo.findHistoryByToId(toId);
    	this.merge(list,repo.findHistoryByFromId(toId));
    	for(int i=0;i<list.size();i++) {
    		CountMessage cm = list.get(i);
    		JSONObject jsonObject = JSONObject.fromObject(cm);
    		jsonObject.put("flag",flag);
    		TextMessage tm = new TextMessage(jsonObject.toString());
    		webSocketSession.sendMessage(tm);
    	}
    }
    
    private void merge(List<CountMessage> list,List<CountMessage> list2){
    	for(CountMessage cm : list2) {
    		boolean b = containsFromId(list,cm.getOtherId());
    		if(!b) list.add(cm);
    	}
    	
    }

    
    private boolean containsFromId(List<CountMessage> list,Long id) {
    	for(CountMessage cm : list) {
    		if(cm.getOtherId().longValue() == id.longValue()) return true;
    	}
    	return false;
    }
    
    private void sendHistoryMsg(String flag,WebSocketSession webSocketSession,
    		Long fromId,Long toId) throws Exception
    {
    	Pageable pageable = PageRequest.of(0, 100, Sort.by("sendTime").descending());
    	Page<Message> page = repo.findMessageByIDs(fromId, toId, pageable);
    
    	List<Message> list  = page.getContent();
    	for(int i=0;i<list.size();i++) {
    		Message msg = list.get(list.size()-i-1);
    		JSONObject jsonObject = JSONObject.fromObject(msg);
    		jsonObject.put("flag",flag);
    		jsonObject.put("msgId",msg.getId());
    		jsonObject.put("sendTime",msg.getSendTime().getTime());
    		TextMessage tm = new TextMessage(jsonObject.toString());
    		webSocketSession.sendMessage(tm);
    	}
    }

    // Triggered when a connection error occurred
    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
        if(webSocketSession.isOpen()){
            webSocketSession.close();
        }

        log.debug("链接出错，关闭链接......");
        WebSocketBeanSpring bean = webSocketMap.remove(webSocketSession.getId());
        if(bean != null) {
        	String sId = bean.getSession().getAttributes().get(MyUtil.ATTR_LAST_USERID).toString();
        	userMap.remove(sId);
        }
    }

    // Triggered when webSocket is closed
    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {

        log.debug("链接关闭......" + closeStatus.toString());
        //webSocketMap.remove(webSocketSession.getId());
        WebSocketBeanSpring bean = webSocketMap.remove(webSocketSession.getId());
        if(bean != null) {
        	Long id = (Long)bean.getSession().getAttributes().get(MyUtil.ATTR_LAST_USERID);
        	if(id !=null) userMap.remove(id.toString());
        }

    }

    /**
     * Send a message to all online users (text message used here)
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

    public void sendMessage(String sId, TextMessage message)
    {
        WebSocketBeanSpring bean = userMap.get(sId);
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