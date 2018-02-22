package com.tspeiz.modules.common.dao.impl.newrelease;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.tspeiz.modules.common.bean.OrderStatusEnum;
import com.tspeiz.modules.common.bean.dto.VedioOrderDto;
import com.tspeiz.modules.common.bean.weixin.ReSourceBean;
import com.tspeiz.modules.common.dao.base.impl.BaseDaoImpl;
import com.tspeiz.modules.common.dao.newrelease.IBusinessVedioOrderDao;
import com.tspeiz.modules.common.entity.newrelease.BusinessVedioOrder;
import com.tspeiz.modules.common.entity.newrelease.SpecialAdviceOrder;

@Repository
public class BusinessVedioOrderDaoImpl extends BaseDaoImpl<BusinessVedioOrder>
		implements IBusinessVedioOrderDao {

	@SuppressWarnings("unchecked")
	public BusinessVedioOrder queryBusinessVedioOrderById(Integer id) {
		// TODO Auto-generated method stub
		final StringBuilder sqlBuilder = new StringBuilder("");
		// TODO Auto-generated method stub
		sqlBuilder
				.append("SELECT  remote.ConsultationOpinionUuid,remote.UUID as uuid,remote.CaseId as caseId,remote.Id,remote.ExpertId ,remote.LocalHospitalId,remote.LocalDepartId,"
						+ "remote.LocalDoctorId,remote.CreateTime,remote.ConsultationTime,ca.ContactName as patientName,ca.IdCard as idCard,ca.Telephone as telephone,"
						+ "remote.Status,remote.ProgressTag,remote.ConsultationResult,ca.Age as age,ca.Sex as sex,"
						+ "remote.ConsultationDate,remote.ConsultationDur,remote.VedioDur,remote.ReplyTime,"
						+ "remote.PayStatus as payStatus,remote.TreatPlan as treatPlan,remote.Attentions as attentions");
		sqlBuilder.append(",remote.OrderMode as orderMode,remote.VedioSubType");
		sqlBuilder
				.append(",hos.DisplayName as hosName ,department.DisplayName as depName,specal.DisplayName as expertName,remote.Source as source,remote.ExLevel as exLevel,remote.VedioSubType");
		sqlBuilder
				.append(",localHos.DisplayName as localHosName ,localDep.DisplayName as localDepName,doc.DisplayName as localDocName,localHos.DockingMode as dockingMode ");

		sqlBuilder.append(" FROM business_vedio_order remote");
		sqlBuilder
				.append(" left join doctor_detail_info specal on remote.ExpertId=specal.Id ");
		sqlBuilder
				.append(" left join hospital_detail_info hos on specal.HospitalId=hos.Id ");
		sqlBuilder
				.append(" left join hospital_department_info department on specal.DepId=department.Id");

		sqlBuilder
				.append(" left join hospital_detail_info localHos on remote.LocalHospitalId=localHos.Id ");
		sqlBuilder
				.append(" left join hospital_department_info localDep on remote.LocalDepartId=localDep.Id");
		sqlBuilder
				.append(" left join doctor_detail_info doc on remote.LocalDoctorId=doc.Id ");
		sqlBuilder
				.append(" left join user_case_info ca on ca.Id=remote.CaseId ");
		sqlBuilder.append(" WHERE remote.Id = " + id);
		List<BusinessVedioOrder> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());

						query.setResultTransformer(Transformers
								.aliasToBean(BusinessVedioOrder.class));
						return query.list();
					}
				});

		return list.size() > 0 ? list.get(0) : null;
	}

	public Object queryFlowNumberCallPROCEDURE(Integer type) {
		// TODO Auto-generated method stub
		Session session = this.hibernateTemplate.getSessionFactory()
				.openSession();
		Connection conn = session.connection();
		String sql = "{call GetOrderSerialNumber(?,?)}";
		Integer flownum = null;
		try {
			CallableStatement cs = conn.prepareCall(sql);
			cs.setObject(1, type);
			cs.registerOutParameter(2, java.sql.Types.INTEGER);
			cs.execute();
			flownum = cs.getInt(2);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
				session.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return flownum;
	}

	public BusinessVedioOrder queryBusinessVedioOrderByUid(String uid) {
		// TODO Auto-generated method stub
		String hql = "from BusinessVedioOrder where uuid='" + uid + "' ";
		List<BusinessVedioOrder> list = this.hibernateTemplate.find(hql);
		if (list != null && list.size() > 0)
			return list.get(0);
		return null;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Integer> queryOrdersAddCon(String startDate,String endDate, Integer type,String hosid) {
		// TODO Auto-generated method stub
		Map<String, Integer> map = new HashMap<String, Integer>();
		// TODO Auto-generated method stub
		final StringBuilder counthql = new StringBuilder();
		counthql.append("select count(1) ");
		counthql.append(" from business_vedio_order vedio ");
		counthql.append(" where 1=1 and vedio.PayStatus=1 ");
		counthql.append(" and vedio.Status not in(0) ");
		counthql.append(" and (vedio.DelFlag=0 or vedio.DelFlag is null) ");
		if(StringUtils.isNotBlank(hosid)){
			counthql.append(" and vedio.LocalHospitalId="+hosid );
		}
		String count_mdata = counthql.toString();
		if (StringUtils.isNotBlank(startDate)) {
			counthql.append(" and (DATE_FORMAT(vedio.CreateTime,'%Y-%m-%d') >='"
					+ startDate + "' ) ");
		}
		if (StringUtils.isNotBlank(endDate)) {
			counthql.append(" and (DATE_FORMAT(vedio.CreateTime,'%Y-%m-%d') <='"
					+ endDate + "' ) ");
		}
		Object effectNumber = this.hibernateTemplate
				.execute(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						return session.createSQLQuery(counthql.toString())
								.uniqueResult();
					}
				});
		Integer num = Integer.parseInt(effectNumber.toString());
		map.put("modata", num);
		return map;
	}

	
	@SuppressWarnings("unchecked")
	public Map<String, Object> queryOrdersAddCon(List<String> dates,String hosid) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		// TODO Auto-generated method stub
		final StringBuilder counthql = new StringBuilder();
		counthql.append(" select count(1) as count ");
		counthql.append(" from business_vedio_order vedio ");
		counthql.append(" where 1=1 and vedio.PayStatus=1 ");
		counthql.append(" and vedio.Status not in(0) ");
		if(StringUtils.isNotBlank(hosid)){
			counthql.append(" and vedio.LocalHospitalId="+hosid+" " );
		}
		counthql.append(" and (vedio.DelFlag=0 or vedio.DelFlag is null) ");
		String count_mdata = counthql.toString();
		StringBuilder finalcounthql=new StringBuilder();
		if(dates!=null&&dates.size()>0){
			for(String date:dates){
				finalcounthql.append(count_mdata).append("");
				finalcounthql.append(" and (DATE_FORMAT(vedio.CreateTime,'%Y-%m')='"
						+ date + "' ) union all");
			}
			count_mdata=finalcounthql.substring(0, finalcounthql.length()-9);
		}
		final String cout_sql=count_mdata;
		List<ReSourceBean> modatas = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(cout_sql
								.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(ReSourceBean.class));
						return query.list();
					}
				});
		map.put("modata", modatas);
		/*final StringBuilder _counthql=new StringBuilder();
		for(String date:dates){
			_counthql.append(count_mdata).append("");
			_counthql.append("and (DATE_FORMAT(vedio.CreateTime,'%Y-%m')<='"
				+ date + "' ) union all");
		}
		final String _cout_sql=_counthql.substring(0, _counthql.length()-9);
		System.out.println("==sql:"+_cout_sql);
		List<ReSourceBean> todata = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(_cout_sql
								.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(ReSourceBean.class));
						return query.list();
					}
				});
			map.put("todata", todata);*/
		return map;
	}
	
	@SuppressWarnings("unchecked")
	public List<ReSourceBean> queryOrderHosCal(String startDate,String endDate) {
		// TODO Auto-generated method stub
		final StringBuilder sqlBuilder = new StringBuilder("");
		// TODO Auto-generated method stub
		sqlBuilder
				.append("SELECT COUNT(1) as count,vedio.LocalHospitalId as id,hos.DisplayName as name FROM business_vedio_order vedio");
		sqlBuilder.append(" left join hospital_detail_info hos on hos.Id=vedio.LocalHospitalId ");
		sqlBuilder.append(" WHERE vedio.STATUS NOT IN(0) AND vedio.PayStatus=1  ");
		if(StringUtils.isNotBlank(startDate)){
			sqlBuilder.append(" and (DATE_FORMAT(vedio.CreateTime,'%Y-%m-%d') >='"
					+ startDate + "' ) ");
		}
		if(StringUtils.isNotBlank(startDate)){
			sqlBuilder.append(" and (DATE_FORMAT(vedio.CreateTime,'%Y-%m-%d') <='"
					+ endDate + "' ) ");
		}
		sqlBuilder.append(" and (vedio.DelFlag=0 or vedio.DelFlag is null) ");

		sqlBuilder
		.append(" GROUP BY vedio.LocalHospitalId");
		List<ReSourceBean> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());

						query.setResultTransformer(Transformers
								.aliasToBean(ReSourceBean.class));
						return query.list();
					}
				});
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<ReSourceBean> orderexcal(String hosid,String startDate,String endDate) {
		// TODO Auto-generated method stub
		final StringBuilder sqlBuilder = new StringBuilder("");
		// TODO Auto-generated method stub
		sqlBuilder
				.append("SELECT cout as count,ExpertId as id,doc.DisplayName as name  FROM (SELECT COUNT(1) AS cout,ExpertId FROM business_vedio_order WHERE STATUS NOT IN(0) AND PayStatus=1 AND ExpertId IS NOT NULL ");
		sqlBuilder.append(" and (DelFlag=0 or DelFlag is null) ");
		if(StringUtils.isNotBlank(hosid)){
			sqlBuilder.append(" and LocalHospitalId="+hosid);
		}
		if(StringUtils.isNotBlank(startDate)){
			sqlBuilder.append(" and (DATE_FORMAT(CreateTime,'%Y-%m-%d') >='"
					+ startDate + "' ) ");
		}
		if(StringUtils.isNotBlank(endDate)){
			sqlBuilder.append(" and (DATE_FORMAT(CreateTime,'%Y-%m-%d') <='"
					+ endDate + "' ) ");
		}
		sqlBuilder
		.append(" GROUP BY ExpertId) tab ");
		sqlBuilder.append(" left join doctor_detail_info doc on tab.ExpertId=doc.Id ");
		sqlBuilder.append("ORDER BY cout DESC");
		List<ReSourceBean> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setFirstResult(0);
						query.setMaxResults(15);
						query.setResultTransformer(Transformers
								.aliasToBean(ReSourceBean.class));
						return query.list();
					}
				});
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<ReSourceBean> orderdepcal(String hosid,String startDate,String endDate) {
		// TODO Auto-generated method stub
		final StringBuilder sqlBuilder = new StringBuilder("");
		// TODO Auto-generated method stub
		sqlBuilder.append("SELECT cout as count,id,display as name FROM (");
		sqlBuilder
				.append("SELECT COUNT(*) AS cout,dsr.`StandardDepId` AS id,stand.`DisplayName` AS display FROM business_vedio_order vedio ");
		sqlBuilder
				.append(" LEFT JOIN doctor_detail_info doc ON vedio.`ExpertId`=doc.`Id` ");
		sqlBuilder
				.append(" LEFT JOIN dep_standarddep_r dsr ON doc.`DepId`=dsr.`DepId` ");
		sqlBuilder
		.append(" LEFT JOIN standard_department_info stand ON stand.`Id`=dsr.`StandardDepId` ");
		sqlBuilder
				.append(" WHERE vedio.status NOT IN(0) AND vedio.PayStatus=1 AND vedio.DelFlag=0 AND vedio.ExpertId IS NOT NULL ");
		sqlBuilder.append(" and (vedio.DelFlag=0 or vedio.DelFlag is null) ");
		if(StringUtils.isNotBlank(hosid)){
			sqlBuilder.append(" and vedio.LocalHospitalId="+hosid);
		}
		if(StringUtils.isNotBlank(startDate)){
			sqlBuilder.append(" and (DATE_FORMAT(vedio.CreateTime,'%Y-%m-%d') >='"
					+ startDate + "' ) ");
		}
		if(StringUtils.isNotBlank(endDate)){
			sqlBuilder.append(" and (DATE_FORMAT(vedio.CreateTime,'%Y-%m-%d') <='"
					+ endDate + "' ) ");
		}
		sqlBuilder.append(" GROUP BY dsr.`StandardDepId` ");
		sqlBuilder.append(")tab ORDER BY cout DESC");
		List<ReSourceBean> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setFirstResult(0);
						query.setMaxResults(15);
						query.setResultTransformer(Transformers
								.aliasToBean(ReSourceBean.class));
						return query.list();
					}
				});
		return list;
	}
	
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> gainvedioorderdatas(String search,
			final Integer start,final Integer length, Integer type) {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<String,Object>();
		final StringBuilder sb=new StringBuilder();
		sb.append("select vedio.Id, ");
		sb.append(" cinfo.ContactName as patientName,cinfo.sex,cinfo.Age,");
		sb.append(" (CASE WHEN cinfo.Telephone is null or cinfo.Telephone='' THEN uinfo.Telephone WHEN cinfo.Telephone is not null and cinfo.Telephone!='' THEN cinfo.Telephone END ) as telephone, ");
		sb.append(" exdoc.DisplayName as expertName,vedio.ExpertId,");
		sb.append(" (CASE WHEN vedio.ExpertId is null THEN exhos.DisplayName WHEN vedio.ExpertId is not null THEN exdhos.DisplayName END) as hosName, ");
		sb.append(" (CASE WHEN vedio.ExpertId is null THEN exddep.DisplayName WHEN vedio.ExpertId is not null THEN exddep.DisplayName END) as depName, ");
		sb.append(" exdoc.Profession as exProfession,");
		sb.append(" localdoc.DisplayName as localDocName,localdoc.Profession as localProfession,localhos.DisplayName as localHosName,localdep.DisplayName as localDepName, ");
		sb.append("bpay.RefundStatus as refundStatus,date_format(bpay.RefundTime, '%Y-%m-%d %H:%i:%s') as refundTime,");
		sb.append(" vedio.CreateTime,vedio.ConsultationDate,vedio.ConsultationTime,vedio.PayStatus ");
		sb.append(" from business_vedio_order vedio ");
		sb.append(" left join user_case_info cinfo on vedio.CaseId=cinfo.Id ");
		sb.append(" left join user_contact_info uinfo on uinfo.UUID=vedio.SubUserUuid");
		sb.append(" left join doctor_detail_info exdoc on exdoc.Id=vedio.ExpertId ");//专家
		sb.append(" left join hospital_detail_info exdhos on exdhos.Id=exdoc.HospitalId ");//专家存在
		sb.append(" left join hospital_department_info exddep on exddep.Id=exdoc.DepId ");//专家存在
		sb.append(" left join hospital_detail_info exhos on exhos.Id=vedio.ExpertHospitalId ");//专家医院存在
		sb.append(" left join hospital_department_info exdep on exdep.Id=vedio.ExpertDepId ");//专家科室存在
		sb.append(" left join doctor_detail_info localdoc on localdoc.Id=vedio.LocalDoctorId ");
		sb.append(" left join hospital_detail_info localhos on localhos.Id=vedio.LocalHospitalId ");
		sb.append(" left join hospital_department_info localdep on localdep.Id=vedio.LocalDepartId ");
		sb.append(" left join business_pay_info bpay on vedio.Id =bpay.OrderId and bpay.OrderType=4 and bpay.PayStatus=1 ");
		sb.append(" where 1=1 ");
		sb.append(gainstatusquery(type));
		sb.append(gainsearchquery(search,true));
		sb.append(" and (vedio.DelFlag=0 or vedio.DelFlag IS NULL) ");
		sb.append("  order by vedio.CreateTime desc ");
		List<BusinessVedioOrder> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sb
								.toString());
						query.setFirstResult(start);
						query.setMaxResults(length);
						query.setResultTransformer(Transformers
								.aliasToBean(BusinessVedioOrder.class));
						return query.list();
					}
				});
		map.put("items", list);
		final StringBuilder counthql = new StringBuilder();	
		counthql.append("select count(vedio.Id) ");
		counthql.append(" from business_vedio_order vedio ");
		counthql.append(" left join user_case_info cinfo on vedio.CaseId=cinfo.Id ");
		counthql.append(" left join user_contact_info uinfo on uinfo.UUID=vedio.SubUserUuid");
		counthql.append(" left join doctor_detail_info exdoc on exdoc.Id=vedio.ExpertId ");//专家
		counthql.append(" left join hospital_detail_info exdhos on exdhos.Id=exdoc.HospitalId ");//专家存在
		counthql.append(" left join hospital_department_info exddep on exddep.Id=exdoc.DepId ");//专家存在
		counthql.append(" left join hospital_detail_info exhos on exhos.Id=vedio.ExpertHospitalId ");//专家医院存在
		counthql.append(" left join hospital_department_info exdep on exdep.Id=vedio.ExpertDepId ");//专家科室存在
		counthql.append(" left join doctor_detail_info localdoc on localdoc.Id=vedio.LocalDoctorId ");
		counthql.append(" left join hospital_detail_info localhos on localhos.Id=vedio.LocalHospitalId ");
		counthql.append(" left join hospital_department_info localdep on localdep.Id=vedio.LocalDepartId ");
		counthql.append(" left join business_pay_info bpay on vedio.Id =bpay.OrderId and bpay.OrderType=4 and bpay.PayStatus=1 ");
		counthql.append(" where 1=1 ");
		counthql.append(gainstatusquery(type));
		counthql.append(gainsearchquery(search,true));
		counthql.append(" and (vedio.DelFlag=0 or vedio.DelFlag IS NULL) ");
		Object effectNumber = this.hibernateTemplate
				.execute(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						return session.createSQLQuery(counthql.toString())
								.uniqueResult();
					}
				});
		Integer num = Integer.parseInt(effectNumber.toString());
		map.put("num", num);
		return map;
	}
	
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> queryVedioOrderDatas_doc(Integer docid,
			String searchContent, Integer ostatus,final Integer start,
			final Integer length,final Integer dtype) {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<String,Object>();
		final StringBuilder sb=new StringBuilder();
		sb.append("select vedio.Id as vedioId, ");
		sb.append(" cinfo.ContactName as userName,cinfo.sex,cinfo.Age,");
		sb.append(" (CASE WHEN cinfo.Telephone is null or cinfo.Telephone='' THEN uinfo.Telephone WHEN cinfo.Telephone is not null and cinfo.Telephone!='' THEN cinfo.Telephone END ) as telephone, ");
		sb.append(" DATE_FORMAT(vedio.CreateTime,'%Y-%m-%d %T') as createTime,vedio.ConsultationDate,vedio.ConsultationTime,vedio.PayStatus, ");
		sb.append(" localdoc.DisplayName as localDocName,localhos.DisplayName as localHosName,localdep.DisplayName as localDepName,");
		sb.append(" exdoc.DisplayName as exDocName,");
		sb.append(" (CASE WHEN vedio.ExpertId is null THEN exhos.DisplayName WHEN vedio.ExpertId is not null THEN exdhos.DisplayName END) as exHosName, ");
		sb.append(" (CASE WHEN vedio.ExpertId is null THEN exdep.DisplayName WHEN vedio.ExpertId is not null THEN exddep.DisplayName END) as exDepName, ");
		sb.append(" vedio.Status as status ");
		sb.append(" from business_vedio_order vedio ");
		sb.append(" left join user_case_info cinfo on vedio.CaseId=cinfo.Id ");
		sb.append(" left join user_contact_info uinfo on uinfo.UUID=vedio.SubUserUuid");
		sb.append(" left join doctor_detail_info localdoc on localdoc.Id=vedio.LocalDoctorId ");
		sb.append(" left join hospital_detail_info localhos on localhos.Id=vedio.LocalHospitalId ");
		sb.append(" left join hospital_department_info localdep on localdep.Id=vedio.LocalDepartId ");
		sb.append(" left join doctor_detail_info exdoc on exdoc.Id=vedio.ExpertId ");//专家
		sb.append(" left join hospital_detail_info exdhos on exdhos.Id=exdoc.HospitalId ");//专家存在
		sb.append(" left join hospital_department_info exddep on exddep.Id=exdoc.DepId ");//专家存在
		sb.append(" left join hospital_detail_info exhos on exhos.Id=vedio.ExpertHospitalId ");//专家医院存在
		sb.append(" left join hospital_department_info exdep on exdep.Id=vedio.ExpertDepId ");//专家科室存在
		sb.append(" where 1=1 ");
		sb.append(gainstatusquery(ostatus));
		sb.append(gainsearchquery(searchContent,false));
		sb.append(" and (vedio.DelFlag=0 or vedio.DelFlag IS NULL) ");
		if(dtype.equals(1)){
			//我发起的
			sb.append(" and vedio.LocalDoctorId="+docid);
		}else{
			//我接受到
			sb.append(" and vedio.ExpertId="+docid);
		}
		sb.append("  order by vedio.CreateTime ");
		List<VedioOrderDto> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sb
								.toString());
						query.setFirstResult(start);
						query.setMaxResults(length);
						query.setResultTransformer(Transformers
								.aliasToBean(VedioOrderDto.class));
						return query.list();
					}
				});
		map.put("items", list);
		final StringBuilder counthql = new StringBuilder();	
		counthql.append("select count(vedio.Id) ");
		counthql.append(" from business_vedio_order vedio ");
		counthql.append(" left join user_case_info cinfo on vedio.CaseId=cinfo.Id ");
		counthql.append(" left join user_contact_info uinfo on uinfo.UUID=vedio.SubUserUuid");
		counthql.append(" left join doctor_detail_info localdoc on localdoc.Id=vedio.LocalDoctorId ");
		counthql.append(" left join hospital_detail_info localhos on localhos.Id=vedio.LocalHospitalId ");
		counthql.append(" left join hospital_department_info localdep on localdep.Id=vedio.LocalDepartId ");
		counthql.append(" left join doctor_detail_info exdoc on exdoc.Id=vedio.ExpertId ");//专家
		counthql.append(" left join hospital_detail_info exdhos on exdhos.Id=exdoc.HospitalId ");//专家存在
		counthql.append(" left join hospital_department_info exddep on exddep.Id=exdoc.DepId ");//专家存在
		counthql.append(" left join hospital_detail_info exhos on exhos.Id=vedio.ExpertHospitalId ");//专家医院存在
		counthql.append(" left join hospital_department_info exdep on exdep.Id=vedio.ExpertDepId ");//专家科室存在
		counthql.append(" where 1=1 ");
		counthql.append(gainstatusquery(ostatus));
		counthql.append(gainsearchquery(searchContent,false));
		counthql.append(" and (vedio.DelFlag=0 or vedio.DelFlag IS NULL) ");
		if(dtype.equals(1)){
			//我发起的
			counthql.append(" and vedio.LocalDoctorId="+docid);
		}else{
			//我接受到
			counthql.append(" and vedio.ExpertId="+docid);
		}
		Object effectNumber = this.hibernateTemplate
				.execute(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						return session.createSQLQuery(counthql.toString())
								.uniqueResult();
					}
				});
		Integer num = Integer.parseInt(effectNumber.toString());
		map.put("num", num);
		return map;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> queryVedioOrderDatas_expert(Integer docid,
			String searchContent, Integer ostatus,final Integer start,final Integer length) {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<String,Object>();
		final StringBuilder sb=new StringBuilder();
		sb.append("select vedio.Id as vedioId, ");
		sb.append(" cinfo.ContactName as userName,cinfo.sex,cinfo.Age,");
		sb.append(" (CASE WHEN cinfo.Telephone is null or cinfo.Telephone='' THEN uinfo.Telephone WHEN cinfo.Telephone is not null and cinfo.Telephone!='' THEN cinfo.Telephone END ) as telephone, ");
		sb.append(" DATE_FORMAT(vedio.CreateTime,'%Y-%m-%d %T') as createTime,vedio.ConsultationDate,vedio.ConsultationTime,vedio.PayStatus, ");
		sb.append(" localdoc.DisplayName as localDocName,localhos.DisplayName as localHosName,localdep.DisplayName as localDepName,");
		sb.append(" exdoc.DisplayName as exDocName,");
		sb.append(" (CASE WHEN vedio.ExpertId is null THEN exhos.DisplayName WHEN vedio.ExpertId is not null THEN exdhos.DisplayName END) as exHosName, ");
		sb.append(" (CASE WHEN vedio.ExpertId is null THEN exddep.DisplayName WHEN vedio.ExpertId is not null THEN exddep.DisplayName END) as exDepName, ");
		sb.append(" vedio.Status as status ");
		sb.append(" from business_vedio_order vedio ");
		sb.append(" left join user_case_info cinfo on vedio.CaseId=cinfo.Id ");
		sb.append(" left join user_contact_info uinfo on uinfo.UUID=vedio.SubUserUuid");
		sb.append(" left join doctor_detail_info localdoc on localdoc.Id=vedio.LocalDoctorId ");
		sb.append(" left join hospital_detail_info localhos on localhos.Id=vedio.LocalHospitalId ");
		sb.append(" left join hospital_department_info localdep on localdep.Id=vedio.LocalDepartId ");
		sb.append(" left join doctor_detail_info exdoc on exdoc.Id=vedio.ExpertId ");//专家
		sb.append(" left join hospital_detail_info exdhos on exdhos.Id=exdoc.HospitalId ");//专家存在
		sb.append(" left join hospital_department_info exddep on exddep.Id=exdoc.DepId ");//专家存在
		sb.append(" left join hospital_detail_info exhos on exhos.Id=vedio.ExpertHospitalId ");//专家医院存在
		sb.append(" left join hospital_department_info exdep on exdep.Id=vedio.ExpertDepId ");//专家科室存在
		sb.append(" where 1=1 ");
		sb.append(gainstatusquery(ostatus));
		sb.append(gainsearchquery(searchContent,false));
		sb.append(" and (vedio.DelFlag=0 or vedio.DelFlag IS NULL) ");
		sb.append(" and vedio.ExpertId="+docid);
		sb.append("  order by vedio.CreateTime ");
		List<VedioOrderDto> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sb
								.toString());
						query.setFirstResult(start);
						query.setMaxResults(length);
						query.setResultTransformer(Transformers
								.aliasToBean(VedioOrderDto.class));
						return query.list();
					}
				});
		map.put("items", list);
		final StringBuilder counthql = new StringBuilder();	
		counthql.append("select count(vedio.Id) ");
		counthql.append(" from business_vedio_order vedio ");
		counthql.append(" left join user_case_info cinfo on vedio.CaseId=cinfo.Id ");
		counthql.append(" left join user_contact_info uinfo on uinfo.UUID=vedio.SubUserUuid");
		counthql.append(" left join doctor_detail_info localdoc on localdoc.Id=vedio.LocalDoctorId ");
		counthql.append(" left join hospital_detail_info localhos on localhos.Id=vedio.LocalHospitalId ");
		counthql.append(" left join hospital_department_info localdep on localdep.Id=vedio.LocalDepartId ");
		counthql.append(" left join doctor_detail_info exdoc on exdoc.Id=vedio.ExpertId ");//专家
		counthql.append(" left join hospital_detail_info exdhos on exdhos.Id=exdoc.HospitalId ");//专家存在
		counthql.append(" left join hospital_department_info exddep on exddep.Id=exdoc.DepId ");//专家存在
		counthql.append(" left join hospital_detail_info exhos on exhos.Id=vedio.ExpertHospitalId ");//专家医院存在
		counthql.append(" left join hospital_department_info exdep on exdep.Id=vedio.ExpertDepId ");//专家科室存在
		counthql.append(" where 1=1 ");
		counthql.append(gainstatusquery(ostatus));
		counthql.append(gainsearchquery(searchContent,false));
		counthql.append(" and (vedio.DelFlag=0 or vedio.DelFlag IS NULL) ");
		counthql.append(" and vedio.ExpertId="+docid);
		Object effectNumber = this.hibernateTemplate
				.execute(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						return session.createSQLQuery(counthql.toString())
								.uniqueResult();
					}
				});
		Integer num = Integer.parseInt(effectNumber.toString());
		map.put("num", num);
		return map;
	}
	
	private String gainsearchquery(String search,boolean refundSearch){
		StringBuilder sb=new StringBuilder();
		if(StringUtils.isNotBlank(search)){
			sb.append(" and ( ");
			sb.append(" cinfo.ContactName like '%"+search+"%' or ");
			sb.append(" cinfo.Telephone like '%"+search+"%' or ");
			sb.append(" uinfo.Telephone like '%"+search+"%' or ");
			sb.append(" localdoc.DisplayName like '%"+search+"%' or ");
			sb.append(" localhos.DisplayName like '%"+search+"%' or ");
			sb.append(" localdep.DisplayName like '%"+search+"%' or ");
			sb.append(" exdoc.DisplayName like '%"+search+"%' or ");
			sb.append(" exdhos.DisplayName like '%"+search+"%' or ");
			sb.append(" exhos.DisplayName like '%"+search+"%' or ");
			sb.append(" exdep.DisplayName like '%"+search+"%' ");
			if(refundSearch){
				sb.append(" or ");
				sb.append(" (bpay.PayStatus=1 and (CASE WHEN (bpay.RefundStatus IS NULL or bpay.RefundStatus=0) THEN \"待退款\" WHEN bpay.RefundStatus=1 THEN \"退款成功\" "
						+ " WHEN bpay.RefundStatus=-1 THEN \"退款失败\"  END ) LIKE '%"+search+"%' ) ");
			}
			sb.append(" ) ");
		}
		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> queryVedioordersByCondition_hos(Integer hosId,
			String search, Integer ostatus,final Integer start,
			final Integer length,final Integer dtype) {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<String,Object>();
		final StringBuilder sb=new StringBuilder();
		sb.append("select vedio.Id as vedioId, ");
		sb.append(" cinfo.ContactName as userName,cinfo.sex,cinfo.Age,");
		sb.append(" (CASE WHEN cinfo.Telephone is null or cinfo.Telephone='' THEN uinfo.Telephone WHEN cinfo.Telephone is not null and cinfo.Telephone!='' THEN cinfo.Telephone END ) as telephone, ");
		sb.append(" DATE_FORMAT(vedio.CreateTime,'%Y-%m-%d %T') as createTime,vedio.ConsultationDate,vedio.ConsultationTime,vedio.PayStatus, ");
		sb.append(" localdoc.DisplayName as localDocName,localhos.DisplayName as localHosName,localdep.DisplayName as localDepName,");
		sb.append(" exdoc.DisplayName as exDocName,");
		sb.append(" (CASE WHEN vedio.ExpertId is null THEN exhos.DisplayName WHEN vedio.ExpertId is not null THEN exdhos.DisplayName END) as exHosName, ");
		sb.append(" (CASE WHEN vedio.ExpertId is null THEN exdep.DisplayName WHEN vedio.ExpertId is not null THEN exddep.DisplayName END) as exDepName, ");
		sb.append(" vedio.Status as status,vedio.PayStatus ");
		sb.append(" from business_vedio_order vedio ");
		sb.append(" left join user_case_info cinfo on vedio.CaseId=cinfo.Id ");
		sb.append(" left join user_contact_info uinfo on uinfo.UUID=vedio.SubUserUuid");
		sb.append(" left join doctor_detail_info localdoc on localdoc.Id=vedio.LocalDoctorId ");
		sb.append(" left join hospital_detail_info localhos on localhos.Id=vedio.LocalHospitalId ");
		sb.append(" left join hospital_department_info localdep on localdep.Id=vedio.LocalDepartId ");
		sb.append(" left join doctor_detail_info exdoc on exdoc.Id=vedio.ExpertId ");//专家
		sb.append(" left join hospital_detail_info exdhos on exdhos.Id=exdoc.HospitalId ");//专家存在
		sb.append(" left join hospital_department_info exddep on exddep.Id=exdoc.DepId ");//专家存在
		sb.append(" left join hospital_detail_info exhos on exhos.Id=vedio.ExpertHospitalId ");//专家医院存在
		sb.append(" left join hospital_department_info exdep on exdep.Id=vedio.ExpertDepId ");//专家科室存在
		sb.append(" where 1=1 ");
		sb.append(gainstatusquery_hos(ostatus,dtype));
		sb.append(gainsearchquery(search,false));
		sb.append(" and (vedio.DelFlag=0 or vedio.DelFlag IS NULL) ");
		if(dtype.equals(1)){
			//发起的
			sb.append(" and localhos.Id="+hosId);
		}else{
			//接受到
			sb.append(" and (exdhos.Id="+hosId+" or exhos.Id="+hosId+") ");
		}
		sb.append("  order by vedio.CreateTime desc ");
		List<VedioOrderDto> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sb
								.toString());
						query.setFirstResult(start);
						query.setMaxResults(length);
						query.setResultTransformer(Transformers
								.aliasToBean(VedioOrderDto.class));
						return query.list();
					}
				});
		map.put("items", list);
		final StringBuilder counthql = new StringBuilder();	
		counthql.append("select count(vedio.Id) ");
		counthql.append(" from business_vedio_order vedio ");
		counthql.append(" left join user_case_info cinfo on vedio.CaseId=cinfo.Id ");
		counthql.append(" left join user_contact_info uinfo on uinfo.UUID=vedio.SubUserUuid");
		counthql.append(" left join doctor_detail_info localdoc on localdoc.Id=vedio.LocalDoctorId ");
		counthql.append(" left join hospital_detail_info localhos on localhos.Id=vedio.LocalHospitalId ");
		counthql.append(" left join hospital_department_info localdep on localdep.Id=vedio.LocalDepartId ");
		counthql.append(" left join doctor_detail_info exdoc on exdoc.Id=vedio.ExpertId ");//专家
		counthql.append(" left join hospital_detail_info exdhos on exdhos.Id=exdoc.HospitalId ");//专家存在
		counthql.append(" left join hospital_department_info exddep on exddep.Id=exdoc.DepId ");//专家存在
		counthql.append(" left join hospital_detail_info exhos on exhos.Id=vedio.ExpertHospitalId ");//专家医院存在
		counthql.append(" left join hospital_department_info exdep on exdep.Id=vedio.ExpertDepId ");//专家科室存在
		counthql.append(" where 1=1 ");
		counthql.append(gainstatusquery_hos(ostatus,dtype));
		counthql.append(gainsearchquery(search,false));
		counthql.append(" and (vedio.DelFlag=0 or vedio.DelFlag IS NULL) ");
		if(dtype.equals(1)){
			//我发起的
			counthql.append(" and localhos.Id="+hosId);
		}else{
			//我接受到
			counthql.append(" and (exdhos.Id="+hosId+" or exhos.Id="+hosId+") ");
		}
		Object effectNumber = this.hibernateTemplate
				.execute(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						return session.createSQLQuery(counthql.toString())
								.uniqueResult();
					}
				});
		Integer num = Integer.parseInt(effectNumber.toString());
		map.put("num", num);
		return map;
	}

	private String gainstatusquery_hos(Integer ostatus,Integer dtype) {
		String str = "";
		switch (ostatus) {
		case 1:
			if(dtype.equals(1)){
				str = " and vedio.LocalDoctorId is null and vedio.Status in("+OrderStatusEnum.D2P_ORDER_STATUS_WAIT.getKey()+","
						+OrderStatusEnum.D2P_ORDER_STATUS_RUNNING.getKey()+") ";
			}else{
				str = " and vedio.ExpertId is null and vedio.Status in("+OrderStatusEnum.D2P_ORDER_STATUS_WAIT.getKey()+","
						+OrderStatusEnum.D2P_ORDER_STATUS_RUNNING.getKey()+") ";
			}
			break;
		case 2:
			if(dtype.equals(1)){
				str = " and vedio.LocalDoctorId is not null and vedio.Status="+OrderStatusEnum.D2P_ORDER_STATUS_WAIT.getKey();
			}else{
				str = " and vedio.ExpertId is not null and vedio.Status="+OrderStatusEnum.D2P_ORDER_STATUS_WAIT.getKey();
			}	
			break;
		case 3:
			str = " and vedio.Status="+OrderStatusEnum.D2P_ORDER_STATUS_RUNNING.getKey();
			break;
		case 4:
			str = " and vedio.Status="+OrderStatusEnum.D2P_ORDER_STATUS_COMPLETED.getKey();
			break;
		case 5:
			str = " and vedio.Status="+OrderStatusEnum.D2P_ORDER_STATUS_OUT.getKey();
			break;
		case 6:
			str = " and vedio.Status="+OrderStatusEnum.D2P_ORDER_STATUS_CANCELED.getKey();
			break;
		}
		return str;
	}
	private String gainstatusquery(Integer ostatus) {
		String str = "";
		switch (ostatus) {
		case 1:
			str = " and vedio.PayStatus=4 and vedio.Status in("+OrderStatusEnum.D2P_ORDER_STATUS_WAIT.getKey()+","+OrderStatusEnum.D2P_ORDER_STATUS_RUNNING.getKey()+") ";
			break;
		case 2:
			str = " and vedio.Status="+OrderStatusEnum.D2P_ORDER_STATUS_WAIT.getKey();
			break;
		case 3:
			str = " and vedio.Status="+OrderStatusEnum.D2P_ORDER_STATUS_RUNNING.getKey();
			break;
		case 4:
			str = " and vedio.Status="+OrderStatusEnum.D2P_ORDER_STATUS_COMPLETED.getKey();
			break;
		case 5:
			str = " and vedio.Status="+OrderStatusEnum.D2P_ORDER_STATUS_OUT.getKey();
			break;
		case 6:
			str = " and vedio.Status="+OrderStatusEnum.D2P_ORDER_STATUS_CANCELED.getKey();
			break;
		}
		return str;
	}

	@SuppressWarnings("unchecked")
	public SpecialAdviceOrder querySpecialAdviceOrderById(Integer oid) {
		// TODO Auto-generated method stub
		final StringBuilder sqlBuilder = new StringBuilder("");
		// TODO Auto-generated method stub
		sqlBuilder
		.append("SELECT sp.Id,sp.UUID as uuid,sp.CaseId,sp.DoctorId,sp.ExpertId,sp.ConsultationOpinionUuid,ca.ContactName as userName,ca.IdCard as idCard,ca.AskProblem as diseaseDes"
				+ ",ca.Sex as sex,ca.Age as age,special.DisplayName as expertName,special.HeadImageUrl as headImageUrl,"
				+ "ehos.DisplayName as hosName,edep.DisplayName as depName,doc.DisplayName as localDocName,hos.DisplayName as localHosName,"
				+ "dep.DisplayName as localDepName,sp.CreateTime,sp.PayStatus,sp.Status,sp.ReceiveTime from business_d_tuwen_order sp");
		sqlBuilder.append(" left join user_case_info ca on sp.CaseId=ca.Id ");
		sqlBuilder.append(" left join doctor_detail_info doc on doc.Id=sp.DoctorId ");//当地医生
		sqlBuilder.append(" left join doctor_detail_info special on special.Id=sp.ExpertId ");//专家
		sqlBuilder.append(" left join hospital_detail_info hos on hos.Id=doc.HospitalId");//当地医院
		sqlBuilder.append(" left join hospital_department_info dep on dep.Id=doc.DepId ");//当地科室
		sqlBuilder.append(" left join hospital_detail_info ehos on ehos.Id=special.HospitalId ");//专家医院
		sqlBuilder.append(" left join hospital_department_info edep on edep.Id=special.DepId ");//专家科室
		sqlBuilder.append(" WHERE sp.Id = " + oid);
		List<SpecialAdviceOrder> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(SpecialAdviceOrder.class));
						return query.list();
					}
				});

		return list.size() > 0 ? list.get(0) : null;
	}

	@SuppressWarnings("unchecked")
	public List<BusinessVedioOrder> getfinshvedioList(Integer sta, String startDate, String endDate, String hosid,String depid) {
		final StringBuilder sqlBuilder = new StringBuilder("");
		// TODO Auto-generated method stub
		sqlBuilder.append(" SELECT DISTINCT (e.id),e.status,e.patientName,e.sex,e.Age,e.expertName,e.ExpertId,e.hosName,e.depName,e.exProfession,e.localDocName,"
				+ " e.localProfession,e.localHosName,e.localDepName,e.CreateTime,e.ConsultationDate,e.PayStatus,e.ConsultationTime, "
				+ " (CASE WHEN e.TotalMoney IS NULL THEN e.Amount ELSE e.TotalMoney END ) AS money "
				+ "  FROM( SELECT DISTINCT (vedio.Id ) AS id,vedio.Status AS status,cinfo.ContactName AS patientName,cinfo.sex,cinfo.Age,exdoc.DisplayName AS expertName,vedio.ExpertId, "
				+ " (CASE WHEN vedio.ExpertId IS NULL THEN exhos.DisplayName WHEN vedio.ExpertId IS NOT NULL THEN exdhos.DisplayName END) AS hosName, "
				+ " (CASE WHEN vedio.ExpertId IS NULL THEN exddep.DisplayName WHEN vedio.ExpertId IS NOT NULL THEN exddep.DisplayName END) AS depName,exdoc.Profession AS exProfession, "
				+ " localdoc.DisplayName AS localDocName,localdoc.Profession AS localProfession,localhos.DisplayName AS localHosName,localdep.DisplayName AS localDepName, "
				+ " vedio.CreateTime,vedio.ConsultationDate,vedio.ConsultationTime,vedio.PayStatus,dsi.Amount, "
				+ " (SELECT b.TotalMoney FROM business_pay_info b WHERE vedio.Id = b.OrderId AND b.OrderType=4 ORDER BY b.CreateTime DESC LIMIT 1) TotalMoney "
				+ " FROM business_vedio_order vedio "
				+ " LEFT JOIN user_case_info cinfo ON vedio.CaseId=cinfo.Id  "
				+ " LEFT JOIN doctor_service_info dsi ON dsi.DoctorId=vedio.ExpertId AND dsi.ServiceId=4 "
				+ " LEFT JOIN user_contact_info uinfo ON uinfo.UUID=vedio.SubUserUuid "
				+ " LEFT JOIN doctor_detail_info exdoc ON exdoc.Id=vedio.ExpertId  "
				+ " LEFT JOIN hospital_detail_info exdhos ON exdhos.Id =exdoc.HospitalId "
				+ "  LEFT JOIN hospital_department_info exddep ON exddep.Id =exdoc.DepId "
				+ " LEFT JOIN hospital_detail_info exhos ON exhos.Id =vedio.ExpertHospitalId  "
				+ " LEFT JOIN doctor_detail_info localdoc ON localdoc.Id =vedio.LocalDoctorId "
				+ " LEFT JOIN hospital_detail_info localhos ON localhos.Id =vedio.LocalHospitalId "
				+ " LEFT JOIN hospital_department_info localdep ON localdep.Id =vedio.LocalDepartId  "
				+ " LEFT JOIN doctor_card_info doccard ON doccard.DoctorId=vedio.LocalDoctorId "
				+ " LEFT JOIN  business_pay_info b ON vedio.Id = b.OrderId "
				+ " LEFT JOIN doctor_card_info expercard ON expercard.DoctorId=vedio.ExpertId  ");
		sqlBuilder.append(" WHERE 1=1 ");
		if(sta!=null){
                sqlBuilder.append(" and vedio.Status = "+sta+" ");
		}
		if(StringUtils.isNotBlank(hosid)){
			sqlBuilder.append(" and (exdoc.HospitalId ='"+hosid+"' OR vedio.LocalHospitalId='"+hosid+"') ");
		}
		if(StringUtils.isNotBlank(depid)){
			sqlBuilder.append(" AND (exdoc.DepId='"+depid+"' OR vedio.LocalDepartId='"+depid+"') ");
		}
		if(StringUtils.isNotBlank(startDate)){
			sqlBuilder.append(" and (DATE_FORMAT(vedio.CreateTime,'%Y-%m-%d') >='"
					+ startDate + "' ) ");
		}
		if(StringUtils.isNotBlank(endDate)){
			sqlBuilder.append(" and (DATE_FORMAT(vedio.CreateTime,'%Y-%m-%d') <='"
					+ endDate + "' ) ");
		}
		sqlBuilder.append(" AND (vedio.DelFlag=0 OR vedio.DelFlag IS NULL) GROUP BY vedio.Id ORDER BY vedio.CreateTime DESC  ");
		sqlBuilder.append(" ) e ");
		List<BusinessVedioOrder> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(BusinessVedioOrder.class));
						return query.list();
					}
				});
		return list;
	}

	@SuppressWarnings("unchecked")
	public BusinessVedioOrder vedioorderRemrk(Integer sta, Integer id) {
		final StringBuilder sqlBuilder = new StringBuilder("");
		// TODO Auto-generated method stub
		sqlBuilder.append(" SELECT bvo.Remark AS remark ");
		sqlBuilder.append(" FROM business_vedio_order bvo ");
		sqlBuilder.append(" WHERE  bvo.STATUS = "+sta+" AND bvo.Id ="+id+" ");
		List<BusinessVedioOrder> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sqlBuilder
								.toString());
						query.setResultTransformer(Transformers
								.aliasToBean(BusinessVedioOrder.class));
						return query.list();
					}
				});

		return list.size() > 0 ? list.get(0) : null;
	}

	
	private String gainstatusquery_new(Integer type) {
		String str = "";
		switch (type) {
		case 0:
			//待申请
			str = " and third.Id is null and vedio.Status not in (30,40,50)";
			break;
		case 1:
			//待前质控
			str = " and (third.Status='10' or third.Status='15') ";
			break;
		case 2:		
			//待分配
			str = " and third.Status='20' ";
			break;
		case 3:
			//待报告
			str = " and third.Status='30' ";
			break;
		case 4:
			//待审核
			str = " and third.Status='40' ";
			break;
		case 5:
			//完成
			str = " and third.Status='50' ";
			break;
		case 6:
			//会诊取消
			str = " and third.Status='90' ";
			break;
		}
		return str;
	}
	@SuppressWarnings("unchecked")
	public Map<String, Object> gainrecriveorderdatas(String search, final Integer start, final Integer length, Integer type) {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<String,Object>();
		final StringBuilder sb=new StringBuilder();
		sb.append("select vedio.Id, ");
		sb.append(" cinfo.ContactName as patientName,cinfo.sex,cinfo.Age,");
		sb.append(" (CASE WHEN cinfo.Telephone is null or cinfo.Telephone='' THEN uinfo.Telephone WHEN cinfo.Telephone is not null and cinfo.Telephone!='' THEN cinfo.Telephone END ) as telephone, ");
		sb.append(" exdoc.DisplayName as expertName,vedio.ExpertId,");
		sb.append(" (CASE WHEN vedio.ExpertId is null THEN exhos.DisplayName WHEN vedio.ExpertId is not null THEN exdhos.DisplayName END) as hosName, ");
		sb.append(" (CASE WHEN vedio.ExpertId is null THEN exddep.DisplayName WHEN vedio.ExpertId is not null THEN exddep.DisplayName END) as depName, ");
		sb.append(" exdoc.Profession as exProfession,");
		sb.append(" localdoc.DisplayName as localDocName,localdoc.Profession as localProfession,localhos.DisplayName as localHosName,localdep.DisplayName as localDepName, ");
		sb.append("bpay.RefundStatus as refundStatus,date_format(bpay.RefundTime, '%Y-%m-%d %H:%i:%s') as refundTime,");
		sb.append("date_format(third.CreateTime, '%Y-%m-%d %H:%i:%s') as appTime,");
		sb.append(" vedio.CreateTime,vedio.ConsultationDate,vedio.ConsultationTime,vedio.PayStatus ");
		sb.append(" from business_vedio_order vedio ");
		sb.append(" left join user_case_info cinfo on vedio.CaseId=cinfo.Id ");
		sb.append(" left join user_contact_info uinfo on uinfo.UUID=vedio.SubUserUuid");
		sb.append(" left join doctor_detail_info exdoc on exdoc.Id=vedio.ExpertId ");//专家
		sb.append(" left join hospital_detail_info exdhos on exdhos.Id=exdoc.HospitalId ");//专家存在
		sb.append(" left join hospital_department_info exddep on exddep.Id=exdoc.DepId ");//专家存在
		sb.append(" left join hospital_detail_info exhos on exhos.Id=vedio.ExpertHospitalId ");//专家医院存在
		sb.append(" left join hospital_department_info exdep on exdep.Id=vedio.ExpertDepId ");//专家科室存在
		sb.append(" left join doctor_detail_info localdoc on localdoc.Id=vedio.LocalDoctorId ");
		sb.append(" left join hospital_detail_info localhos on localhos.Id=vedio.LocalHospitalId ");
		sb.append(" left join hospital_department_info localdep on localdep.Id=vedio.LocalDepartId ");
		sb.append(" left join business_pay_info bpay on vedio.Id =bpay.OrderId and bpay.OrderType=4 and bpay.PayStatus=1 ");
		sb.append(" left join third_order_info third on third.OrderUuid= vedio.UUID ");
		sb.append(" where 1=1 ");
		sb.append(gainstatusquery_new(type));
		sb.append(gainsearchquery(search,true));
		sb.append(" and (vedio.DelFlag=0 or vedio.DelFlag IS NULL)  and exdhos.id=34");
		sb.append("  order by vedio.CreateTime desc ");
		List<BusinessVedioOrder> list = this.hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sb
								.toString());
						query.setFirstResult(start);
						query.setMaxResults(length);
						query.setResultTransformer(Transformers
								.aliasToBean(BusinessVedioOrder.class));
						return query.list();
					}
				});
		map.put("items", list);
		final StringBuilder counthql = new StringBuilder();	
		counthql.append("select count(vedio.Id) ");
		counthql.append(" from business_vedio_order vedio ");
		counthql.append(" left join user_case_info cinfo on vedio.CaseId=cinfo.Id ");
		counthql.append(" left join user_contact_info uinfo on uinfo.UUID=vedio.SubUserUuid");
		counthql.append(" left join doctor_detail_info exdoc on exdoc.Id=vedio.ExpertId ");//专家
		counthql.append(" left join hospital_detail_info exdhos on exdhos.Id=exdoc.HospitalId ");//专家存在
		counthql.append(" left join hospital_department_info exddep on exddep.Id=exdoc.DepId ");//专家存在
		counthql.append(" left join hospital_detail_info exhos on exhos.Id=vedio.ExpertHospitalId ");//专家医院存在
		counthql.append(" left join hospital_department_info exdep on exdep.Id=vedio.ExpertDepId ");//专家科室存在
		counthql.append(" left join doctor_detail_info localdoc on localdoc.Id=vedio.LocalDoctorId ");
		counthql.append(" left join hospital_detail_info localhos on localhos.Id=vedio.LocalHospitalId ");
		counthql.append(" left join hospital_department_info localdep on localdep.Id=vedio.LocalDepartId ");
		counthql.append(" left join business_pay_info bpay on vedio.Id =bpay.OrderId and bpay.OrderType=4 and bpay.PayStatus=1 ");
		counthql.append(" left join third_order_info third on third.OrderUuid= vedio.UUID ");
		counthql.append(" where 1=1 ");
		counthql.append(gainstatusquery_new(type));
		counthql.append(gainsearchquery(search,true));
		counthql.append(" and (vedio.DelFlag=0 or vedio.DelFlag IS NULL) and exdhos.id=34 ");
		Object effectNumber = this.hibernateTemplate
				.execute(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						return session.createSQLQuery(counthql.toString())
								.uniqueResult();
					}
				});
		Integer num = Integer.parseInt(effectNumber.toString());
		map.put("num", num);
		return map;
	}
}
