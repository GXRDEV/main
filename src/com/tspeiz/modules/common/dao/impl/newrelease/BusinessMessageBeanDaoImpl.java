package com.tspeiz.modules.common.dao.impl.newrelease;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.bean.AdviceBean;
import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.newrelease.IBusinessMessageBeanDao;
import com.tspeiz.modules.common.entity.newrelease.AppraisementImpressionDoctor;
import com.tspeiz.modules.common.entity.newrelease.BusinessMessageBean;
@Repository
public class BusinessMessageBeanDaoImpl extends BaseDaoImpl<BusinessMessageBean> implements IBusinessMessageBeanDao{

	@SuppressWarnings("unchecked")
	public List<BusinessMessageBean> queryBusinessMessageBeansByCon(
			Integer oid,String orderUuid, Integer type) {
		// TODO Auto-generated method stub
		String hql="from BusinessMessageBean where 1=1 and (orderId="+oid+" or orderUuid='"+orderUuid+"') and orderType="+type;
		return this.hibernateTemplate.find(hql);
	}

	@SuppressWarnings("unchecked")
	public List<BusinessMessageBean> queryBusinessMessageBeansByCon(
			Integer oid,String orderUuid, Integer type, Integer docid) {
		// TODO Auto-generated method stub
		String hql="from BusinessMessageBean where 1=1 and (orderId="+oid+" or orderUuid='"+orderUuid+"') and orderType="+type+" and ((recvType IS NULL) OR (recvType IS NOT NULL and recvId="+docid+")) ORDER BY sendTime";
		return this.hibernateTemplate.find(hql);
	}

	@SuppressWarnings("unchecked")
	public List<BusinessMessageBean> queryBusinessMessageBeansByCon(
			Integer oid, Integer otype,final Integer pageNo,final Integer pageSize) {
		// TODO Auto-generated method stub
		final StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select msg.MsgType,msg.MsgContent,msg.FileUrl,msg.SendType,msg.SendTime from business_message_info msg ");
		sqlBuilder.append(" where 1=1 and msg.OrderId="+oid+" and msg.OrderType="+otype+" order by msg.SendTime desc");
		List<BusinessMessageBean> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setFirstResult((pageNo - 1) * pageSize);
						query.setMaxResults(pageSize);
						query.setResultTransformer(Transformers
								.aliasToBean(BusinessMessageBean.class));
						return query.list();
					}
				});
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<AdviceBean> newlyadvices(Integer userId) {
		// TODO Auto-generated method stub
		final StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("SELECT doc.HeadImageUrl as headImage,doc.DisplayName as docName,hos.DisplayName as hosName,dep.DisplayName as depName,main.orderId,main.OrderType,doc.Duty,");
		sqlBuilder.append(" (CASE WHEN msg.MsgType='text' THEN msg.MsgContent  WHEN msg.MsgType='image/jpg' THEN '[图片]' WHEN msg.MsgType='audio/amr' THEN '[语音消息]' END ) AS msgContent");
		sqlBuilder.append(",msg.SendTime as msgTime ");
		sqlBuilder.append(" FROM(");
		sqlBuilder.append(" SELECT UUID AS uid,doctorId,Id AS orderId,'9' AS orderType FROM `business_d2p_fastask_order` where UserId="+userId);
		sqlBuilder.append(" and Status not in(10) ");
		sqlBuilder.append(" UNION");
		sqlBuilder.append(" SELECT UUID AS uid,doctorId,Id AS orderId,'7' AS orderType FROM `business_d2p_tel_order` where UserId="+userId);
		sqlBuilder.append(" and Status not in(10) ");
		sqlBuilder.append(" UNION ");
		sqlBuilder.append(" SELECT UUID AS uid,doctorId,Id AS orderId,'6' AS orderType FROM `business_d2p_tuwen_order` where UserId="+userId);
		sqlBuilder.append(" and Status not in(10) ");
		sqlBuilder.append(" )main");
		sqlBuilder.append(" LEFT JOIN business_message_info msg ON msg.OrderUuid=main.uid");
		sqlBuilder.append(" LEFT JOIN doctor_detail_info doc ON main.doctorId=doc.Id");
		sqlBuilder.append(" LEFT JOIN hospital_detail_info hos ON hos.Id=doc.HospitalId");
		sqlBuilder.append(" LEFT JOIN hospital_department_info dep ON dep.Id=doc.DepId");
		sqlBuilder.append(" WHERE msg.Id IN(");
		sqlBuilder.append(" SELECT MAX(Id) FROM business_message_info GROUP BY OrderUuid");
		sqlBuilder.append(" ) OR (msg.Id='' OR msg.Id IS NULL) ORDER BY msg.SendTime DESC");
		List<AdviceBean> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(AdviceBean.class));
						return query.list();
					}
				});
		return list;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Integer queryUnReadMsg(String uuid, Integer otype, Integer docid) {
		// TODO Auto-generated method stub
		final StringBuilder counthql = new StringBuilder();
		counthql.append("select count(*) ");
		counthql.append(" from business_message_info msg ");
		counthql.append(" where 1=1 and msg.OrderUuid='" + uuid+"' ");
		counthql.append(" and msg.OrderType="+otype);
		counthql.append(" and msg.SendId not in("+docid+") and (msg.IsRead=0 or msg.IsRead is null) ");
		Object effectNumber = this.hibernateTemplate
				.execute(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						return session.createSQLQuery(counthql.toString())
								.uniqueResult();
					}
				});
		Integer num = Integer.parseInt(effectNumber.toString());
		return num;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void updateMsgToRead(String uuid, Integer otype, Integer docid) {
		// TODO Auto-generated method stub
		final StringBuilder counthql = new StringBuilder();
		counthql.append("update business_message_info set IsRead=1 where OrderUuid='" + uuid+"' ");
		counthql.append(" and OrderType="+otype);
		counthql.append(" and SendId not in("+docid+") and (IsRead=0 or IsRead is null) ");
		Object effectNumber = this.hibernateTemplate
				.execute(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						
						return session.createSQLQuery(counthql.toString()).executeUpdate();
					}
				});
	}
	
}
