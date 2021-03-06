package com.yibee;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;

import com.yibee.entity.Member;
import com.yibee.websocket.MyWebSocketHandler;

import net.sf.json.JSONObject;

@RestController
@RequestMapping("/member")
public class MemberController {
	@Autowired
	private MyWebSocketHandler webSocketHander;
	
	@Resource
	private MemberRepository repo;
	
	@PersistenceContext
    private EntityManager entityManager;

	/*
	@RequestMapping(value = "/upIcon")
	@CrossOrigin(origins = "*", maxAge = 3600)
	public Properties upIcon(HttpServletRequest request,HttpServletResponse response,@RequestParam("id") int id) throws IOException, ServletException {
		HttpSession session = request.getSession();
		Object o = session.getAttribute(MyUtil.ATTR_LOGIN_NAME);
		Member m =(Member)o;
		if(m == null || m.getId() != id) {
			response.setStatus(606);
			return null;
		}
		
		Properties p = new Properties();
		p.put("ID", id);
		Part part;
		Properties pp;
		try {
			part = request.getPart("photo");
			pp = MyUtil.getConfProperties();
			String savePath = pp.getProperty("member_icon.dir");
			int folderName = (int)Math.floor(id/1000);
			String save2Path = savePath + ("/"+folderName); 
			File fileSaveDir = new File(save2Path);
	        if (!fileSaveDir.exists()) {
	            fileSaveDir.mkdir();
	        }
			part.write(save2Path + File.separator + id+".jpg");
			MyUtil.manageImage(160,save2Path + File.separator + id+".jpg",save2Path + File.separator + id+"_2.jpg");
			MyUtil.manageImage(64,save2Path + File.separator + id+".jpg",save2Path + File.separator + id+"_1.jpg");
			MyUtil.manageImage(32,save2Path + File.separator + id+".jpg",save2Path + File.separator + id+"_0.jpg");
			p.put("msg", 1);
		} catch (IOException | ServletException e) {
			p.put("msg", 0);
			e.printStackTrace();
		}
		return p;
	}
	*/
	
