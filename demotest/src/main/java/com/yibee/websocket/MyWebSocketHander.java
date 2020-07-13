package com.yibee.websocket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import com.yibee.MessageRepository;
import com.yibee.MyUtil;
import com.yibee.entity.CountMessage;
import com.yibee.entity.Member;
import com.yibee.entity.Message;

import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.List;
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
        String sId = null;
        if(m != null) {
        	sId = m.getId().toString();
        	userMap.put(sId,bean);
        }
        
        long n = repo.count();
        
        log.info("客户端连接服务器session id :"+session.getId()+"，当前连接数：" + webSocketMap.size());

    }

    
    /*
	    获取客户端发送的消息,这里使用文件消息，也就是字符串进行接收
	    消息可以通过字符串，或者字节流进行接收
	TextMessage String/byte[]接收均可以
	BinaryMessage byte[]接收
	*/
    @Override
    public void handleMessage(WebSocketSession webSocketSession,
        WebSocketMessage<?> webSocketMessage)
        throws Exception
    {
        log.info("客户端发送消息" + webSocketMessage.getPayload().toString());
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

	        	msg.setId(0L);
	        	repo.save(msg);
	        	msgId = repo.findLastInsertId();
        	}
        	if(msgId.longValue()>0L) {
	        	jsonObject.put("fromId", sender.getId());
	        	jsonObject.put("fromName", sender.getUserName());
	        	jsonObject.put("msgId",msgId);

	            TextMessage tm = new TextMessage(jsonObject.toString());
	            this.sendMessage(toId.toString(), tm);
	            //回送给自己
	            webSocketSession.sendMessage(tm);
        	}
        }
        else if(flag.equals("msg_new") && sender !=null) {//Main Message handshake
        	this.sendAllCountMessages(flag,webSocketSession,sender.getId());
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
        	//repo.readAllByToAndFrom(toId,fromId);
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
    		TextMessage tm = new TextMessage(jsonObject.toString());
    		webSocketSession.sendMessage(tm);
    	}
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
        	String sId = bean.getSession().getAttributes().get(MyUtil.ATTR_LAST_USERID).toString();
        	userMap.remove(sId);
        }
    }

    // 关闭websocket时触发
    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {

        log.debug("链接关闭......" + closeStatus.toString());
        //webSocketMap.remove(webSocketSession.getId());
        WebSocketBeanSpring bean = webSocketMap.remove(webSocketSession.getId());
        if(bean != null) {
        	String sId = bean.getSession().getAttributes().get(MyUtil.ATTR_LAST_USERID).toString();
        	userMap.remove(sId);
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