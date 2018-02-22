package com.tspeiz.modules.app.controller.dictionary;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tspeiz.modules.app.base.BaseController;

@Controller
@RequestMapping("/dictionary")
public class DictionaryController extends BaseController {
/*
	@Resource(name = "appDictionaryService")
	private IAppDictionaryService appDictionaryService;

	public DictionaryController() {
		// TODO Auto-generated constructor stub

	}

	@RequestMapping(value = "/hospitallist", method = RequestMethod.GET)
	@ResponseBody
	public ReturnValueNet<List<KeyValueBean>> getSimpleHospitalList(
			@RequestParam("province") Integer provinceId,
			@RequestParam("city") Integer cityId) {
		ReturnValueNet<List<KeyValueBean>> returnValueNet = new ReturnValueNet<List<KeyValueBean>>();

		try {

			List<KeyValueBean> hospitals = appDictionaryService
					.getSimpleHospitals(provinceId, cityId);
			returnValueNet.setCode(AppReturnCodeEnum.CODE_OK.getCode());
			returnValueNet.setResult(hospitals);
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

	@RequestMapping(value = "/departmentlist", method = RequestMethod.GET)
	@ResponseBody
	public ReturnValueNet<List<KeyValueBean>> getSimpleDepartmentList(
			@RequestParam("hospitalId") Integer hospitalId) {
		ReturnValueNet<List<KeyValueBean>> returnValueNet = new ReturnValueNet<List<KeyValueBean>>();

		try {

			List<KeyValueBean> hospitals = appDictionaryService
					.getSimpleDepartments(hospitalId);
			returnValueNet.setCode(AppReturnCodeEnum.CODE_OK.getCode());
			returnValueNet.setResult(hospitals);
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

	@RequestMapping(value = "/datasetlist", method = RequestMethod.GET)
	@ResponseBody
	public ReturnValueNet<List<KeyValueBean>> getSimpleDataSetList(
			@RequestParam("parentId") Integer parentId) {
		ReturnValueNet<List<KeyValueBean>> returnValueNet = new ReturnValueNet<List<KeyValueBean>>();

		try {
			List<KeyValueBean> dataSets = appDictionaryService
					.getSimpleDataSets(parentId);
			returnValueNet.setCode(AppReturnCodeEnum.CODE_OK.getCode());
			returnValueNet.setResult(dataSets);
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

	@RequestMapping(value = "/nurseGradeList", method = RequestMethod.GET)
	@ResponseBody
	public ReturnValueNet<List<KeyValueBean>> getNurseGradeList() {
		ReturnValueNet<List<KeyValueBean>> returnValueNet = new ReturnValueNet<List<KeyValueBean>>();

		try {

			List<KeyValueBean> dutys = appDictionaryService
					.getSimpleNurseGrades();
			returnValueNet.setCode(AppReturnCodeEnum.CODE_OK.getCode());
			returnValueNet.setResult(dutys);
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
