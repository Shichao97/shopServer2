package com.yibee;

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
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yibee.entity.Goods;
import com.yibee.entity.Member;
import com.yibee.entity.Order;

@RestController
@RequestMapping("/order")
public class OrderController {
	@Resource
	private OrderRepository repo;
	private GoodsRepository goodsRepo;
	private MemberRepository memberRepo;
	@PersistenceContext
    private EntityManager em;
	
	@CrossOrigin(origins = "*", maxAge = 3600)
	@GetMapping(value="/placeOrder")
	public Properties placeOrder(
			HttpServletRequest request,
			@RequestParam("buyerId") Long buyerId,
			@RequestParam("goodsId") Long goodsId,
			@RequestParam("receiveAddr") String receiveAddr
			) {
		Properties p = new Properties();
		HttpSession session = request.getSession();
		Object o = session.getAttribute(MyUtil.ATTR_LOGIN_NAME);
		Member m =(Member)o;
		
		if(m==null || m.getId() != buyerId) {
			p.put("success", 0);
			p.put("msg", "UserId is empty or wrong!");
			return p;
		}
		Optional<Goods> gop = goodsRepo.findById(goodsId);
		if(gop.isPresent() == false || buyerId==gop.get().getSellerId() || gop.get().getStatus()!=Goods.STATUS_SALLING_NOW) {
			p.put("success", 0);
			p.put("msg", "goods is empty or wrong!");
			return p;
		}
		
		Goods g = gop.get();
		Optional<Member> mop = memberRepo.findById(g.getSellerId());
		Member seller = mop.get();
        EntityTransaction tran = em.getTransaction();
        try {
        	tran.begin();
        	
        	g.setStatus(Goods.STATUS_SELLED_OUT);
        	goodsRepo.save(g);
        	Order order = new Order();
        	order.setId(0L);
        	order.setBuyerId(buyerId);
        	order.setBuyerName(m.getUserName());
        	order.setGoodsId(goodsId);
        	order.setPaymentStatus(Order.PAYMENT_NO);
        	order.setStatus(Order.STATUS_WAIT_COMPLETE);
        	order.setOrderPrice(g.getPrice());
        	order.setReceiveAddr(receiveAddr);
        	order.setSellerId(g.getSellerId());
        	order.setSellerName(seller.getUserName());
        	repo.save(order);
        	
            tran.commit();
            p.put("success", 1);
            return p;

        } catch (Exception e) {
            tran.rollback();
            p.put("success", 0);
			p.put("msg", e.getMessage());
			return p;
        }
        
	}
	
	
	@CrossOrigin(origins = "*", maxAge = 3600)
	@GetMapping(value="/cancelOrder")
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
		
		if(oop.isPresent()==false || oop.get().getBuyerId()!=m.getId()) {
			p.put("success", 0);
			p.put("msg", "Has no order or order is not yours!");
			return p;
		}
		Order order = oop.get();
		if(order.getStatus()!=Order.STATUS_WAIT_COMPLETE) {
			p.put("success", 0);
			p.put("msg", "Order has completed or canced!");
			return p;
		}
		
		Optional<Goods> gop = goodsRepo.findById(order.getGoodsId());

		Goods g = gop.get();

        EntityTransaction tran = em.getTransaction();
        try {
        	tran.begin();
        	
        	g.setStatus(Goods.STATUS_SALLING_NOW);
        	goodsRepo.save(g);
        	order.setStatus(Order.STATUS_CANCELED);
        	//TODO money must return to buyer
        	repo.save(order);
        	
            tran.commit();

            p.put("success", 1);
            return p;
            
        } catch (Exception e) {
            tran.rollback();
            p.put("success", 0);
			p.put("msg", e.getMessage());
			return p;
        }
        
	}
	
	
}
