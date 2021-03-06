package com.yibee;

import java.text.NumberFormat;
import java.text.spi.NumberFormatProvider;
import java.util.Date;
import java.util.Optional;
import java.util.Properties;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yibee.entity.Goods;
import com.yibee.entity.Member;
import com.yibee.entity.NameOrder;
import com.yibee.entity.Order;
import com.yibee.entity.OrderWithGoods;
import com.yibee.websocket.MyWebSocketHandler;

@RestController
@RequestMapping("/order")
public class OrderController {
	@Autowired
	private MyWebSocketHandler webSocketHander;

	@Resource
	private OrderRepository repo;
	@Resource
	private GoodsRepository goodsRepo;
	@Resource
	private MemberRepository memberRepo;
	@PersistenceContext
    private EntityManager em;
	
	/**
	 * invoke when user place order
	 * 
	 * @param request
	 * @param buyerId
	 * @param goodsId
	 * @param receiveAddr
	 * @param receiveMethod
	 * @return if the order placement is successful
	 */
	@CrossOrigin(origins = "*", maxAge = 3600)
	@GetMapping(value="/placeOrder")
	@Transactional
	public Properties placeOrder(
			HttpServletRequest request,
			@RequestParam("buyerId") Long buyerId,
			@RequestParam("goodsId") Long goodsId,
			@RequestParam("receiveAddr") String receiveAddr,
			@RequestParam("receiveMethod") byte receiveMethod
			) {
		Properties p = new Properties();
		HttpSession session = request.getSession();
		Object o = session.getAttribute(MyUtil.ATTR_LOGIN_NAME);
		Member m =(Member)o;
		
		if(m==null || m.getId().longValue() != buyerId.longValue()) {
			p.put("success", 0);
			p.put("msg", "UserId is empty or wrong!");
			return p;
		}
		Goods g = goodsRepo.findGoodsById(goodsId);
		if(g == null || buyerId.longValue() == g.getSellerId().longValue() || g.getStatus()!=Goods.STATUS_SELLING_NOW) {
			p.put("success", 0);
			p.put("msg", "goods is empty or wrong!");
			return p;
		}
		
//		Goods g = gop.get();
//		Optional<Member> mop = memberRepo.findById(g.getSellerId());
//		Member seller = mop.get();
		String sellerName = memberRepo.findNameById(g.getSellerId());
		Date orderTime = new Date();
//        EntityTransaction tran = em.getTransaction();
        try {
//        	tran.begin();
        	
        	g.setStatus(Goods.STATUS_SOLD_OUT);
        	goodsRepo.save(g);
        	Order order = new Order();
        	String orderNo = produceOrderNo(g.getId());
        	
        	Long maxId = repo.getMaxId();
        	Long id = maxId==null?1L:maxId + 1;
        	order.setId(id);
        	order.setBuyerId(buyerId);
        	order.setBuyerName(m.getUserName());
        	order.setGoodsId(goodsId);
        	order.setPaymentStatus(Order.PAYMENT_NO);
        	order.setStatus(Order.STATUS_WAIT_COMPLETE);
        	order.setOrderPrice(g.getPrice());
        	order.setReceiveAddr(receiveAddr);
        	order.setSellerId(g.getSellerId());
        	order.setSellerName(sellerName);
        	order.setOrderTime(orderTime);
        	order.setReceiveMethod(receiveMethod);
        	order.setOrderNo(orderNo);
        	repo.save(order);
        	
        	webSocketHander.sendSysMessage(g.getSellerId(), "Your goods '"+g.getName()+"' is ordered by "+m.getUserName());
            
        	p.put("success", id);
            p.put("orderNo",orderNo);
            return p;

        } catch (Exception e) {
//            tran.rollback();
            p.put("success", 0);
			p.put("msg", e.getMessage());
			return p;
        }
        
	}
	
	/**
	 * produce random order no.
	 * @param gid
	 * @return order no.
	 */
	private String produceOrderNo(Long gid) {
		int r1=(int)(Math.random()*(10));//产生2个0-9的随机数
		int r2=(int)(Math.random()*(10));
		long now = System.currentTimeMillis();//一个13位的时间戳
		String paymentID =String.valueOf(r1)+String.valueOf(r2)+String.valueOf(now);// 订单ID
		NumberFormat numberFormat = NumberFormat.getNumberInstance();
	    numberFormat.setMinimumIntegerDigits(4);//最小位数
	    numberFormat.setMaximumIntegerDigits(4);//最大位数
	    numberFormat.setGroupingUsed(false);
	    String num = numberFormat.format(gid);
		return paymentID+num;
	}
	
