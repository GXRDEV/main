<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<script type="text/html" id="report_model">
			<div class="report small">
				<h4>六安市立医院{record_name}</h4>
				<div class="reportbaseinfo clearfix">
					<span class="baseitem">
						<label>姓名：</label><span>{姓名}</span>
					</span>
					<span class="baseitem">
						<label>性别：</label><span>{性别}</span>
					</span>
					<span class="baseitem">
						<label>年龄：</label><span>{年龄}</span>
					</span>
					<span class="baseitem">
						<label>标识号：</label><span>{标识号}</span>
					</span>
					<span class="baseitem">
						<label>床号：</label><span>{当前床号}</span>
					</span>
					<span class="baseitem">
						<label>送检医生：</label><span>{送检医生}</span>
					</span>
					<span class="baseitem">
						<label>科室：</label><span>{送检科室}</span>
					</span>
					<span class="baseitem">
						<label>标本类型：</label><span>{标本类型}</span>
					</span>
					<span class="baseitem">
						<label>标本号：</label><span>{标本序号}</span>
					</span>
					<span class="baseitem">
						<label>检验项目：</label><span>{检验项目}</span>
					</span>
				</div>
				<div class="reporttable clearfix">
					<div class="span6">
						<table class="table">
							<thead>
								<tr>
									<th></th>
									<th>检验项目名称</th>
									<th>结果</th>
									<th>标志</th>
									<th>参考区间</th>
									<th>单位</th>
								</tr>
							</thead>
							<tbody>{tbody}</tbody>
						</table>
					</div>
					<div class="span6 ansysdata">
						<table class="table">
							<thead>
								<tr>
									<th></th>
									<th>检验项目名称</th>
									<th>结果</th>
									<th>标志</th>
									<th>参考区间</th>
									<th>单位</th>
								</tr>
							</thead>
							<tbody>{tbody1}</tbody>
						</table>
					</div>
				</div>
				<div class="reportfooter clearfix">
					<span class="baseitem">
						<label>接收时间：</label><span>{接收时间}</span>
					</span>
					<span class="baseitem">
						<label>报告时间：</label><span>{报告时间}</span>
					</span>
					<span class="baseitem">
						<label>检验医师：</label><span>{检验医师}</span>
					</span>
					<span class="baseitem">
						<label>审核医师：</label><span>{审核医师}</span>
					</span>
					<span class="baseitem">
						<label>采样时间：</label><span>{采样时间}</span>
					</span>
					<span class="baseitem">
						<label>报告备注：</label><span>{检验备注}</span>
					</span>
				</div>
				<div class="reportremark">
					<h6>**此结果仅对本标负责**  如对检验结果有疑义，请在报告发出三日内与检验科联系核对复查。</h6>
				</div>
			</div>
</script>