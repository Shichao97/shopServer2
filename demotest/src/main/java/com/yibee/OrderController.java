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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

@RestController
@RequestMapping("/order")
public class OrderController {
	@Resource
	private OrderRepository repo;
	@Resource
	private GoodsRepository goodsRepo;
	@Resource
	private MemberRepository memberRepo;
	@PersistenceContext
    private EntityManager em;
	
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
        	order.setId(maxId+1);
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
        	

            p.put("success", maxId+1);
            p.put("orderNo",orderNo);
            return p;

        } catch (Exception e) {
//            tran.rollback();
            p.put("success", 0);
			p.put("msg", e.getMessage());
			return p;
        }
        
	}
	
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
        	
//            tran.commit();

            p.put("success", 1);
            return p;
            
        } catch (Exception e) {
//            tran.rollback();
            p.put("success", 0);
			p.put("msg", e.getMessage());
			return p;
        }
        
	}
	
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
		return p;
	}
	
	
	@CrossOrigin(origins = "*", maxAge = 3600)
	@GetMapping(value="/searchOrder")
	public Page<NameOrder> searchOrder(HttpServletRequest request,
			@RequestParam("buyerId") Long buyerId,
			@RequestParam(value="searchValue",defaultValue="") String searchValue,
			@RequestParam(value="searchStatus",defaultValue="") String searchStatus,
			@RequestParam(value="pageNo",defaultValue="0") Integer pageNo,
			@RequestParam(value="pageSize",defaultValue="8") Integer pageSize,
			@RequestParam(value="sortBy",defaultValue="") String sortBy) {
		Page<NameOrder> page = null;
		Pageable pageable = null; 
		HttpSession session = request.getSession();
		Object o = session.getAttribute(MyUtil.ATTR_LOGIN_NAME);
		Member m =(Member)o;
		if(m == null || m.getId() != buyerId) {
			return Page.empty();
		}
		
		
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
	@GetMapping(value="/payOrder")
	public Properties payOrder(HttpServletRequest request,
			@RequestParam("orderID") Long orderId) {
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
		
		Order order = oo.get();
		order.setPaymentStatus(Order.PAYMENT_YES);
		repo.save(order);
		p.put("success", 1);
		return p;
 	}
	
	
}
