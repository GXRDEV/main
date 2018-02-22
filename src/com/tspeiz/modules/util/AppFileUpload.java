package com.tspeiz.modules.util;


public class AppFileUpload {

	/*private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");

	public static String UPLOAD_VISITIMAGES = "addvisituploads";

	public static String UPLOAD_INQUIRYIMAGES = "addinquiryuploads";

	public AppFileUpload() {

	}

	// 允许图片的类型
	public static final String[] ALLOW_PHOTO_EXT = { ".jpg", ".gif", ".png",
			".tif", ".bmp", ".JPG", ".PNG", ".GIF", ".BMP", ".TIF" };
	// 允许文件的类型
	
	 * private static final String[] ALLOW_FILE_EXT = { ".doc", ".docx", ".pdf",
	 * ".mp4", ".flv", ".xls", ".xlsx" };
	 

	private HttpServletRequest request;
	private Map<String, String> requestParams;
	private List<UserUpload> uploadBeans;
	*//**
	 * HttpSession ID
	 *//*
	private String sessionId;

	public AppFileUpload(HttpServletRequest request) {
		requestParams = new HashMap<String, String>();
		uploadBeans = new ArrayList<UserUpload>();
		this.request = request;
	}

	@SuppressWarnings("unchecked")
	public Integer uploadPhoto(String upladType) throws FileUploadException,
			IOException, FileUploadBase.FileSizeLimitExceededException,
			FileUploadBase.SizeLimitExceededException {

		// 判断是否是multipart类型
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (!isMultipart)
			return AppReturnCodeEnum.CODE_NOT_MULTIPART_REQUEST.getCode();

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
				if ("sessionId".equalsIgnoreCase(paramName)) {
					this.sessionId = item.getString();
				}
			}
		}

		String realUploadPath = "";
		// 上传 就诊记录图片
		if (UPLOAD_VISITIMAGES.equalsIgnoreCase(upladType)) {
			String userRealName = requestParams.get("userRealName");
			String diseaseName = requestParams.get("diseaseName");
			String fileType = requestParams.get("fileType");

			realUploadPath = request
					.getSession()
					.getServletContext()
					.getRealPath(
							"/upload/患者/" + userRealName + "/病例/" + diseaseName
									+ "/");

			// 上传 咨询记录图片
		} else if (UPLOAD_INQUIRYIMAGES.equalsIgnoreCase(upladType)) {
			String registerName = requestParams.get("registerName");
			String fileType = requestParams.get("fileType");

			realUploadPath = request.getSession().getServletContext()
					.getRealPath("/upload/用户/" + registerName + "/问诊/");

		}

		for (FileItem item : fileItems) {
			// 不是普通表单字段
			if (!item.isFormField()) {

				String fileName = item.getName();
				if (fileName == null || fileName.trim().equals("")) {
					continue;
				}
				fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);// a.txt
				String fileExt = fileName.substring(fileName.lastIndexOf("."));

				fileName = fileName.substring(0, fileName.lastIndexOf("."))
						+ sdf.format(new Date()) + ".fileExt";

				System.out.println("---上传图片名称----" + fileName);

				if (!isPicture(fileExt))
					System.out.println("您上传的不是图片！");

				String fullFilePath = makeDirs(realUploadPath) + "/" + fileName;
				int pos = fullFilePath.indexOf("\\upload");
				String relativeFilePath = fullFilePath.substring(pos + 1);
				relativeFilePath = relativeFilePath.replaceAll("\\\\", "/");
				System.out.println("===上传路径===" + relativeFilePath);

				UserUpload upladBean = new UserUpload();
				upladBean.setFileName(fileName);
				upladBean.setFileSize(String.valueOf(item.getSize()));
				upladBean.setPrefix(fileExt);
				upladBean.setUrlPath(relativeFilePath);

				// 类型
				String fileType = requestParams.get("fileType");
				if (!StringUtil.isBlank(fileType)) {
					upladBean.setFileType(Integer.parseInt(fileType));
				}
				uploadBeans.add(upladBean);

				InputStream in = item.getInputStream();// 获取输入流
				OutputStream out = new FileOutputStream(fullFilePath);
				Streams.copy(in, out, true);
				in.close();
				out.close();
				item.delete();// 删除临时文件
			}
		}

		return AppReturnCodeEnum.CODE_OK.getCode();
	}

	*//**
	 * 根据文件名分配存储路径
	 * 
	 * @param uploadPath
	 * @return
	 *//*

	*//**
	 * 得到sessionid
	 * 
	 * @return
	 *//*
	public String getSessionId() {
		return sessionId;
	}

	public Map<String, String> getRequestParams() {
		return requestParams;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public List<UserUpload> getUploadBeans() {
		return uploadBeans;
	}

	public static boolean isPicture(String fileExt) {

		for (int i = 0; i < ALLOW_PHOTO_EXT.length; i++) {
			if (ALLOW_PHOTO_EXT[i].equalsIgnoreCase(fileExt)) {

				return true;
			}
		}
		return false;
	}

	public static String getRelativePath(String fullFileName) {

		int pos = fullFileName.indexOf("\\upload");
		String relativeFilePath = fullFileName.substring(pos + 1);
		relativeFilePath = relativeFilePath.replaceAll("\\\\", "/");

		return relativeFilePath;
	}

	public static String getRelativePath(String uploadPath, String fileName) {

		String fullFilePath = makeDirs(uploadPath) + "/" + fileName;
		int pos = fullFilePath.indexOf("\\upload");
		String relativeFilePath = fullFilePath.substring(pos + 1);
		relativeFilePath = relativeFilePath.replaceAll("\\\\", "/");

		return relativeFilePath;
	}

	public static String makeDirs(String uploadPath) {
		File file = new File(uploadPath);
		if (!file.exists())
			file.mkdirs();
		return uploadPath;
	}*/

}
