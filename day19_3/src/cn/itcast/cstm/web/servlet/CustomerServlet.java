package cn.itcast.cstm.web.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.itcast.cstm.domain.Customer;
import cn.itcast.cstm.domain.PageBean;
import cn.itcast.cstm.service.CustomerService;
import cn.itcast.servlet.BaseServlet;

/**
 * Web层
 * @author cxf
 *
 */
public class CustomerServlet extends BaseServlet {
	private CustomerService customerService = new CustomerService();
	
	/**
	 * 添加客户
	 */
	public String add(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*
		 * 1. 封装表单数据到Customer对象
		 * 2. 补全：cid，使用uuid
		 * 3. 使用service方法完成添加工作
		 * 4. 向request域中保存成功信息
		 * 5. 转发到msg.jsp
		 */
		Customer c = CommonUtils.toBean(request.getParameterMap(), Customer.class);
		c.setCid(CommonUtils.uuid());
		customerService.add(c);
		request.setAttribute("msg", "恭喜，添加客户成功！");
		return "f:/msg.jsp";
	}
	
	/**
	 * 查询所有
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
//	public String findAll(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//		/*
//		 * 1. 调用service得到所有客户
//		 * 2. 保存到request域
//		 * 3. 转发到list.jsp
//		 */
//		request.setAttribute("cstmList", customerService.findAll());
//		return "f:/list.jsp";
//	}
	
	public String findAll(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*
		 * 1. 获取页面传递的pc
		 * 2. 给定ps的值
		 * 3. 使用pc和ps调用service方法，得到PageBean，保存到request域
		 * 4. 转发到list.jsp
		 */
		int pc = getPc(request);//得到pc
		int ps = 10;//给定ps的值，第页10行记录
		PageBean<Customer> pb = customerService.findAll(pc, ps);//传递pc, ps给Service，得到PageBean
		
		// 设置url
		pb.setUrl(getUrl(request));
		
		request.setAttribute("pb", pb);//保存到request域中
		return "f:/list.jsp";//转发到list.jsp
	}
	
	/**
	 * 获取pc
	 * @param request
	 * @return
	 */
	private int getPc(HttpServletRequest request) {
		/*
		 * 1. 得到pc
		 *   如果pc参数不存在，说明pc=1
		 *   如果pc参数存在，需要转换成int类型即可
		 */
		String value = request.getParameter("pc");
		if(value == null || value.trim().isEmpty()) {
			return 1;
		}
		return Integer.parseInt(value);
	}
	
	/**
	 * 编辑之前的加载工作
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String preEdit(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*
		 * 1. 获取cid
		 * 2. 使用cid来调用service方法，得到Customer对象
		 * 3. 把Customer保存到request域中
		 * 4. 转发到edit.jsp显示在表单中
		 */
		String cid = request.getParameter("cid");
		Customer cstm = customerService.load(cid);
		request.setAttribute("cstm", cstm);
		return "f:/edit.jsp";
	}
	
	/**
	 * 编辑方法
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String edit(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*
		 * 1. 封装表单数据到Customer对象中
		 * 2. 调用service方法完成修改
		 * 3. 保存成功信息到request域
		 * 4. 转发到msg.jsp显示成功信息
		 */
		// 已经封装了cid到Customer对象中
		Customer c = CommonUtils.toBean(request.getParameterMap(), Customer.class);
		customerService.edit(c);
		request.setAttribute("msg", "恭喜，编辑客户成功！");
		return "f:/msg.jsp";
	}
	
//	public String query(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//		/*
//		 * 1. 封装表单数据到Customer对象中，它只有四个属性（cname、gender、cellphone、email）
//		 *   它就是一个条件
//		 * 2. 使用Customer调用service方法，得到List<Customer>
//		 * 3. 保存到request域中
//		 * 4. 转发到list.jsp
//		 */
//		Customer criteria = CommonUtils.toBean(request.getParameterMap(), Customer.class);
//		List<Customer> cstmList = customerService.query(criteria);
//		request.setAttribute("cstmList", cstmList);
//		return "/list.jsp";
//	}
	
	public String query(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
//		System.out.println(getUrl(request));
		/*
		 * 0. 把条件封装到Customer对象中
		 * 1. 得到pc
		 * 2. 给定ps
		 * 3. 使用pc和ps，以及条件对象，调用service方法得到PageBean
		 * 4. 把PageBean保存到request域中
		 * 5. 转发到list.jsp
		 */
		// 获取查询条件
		Customer criteria = CommonUtils.toBean(request.getParameterMap(), Customer.class);
		
		/*
		 * 处理GET请求方式编码问题！
		 */
		criteria = encoding(criteria);
		
		int pc = getPc(request);//得到pc
		int ps = 10;//给定ps的值，第页10行记录
		PageBean<Customer> pb = customerService.query(criteria, pc, ps);
		
		// 得到url，保存到pb中
		pb.setUrl(getUrl(request));
		
		request.setAttribute("pb", pb);
		return "f:/list.jsp";
	}
	
	/**
	 * 处理四样
	 * @param criteria
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	private Customer encoding(Customer criteria) throws UnsupportedEncodingException {
		String cname = criteria.getCname();
		String gender = criteria.getGender();
		String cellphone = criteria.getCellphone();
		String email = criteria.getEmail();
		
		if(cname != null && !cname.trim().isEmpty()) {
			cname = new String(cname.getBytes("ISO-8859-1"), "utf-8");
			criteria.setCname(cname);
		}
		
		if(gender != null && !gender.trim().isEmpty()) {
			gender = new String(gender.getBytes("ISO-8859-1"), "utf-8");
			criteria.setGender(gender);
		}
		
		if(cellphone != null && !cellphone.trim().isEmpty()) {
			cellphone = new String(cellphone.getBytes("ISO-8859-1"), "utf-8");
			criteria.setCellphone(cellphone);
		}
		
		if(email != null && !email.trim().isEmpty()) {
			email = new String(email.getBytes("ISO-8859-1"), "utf-8");
			criteria.setEmail(email);
		}
		return criteria;
	}

	/**
	 * 截取url
	 *   /项目名/Servlet路径?参数字符串
	 * @param request
	 * @return
	 */
	private String getUrl(HttpServletRequest request) {
		String contextPath = request.getContextPath();//获取项目名
		String servletPath = request.getServletPath();//获取servletPath，即/CustomerServlet
		String queryString = request.getQueryString();//获取问号之后的参数部份
		
		//  判断参数部份中是否包含pc这个参数，如果包含，需要截取下去，不要这一部份。
		if(queryString.contains("&pc=")) {
			int index = queryString.lastIndexOf("&pc=");
			queryString = queryString.substring(0, index);
		}
		
		return contextPath + servletPath + "?" + queryString;
	}
}
