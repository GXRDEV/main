package com.tspeiz.modules.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.tspeiz.modules.common.service.weixin.IWeixinService;


public class FileUpload {
	private Logger log=Logger.getLogger(FileUpload.class);
	private static IWeixinService weixinService;
	static {
			ApplicationContext ac = SpringUtil.getApplicationContext();
			weixinService = (IWeixinService) ac.getBean("weixinService");
	}
	public FileUpload(){
		
	}
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmssSSS");
	// 允许图片的类型
	private static final String[] ALLOW_PHOTO_EXT = { ".jpg", ".gif", ".png",".tif",
			".bmp", ".JPG", ".PNG", ".GIF", ".BMP",".TIF"};
	// 允许文件的类型
	private static final String[] ALLOW_FILE_EXT = { ".doc", ".docx", ".pdf",
			".mp4", ".flv", ".xls", ".xlsx" };

	private HttpServletRequest request;
	private List<String> filepaths = new ArrayList<String>();

	/**
	 * 上传文件的文件名
	 */
	private String oldFileName;

	/**
	 * 上传到的路径
	 */
	private String newFilePath;

	/**
	 * 上传文件的真实路径
	 */
	private String realPath;

	/**
	 * HttpSession ID
	 */
	private String sessionId;

	/**
	 * 上传文件的扩展名
	 */
	private String fileExt;

	/**
	 * 上传文件大小
	 */
	private long fileSize;

	private Map<String, String> requestParams = null;

	public FileUpload(HttpServletRequest request) {
		requestParams = new HashMap<String, String>();
		this.request = request;
	}
	
	public void uploadPhoto() throws FileUploadException, IOException,
	FileUploadBase.FileSizeLimitExceededException,
	FileUploadBase.SizeLimitExceededException {
		try {
			request.setCharacterEncoding("UTF-8");
			} catch (UnsupportedEncodingException e1) {
			log.error(e1.getMessage());
			}
			File file = null;
			Map<String,Object> map=parseStr(request.getQueryString());
			String fileName=(String)map.get("name");
			//String ext = fileName.substring(fileName.lastIndexOf("."));
			//fileName= UUIDUtil.getUUID()+"_"+sdf.format(new Date())+ext;
			requestParams.put("name", fileName);
			String size=(String)map.get("size");
			requestParams.put("size", size);
			request.getInputStream();
	}
	
	
	
	public static String copyFileToLocal(String ftp_url,String localdir){
		ftp_url = ftp_url.replaceAll("\\\\", "/");
		localdir=localdir.replaceAll("\\\\","/");
		String fname=ftp_url.substring(ftp_url.lastIndexOf("/")+1);
		InputStream in = null;
		String retstr="";
		try {
			URL url = new URL(ftp_url);
			in = url.openStream();
			if (in != null) {
				retstr=localdir+"/"+fname;
				System.out.println("==========="+retstr);
				File file=new File(retstr);
				if(!file.exists())file.createNewFile();
				OutputStream outputStream = new FileOutputStream(retstr);
				int len = in.read();
				System.out.println("开始复制.....");
				long startTime = System.currentTimeMillis();//开始时间
				while(len != -1){
					outputStream.write(len);
					len = in.read();
				}
				long endTime = System.currentTimeMillis();//结束时间
				System.out.println("文件复制完成，共耗时："+(endTime-startTime)+"ms");
				in.close();
				outputStream.close();
			}
		} catch (Exception e) {
		}
		return retstr;
	}
	
	private  Map<String,Object> parseStr(String str){
		Map<String,Object> map=new HashMap<String,Object>();
		String[] strs=str.split("&");
		if(strs!=null&&strs.length>0){
			for(String _str:strs){
				String[] _strs=_str.split("=");
				if(_strs.length>1){
					map.put(_strs[0], _strs[1]);
				}else{
					map.put(_strs[0],"");
				}
				
				
			}
		}
		return map;
	}
	/**
	 * 返回上传文件的扩展名
	 * 
	 * @return
	 */
	public String getUploadFileExt() {
		return fileExt;
	}

	/**
	 * 得到上传文件的文件名
	 * 
	 * @return
	 */
	public String getOldFileName() {
		return oldFileName;
	}

	/**
	 * 得到新的文件路径(保存到数据库的路径)
	 * 
	 * @return
	 */
	public String getPhysicalFilePath() {
		return newFilePath;
	}

	/**
	 * 得到sessionid
	 * 
	 * @return
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * 得到请求参数
	 * 
	 * @return
	 */
	public Map<String, String> getRequestParams() {
		return requestParams;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	/**
	 * 获得图片在硬盘上的真是路径
	 * 
	 * @return
	 */
	public String getRealPath() {
		return realPath;
	}

	/**
	 * 根据文件名分配存储路径
	 * 
	 * @param uploadPath
	 * @param fileName
	 * @return
	 */
	private String makeDirs(String uploadPath, String fileName) {
		int hashCode = fileName.hashCode();
		int dir1 = hashCode & 0x0f;// 取低四位
		int dir2 = (hashCode & 0xf0) >> 4;// 取高四位
		String newPath = uploadPath ;//+ "\\" + dir1 + "\\" + dir2;
		File file = new File(newPath);
		if (!file.exists())
			file.mkdirs();
		return newPath;
	}

	/**
	 * 得到上传文件的大小
	 * 
	 * @return
	 */
	public long getFileSize() {
		return fileSize;
	}

	public List<String> getFilepaths() {
		return filepaths;
	}
}