	/**
	 * upload member icon
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value = "/edit/upIcon")
	@CrossOrigin(origins = "*", maxAge = 3600)
	public Properties upIcon2(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
		HttpSession session = request.getSession();
		Object o = session.getAttribute(MyUtil.ATTR_LOGIN_NAME);
		Member m =(Member)o;
		String sid = request.getParameter("id");
		Long id = Long.parseLong(sid);
		Properties p = new Properties();
		if(m == null || m.getId().longValue() != id.longValue()) {
			response.setStatus(606);
			return null;
		}
		
		
		p.put("ID", id);
		Part part;
		Properties pp;
		try {
			part = (Part) request.getParts().toArray()[0];
			pp = MyUtil.getConfProperties();
			String savePath = pp.getProperty("member_icon.dir");
			int folderName = (int)Math.floor(id/1000);
			String save2Path = savePath + ("/"+folderName); 
			File fileSaveDir = new File(save2Path);
	        if (!fileSaveDir.exists()) {
	            fileSaveDir.mkdir();
	        }
			part.write(save2Path + File.separator + id+".jpg");
//			MyUtil.resizeImage(160,save2Path + File.separator + id+".jpg",save2Path + File.separator + id+"_2.jpg");
//			MyUtil.resizeImage(64,save2Path + File.separator + id+".jpg",save2Path + File.separator + id+"_1.jpg");
//			MyUtil.resizeImage(32,save2Path + File.separator + id+".jpg",save2Path + File.separator + id+"_0.jpg");

			MyUtil.resizeCenterRegionImage(new Rectangle(0,0,160,160),save2Path + File.separator + id+".jpg",save2Path + File.separator + id+"_2.jpg");
			MyUtil.resizeCenterRegionImage(new Rectangle(0,0,64,64),save2Path + File.separator + id+".jpg",save2Path + File.separator + id+"_1.jpg");
			MyUtil.resizeCenterRegionImage(new Rectangle(0,0,32,32),save2Path + File.separator + id+".jpg",save2Path + File.separator + id+"_0.jpg");
			
			p.put("msg", 1);
			//tell client icon has updated
			JSONObject jsonObject = new JSONObject();
        	jsonObject.put("flag","icon_update");
            TextMessage tm = new TextMessage(jsonObject.toString());
			webSocketHander.sendMessage(""+id, tm);
			
		} catch (IOException | ServletException e) {
			p.put("msg", 0);
			e.printStackTrace();
		}
		return p;
	}	
	
	/*
	@CrossOrigin(origins = "*", maxAge = 3600)
	@GetMapping(value="/editicon")
	public Properties editIcon(HttpServletRequest request,@RequestParam("id") int id) {
		//JSONObject result = new JSONObject();
		Properties p = new Properties();
		p.put("ID", id);
		//result.put("ID", id);
		Part part;
		Properties pp;
		try {
			part = request.getPart("photo");
			pp = MyUtil.getConfProperties();
			String savePath = pp.getProperty("member_icon.dir");
			int folderName = (int)Math.floor(id/1000);
			String save2Path = savePath + ("/"+folderName); 
			File fileSaveDir = new File(save2Path);
	        if (!fileSaveDir.exists()) {
	            fileSaveDir.mkdir();
	        }
			part.write(save2Path + File.separator + id+".jpg");
			MyUtil.manageImage(160,save2Path + File.separator + id+".jpg",save2Path + File.separator + id+"_2.jpg");
			MyUtil.manageImage(64,save2Path + File.separator + id+".jpg",save2Path + File.separator + id+"_1.jpg");
			MyUtil.manageImage(32,save2Path + File.separator + id+".jpg",save2Path + File.separator + id+"_0.jpg");
			p.put("msg", 1);
		} catch (IOException | ServletException e) {
			p.put("msg", 0);
			e.printStackTrace();
		}
		return p;
	}
	*/
	
	
	/**
	 * 
	 * @return member icon source
	 */
	@CrossOrigin(origins = "*", maxAge = 3600)
	@GetMapping(value="/geticon")
	public ResponseEntity<FileSystemResource> getIconById(HttpServletResponse response,@RequestParam("Id") int Id,@RequestParam("size") int size) {
		//String savePath = "/Users/liushichao/desktop/member_icon";
		Properties pp;
		try {
			pp = MyUtil.getConfProperties();
			String savePath = pp.getProperty("member_icon.dir");
			int folderName = (int)Math.floor(Id/1000);
			String save2Path = savePath + ("/"+folderName); 
			String absolutePath = save2Path+"/"+Id+"_"+size+".jpg";
			File file = new File(absolutePath);
			if(!file.exists()) {
				file = new File(savePath+"/default"+"_"+size+".jpg");
			}
			HttpHeaders headers = new HttpHeaders();
		    headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		   //浏览器自动下载
		   //headers.add("Content-Disposition", "attachment; filename=ttt.jpg");
		   //缓存
		    headers.add("Pragma", "no-cache");
		    headers.add("Expires", "0");
		    headers.add("Last-Modified", new Date().toString()); 
		    headers.add("ETag", String.valueOf(System.currentTimeMillis()));
		    return ResponseEntity
		      .ok()
		      .headers(headers)
		      .contentLength(file.length())
		      .contentType(MediaType.IMAGE_JPEG)
		      .body(new FileSystemResource(file));


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.status(602).build();


	}
	
	
	
	@CrossOrigin(origins = "*")
	@GetMapping(value="/checkUsername")
	public Properties checkUsername(HttpServletResponse response,@RequestParam("userName") String userName) throws IOException {
		Properties p = new Properties();
		Optional op = repo.findByUserName(userName);
		if(!op.isPresent()) {
			p.put("success", 1);
		}
		else {
			p.put("success",0);
			p.put("msg","Username has been used,please try another.");
		}
		return p;
	}
	
	
	
	@CrossOrigin(origins = "*")
	@GetMapping(value="/checkEmail")
	public Properties checkEmail(HttpServletResponse response,@RequestParam("email") String email)  {
		Properties p = new Properties();
		int om = repo.findUniqueEmail(email);
		if(om == 0) {
			p.put("success", 1);
		}
		else {
			p.put("success",0);
			p.put("msg","This email address has been used,please try another.");
		}
		return p;
	} 
	
	/**
	 * When reset password, this function is used for checking if there is an account 
	 * with this specific email and send out reset link
	 * 
	 * @param email
	 * @return
	 */
	@CrossOrigin(origins = "*")
	@PostMapping(value="/beginReset")
	public Properties beginReset(@RequestParam("email") String email) {
		
		Properties p = new Properties();
		int count = repo.findUniqueEmail(email);
		if(count == 0) {
			p.put("success","0");
			p.put("msg","Member of input email is not found!");
			return p;
		}
		//else count == 1
		Member m = repo.findMemberByEmail(email);
		int resetcode = (int)((Math.random()*9+1)*100000);  //generate random reset code
		String codestring = Integer.toString(resetcode);
		String userName = m.getUserName();
		
		Properties pp;
		try {
			pp = MyUtil.getConfProperties();
			String url = pp.getProperty("reset.url")+"userName="+userName+"&resetcode="+codestring;
			System.out.println(url);
			MailSendObj send = new MailSendObj();
			send.sendOut("shichaostats@outlook.com", "Yibee", email, "you", "Password Reset", "Hi! Please reset your password here: "+url +
					". If you haven't request that, please ignore this email.");
			m.setResetCode(codestring);
		} catch (IOException e
				) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		repo.save(m);
		p.put("success","1");
		return p;
	}
	
	/**
	 * Reset Password with account
	 * 
	 * @param userName
	 * @param resetcode
	 * @param passWord
	 * @return
	 */
	@CrossOrigin(origins = "*")
	@PostMapping(value="/resetPassword")
	public Properties resetPassword(@RequestParam("userName") String userName,
			@RequestParam("resetcode") String resetcode,
			@RequestParam("passWord") String passWord) {
		Properties p = new Properties();
		Optional<Member> om = repo.findByUserName(userName);
		if(!om.isPresent()) {
			p.put("success", 0);
			p.put("msg", "The account does not exist, please register first.");
			return p;
		}
		Member m = om.get();
		if(!m.getResetCode().contentEquals(resetcode)) {
			p.put("success", 0);
			p.put("msg", "Wrong code! You do not have the right to reset.");
			return p;
		}
		passWord = MyUtil.encrypt(passWord);
		m.setPassWord(passWord);
		m.setResetCode(null);
		repo.save(m);
		p.put("success", 1);
		return p;
		
	}
	

	/**
	 * member register 
	 * 
	 * @param response
	 * @param userName
	 * @param passWord
	 * @param email
	 * @param schoolCode
	 * @return
	 * @throws IOException
	 */
	@CrossOrigin(origins = "*")
	@PostMapping(value="/register")
	public Member memberRegister(HttpServletResponse response,@RequestParam("userName") String userName,
			@RequestParam("passWord") String passWord,@RequestParam("email") String email,
			@RequestParam("schoolCode") String schoolCode) throws IOException {
		
		Member m = new Member();
		Long mid = repo.getMaxId();
		//id<100 is system user
		Long id = mid==null?100L:mid + 1;
		Date registerDate = new Date(); 
		m.setId(id);
		m.setSellDisabled(1);
		m.setEmail(email);
		m.setRegisterDate(registerDate);
		m.setUserName(userName);
		passWord = MyUtil.encrypt(passWord);
		m.setPassWord(passWord);
		m.setSchoolCode(schoolCode);
		//response.sendError(601, "wrong!");
		int actcode = (int)((Math.random()*9+1)*100000);  //generate random activate code
		String codestring = Integer.toString(actcode);
		Properties pp;
		try {
			pp = MyUtil.getConfProperties();
			String url = pp.getProperty("active.url")+"userName="+userName+"&actcode="+codestring;
			System.out.println(url);
			MailSendObj send = new MailSendObj();
			send.sendOut("shichaostats@outlook.com", "Website", email, "you", "Website Activation", "activate your account here: "+url);
			m.setActived(-actcode);
		} catch (IOException e
				) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Member m2 = repo.save(m);
			return m2;
		}catch(Exception e){
			//String err = e.getMessage();
			//response.sendError(601, err);
			e.printStackTrace();
			response.setStatus(601);
			return null;
		}
		
		
	}
	