	/**
	 * invoke when cancel order
	 * 
	 * @param request
	 * @param id
	 * @return if the order has been successfully canceled
	 */
	@CrossOrigin(origins = "*", maxAge = 3600)
	@GetMapping(value="/cancelOrder")
	@Transactional
	public Properties cancelOrder(
			HttpServletRequest request,
			@RequestParam("id") Long id
			) {
		Properties p = new Properties();
		HttpSession session = request.getSession();
		Object o = session.getAttribute(MyUtil.ATTR_LOGIN_NAME);
		Member m =(Member)o;
		Optional<Order> oop = repo.findById(id);
		if(m==null) {
			p.put("success", 0);
			p.put("msg", "User has not login!");
			return p;
		}
		
		if(oop.isPresent()==false || oop.get().getBuyerId().longValue()!=m.getId().longValue()) {
			p.put("success", 0);
			p.put("msg", "Has no order or order is not yours!");
			return p;
		}
		
		Order order = oop.get();
		if(order.getStatus()!=Order.STATUS_WAIT_COMPLETE || order.getPaymentStatus()!= Order.PAYMENT_NO) {
			p.put("success", 0);
			p.put("msg", "Order cannot be cancled!");
			return p;
		}
		
		Optional<Goods> gop = goodsRepo.findById(order.getGoodsId());

		Goods g = gop.get();

//        EntityTransaction tran = em.getTransaction();
        try {
//        	tran.begin();
        	
        	g.setStatus(Goods.STATUS_SELLING_NOW);
        	goodsRepo.save(g);
        	order.setStatus(Order.STATUS_CANCELED);
        	//TODO money must return to buyer
        	repo.save(order);
        	
        	webSocketHander.sendSysMessage(g.getSellerId(), "The order with goods '"+g.getName()+"' is canceled by "+m.getUserName());


            p.put("success", 1);
            return p;
            
        } catch (Exception e) {
//            tran.rollback();
            p.put("success", 0);
			p.put("msg", e.getMessage());
			return p;
        }
        
	}
	
	/**
	 * invoke when user want to complete order
	 * 
	 * @param request
	 * @param orderId
	 * @return if successfully complete order
	 */
	@CrossOrigin(origins = "*", maxAge = 3600)
	@GetMapping(value="/completeOrder")
	public Properties completeOrder(
			HttpServletRequest request,
			@RequestParam("orderId") Long orderId
			) {
		Properties p = new Properties();
		HttpSession session = request.getSession();
		Object o = session.getAttribute(MyUtil.ATTR_LOGIN_NAME);
		Member m =(Member)o;
		Optional<Order> oop = repo.findById(orderId);
		if(m==null) {
			p.put("success", 0);
			p.put("msg", "User has not login!");
			return p;
		}
		if(oop.isPresent()==false || oop.get().getBuyerId().longValue()!=m.getId().longValue()) {
			p.put("success", 0);
			p.put("msg", "No such order exists or order is not yours!");
			return p;
		}
		Order order = oop.get();
		order.setStatus(Order.STATUS_COMPLETED);
		repo.save(order);
		p.put("success", 1);
    	webSocketHander.sendSysMessage(order.getSellerId(), "Your order '"+order.getOrderNo()+"' is finished by "+m.getUserName());

		return p;
	}
	
	/**
	 * search order
	 * @param request
	 * @param buyerId
	 * @param searchValue
	 * @param searchStatus
	 * @param pageNo
	 * @param pageSize
	 * @param sortBy
	 * @return page with order and goods info
	 */
	@CrossOrigin(origins = "*", maxAge = 3600)
	@GetMapping(value="/searchOrder")
	public Page<OrderWithGoods> searchOrder(HttpServletRequest request,
			@RequestParam("buyerId") Long buyerId,
			@RequestParam(value="searchValue",defaultValue="") String searchValue,
			@RequestParam(value="searchStatus",defaultValue="") String searchStatus,
			@RequestParam(value="pageNo",defaultValue="1") Integer pageNo,
			@RequestParam(value="pageSize",defaultValue="5") Integer pageSize,
			@RequestParam(value="sortBy",defaultValue="") String sortBy) {
		Page<OrderWithGoods> page = null;
		Pageable pageable = null; 
		HttpSession session = request.getSession();
		Object o = session.getAttribute(MyUtil.ATTR_LOGIN_NAME);
		Member m =(Member)o;
		if(m == null || m.getId().longValue() != buyerId.longValue()) {
			return Page.empty();
		}
		pageNo--;
		
		if(sortBy.length() == 0) {
			pageable = PageRequest.of(pageNo, pageSize);
		}else {
			pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		}
		
		if(searchValue.contentEquals("") && searchStatus.contentEquals("")) {
			page = repo.findBuyerNameOrder(buyerId, pageable);
		}else if(searchValue.contentEquals("") && searchStatus.contentEquals("notPaid")) {
			page = repo.findBuyerNameOrderNotPaid(buyerId, pageable);
		}else if(searchValue.contentEquals("") && searchStatus.contentEquals("notFinished")) {
			page = repo.findBuyerNameOrderNotFinished(buyerId, pageable);
		}else {
			page = repo.findBuyerNameOrderLikeName(buyerId, searchValue, pageable);
		}
		
		
		return page;
	}
	
	
	@CrossOrigin(origins = "*", maxAge = 3600)
	@GetMapping(value="/getOGById")
	public Optional<OrderWithGoods> getOGById(
			HttpServletRequest request,
			@RequestParam("orderId") Long orderId){
		HttpSession session = request.getSession();
		Object o = session.getAttribute(MyUtil.ATTR_LOGIN_NAME);
		Member m =(Member)o;
		Optional<OrderWithGoods> oog =  repo.findOGById(orderId);
		if(oog.isPresent() && oog.get().getOrder().getBuyerId().longValue() != m.getId().longValue()) {
			return Optional.empty();
		}else {
			return oog;
		}
		
	}
	
