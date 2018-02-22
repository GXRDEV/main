<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<!DOCTYPE html>
<html lang="en">
<head>
	<title>服务信息</title>
	<meta charset="UTF-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<link rel="stylesheet" href="/css/bootstrap.min.css" />
	<link rel="stylesheet" href="/fonticon/base/iconfont.css" type="text/css">
	<link rel="stylesheet" href="/css/view/admin.css" />
	<style>
		.pleft {
			padding-left:60px;
		}
		.redcolor {
			color: red;
		}
	</style>
</head>
<body class="exportdetail">
	<div class="row-fluid form-horizontal" id="serviceInfo">
		<div class="userinfo">
			<div class="header clearfix baseformaction">
				<h3>
				编辑服务
				</h3>								
			</div>
			<div class="bodyer">
				<div class="section">
					<div class="bodyer clearfix">
						<form id="myserviceform">
							<input type="hidden" name="id" value="${systemServiceInfo.id }"/>
							 <div class="control-group">
								<label class="control-label"><span class="redcolor">*</span>服务名称：</label>
								<div class="controls">
									<input type="text" name="serviceName" value="${systemServiceInfo.serviceName }"/>
								</div>
							 </div>
							 <div class="control-group">
								<label class="control-label">账户状态：</label>
								<div class="controls">
									<select name="userType">
										<option value="">---请选择---</option>
										<option value="1" <c:if test="${systemServiceInfo.userType== 1}">selected</c:if>>对患者</option>
										<option value="2" <c:if test="${systemServiceInfo.status== 2}">selected</c:if>>对专家</option>
										<option value="3" <c:if test="${systemServiceInfo.status== 3}">selected</c:if>>对医生</option>
										<option value="-1" <c:if test="${systemServiceInfo.status== -1}">selected</c:if>>平台服务</option>
									</select>
								</div>
							</div>
							 <div class="control-group">
								<label class="control-label">服务价格：</label>
								<div class="controls">
									<input type="number" name="servicePrice" value="${systemServiceInfo.servicePrice }"/>
								</div>
							 </div>
							<div class="control-group">
								<label class="control-label">定价参数：</label>
								<div class="controls">
									<input type="text" name="priceParameter" value="${systemServiceInfo.priceParameter }"/>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">服务状态：</label>
								<div class="controls">
									<select name="status">
										<option value="">---请选择---</option>
										<option value="1" <c:if test="${systemServiceInfo.status== 1}">selected</c:if>>启用</option>
										<option value="0" <c:if test="${systemServiceInfo.status== 0}">selected</c:if>>未启用</option>
									</select>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">服务对象：</label>
								<div class="controls">
									<select name="groupType">
										<option value="">---请选择---</option>
										<option value="1" <c:if test="${systemServiceInfo.groupType== 1}">selected</c:if>>患者服务</option>
										<option value="2" <c:if test="${systemServiceInfo.groupType== 2}">selected</c:if>>医生协作</option>
									</select>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">描述：</label>
								<div class="controls">
									<input type="text" name="description" value="${systemServiceInfo.description }"/>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">专家分成：</label>
								<div class="controls">
									<input type="number" name="expertDivided" value="${systemServiceInfo.expertDivided }"/>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">医生分成：</label>
								<div class="controls">
									<input type="number" name="doctorDivided" value="${systemServiceInfo.doctorDivided }"/>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">平台分成：</label>
								<div class="controls">
									<input type="number" name="platformDivided" value="${systemServiceInfo.platformDivided }"/>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">备注：</label>
								<div class="controls doublewidth">
									<input type="text" name="remark" value="${systemServiceInfo.remark }"/>
								</div>
							</div>

							<div class="control-group">
								<label class="control-label">分账说明：</label>
								<div class="controls doublewidth">
									<input type="text" name="serviceNote" value="${systemServiceInfo.serviceNote }"/>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">服务说明地址：</label>
								<div class="controls doublewidth">
									<input type="text" name="serviceDescUrl" value="${ systemServiceInfo.serviceDescUrl }"/>
								</div>
							</div>
							 <div class="control-group">
								<label class="control-label">服务图片：</label>
								<div class="controls">
									<div id="picsheadImageUrl" class="diyUpload">
										<div class="parentFileBox">
											<ul class="fileBoxUl clearfix">
												<c:if test="${fn:length(systemServiceInfo.imageUrl) > 1}">
													<li data-id="${systemServiceInfo.imageUrl}" class="browser">
													<div class="viewThumb">
														<img src="${systemServiceInfo.imageUrl}" />
													</div>
													<div class="diyCancel"><i class="iconfont" title="删除"></i></div>
													</li>
												</c:if>
												<li class="actionAdd">
													<div id="addheadImageUrl"><i class="iconfont">&#xe60e;</i></div>
												</li>
											</ul>
										</div>
										<input type="hidden" name="imageUrl" value="${systemServiceInfo.imageUrl}"/>
									</div>
								</div>
							 </div>
							 <div class="control-group">
								<label class="control-label">未用过的服务图片：</label>
								<div class="controls">
									<div id="picsrelatedPics" class="diyUpload">
										<div class="parentFileBox">
											<ul class="fileBoxUl clearfix">
												<c:if test="${fn:length(systemServiceInfo.imageUrlUnused) > 1}">
													<li data-id="${systemServiceInfo.imageUrlUnused}" class="browser">
														<div class="viewThumb">
															<img src="${systemServiceInfo.imageUrlUnused}" />
														</div>
														<div class="diyCancel"><i class="iconfont" title="删除"></i></div>
													</li>
												</c:if>
												<li class="actionAdd">
													<div id="addUnnsedImageUrl"><i class="iconfont">&#xe60e;</i></div>
												</li>
											</ul>
										</div>
										<input type="hidden" name="imageUrlUnused" value="${systemServiceInfo.imageUrlUnused}"/>
									</div>
								</div>
							 </div>
							<input type="hidden" name="multiplePackage" value="${systemServiceInfo.multiplePackage }"/>
						</form>
						<c:if test="${systemServiceInfo.multiplePackage== 0 }">
							<form id="siglepackform">
								<div class="control-group">
									<div style="padding-left:118px;">单种：</div>
									<input type="hidden" name="id" value="${systemServicePackage.id }"/>
									<div class="control-group pleft">
										<label class="control-label"><span class="redcolor">*</span>包名：</label>
										<div class="controls">
											<input type="text" name="packageName"placeholder ="必填项"  value="${systemServicePackage.packageName }"/>
										</div>
									</div>
									<div class="control-group pleft">
										<label class="control-label"><span class="redcolor">*</span>价格：</label>
										<div class="controls">
											<input type="number" name="packagePrice" placeholder ="必填项" value="${systemServicePackage.packagePrice }"/>
										</div>
									</div>
									<div class="control-group pleft">
										<label class="control-label">状态：</label>
										<div class="controls">
											<select name="status">
												<option value="">---请选择---</option>
												<option value="1" <c:if test="${systemServiceInfo.status== 1}">selected</c:if>>生效</option>
												<option value="0" <c:if test="${systemServiceInfo.status== 0}">selected</c:if>>不生效</option>
											</select>
										</div>
									</div>
									<div class="control-group pleft">
										<label class="control-label">描述：</label>
										<div class="controls">
											<textarea name="description">${systemServicePackage.description }</textarea>
										</div>
									</div>
								</div>
							</form>
						</c:if>
						<c:if test="${systemServiceInfo.multiplePackage== 1 }">
								<div class="control-group" id="servicepackages">
									<div style="padding-left:118px;">多种：</div>
									<c:forEach items="${systemServicePackages}" var="pack">
										<form id="'doublePackages'${pack.id }" method="post">
											<input type="hidden" name="id" value="${pack.id }"/>
											<div class="control-group pleft">
												<label class="control-label"><span class="redcolor">*</span>排序：</label>
												<div class="controls">
													<input type="number" name="rank" placeholder ="请输入1-99的整数" value="${pack.rank }"/>
												</div>
											</div>
											<div class="control-group pleft">
												<label class="control-label"><span class="redcolor">*</span>包名：</label>
												<div class="controls">
												<input type="text" name="packageName" placeholder ="必填项" value="${pack.packageName }"/>
												</div>
											</div>
											<div class="control-group pleft">
												<label class="control-label"><span class="redcolor">*</span>价格：</label>
												<div class="controls">
												<input type="number" name="packagePrice" placeholder ="必填项" value="${pack.packagePrice }"/>
												</div>
											</div>
											<div class="control-group pleft">
												<label class="control-label">状态：</label>
												<div class="controls">
													<select name="status">
														<option value="1" <c:if test="${pack.status== 1}">selected</c:if>>生效</option>
														<option value="0" <c:if test="${pack.status== 0}">selected</c:if>>不生效</option>
													</select>
												</div>
											</div>
											<div class="control-group pleft">
												<label class="control-label">描述：</label>
												<div class="controls">
													<textarea name="description">${pack.description }</textarea>
												</div>
											</div>
										</form>
										<hr/>
									</c:forEach>
								</div>
						</c:if>
						<div class="form-action">
							<button type="button" class="btn btn-primary" name="submitServiceform">保存</button>
						</div>
				</div>
			</div>
		</div>
	</div>
	</div>
	<script src="/js/jquery.min.js"></script>
	<script src="/js/bootstrap.min.js"></script>
	<script src="/sea-modules/seajs/2.2.3/sea.js" id="seajsnode"></script>
	<script src="/sea-modules/seajs/seajs.config.js"></script>
	<script type="text/javascript">
		var _burl='/',_h = _burl,_id = '';
		seajs.use('view/admin/main',function(controller){
			controller.serviceinfoform();
		});
	</script>
</body>
</html>

