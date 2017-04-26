package cn.itcast.cstm.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.cstm.domain.Customer;
import cn.itcast.cstm.domain.PageBean;
import cn.itcast.jdbc.TxQueryRunner;

/**
 * 持久层
 * 
 * @author cxf
 * 
 */
public class CustomerDao {
	private QueryRunner qr = new TxQueryRunner();

	/**
	 * 添加客户
	 * 
	 * @param c
	 */
	public void add(Customer c) {
		try {
			String sql = "insert into t_customer values(?,?,?,?,?,?,?)";
			Object[] params = { c.getCid(), c.getCname(), c.getGender(),
					c.getBirthday(), c.getCellphone(), c.getEmail(),
					c.getDescription()};
			qr.update(sql, params);
		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 查询所有
	 * @return
	 */
	public PageBean<Customer> findAll(int pc, int ps) {
		try {
			/*
			 * 1. 他都PageBean对象pb
			 * 2. 设置pb的pc和ps
			 * 3. 得到tr，设置给pb
			 * 4. 得到beanList，设置给pb
			 * 5. 返回pb
			 */
			PageBean<Customer> pb = new PageBean<Customer>();
			pb.setPc(pc);
			pb.setPs(ps);
			/*
			 * 得到tr
			 */
			String sql = "select count(*) from t_customer";
			Number num = (Number)qr.query(sql, new ScalarHandler());
			int tr = num.intValue();
			pb.setTr(tr);
			/*
			 * 得到beanList
			 */
			sql = "select * from t_customer order by cname limit ?,?";
			List<Customer> beanList = qr.query(sql, 
					new BeanListHandler<Customer>(Customer.class), 
					(pc-1)*ps, ps);
			pb.setBeanList(beanList);
			return pb;
		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 加载客户
	 * @param cid
	 * @return
	 */
	public Customer load(String cid) {
		try {
			String sql = "select * from t_customer where cid=?";
			return qr.query(sql, new BeanHandler<Customer>(Customer.class), cid);
		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 编辑客户
	 * @param c
	 */
	public void edit(Customer c) {
		try {
			String sql = "update t_customer set cname=?,gender=?,birthday=?," +
					"cellphone=?,email=?,description=? where cid=?";
			Object[] params = {c.getCname(), c.getGender(),
					c.getBirthday(), c.getCellphone(), c.getEmail(),
					c.getDescription(), c.getCid()};
			qr.update(sql, params);
		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 多条件组合查询
	 * @param criteria
	 * @return
	 */
//	public List<Customer> query(Customer criteria) {
//		try {
//			/*
//			 * 1. 给出sql模板
//			 * 2. 给出参数
//			 * 3. 调用query方法，使用结果集处理器：BeanListHandler
//			 */
//			/*
//			 * 一、　给出sql模板
//			 * 二、　给出参数！
//			 */
//			/*
//			 * 1. 给出一个sql语句前半部
//			 */
//			StringBuilder sql = new StringBuilder("select * from t_customer where 1=1");
//			/*
//			 * 2. 判断条件，完成向sql中追加where子句
//			 */
//			/*
//			 * 3. 创建一个ArrayList，用来装载参数值
//			 */
//			List<Object> params = new ArrayList<Object>();
//			String cname = criteria.getCname();
//			if(cname != null && !cname.trim().isEmpty()) {
//				sql.append(" and cname like ?");
//				params.add("%" + cname + "%");
//			}
//			
//			String gender = criteria.getGender();
//			if(gender != null && !gender.trim().isEmpty()) {
//				sql.append(" and gender=?");
//				params.add(gender);
//			}
//			
//			String cellphone = criteria.getCellphone();
//			if(cellphone != null && !cellphone.trim().isEmpty()) {
//				sql.append(" and cellphone like ?");
//				params.add("%" + cellphone + "%");
//			}
//			
//			String email = criteria.getEmail();
//			if(email != null && !email.trim().isEmpty()) {
//				sql.append(" and email like ?");
//				params.add("%" + email + "%");
//			}
//			
//			/*
//			 * 三、执行query
//			 */
//			return qr.query(sql.toString(), 
//					new BeanListHandler<Customer>(Customer.class), 
//					params.toArray());
//		} catch(SQLException e) {
//			throw new RuntimeException(e);
//		}
//	}
	
	public PageBean<Customer> query(Customer criteria, int pc, int ps) {
		try {
			/*
			 * 1. 创建PageBean对象　
			 * 2. 设置已有的属性，pc和ps
			 * 3. 得到tr
			 * 4. 得到beanList
			 */
			/*
			 * 创建pb，设置已有属性
			 */
			PageBean<Customer> pb = new PageBean<Customer>();
			pb.setPc(pc);
			pb.setPs(ps);
			
			/*
			 * 得到tr
			 */
			
			/*
			 * 1. 给出一个sql语句前半部
			 */
			StringBuilder cntSql = new StringBuilder("select count(*) from t_customer");
			StringBuilder whereSql = new StringBuilder(" where 1=1");
			/*
			 * 2. 判断条件，完成向sql中追加where子句
			 */
			/*
			 * 3. 创建一个ArrayList，用来装载参数值
			 */
			List<Object> params = new ArrayList<Object>();
			String cname = criteria.getCname();
			if(cname != null && !cname.trim().isEmpty()) {
				whereSql.append(" and cname like ?");
				params.add("%" + cname + "%");
			}
			
			String gender = criteria.getGender();
			if(gender != null && !gender.trim().isEmpty()) {
				whereSql.append(" and gender=?");
				params.add(gender);
			}
			
			String cellphone = criteria.getCellphone();
			if(cellphone != null && !cellphone.trim().isEmpty()) {
				whereSql.append(" and cellphone like ?");
				params.add("%" + cellphone + "%");
			}
			
			String email = criteria.getEmail();
			if(email != null && !email.trim().isEmpty()) {
				whereSql.append(" and email like ?");
				params.add("%" + email + "%");
			}
			
			/*
			 * select count(*) .. + where子句
			 * 执行之
			 */
			Number num = (Number)qr.query(cntSql.append(whereSql).toString(), 
					new ScalarHandler(), params.toArray());
			int tr = num.intValue();
			pb.setTr(tr);
			
			/*
			 * 得到beanList
			 */
			StringBuilder sql = new StringBuilder("select * from t_customer");
			// 我们查询beanList这一步，还需要给出limit子句
			StringBuilder limitSql = new StringBuilder(" limit ?,?");
			// params中需要给出limit后两个问号对应的值
			params.add((pc-1)*ps);
			params.add(ps);
			// 执行之
			List<Customer> beanList = qr.query(sql.append(whereSql).append(limitSql).toString(), 
					new BeanListHandler<Customer>(Customer.class), 
					params.toArray());
			pb.setBeanList(beanList);
			
			return pb;
			
		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