	/**
	 * activate member account
	 * 
	 * @param userName
	 * @param actcode
	 * @return 
	 */
	@CrossOrigin(origins = "*", maxAge = 3600)
	@GetMapping(value="/active")
	public Properties memberActive(@RequestParam("userName") String userName, 
			@RequestParam("actcode") int actcode) {
		Properties p = new Properties();
		Optional<Member> om = repo.findByUserName(userName);
		if(!om.isPresent()) {
			p.put("success", 0);
			p.put("msg", "The account does not exist, please register first to get activated.");
			return p;
		}
		Member m = om.get();
		int count = repo.findUniqueEmail(m.getEmail()); //same email ,already activated
		if(count > 0) {
			p.put("success", 0);
			p.put("msg","You cannot active two account with same email, please use another email to register!");
			return p;
		}
		if(m.getActived() == 1) {
			p.put("success", 0);
			p.put("msg","You have already activated your account!");
			return p;
		}
		if(actcode != -m.getActived()) {
			p.put("success", 0);
			p.put("msg","The activate code is wrong!");
			return p;
		}
		m.setActived(1);
		repo.save(m);
		p.put("success", 1);
		p.put("msg",userName);
		webSocketHander.sendSysMessage(m.getId(), "Active success! Wellcome to Yibee Second Hands website.");
		return p;
	}
	
	
	@PostMapping(value="/add")
	public Member memberAdd(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="actived",defaultValue="0") int actived,
			@RequestParam(value="disabled",defaultValue="0") int disabled,
			@RequestParam(value="sellDisabled",defaultValue="0") int sellDisabled,
			@RequestParam("email") String email,
			@RequestParam("firstName") String firstName,
			@RequestParam("lastName") String lastName,
			@RequestParam("gender") int gender,
			@RequestParam("userName") String userName,
			@RequestParam("passWord") String passWord,
			@RequestParam("schoolCode") String schoolCode,
			@RequestParam("birthday") String birthday,
			@RequestParam("phoneNumber") String phoneNumber
			){
		SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");

		Date date;
		Calendar calendar = null;
		try {
			date = sdf.parse(birthday);
			calendar = Calendar.getInstance();
			calendar.setTime(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
		Member m = new Member();
		Long mid = repo.getMaxId();
		Long id = mid==null?1L:mid + 1;
		Date registerDate = new Date(); 
		m.setId(id);
		m.setActived(actived);
		m.setDisabled(disabled);
		m.setSellDisabled(sellDisabled);
		m.setEmail(email);
		
		m.setFirstName(firstName);
		m.setLastName(lastName);
		m.setGender(gender);
		m.setRegisterDate(registerDate);
		m.setUserName(userName);
		passWord = MyUtil.encrypt(passWord);
		m.setPassWord(passWord);
		
		m.setSchoolCode(schoolCode);
		m.setBirthday(calendar);
		
		m.setPhoneNumber(phoneNumber);
		
		Part part;
		try {
			part = request.getPart("upfile");
			String fileName = part.getSubmittedFileName();
			fileName = new File(fileName).getName();
			String savePath = "/Users/liushichao/desktop/member_icon";
			int folderName = (int)Math.floor(id/1000);
			savePath += ("/"+folderName);
			File fileSaveDir = new File(savePath);
	        if (!fileSaveDir.exists()) {
	            fileSaveDir.mkdir();
	        }
			part.write(savePath + File.separator + id+".jpg");
			MyUtil.resizeImage(160,savePath + File.separator + id+".jpg",savePath + File.separator + id+"_l.jpg");
			MyUtil.resizeImage(64,savePath + File.separator + id+".jpg",savePath + File.separator + id+"_m.jpg");
			MyUtil.resizeImage(32,savePath + File.separator + id+".jpg",savePath + File.separator + id+"_s.jpg");
			
		} catch (IOException | ServletException e) {
			e.printStackTrace();
		}
		int actcode = (int)((Math.random()*9+1)*100000);
		String codestring = Integer.toString(actcode);
		Properties pp;
		try {
			pp = MyUtil.getConfProperties();
			String url = pp.getProperty("active.url")+"?userName="+userName+"&actcode="+codestring;
			System.out.println(url);
			MailSendObj send = new MailSendObj();
			send.sendOut("shichaostats@outlook.com", "Website", email, "you", "Website Activation", "activate your account here: "+url);
			m.setActived(-actcode);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		Member m2 = repo.save(m);
		
		return m2;
	}
	
	
	//查询所有数据
	@GetMapping("/showAll")
	//@CrossOrigin(origins = "*", maxAge = 3600)
	public Iterable<Member> showAll(){
		Iterable<Member> list = repo.findAll();
		//this.detachTeacher(list);
		//return null;
		return list;
		//return teacherRepo.findAll();
	}

	/*
	@GetMapping("/getMaxId")
	public Long getMaxId() {
		return repo.getMaxId();
	}
	*/
	@GetMapping("/getMaxId")
	@CrossOrigin(origins = "*", maxAge = 3600)
	 public Long getMaxId() {
	  Properties p = new Properties();
	     ClassLoader cl = this.getClass().getClassLoader();
	     InputStream in = cl.getResourceAsStream("my.properties");
	     try {
	   p.load(in);
	   in.close();
	  } catch (IOException e) {
	   // TODO Auto-generated catch block
	   e.printStackTrace();
	  }
	     
	     
	  return repo.getMaxId();
	}

	
	@GetMapping("/getMemberById")
	@CrossOrigin(origins = "*", maxAge = 3600)
	 public Optional<Member> getMemberById(@RequestParam("id") Long id) {
		  return repo.findById(id);
	}	
	
	
	@PostMapping(value = "/login")
	@CrossOrigin(origins = "*", maxAge = 3600)
    public Properties login(HttpServletRequest request,
    		HttpServletResponse response,
    		@RequestParam(value="userName",defaultValue="") String userName,
    		@RequestParam(value="passWord",defaultValue="") String passWord
    		) throws IOException, ServletException {
		
		Properties p = new Properties();
		HttpSession session = request.getSession();
		Optional<Member> op = repo.findByUserName(userName);
		
//		String url = request.getHeader("Origin");  
//		if(url !=null && url.length()>0) {
//			response.addHeader("Access-Control-Allow-Origin", url);                
//			response.addHeader("Access-Control-Allow-Credentials", "true");     
//		}
		
		System.out.println("Login username="+userName);
		
		if(op.isPresent()) {
			Member m = op.get();
			if(m.getActived()<=0) {
				p.put("success", 0);
				p.put("msg", "Login Failed. User is not actived.");
				return p;
			}
			else if(m.getDisabled()==1) {
				p.put("success", 0);
				p.put("msg", "Login Failed. User is disabled.");
				return p;
			}
			String enpass = MyUtil.encrypt(passWord);
			System.out.println("Login enpass="+enpass);
			System.out.println("Login m.getPassWord()="+m.getPassWord());
			if(m.getPassWord().contentEquals(enpass)) {
				this.entityManager.detach(m);
				session.setAttribute(MyUtil.ATTR_LOGIN_NAME, m);
				session.setAttribute(MyUtil.ATTR_LAST_USERID, m.getId());
				p.put("success", 1);
				p.put("member",m);
				//cookie
				Cookie c = new Cookie("userId", ""+m.getId());
				Cookie c2 = new Cookie("username",m.getUserName());
				c.setPath("/");
				c2.setPath("/");
				
				response.addCookie(c);//添加到response中
				response.addCookie(c2);
				  
			    Cookie coo[] = request.getCookies();//获取request中cookie集合
				  //循环遍历
			    if(coo != null) {
			    
				    for (Cookie co : coo) {
					   System.out.println("Cookie:  "+co.getName() + "=" + co.getValue());
					}
			    }
//			    webSocketHander.batchSendMessage( new TextMessage("{\"flag\":\"login\"}"));
				return p;
			}
		}
		
		
		p.put("success", 0);
		p.put("msg", "Username or password is wrong.");
		return p;
    }		
	
	@PostMapping(value = "/logout")
	//@CrossOrigin(origins = "*", maxAge = 3600)
    public Properties login(HttpServletRequest request,HttpServletResponse response,    		
    		@RequestParam(value="id",defaultValue="0") long id) throws IOException, ServletException {
		
		Properties p = new Properties();
		HttpSession session = request.getSession();
		//Optional<Member> op = repo.findByUserName(userName);
		Member m = (Member)session.getAttribute(MyUtil.ATTR_LOGIN_NAME);
		
		String url = request.getHeader("Origin");  
		if(url !=null && url.length()>0) {
			//response.addHeader("Access-Control-Allow-Origin", url);                
			//response.addHeader("Access-Control-Allow-Credentials", "true");     
			Cookie c = new Cookie("userId", "");
			Cookie c2 = new Cookie("username","");
			c.setPath("/");
			c2.setPath("/");
			
			response.addCookie(c);//添加到response中
			response.addCookie(c2);
		}
		//System.out.println("Login username="+userName);
		
		if(m == null || m.getId().longValue() == id) {
			session.removeAttribute(MyUtil.ATTR_LOGIN_NAME);
			
			session.removeAttribute(MyUtil.ATTR_LAST_USERID);
			
			p.put("success", 1);
			//p.put("member",m);
			return p;
			
		}
		
		
		p.put("success", 0);
		p.put("msg", "Username is wrong.");
		return p;
    }			
}
