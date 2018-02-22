package com.tspeiz.modules.app.controller.nurse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tspeiz.modules.app.base.BaseController;

@Controller
@RequestMapping("/nurse:{nurseId}")
public class NurseOtherController extends BaseController {
/*
	@Resource(name = "appNurseService")
	private IAppNurseService appNurseService;

	public NurseOtherController() {
		// TODO Auto-generated constructor stub
	}

	@RequestMapping(value = "/uploadIDNoPhoto", method = RequestMethod.POST)
	@ResponseBody
	public ReturnValueNet<String> updateCertificationInfo(
			@PathVariable Integer nurseId,
			@RequestParam(value = "file") MultipartFile file,
			HttpServletRequest request) {

		ReturnValueNet<String> returnValueNet = new ReturnValueNet<String>();
		try {

			request.setCharacterEncoding("UTF-8");

			NurseInfo nurseInfo = appNurseService.getNurseInfoById(nurseId);

			if (nurseInfo == null) {
				returnValueNet.setCode(AppReturnCodeEnum.CODE_NURSE_NOT_EXIST
						.getCode());
				returnValueNet.setDesc(AppReturnCodeEnum.CODE_NURSE_NOT_EXIST
						.getDesc());

				return returnValueNet;
			}

			String nurseName = nurseInfo.getRealName();
			if (StringUtil.isBlank(nurseName)) {
				nurseName = "IDNoPhoto";
			}

			String uploadPath = request
					.getSession()
					.getServletContext()
					.getRealPath("/upload/头像/护士/" + nurseInfo.getRegisterName());
			String srcfileName = file.getOriginalFilename();
			String suffix = srcfileName.substring(srcfileName.lastIndexOf("."));
			nurseName = nurseName + suffix;
			File targetFile = new File(uploadPath, nurseName);
			if (!targetFile.exists()) {
				targetFile.mkdirs();
			}

			file.transferTo(targetFile);

			String relativeFilePath = AppFileUpload.getRelativePath(targetFile
					.getAbsolutePath());
			System.out.print("=====uploadIDNoPhoto:relativeFilePath "
					+ relativeFilePath);

			returnValueNet.setCode(AppReturnCodeEnum.CODE_OK.getCode());
			returnValueNet.setResult(relativeFilePath);

			return returnValueNet;

		} catch (Exception e) {
			// TODO: handle exception
			returnValueNet.setCode(AppReturnCodeEnum.CODE_EXCEPTION.getCode());
			returnValueNet.setDesc(AppReturnCodeEnum.CODE_EXCEPTION.getDesc());
			e.printStackTrace();
			log.error(getFileLineMethod() + e.getMessage());
			return returnValueNet;
		}
	}

	@RequestMapping(value = "/uploadIDCardPhoto", method = RequestMethod.POST)
	@ResponseBody
	public ReturnValueNet<String> uploadIDCardPhoto(
			@PathVariable Integer nurseId,
			@RequestParam(value = "file") MultipartFile file,
			HttpServletRequest request) {

		ReturnValueNet<String> returnValueNet = new ReturnValueNet<String>();
		try {

			System.out.print("=====uploadIDCardPhoto:nurseId " + nurseId);

			request.setCharacterEncoding("UTF-8");

			NurseInfo nurseInfo = appNurseService.getNurseInfoById(nurseId);

			if (nurseInfo == null) {
				returnValueNet.setCode(AppReturnCodeEnum.CODE_NURSE_NOT_EXIST
						.getCode());
				returnValueNet.setDesc(AppReturnCodeEnum.CODE_NURSE_NOT_EXIST
						.getDesc());

				return returnValueNet;
			}

			String uploadPath = request
					.getSession()
					.getServletContext()
					.getRealPath(
							"/upload/护士/" + nurseInfo.getRegisterName() + "/证件");

			String srcfileName = file.getOriginalFilename();
			String suffix = srcfileName.substring(srcfileName.lastIndexOf("."));
			String idCardPhoto = "IDCardPhoto" + suffix;
			File targetFile = new File(uploadPath, idCardPhoto);
			if (!targetFile.exists()) {
				targetFile.mkdirs();
			}

			file.transferTo(targetFile);

			String relativeFilePath = AppFileUpload.getRelativePath(targetFile
					.getAbsolutePath());
			System.out.print("=====uploadIDCardPhoto:relativeFilePath "
					+ relativeFilePath);

			returnValueNet.setCode(AppReturnCodeEnum.CODE_OK.getCode());
			returnValueNet.setResult(relativeFilePath);
			return returnValueNet;

		} catch (Exception e) {
			// TODO: handle exception
			returnValueNet.setCode(AppReturnCodeEnum.CODE_EXCEPTION.getCode());
			returnValueNet.setDesc(AppReturnCodeEnum.CODE_EXCEPTION.getDesc());
			e.printStackTrace();
			log.error(getFileLineMethod() + e.getMessage());
			return returnValueNet;
		}
	}

	@RequestMapping(value = "/uploadQualificationPhoto", method = RequestMethod.POST)
	@ResponseBody
	public ReturnValueNet<String> uploadQualificationPhoto(
			@PathVariable Integer nurseId,
			@RequestParam(value = "file") MultipartFile file,
			HttpServletRequest request) {

		ReturnValueNet<String> returnValueNet = new ReturnValueNet<String>();
		try {

			System.out.print("=====uploadIDNoPhoto:nurseId " + nurseId);

			request.setCharacterEncoding("UTF-8");

			NurseInfo nurseInfo = appNurseService.getNurseInfoById(nurseId);

			if (nurseInfo == null) {
				returnValueNet.setCode(AppReturnCodeEnum.CODE_NURSE_NOT_EXIST
						.getCode());
				returnValueNet.setDesc(AppReturnCodeEnum.CODE_NURSE_NOT_EXIST
						.getDesc());

				return returnValueNet;
			}

			String uploadPath = request
					.getSession()
					.getServletContext()
					.getRealPath(
							"/upload/护士/" + nurseInfo.getRegisterName() + "/证件");
			String srcfileName = file.getOriginalFilename();
			String suffix = srcfileName.substring(srcfileName.lastIndexOf("."));
			String idCardPhoto = "QualificationPhoto" + suffix;
			File targetFile = new File(uploadPath, idCardPhoto);
			if (!targetFile.exists()) {
				targetFile.mkdirs();
			}

			file.transferTo(targetFile);

			String relativeFilePath = AppFileUpload.getRelativePath(targetFile
					.getAbsolutePath());
			System.out.print("=====uploadQualificationPhoto:relativeFilePath "
					+ relativeFilePath);

			returnValueNet.setCode(AppReturnCodeEnum.CODE_OK.getCode());
			returnValueNet.setResult(relativeFilePath);

			return returnValueNet;

		} catch (Exception e) {
			// TODO: handle exception
			returnValueNet.setCode(AppReturnCodeEnum.CODE_EXCEPTION.getCode());
			returnValueNet.setDesc(AppReturnCodeEnum.CODE_EXCEPTION.getDesc());
			e.printStackTrace();
			log.error(getFileLineMethod() + e.getMessage());
			return returnValueNet;
		}
	}

	//
	@RequestMapping(value = "/updateCertificationInfo", method = RequestMethod.PUT)
	@ResponseBody
	public ReturnValueNet<Integer> updateCertificationInfo(
			@PathVariable Integer nurseId,
			@RequestBody Map<String, Object> updateFields) {
		ReturnValueNet<Integer> returnValueNet = new ReturnValueNet<Integer>();

		try {

			if (updateFields != null) {
				// 注册名，密码 ，余额，电话号码 不能修改
				updateFields.remove("registerTime");
				updateFields.remove("registerName");
				updateFields.remove("password");
				updateFields.remove("telephone");
				updateFields.remove("balance");
			}

			if (updateFields.get("sex").toString().equalsIgnoreCase("false")) {
				updateFields.put("sex", "0");
			} else if (updateFields.get("sex").toString()
					.equalsIgnoreCase("true")) {
				updateFields.put("sex", "1");
			}

			Integer code = appNurseService.updateNurseAutInfo(nurseId,
					updateFields);

			returnValueNet.setCode(code);
			returnValueNet.setDesc(AppReturnCodeEnum.getCodeDesc(code));

			return returnValueNet;

		} catch (Exception e) {
			// TODO: handle exception
			returnValueNet.setCode(AppReturnCodeEnum.CODE_EXCEPTION.getCode());
			returnValueNet.setDesc(AppReturnCodeEnum.CODE_EXCEPTION.getDesc());
			e.printStackTrace();
			log.error(getFileLineMethod() + e.getMessage());
			return returnValueNet;
		}
	}

	@RequestMapping(value = "/updateCertificationInfoEx", method = RequestMethod.POST)
	@ResponseBody
	public ReturnValueNet<Map<String, Object>> updateCertificationInfoEx(
			@PathVariable Integer nurseId, HttpServletRequest request) {
		ReturnValueNet<Map<String, Object>> returnValueNet = new ReturnValueNet<Map<String, Object>>();
		try {

			request.setCharacterEncoding("UTF-8");

			System.out.println("===上updateCertificationInfoEx==");

			NurseInfo nurseInfo = appNurseService.getNurseInfoById(nurseId);

			if (nurseInfo == null) {
				returnValueNet.setCode(AppReturnCodeEnum.CODE_NURSE_NOT_EXIST
						.getCode());
				returnValueNet.setDesc(AppReturnCodeEnum.CODE_NURSE_NOT_EXIST
						.getDesc());

				return returnValueNet;
			}

			String uploadPath = request
					.getSession()
					.getServletContext()
					.getRealPath(
							"/upload/护士/" + nurseInfo.getRegisterName() + "/证件");

			System.out.println("===上uploadPath==" + uploadPath);

			// 判断是否是multipart类型
			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			if (!isMultipart) {

				returnValueNet
						.setCode(AppReturnCodeEnum.CODE_NOT_MULTIPART_REQUEST
								.getCode());
				returnValueNet
						.setDesc(AppReturnCodeEnum.CODE_NOT_MULTIPART_REQUEST
								.getDesc());

				return returnValueNet;
			}

			String tmpPath = request.getSession().getServletContext()
					.getRealPath("/temp");
			// 用 DiskFileItemFactory 创建新的 file items （只是临时的）
			// DiskFileItemFactory 创建FileItem 实例，并保存其内容在<b>内存</b>或者<b>硬盘中</b>
			// 通过一个阀值来决定这个 FileItem 实例是存放在<b>内存</b>或者<b>硬盘中</b>
			DiskFileItemFactory factory = new DiskFileItemFactory();
			// 设置阀值大小
			// 不设置的话，默认10k
			// 超过这个阀值，FileItem将直接写入到磁盘
			factory.setSizeThreshold(1024 * 10);
			// 设置临时文件夹
			// 不设置，默认为系统默认Temp文件路径，调用 System.getProperty("java.io.tmpdir") 获取
			// 超过阀值的 FileItem 实例将存放在这个目录中
			factory.setRepository(new File(tmpPath));

			// 构造servletFileUpload 的实例，该实例提供工厂模式创建FileItem的DiskFileItemFactory实例
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setHeaderEncoding("UTF-8");
			// 设置一个<b>完整的请求</b>允许的最大大小(注意是完整请求，包括非file类型的表单,比如Text类
			upload.setFileSizeMax(1000 * 1024 * 1024);// 10M
			// 设置一个<b>完整的请求</b>允许的最大大小(注意是完整请求，包括非file类型的表单,比如Text类型)
			upload.setSizeMax(1000 * 1024 * 1024);// 总文件大小

			Map<String, Object> requestParams = new HashMap<String, Object>();
			// 解析请求内容
			List<FileItem> fileItems = upload.parseRequest(request);

			for (FileItem item : fileItems) {
				// 不是普通表单字段
				if (!item.isFormField()) {
				} else {
					// 把请求参数放入map中
					String paramName = item.getFieldName();
					String paramValue = item.getString();
					System.out.println(paramName + ":" + paramValue);
					requestParams.put(paramName, paramValue);

				}
			}

			for (FileItem item : fileItems) {
				// 不是普通表单字段
				if (!item.isFormField()) {

					String paramName = item.getFieldName();
					String fileName = item.getName();
					if (fileName == null || fileName.trim().equals("")) {
						continue;
					}
					fileName = fileName
							.substring(fileName.lastIndexOf("\\") + 1);// a.txt
					String fileExt = fileName.substring(fileName
							.lastIndexOf("."));

					if (paramName.equalsIgnoreCase("IDNoPhoto")) {

						fileName = "IDNoPhoto" + fileExt;

						uploadPath = request
								.getSession()
								.getServletContext()
								.getRealPath(
										"/upload/头像/护士/"
												+ nurseInfo.getRegisterName());

					} else if (paramName.equalsIgnoreCase("IDCardPhoto")) {

						fileName = "IDCardPhoto" + fileExt;

						uploadPath = request
								.getSession()
								.getServletContext()
								.getRealPath(
										"/upload/护士/"
												+ nurseInfo.getRegisterName()
												+ "/证件");
					} else if (paramName.equalsIgnoreCase("QualificationPhoto")) {

						fileName = "QualificationPhoto" + fileExt;

						uploadPath = request
								.getSession()
								.getServletContext()
								.getRealPath(
										"/upload/护士/"
												+ nurseInfo.getRegisterName()
												+ "/证件");
					}

					System.out.println("---上传图片名称----" + fileName);

					if (!AppFileUpload.isPicture(fileExt))
						System.out.println("您上传的不是图片！");

					String fullFilePath = AppFileUpload.makeDirs(uploadPath)
							+ "/" + fileName;
					int pos = fullFilePath.indexOf("\\upload");
					String relativeFilePath = fullFilePath.substring(pos + 1);
					relativeFilePath = relativeFilePath.replaceAll("\\\\", "/");
					System.out.println("===上传路径===" + relativeFilePath);

					// 保存参数
					requestParams.put(paramName, relativeFilePath);

					InputStream in = item.getInputStream();// 获取输入流
					OutputStream out = new FileOutputStream(fullFilePath);
					Streams.copy(in, out, true);
					in.close();
					out.close();
					item.delete();// 删除临时文件
				}
			}

			if (requestParams.size() > 0) {

				if (requestParams != null) {
					// 注册名，密码 ，余额，电话号码 不能修改
					requestParams.remove("registerTime");
					requestParams.remove("registerName");
					requestParams.remove("password");
					requestParams.remove("telephone");
					requestParams.remove("balance");
				}

				Integer code = appNurseService.updateNurseAutInfo(nurseId,
						requestParams);

				returnValueNet.setResult(requestParams);
				returnValueNet.setCode(code);
				returnValueNet.setDesc(AppReturnCodeEnum.getCodeDesc(code));

			} else {

				returnValueNet.setCode(AppReturnCodeEnum.CODE_PARAM_ERROR
						.getCode());
				returnValueNet.setDesc(AppReturnCodeEnum.CODE_PARAM_ERROR
						.getDesc());
			}

			return returnValueNet;

		} catch (Exception e) {
			// TODO: handle exception
			returnValueNet.setCode(AppReturnCodeEnum.CODE_EXCEPTION.getCode());
			returnValueNet.setDesc(AppReturnCodeEnum.CODE_EXCEPTION.getDesc());
			e.printStackTrace();
			log.error(getFileLineMethod() + e.getMessage());
			return returnValueNet;
		}
	}

	*//**********************************************************************//*
	@RequestMapping(value = "/preReceiveOrder", method = RequestMethod.PUT)
	@ResponseBody
	public ReturnValueNet<Integer> receiveOrder(@PathVariable Integer nurseId,
			@RequestParam("orderId") Integer orderId) {
		ReturnValueNet<Integer> returnValueNet = new ReturnValueNet<Integer>();
		try {

			Integer code = appNurseService.preReceiveOrder(nurseId, orderId);

			returnValueNet.setCode(code);
			returnValueNet.setDesc(AppReturnCodeEnum.getCodeDesc(code));

			return returnValueNet;

		} catch (Exception e) {
			// TODO: handle exception
			returnValueNet.setCode(AppReturnCodeEnum.CODE_EXCEPTION.getCode());
			returnValueNet.setDesc(AppReturnCodeEnum.CODE_EXCEPTION.getDesc());
			e.printStackTrace();
			log.error(getFileLineMethod() + e.getMessage());
			return returnValueNet;
		}
	}

	@RequestMapping(value = "/startOrderService", method = RequestMethod.PUT)
	@ResponseBody
	public ReturnValueNet<Integer> startOrder(@PathVariable Integer nurseId,
			@RequestParam("orderId") Integer orderId) {
		ReturnValueNet<Integer> returnValueNet = new ReturnValueNet<Integer>();
		try {

			Integer code = appNurseService.startOrderService(nurseId, orderId);

			returnValueNet.setCode(code);
			returnValueNet.setDesc(AppReturnCodeEnum.getCodeDesc(code));

			return returnValueNet;

		} catch (Exception e) {
			// TODO: handle exception
			returnValueNet.setCode(AppReturnCodeEnum.CODE_EXCEPTION.getCode());
			returnValueNet.setDesc(AppReturnCodeEnum.CODE_EXCEPTION.getDesc());
			e.printStackTrace();
			log.error(getFileLineMethod() + e.getMessage());
			return returnValueNet;
		}
	}

	@RequestMapping(value = "/endOrderService", method = RequestMethod.PUT)
	@ResponseBody
	public ReturnValueNet<Integer> completeOrder(@PathVariable Integer nurseId,
			@RequestParam("orderId") Integer orderId) {
		ReturnValueNet<Integer> returnValueNet = new ReturnValueNet<Integer>();
		try {

			Integer code = appNurseService.endOrderService(nurseId, orderId);

			returnValueNet.setCode(code);
			returnValueNet.setDesc(AppReturnCodeEnum.getCodeDesc(code));

			return returnValueNet;

		} catch (Exception e) {
			// TODO: handle exception
			returnValueNet.setCode(AppReturnCodeEnum.CODE_EXCEPTION.getCode());
			returnValueNet.setDesc(AppReturnCodeEnum.CODE_EXCEPTION.getDesc());
			e.printStackTrace();
			log.error(getFileLineMethod() + e.getMessage());
			return returnValueNet;
		}
	}

	@RequestMapping(value = "/cancelOrder", method = RequestMethod.PUT)
	@ResponseBody
	public ReturnValueNet<Integer> cancelOrder(@PathVariable Integer nurseId,
			@RequestParam("orderId") Integer orderId,
			@RequestBody Map<String, Object> map) {
		ReturnValueNet<Integer> returnValueNet = new ReturnValueNet<Integer>();
		try {

			Integer needManual = Integer.parseInt(map.get("needManual")
					.toString());
			String reason = map.get("reason").toString();

			Integer code = appNurseService.cancelOrder(nurseId, orderId,
					needManual, reason);

			returnValueNet.setCode(code);
			returnValueNet.setDesc(AppReturnCodeEnum.getCodeDesc(code));

			return returnValueNet;

		} catch (Exception e) {
			// TODO: handle exception
			returnValueNet.setCode(AppReturnCodeEnum.CODE_EXCEPTION.getCode());
			returnValueNet.setDesc(AppReturnCodeEnum.CODE_EXCEPTION.getDesc());
			e.printStackTrace();
			log.error(getFileLineMethod() + e.getMessage());
			return returnValueNet;
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////
	*//**
	 * 获取护士的所有订单
	 * 
	 * @param nurseId
	 * @param params
	 * @return
	 *//*
	@RequestMapping(value = "/escortOrders", method = RequestMethod.GET)
	@ResponseBody
	public ReturnValueNet<List<MobilePeizhenOrder>> escortOrders(
			@PathVariable Integer nurseId,
			@RequestParam("pageNo") Integer pageNo,
			@RequestParam("pageSize") Integer pageSize,
			@RequestParam("findStyle") String findStyle) {
		ReturnValueNet<List<MobilePeizhenOrder>> returnValueNet = new ReturnValueNet<List<MobilePeizhenOrder>>();
		try {

			List<MobilePeizhenOrder> orders = appNurseService
					.getEscortOrdersByNurseId(nurseId, pageNo, pageSize,
							findStyle);

			returnValueNet.setCode(AppReturnCodeEnum.CODE_OK.getCode());
			returnValueNet.setResult(orders);

			return returnValueNet;

		} catch (Exception e) {
			// TODO: handle exception
			returnValueNet.setCode(AppReturnCodeEnum.CODE_EXCEPTION.getCode());
			returnValueNet.setDesc(AppReturnCodeEnum.CODE_EXCEPTION.getDesc());
			e.printStackTrace();
			log.error(getFileLineMethod() + e.getMessage());
			return returnValueNet;
		}
	}

	*//**
	 * 获取护士正在处理的订单
	 * 
	 * @param nurseId
	 * @param params
	 * @return
	 *//*
	@RequestMapping(value = "/ongoingOrders", method = RequestMethod.GET)
	@ResponseBody
	public ReturnValueNet<List<MobilePeizhenOrder>> ongoingOrders(
			@PathVariable Integer nurseId) {
		ReturnValueNet<List<MobilePeizhenOrder>> returnValueNet = new ReturnValueNet<List<MobilePeizhenOrder>>();
		try {

			List<MobilePeizhenOrder> orders = appNurseService
					.getOngoingOrdersByNurseId(nurseId);

			returnValueNet.setCode(AppReturnCodeEnum.CODE_OK.getCode());
			returnValueNet.setResult(orders);

			return returnValueNet;

		} catch (Exception e) {
			// TODO: handle exception
			returnValueNet.setCode(AppReturnCodeEnum.CODE_EXCEPTION.getCode());
			returnValueNet.setDesc(AppReturnCodeEnum.CODE_EXCEPTION.getDesc());
			e.printStackTrace();
			log.error(getFileLineMethod() + e.getMessage());
			return returnValueNet;
		}
	}

	*//**
	 * 护士预接单的订单
	 * 
	 * @param nurseId
	 * @return
	 *//*
	@RequestMapping(value = "/nursePreOrders", method = RequestMethod.GET)
	@ResponseBody
	public ReturnValueNet<List<MobilePeizhenOrder>> preOrdersByNurseId(
			@PathVariable Integer nurseId) {
		ReturnValueNet<List<MobilePeizhenOrder>> returnValueNet = new ReturnValueNet<List<MobilePeizhenOrder>>();
		try {

			List<MobilePeizhenOrder> orders = appNurseService
					.getPreOrdersByNurseId(nurseId);

			returnValueNet.setCode(AppReturnCodeEnum.CODE_OK.getCode());
			returnValueNet.setResult(orders);

			return returnValueNet;

		} catch (Exception e) {
			// TODO: handle exception
			returnValueNet.setCode(AppReturnCodeEnum.CODE_EXCEPTION.getCode());
			returnValueNet.setDesc(AppReturnCodeEnum.CODE_EXCEPTION.getDesc());
			e.printStackTrace();
			log.error(getFileLineMethod() + e.getMessage());
			return returnValueNet;
		}
	}

	*//**
	 * 所有待分配的订单不包括护士预接单的订单
	 * 
	 * @param nurseId
	 * @return
	 *//*
	@RequestMapping(value = "/unassignedOrdersExcludePre", method = RequestMethod.GET)
	@ResponseBody
	public ReturnValueNet<List<MobilePeizhenOrder>> unassignedOrdersExcludePre(
			@PathVariable Integer nurseId,
			@RequestParam("pageNo") Integer pageNo,
			@RequestParam("pageSize") Integer pageSize,
			@RequestParam("findStyle") String findStyle) {
		ReturnValueNet<List<MobilePeizhenOrder>> returnValueNet = new ReturnValueNet<List<MobilePeizhenOrder>>();
		try {

			List<MobilePeizhenOrder> orders = appNurseService
					.getUnassignedOrdersExcludeNursePre(pageNo, pageSize,
							findStyle, nurseId);

			returnValueNet.setCode(AppReturnCodeEnum.CODE_OK.getCode());
			returnValueNet.setResult(orders);

			return returnValueNet;

		} catch (Exception e) {
			// TODO: handle exception
			returnValueNet.setCode(AppReturnCodeEnum.CODE_EXCEPTION.getCode());
			returnValueNet.setDesc(AppReturnCodeEnum.CODE_EXCEPTION.getDesc());
			e.printStackTrace();
			log.error(getFileLineMethod() + e.getMessage());
			return returnValueNet;
		}
	}

	*//**
	 * 
	 * @param nurseId
	 * @param pageNo
	 * @param pageSize
	 * @param findStyle
	 * @return
	 *//*
	@RequestMapping(value = "/nurseBills", method = RequestMethod.GET)
	@ResponseBody
	public ReturnValueNet<List<NurseBill>> getNurseBills(
			@PathVariable Integer nurseId,
			@RequestParam("pageNo") Integer pageNo,
			@RequestParam("pageSize") Integer pageSize,
			@RequestParam("findStyle") String findStyle) {
		ReturnValueNet<List<NurseBill>> returnValueNet = new ReturnValueNet<List<NurseBill>>();
		try {

			List<NurseBill> orders = appNurseService.getNurseBillsByNurseId(
					nurseId, pageNo, pageSize, findStyle);

			returnValueNet.setCode(AppReturnCodeEnum.CODE_OK.getCode());
			returnValueNet.setResult(orders);

			return returnValueNet;

		} catch (Exception e) {
			// TODO: handle exception
			returnValueNet.setCode(AppReturnCodeEnum.CODE_EXCEPTION.getCode());
			returnValueNet.setDesc(AppReturnCodeEnum.CODE_EXCEPTION.getDesc());
			e.printStackTrace();
			log.error(getFileLineMethod() + e.getMessage());
			return returnValueNet;
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////////

	@RequestMapping(value = "/applyWithdrawCash", method = RequestMethod.POST)
	@ResponseBody
	public ReturnValueNet<Integer> applyWithdrawCash(
			@PathVariable Integer nurseId,
			@RequestBody NurseWithdrawCash nurseWithdrawCash) {
		ReturnValueNet<Integer> returnValueNet = new ReturnValueNet<Integer>();
		try {

			JSONObject json = JSONObject.fromObject(nurseWithdrawCash);

			System.out.print("===applyWithdrawCash:" + json.toString());

			Integer code = appNurseService.applyWithdrawCash(nurseId,
					nurseWithdrawCash);

			returnValueNet.setCode(code);
			returnValueNet.setDesc(AppReturnCodeEnum.getCodeDesc(code));

			return returnValueNet;
		} catch (Exception e) {
			// TODO: handle exception
			returnValueNet.setCode(AppReturnCodeEnum.CODE_EXCEPTION.getCode());
			returnValueNet.setDesc(AppReturnCodeEnum.CODE_EXCEPTION.getDesc());
			e.printStackTrace();
			log.error(getFileLineMethod() + e.getMessage());
			return returnValueNet;
		}
	}

	@RequestMapping(value = "/nurseBankCards", method = RequestMethod.GET)
	@ResponseBody
	public ReturnValueNet<List<NurseBankCard>> nurseBankCards(
			@PathVariable Integer nurseId) {
		ReturnValueNet<List<NurseBankCard>> returnValueNet = new ReturnValueNet<List<NurseBankCard>>();
		try {

			List<NurseBankCard> cards = appNurseService
					.getNurseBankCards(nurseId);

			returnValueNet.setCode(AppReturnCodeEnum.CODE_OK.getCode());
			returnValueNet.setResult(cards);

			return returnValueNet;
		} catch (Exception e) {
			// TODO: handle exception
			returnValueNet.setCode(AppReturnCodeEnum.CODE_EXCEPTION.getCode());
			returnValueNet.setDesc(AppReturnCodeEnum.CODE_EXCEPTION.getDesc());
			e.printStackTrace();
			log.error(getFileLineMethod() + e.getMessage());
			return returnValueNet;
		}
	}
*/
}