	@CrossOrigin(origins = "*", maxAge = 3600)
	@GetMapping(value="/getSellerOGById")
	public Optional<OrderWithGoods> getSellerOGById(
			HttpServletRequest request,
			@RequestParam("orderId") Long orderId){
		HttpSession session = request.getSession();
		Object o = session.getAttribute(MyUtil.ATTR_LOGIN_NAME);
		Member m =(Member)o;
		Optional<OrderWithGoods> oog =  repo.findOGById(orderId);
		if(oog.isPresent() && oog.get().getOrder().getSellerId().longValue() != m.getId().longValue()) {
			return Optional.empty();
		}else {
			return oog;
		}
		
	}	
	
	@CrossOrigin(origins = "*", maxAge = 3600)
	@GetMapping(value="/getOrder")
	public Properties getOrder(
			HttpServletRequest request,
			@RequestParam("orderId") Long orderId) {
		Properties p = new Properties();
		HttpSession session = request.getSession();
		Object o = session.getAttribute(MyUtil.ATTR_LOGIN_NAME);
		Member m =(Member)o;
		Optional<Order> oo= repo.findById(orderId);
		if(!oo.isPresent() || oo.get().getBuyerId().longValue() != m.getId()) {
			p.put("success", 0);
			p.put("msg", "No such order exists or order is not yours!");
			return p;
		}
		p.put("success", 1);
		p.put("price",oo.get().getOrderPrice());
		return p;
	}
	
	/**
	 * invoke when user pay order
	 * @param request
	 * @param orderId
	 * @return if successfully pay the order
	 */
	@CrossOrigin(origins = "*", maxAge = 3600)
	@GetMapping(value="/payOrder")
	public Properties payOrder(HttpServletRequest request,
			@RequestParam("orderId") Long orderId) {
		Properties p = new Properties();
		HttpSession session = request.getSession();
		Object o = session.getAttribute(MyUtil.ATTR_LOGIN_NAME);
		Member m =(Member)o;
		
		if(m==null ) {
			p.put("success", 0);
			p.put("msg", "User has not login!");
			return p;
		}
		Optional<Order> oo= repo.findById(orderId);
		if(!oo.isPresent() || oo.get().getBuyerId().longValue() != m.getId()) {
			p.put("success", 0);
			p.put("msg", "No such order exists or order is not yours!");
			return p;
		}
		else if(oo.get().getPaymentStatus() != Order.PAYMENT_NO||oo.get().getStatus()!=Order.STATUS_WAIT_COMPLETE) {
			p.put("success", 0);
			p.put("msg", "This order has payed or has canced!");
			return p;
		}
		
		Order order = oo.get();
		order.setPaymentStatus(Order.PAYMENT_YES);
		repo.save(order);
		p.put("success", 1);
		String on = oo.get().getOrderNo();
		p.put("orderNo", on);
		
    	webSocketHander.sendSysMessage(order.getSellerId(), "The order ( orderNo:'"+order.getOrderNo()+"') has been payed by "+m.getUserName());

		return p;
 	}
	
	@CrossOrigin(origins = "*", maxAge = 3600)
	@GetMapping(value="/getCountOrder")
	public Properties getCountOrder(HttpServletRequest request) {
		Properties p = new Properties();
		HttpSession session = request.getSession();
		Object o = session.getAttribute(MyUtil.ATTR_LOGIN_NAME);
		Member m =(Member)o;
		if(m !=null) {
			long n1 = repo.getCountNotPaid(m.getId());
			long n2 = repo.getCountNotFinished(m.getId());
			p.put("notPaidCount",n1);
			p.put("notFinishCount",n2);
		}
		System.out.println("getCountOrder");
		return p;
	}	
}
