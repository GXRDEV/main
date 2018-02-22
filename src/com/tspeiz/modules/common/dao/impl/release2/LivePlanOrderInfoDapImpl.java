package com.tspeiz.modules.common.dao.impl.release2;

import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.release2.ILivePlanOrderInfoDao;
import com.tspeiz.modules.common.entity.newrelease.LivePlanOrder;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kangxin on 2018/2/2.
 */
@Repository
public class LivePlanOrderInfoDapImpl extends BaseDaoImpl<LivePlanOrder> implements ILivePlanOrderInfoDao{
    @SuppressWarnings("unchecked")
	@Override
    public Map<String, Object> liveplandatass(String searchContent, final  Integer start, final Integer length) {
        Map<String,Object> map=new HashMap<String,Object>();
           final StringBuilder sqlBuilder=new StringBuilder();
            sqlBuilder.append(" SELECT plan.Id,plan.Title AS title,DATE_FORMAT(plan.BeginTime,'%Y-%m-%d %H:%i:%S') AS beginTimes,DATE_FORMAT(plan.ChatRoomStartTime,'%Y-%m-%d %H:%i:%S') AS chatRoomStartTimes,  " +
                    " DATE_FORMAT(plan.ChatRoomCloseTime,'%Y-%m-%d %H:%i:%S') AS chatRoomCloseTimes,doc.DisplayName AS DocName,plan.Duration ");
            sqlBuilder.append(" from live_plan_order plan ");
            sqlBuilder.append(" LEFT JOIN doctor_detail_info doc ON doc.Id=plan.UserId  ");
            sqlBuilder.append(" WHERE  1=1 and (doc.Status =1 or doc.Status is not null) ");
            sqlBuilder.append(gainSearch(searchContent));
            sqlBuilder.append(" ORDER BY plan.BeginTime  ");
            List<LivePlanOrder> list = this.hibernateTemplate
                    .executeFind(new HibernateCallback() {
                        public Object doInHibernate(Session session)
                                throws HibernateException, SQLException {
                            Query query = session.createSQLQuery(sqlBuilder
                                    .toString());
                            query.setFirstResult(start);
                            query.setMaxResults(length);
                            query.setResultTransformer(Transformers
                                    .aliasToBean(LivePlanOrder.class));
                            return query.list();
                        }
                    });
            if(list!=null && list.size()>0) {
            	for(LivePlanOrder order:list) {
            		if(order.getDuration()!=null)
            			order.setDurationStr(getVideoLengthStr(order.getDuration().longValue()));
            	}
            }
            map.put("items", list);
            final StringBuilder countSql=new StringBuilder();
            countSql.append(" select count(*) from live_plan_order plan ");
            countSql.append(" LEFT JOIN doctor_detail_info doc ON doc.Id=plan.UserId  ");
            countSql.append(" WHERE  1=1 and (doc.Status =1 or doc.Status is not null) ");
            countSql.append(gainSearch(searchContent));
            Object effectNumber = this.hibernateTemplate
                    .execute(new HibernateCallback() {
                        public Object doInHibernate(Session session)
                                throws HibernateException {
                            return session.createSQLQuery(countSql.toString())
                                    .uniqueResult();
                        }
                    });
            Integer num = Integer.parseInt(effectNumber.toString());
            map.put("num", num);
            return map;
    }

        private String gainSearch(String search) {
             StringBuilder sb = new StringBuilder();
                if(StringUtils.isNotBlank(search)) {
                    sb.append(" and ( ");
                    sb.append(" doc.DisplayName like '%"+search+"%' or ");
                    sb.append(" plan.BeginTime like '%"+search+"%'  ");
                    sb.append(" )");
        }
        return sb.toString();
    }
        public static String getVideoLengthStr(long length){  
            int h=(int)length/(60*60*1000);  
            int m=(int)(length%(60*60*1000))/(60*1000);  
            int s=(int)(length%(60*1000))/(1000);  
            StringBuilder sb=new StringBuilder();  
            if (h<10){  
                sb.append("0").append(h);  
            }else {  
                sb.append(h);  
            }  
            sb.append(":");  
            if (m<10){  
                sb.append("0").append(m);  
            }else {  
                sb.append(m);  
            }  
            sb.append(":");  
            if (s<10){  
                sb.append("0").append(s);  
            }else {  
                sb.append(s);  
            }  
            return sb.toString();  
        } 
        
}
