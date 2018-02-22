package com.tspeiz.modules.app.controller.resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tspeiz.modules.app.base.BaseController;

@Controller
@RequestMapping("/resource")
public class ResourceController extends BaseController {

	/*@Resource(name = "appResourceService")
	private IAppResourceService appResourceService;

	@RequestMapping(value = "/unassignedOrders", method = RequestMethod.GET)
	@ResponseBody
	public ReturnValueNet<List<MobilePeizhenOrder>> getUnassignedOrders(
			@RequestParam("pageNo") Integer pageNo,
			@RequestParam("pageSize") Integer pageSize,
			@RequestParam("findStyle") String findStyle) {
		ReturnValueNet<List<MobilePeizhenOrder>> returnValueNet = new ReturnValueNet<List<MobilePeizhenOrder>>();
		try {
			List<MobilePeizhenOrder> orders = appResourceService
					.getUnassignedOrders(pageNo, pageSize, findStyle);
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

	@RequestMapping(value = "/getUnassignedOrdersByIds", method = RequestMethod.GET)
	@ResponseBody
	public ReturnValueNet<List<MobilePeizhenOrder>> getUnassignedOrders(
			@RequestParam("ids") String ids) {
		ReturnValueNet<List<MobilePeizhenOrder>> returnValueNet = new ReturnValueNet<List<MobilePeizhenOrder>>();
		try {
			List<MobilePeizhenOrder> orders = appResourceService
					.getUnassignedOrdersByIds(ids);
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
	}*/
}
