<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>护士提现</title>
<link rel="stylesheet" href="//css/bootstrap.min.css" />
<link href="//admin/content.css" type="text/css" rel="stylesheet" />
<link href="//admin/input.css" type="text/css" rel="stylesheet" />
<link rel="stylesheet" href="//css/select2.css" />
<link rel="stylesheet" href="//css/datepicker.css" />
<style></style>
<script type="text/javascript" src="//js/jquery-1.8.3.js"></script>
<script type="text/javascript" src="//js/jquery.pager.js"></script>
<script type="text/javascript" src="//admin/content.js"></script>
<script src="//js/bootstrap-datepicker.js" charset="utf-8"></script>
<script src="//js/select2.min.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		var pageNumber = ${
			pager.pageNumber
		};//页面索引初始值 
		var pageCount = ${
			pager.pageCount
		};//页数
		$("#pager").pager({
			pagenumber : pageNumber,
			pagecount : pageCount,
			buttonClickCallback : PageClick
		});
	});
	PageClick = function(pageclickednumber) {
		$("#pageNumber").val(pageclickednumber);
		$("#listForm").attr("action","//admin/orders.do");
		$("#listForm").submit();
	};
</script>

<script type="text/javascript">
	$(function(){
		$("input[ltype='date']").datepicker({
	         format: 'yyyy-mm-dd',
	         autoclose: true,
	         todayBtn: 'linked',              
        });
		$("select").select2();
		
		$("#sizeSet").change(function(){
			$("#listForm").attr("action","//admin/orders.do");
			$("#listForm").submit();
		});
		var pagersize = $("#pagersizeid").val();
		if (!$.trim(pagersize) == "") {
			var $ops = $("#sizeSet").find("option");
			$ops.each(function() {
				if ($.trim($(this).val()) == pagersize) {
					$("#s2id_sizeSet a span").html($(this).text());
					$(this).attr("selected", true);
				}
			});
		}
	});
</script>
</head>
<body>
	<form id="listForm" action="" method="post" name="listForm">
		<input type="hidden" name="pageNumber" id="pageNumber"
			value="${pager.pageNumber}" /> <input type="hidden"
			name="orderBy" id="orderBy" value="${pager.orderBy}" /> <input
			type="hidden" name="pageCount" id="pageCount"
			value="${pager.pageCount}" /> <input type="hidden"
			name="pager.orderType" id="order" value="${pager.orderType}" />
			
			<div class="clear" style="both:clear"></div>
			<div class="batch_op">
			<div class="list_icon"></div>
			<span>订单列表</span>
			<div class="op_btn">
				<a href="javascript:void(0);" id="dels">
					<img src="./images/edit.gif" width="10" height="10">删除&nbsp;&nbsp;&nbsp;&nbsp;
				</a>
			</div>
		</div>
		<div class="table_list">
			<table>
				<thead>
					<tr>
						<th><input type="checkbox" value="全选" id="allCheck"></th>
						<th>订单号</th>
						<th>下单时间</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${orders}" var="order" varStatus="stat">
						<tr>
							<td class="ckbox"><input type="checkbox" name="ids" value="${order.id}"/></td>
							<td>${order.id}</td>
							<td>${order.createTime}</td>
							<td></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<div class="page">
				<div class="page_item">
					共${pager.totalCount}条记录 每页 <input type="hidden" name=""
						value="${pager.pageSize }" id="pagersizeid" /> <select
						name="pageSize" id="sizeSet" style="width:70px; height:20px;">
						<option value="10">10</option>
						<option value="15">15</option>
						<option value="20">20</option>
					</select> 条 当前 ${pager.pageNumber}/${pager.pageCount}页
				</div>
			</div>
			<div class="pager" id="pager">		
			</div>
		</div>
	</form>
</body>
</html>