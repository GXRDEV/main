package com.tspeiz.modules.common.dao.impl;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.dao.IOrderSerialNumberDao;
import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.entity.OrderSerialNumber;

@Repository
public class OrderSerialNumberDaoImpl extends BaseDaoImpl<OrderSerialNumber> implements
	IOrderSerialNumberDao{

	public String getOrderNumberByOrderType(final Integer orderType) {
		// TODO Auto-generated method stub\
		System.out.println("==========执行==========");
		this.hibernateTemplate.execute(new HibernateCallback<Object>() {

			@SuppressWarnings("deprecation")
			@Override
			public Object doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				// TODO Auto-generated method stub
				
				 CallableStatement cs = arg0.connection().prepareCall("{call GetOrderNumberByType(?,?)}");
				 
				//存储过程输入参数 
				 cs.setInt(1, orderType);
				 //注册输出参数
				 cs.registerOutParameter(2, Types.INTEGER); // 设置返回值类型 即返回值 
				 cs.execute(); // 执行存储过程 
				 String orderNum = cs.getString(2); 
				 cs.close(); 
				 return orderNum;
			}
		});
		return null;
	}
      

	@Override
	public Integer getOrderSerialNumberByOrderType(final Integer orderType) {
		// TODO Auto-generated method stub\

		this.hibernateTemplate.execute(new HibernateCallback<Object>() {

			@SuppressWarnings("deprecation")
			@Override
			public Object doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				// TODO Auto-generated method stub
				
				 CallableStatement cs = arg0.connection().prepareCall("{call GetOrderSerialNumber(?,?)}");
				 
				//存储过程输入参数 
				 cs.setInt(1, orderType);
				 //注册输出参数
				 cs.registerOutParameter(2, Types.INTEGER); // 设置返回值类型 即返回值 
				 cs.execute(); // 执行存储过程 
				 Integer serialNum = cs.getInt(2); 
				 cs.close(); 
				 return serialNum;
			}
		});
		return null;
	}
}
