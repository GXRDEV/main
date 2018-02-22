package com.tspeiz.modules.util.oss;

import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.MultimediaInfo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.AppendObjectRequest;
import com.aliyun.oss.model.AppendObjectResult;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.mysql.jdbc.Blob;
import com.tspeiz.modules.common.bean.weixin.MobileSpecial;
import com.tspeiz.modules.common.entity.RemoteConsultation;
import com.tspeiz.modules.util.qr.QRCodeUtil;

public class OSSManageUtil {
	private static Logger log = Logger.getLogger(OSSManageUtil.class);
	private static SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyyMMddHHmmssSSS");
	private static SimpleDateFormat year_sdf=new SimpleDateFormat("yyyy");
	private static SimpleDateFormat month_sdf=new SimpleDateFormat("MM");
	private static SimpleDateFormat day_sdf=new SimpleDateFormat("dd");
	// 上传
	public static String uploadFile(String filePath, String fileDir,
			OSSConfigure ossConfig, HttpServletRequest request)
			throws Exception {

		File file = new File(filePath);
		// File _file=new File(request.getInputStream());
		OSSClient ossClient = new OSSClient(ossConfig.getEndpoint(),
				ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret());
		ObjectMetadata objectMeta = new ObjectMetadata();
		objectMeta.setContentLength(file.length());
		String content_type = contentType(file.getName().substring(
				file.getName().lastIndexOf(".") + 1));
		objectMeta.setContentType(content_type);
		InputStream input = new FileInputStream(file);
		ossClient.putObject(ossConfig.getBucketName(),
				fileDir + "/" + file.getName(), input, objectMeta);
		String returl = ossConfig.getAccessUrl() + "/" + fileDir+"/"
				+ file.getName();
		input.close();
		return returl;
	}

	private static Map<String, Object> parseStr(String str) {
		Map<String, Object> map = new HashMap<String, Object>();
		System.out.println("===str==" + str);
		String[] strs = str.split("&");
		if (strs != null && strs.length > 0) {
			for (String _str : strs) {
				String[] _strs = _str.split("=");
				if (_strs.length > 1) {
					map.put(_strs[0], _strs[1]);
				} else {
					map.put(_strs[0], "");
				}

			}
		}
		return map;
	}
	
	private static List<String> images=null;
	private static List<String> videos = null;
	static {
		images=new ArrayList<String>();
		images.add("jpg");
		images.add("png");
		images.add("gif");
		images.add("JPG");
		images.add("PNG");
		images.add("GIF");
	}
	static {
		videos=new ArrayList<String>();
		videos.add("mp4");
		videos.add("mov");
		videos.add("avi");
		videos.add("wmv");
	}
	
	public static Map<String, Object> uploadFile_in(String fileDir,
			OSSConfigure ossConfig, HttpServletRequest request)
			throws Exception {
		Map<String, Object> ret_map = new HashMap<String, Object>();
		OSSClient ossClient = new OSSClient(ossConfig.getEndpoint(),
				ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret());
		ObjectMetadata objectMeta = new ObjectMetadata();
		System.out.println("==queryString:" + request.getQueryString());
		Map<String, Object> map = parseStr(request.getQueryString());
		String size = (String) map.get("size");
		log.info("size:"+size);
		objectMeta.setContentLength(Long.valueOf(size));
		String fileName = (String) map.get("name");
		fileName = URLDecoder.decode(fileName, "UTF-8");
		ret_map.put("filename", fileName);
		String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
		fileName = UUID.randomUUID().toString().replace("-", "") + "." + prefix;
		String content_type = contentType(prefix);
		objectMeta.setContentType(content_type);
		InputStream in = request.getInputStream();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len;
		while ((len = in.read(buffer)) > -1) {
			baos.write(buffer, 0, len);
		}
		baos.flush();
		byte[] bts = baos.toByteArray();
		log.info("bts.length:"+bts.length);
		if(images.contains(prefix)){
			Metadata metadata = ImageMetadataReader
					.readMetadata(new ByteArrayInputStream(baos.toByteArray()));
			Directory directory = metadata.getDirectory(ExifIFD0Directory.class);
			log.info("==="+JSONObject.fromObject(directory).toString());
			if (directory != null) {
				int orientation = 1;
				try {
					orientation = directory
							.getInt(ExifIFD0Directory.TAG_ORIENTATION);
				} catch (MetadataException me) {
					System.out.println("Could not get orientation");
				}
				log.info("===orientation:"+orientation);
				BufferedImage src = ImageIO.read(new ByteArrayInputStream(bts));
				BufferedImage des = spin(src,getTurn(orientation));
				baos = new ByteArrayOutputStream();
				ImageIO.write(des, prefix, baos);
				bts = baos.toByteArray();
				objectMeta.setContentLength(bts.length);
			}
		}
		ossClient.putObject(ossConfig.getBucketName(),
				fileDir + "/" + fileName, new ByteArrayInputStream(bts),
				objectMeta);
		String returl = ossConfig.getAccessUrl() + "/" + fileDir + "/"
				+ fileName;
		System.out.println("===地址：" + returl);
		ret_map.put("urlpath", returl);
		return ret_map;
	}

	public static Map<String, Object> upload_new(String dirpath,
			OSSConfigure ossConfig, HttpServletRequest request)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		File tempPath = new File(request.getServletContext()
				.getRealPath("upload"));
		File readfile = tempPath;
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(4096);
		factory.setRepository(tempPath);
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setSizeMax(1000000 * 20*100);
		List fileItems = upload.parseRequest(request);
		String itemNo = "";
		Double duration = 0d;
		for (Iterator iter = fileItems.iterator(); iter.hasNext();) {
			FileItem item = (FileItem) iter.next();
			if (item.isFormField()) {
				if ("itemNo".equals(item.getFieldName())) {
					itemNo = item.getString();
				}
			}
			if (!item.isFormField()) {
				// 上传文件的名称和完整路径
				OSSClient ossClient = new OSSClient(ossConfig.getEndpoint(),
						ossConfig.getAccessKeyId(),
						ossConfig.getAccessKeySecret());
				ObjectMetadata objectMeta = new ObjectMetadata();

				String fileName = item.getName();
				long size = item.getSize();
				objectMeta.setContentLength(size);
				// 判断是否选择了文件
				if ((fileName == null || fileName.equals("")) && size == 0) {
					continue;
				}
				// 截取字符串 如：C:\WINDOWS\Debug\PASSWD.LOG
				fileName = fileName.substring(fileName.lastIndexOf("\\") + 1,
						fileName.length());
				map.put("filename", fileName);
				String prefix = fileName
						.substring(fileName.lastIndexOf(".") + 1);
				InputStream vedio_in = null;
				if(videos.contains(prefix)) {
					File file =File.createTempFile("file", "."+prefix);
					item.write(file);
					vedio_in = new FileInputStream(file);
					duration = readVideo(file)*1000;
				}
				fileName = UUID.randomUUID().toString().replace("-", "") + "." + prefix;
				String content_type = contentType(prefix);
				objectMeta.setContentType(content_type);
				String returl;
				if(videos.contains(prefix)){
					ossClient.putObject(ossConfig.getVedioBucketName(), dirpath + "/"
							+ fileName, vedio_in, objectMeta);
				   returl = ossConfig.getAccessUrls() + "/" + dirpath + "/"
						+ fileName;
				}else{
					ossClient.putObject(ossConfig.getBucketName(), dirpath + "/"
							+ fileName, item.getInputStream(), objectMeta);
				    returl = ossConfig.getAccessUrl() + "/" + dirpath + "/"
						+ fileName;
				}
				map.put("urlpath", returl);
				map.put("duration", duration);
			}
		}
		return map;
	}
	
	
	private  static Double readVideo(File file) {
		Encoder encoder = new Encoder();
		Long durationLong = 0l;
		try {
			MultimediaInfo multimediaInfo = encoder.getInfo(file);
			durationLong = multimediaInfo.getDuration() / 1000;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return durationLong.doubleValue();
	}

	public static int getTurn(int orientation) {
		Integer turn = 0;
		if (orientation == 0 || orientation == 1) {
			turn = 0;
		} else if (orientation == 3) {
			turn = 180;
		} else if (orientation == 6) {
			turn = 90;
		} else if (orientation == 8) {
			turn = 270;
		}
		return turn;
	}

	private static Map<String, String> requestParams = null;

	@SuppressWarnings("unchecked")
	public static Map<String, Object> uploadPhoto(String fileDir,
			OSSConfigure ossConfig, HttpServletRequest request)
			throws FileUploadException, IOException,
			FileUploadBase.FileSizeLimitExceededException,
			FileUploadBase.SizeLimitExceededException {
		String rootDir=year_sdf.format(new Date())+"/"+month_sdf.format(new Date())+"/"+day_sdf.format(new Date());
		Map<String, Object> ret_map = new HashMap<String, Object>();
		OSSClient ossClient = new OSSClient(ossConfig.getEndpoint(),
				ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret());
		ObjectMetadata objectMeta = new ObjectMetadata();
		requestParams = new HashMap<String, String>();
		String tmpPath = request.getSession().getServletContext()
				.getRealPath("/temp");
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// 设置缓存目录
		factory.setRepository(new File(tmpPath));
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setHeaderEncoding("UTF-8");
		upload.setFileSizeMax(2000 * 1024 * 1024);// 10M
		upload.setSizeMax(1000 * 1024 * 1024);// 总文件大小
		// 判断是否是multipart类型
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (!isMultipart)
			return ret_map;
		// 解析请求内容
		List<FileItem> fileItems = upload.parseRequest(request);

		for (FileItem item : fileItems) {
			// 不是普通表单字段
			if (!item.isFormField()) {
			} else {
				// 把请求参数放入map中
				requestParams.put(item.getFieldName(), new String(item
						.getString().getBytes("ISO-8859-1"), "UTF-8"));
			}
		}
		for (FileItem item : fileItems) {
			// 不是普通表单字段
			if (!item.isFormField()) {
				String fileName = item.getName();
				objectMeta.setContentLength(item.getSize());
				String prefix = fileName
						.substring(fileName.lastIndexOf(".") + 1);
				fileName = sdf.format(new Date()) + "." + prefix;
				ret_map.put("filename", fileName);
				String content_type = contentType(prefix);
				if (fileName == null || fileName.trim().equals("")) {
					continue;
				}
				fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);// a.txt
				String ext = fileName.substring(fileName.lastIndexOf("."));
				fileName = fileName.substring(0, fileName.lastIndexOf("."))
						+ UUID.randomUUID().toString().replace("-", "") + ext;
				System.out.println("---上传图片名称----" + fileName);
				objectMeta.setContentType(content_type);
				ossClient.putObject(ossConfig.getBucketName(), rootDir+"/"+fileDir + "/"
						+ fileName, item.getInputStream(), objectMeta);
				String returl = ossConfig.getAccessUrl() + "/" + rootDir+"/"+fileDir + "/"
						+ fileName;
				ret_map.put("urlpath", returl);
				System.out.println("====urlpath===" + returl);
				item.delete();// 删除临时文件
			}
		}
		return ret_map;
	}

	/**
	 * 根据key删除OSS服务器上的文件
	 * 
	 * @Title: deleteFile
	 * @Description:
	 * @param @param ossConfigure
	 * @param @param filePath 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public static void deleteFile(OSSConfigure ossConfigure, String filePath) {
		OSSClient ossClient = new OSSClient(ossConfigure.getEndpoint(),
				ossConfigure.getAccessKeyId(),
				ossConfigure.getAccessKeySecret());
		filePath = filePath.substring(filePath.indexOf(ossConfigure
				.getAccessUrl()) + ossConfigure.getAccessUrl().length() + 1);
		ossClient.deleteObject(ossConfigure.getBucketName(), filePath);
		ossClient.shutdown();
	}

	/**
	 * 
	 * @param ossConfigure
	 * @param bucketName
	 *            存储空间
	 * @param prefix
	 *            前缀
	 * @param maxKeys
	 *            分页条数
	 * @return
	 */
	public static List<OSSObjectSummary> listFiles(OSSConfigure ossConfigure,
			String bucketName, String prefix, Integer maxKeys) {
		List<OSSObjectSummary> list = new ArrayList<OSSObjectSummary>();
		OSSClient client = new OSSClient(ossConfigure.getEndpoint(),
				ossConfigure.getAccessKeyId(),
				ossConfigure.getAccessKeySecret());
		ObjectListing objectListing = client
				.listObjects(new ListObjectsRequest(bucketName).withPrefix(
						prefix).withMaxKeys(maxKeys));
		for (OSSObjectSummary objectSummary : objectListing
				.getObjectSummaries()) {
			System.out.println(" - " + objectSummary.getKey() + "  "
					+ "(size = " + objectSummary.getSize() + ")");
			list.add(objectSummary);
		}
		return list;
	}

	public static String uploadFile_in(OSSConfigure ossConfig, String fileurl,
			String imagePath) {
		Map<String, String> map = new HashMap<String, String>();
		log.info("============开始上传影像图片到oss===========");
		imagePath = imagePath.replaceAll("\\\\", "/");
		fileurl = fileurl.replaceAll("\\\\", "/");
		String dir = "liuan"
				+ imagePath.substring(0, imagePath.lastIndexOf("/"));
		System.out.println("===目录====" + dir);
		String name = UUID.randomUUID().toString().replace("-", "");
		String prefix = fileurl.substring(fileurl.lastIndexOf(".") + 1);
		// URL u=null;
		String returl = "";
		InputStream in = null;
		/*
		 * try { FTPUtil ftputil=new FTPUtil(); if(ftputil.connect()){
		 * ftputil.getFtpClient().enterLocalPassiveMode();
		 * in=ftputil.getFtpClient().retrieveFileStream(imagePath); if (in !=
		 * null) { OSSClient ossClient = new OSSClient(ossConfig.getEndpoint(),
		 * ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret());
		 * ObjectMetadata objectMeta = new ObjectMetadata();
		 * objectMeta.setContentType(prefix);
		 * ossClient.putObject(ossConfig.getBucketName(), dir + "/"
		 * +name+"."+prefix, in, objectMeta); returl= ossConfig.getAccessUrl() +
		 * "/" + dir + "/"+ name+"."+prefix; // in.close();
		 * ossClient.shutdown();
		 * log.info("============上传影像图片到oss结束==========="); }
		 * 
		 * if (!ftputil.getFtpClient().completePendingCommand()) {
		 * log.info("===上传oss关闭ftp==="); ftputil.getFtpClient().logout();
		 * ftputil.getFtpClient().disconnect(); } }
		 * 
		 * } catch (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */

		try {
			URL url = new URL(fileurl);
			in = url.openStream();
			if (in != null) {
				OSSClient ossClient = new OSSClient(ossConfig.getEndpoint(),
						ossConfig.getAccessKeyId(),
						ossConfig.getAccessKeySecret());
				ObjectMetadata objectMeta = new ObjectMetadata();
				objectMeta.setContentType(prefix);
				ossClient.putObject(ossConfig.getBucketName(), dir + "/" + name
						+ "." + prefix, in, objectMeta);
				returl = ossConfig.getAccessUrl() + "/" + dir + "/" + name
						+ "." + prefix;
				//
				in.close();
				ossClient.shutdown();
				log.info("============上传影像图片到oss结束===========");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returl;
	}
	public static void uploadBlob(Blob blob, OSSConfigure ossConfig)
			throws Exception {
		String returl = "";
		InputStream in = blob2stream(blob);
		OSSClient ossClient = new OSSClient(ossConfig.getEndpoint(),
				ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret());
		ObjectMetadata objectMeta = new ObjectMetadata();
		objectMeta.setContentType("webm");
		ossClient.putObject(ossConfig.getBucketName(), "test/test" + "."
				+ "webm", in, objectMeta);
		returl = ossConfig.getAccessUrl() + "/test/test.webm";
		in.close();
		ossClient.shutdown();
		System.out.println("==========上传视频地址====" + returl);
	}

	public static Map<String, String> uploadInputStream(InputStream is,
			OSSConfigure ossConfig, RemoteConsultation order,
			MobileSpecial special) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		String returl = "";
		String dir = "liuan/vedio/" + order.getId() + "/";
		String filename = (StringUtils.isNotBlank(order.getPatientName()) ? order
				.getPatientName() : "defalt")
				+ "_"
				+ special.getSpecialName()
				+ "_"
				+ sdf.format(new Date())
				+ ".webm";
		String _filename = UUID.randomUUID().toString().replace("-", "");
		OSSClient ossClient = new OSSClient(ossConfig.getEndpoint(),
				ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret());
		ObjectMetadata objectMeta = new ObjectMetadata();
		objectMeta.setContentType("video/webm");
		ossClient.putObject(ossConfig.getBucketName(), dir + _filename
				+ ".webm", is, objectMeta);
		returl = ossConfig.getAccessUrl() + "/" + dir + _filename + ".webm";
		is.close();
		ossClient.shutdown();
		System.out.println("==========上传视频地址====" + returl);
		map.put("returl", returl);
		map.put("filename", filename);
		return map;
	}
	
	
	public static Map<String, String> uploadInputStream(InputStream is,
			OSSConfigure ossConfig,String turnRate) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		String returl = "";
		String rootDir=year_sdf.format(new Date())+"/"+month_sdf.format(new Date())+"/"+day_sdf.format(new Date());
		String _filename = UUID.randomUUID().toString().replace("-", "");
		OSSClient ossClient = new OSSClient(ossConfig.getEndpoint(),
				ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret());
		ObjectMetadata objectMeta = new ObjectMetadata();
		objectMeta.setContentType("image/jpeg");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len;
		while ((len = is.read(buffer)) > -1) {
			baos.write(buffer, 0, len);
		}
		baos.flush();
		byte[] bts = baos.toByteArray();
		if(StringUtils.isNotBlank(turnRate)){
			BufferedImage src = ImageIO.read(new ByteArrayInputStream(bts));
			BufferedImage des = spin(src,Integer.parseInt(turnRate));
			baos = new ByteArrayOutputStream();
			ImageIO.write(des, "png", baos);
			bts = baos.toByteArray();
			objectMeta.setContentLength(bts.length);
		}
		ossClient.putObject(ossConfig.getBucketName(), rootDir + _filename
				+ ".png",new ByteArrayInputStream(bts), objectMeta);
		returl = ossConfig.getAccessUrl() + "/" + rootDir + _filename + ".png";
		is.close();
		ossClient.shutdown();
		System.out.println("==========上传签名图片====" + returl);
		map.put("url", returl);
		return map;
	}
	
	public static BufferedImage spin(BufferedImage src,int degree) throws Exception {
        int swidth = 0; // 旋转后的宽度
        int sheight = 0; // 旋转后的高度
        int x; // 原点横坐标
        int y; // 原点纵坐标
 
        // 处理角度--确定旋转弧度
        degree = degree % 360;
        if (degree < 0)
            degree = 360 + degree;// 将角度转换到0-360度之间
        double theta = Math.toRadians(degree);// 将角度转为弧度
 
        // 确定旋转后的宽和高
        if (degree == 180 || degree == 0 || degree == 360) {
            swidth = src.getWidth();
            sheight = src.getHeight();
        } else if (degree == 90 || degree == 270) {
            sheight = src.getWidth();
            swidth = src.getHeight();
        } else {
            swidth = (int) (Math.sqrt(src.getWidth() * src.getWidth()
                    + src.getHeight() * src.getHeight()));
            sheight = (int) (Math.sqrt(src.getWidth() * src.getWidth()
                    + src.getHeight() * src.getHeight()));
        }
 
        x = (swidth / 2) - (src.getWidth() / 2);// 确定原点坐标
        y = (sheight / 2) - (src.getHeight() / 2);
 
        BufferedImage spinImage = new BufferedImage(swidth, sheight,
        		src.getType());
        // 设置图片背景颜色
        Graphics2D gs = (Graphics2D) spinImage.getGraphics();
        gs.setColor(Color.white);
        gs.fillRect(0, 0, swidth, sheight);// 以给定颜色绘制旋转后图片的背景
 
        AffineTransform at = new AffineTransform();
        at.rotate(theta, swidth / 2, sheight / 2);// 旋转图象
        at.translate(x, y);
        AffineTransformOp op = new AffineTransformOp(at,
                AffineTransformOp.TYPE_BICUBIC);
        spinImage = op.filter(src, spinImage);
       return spinImage;
 
    }
	// 上传
	public static Map<String, String> uploadFile(String filePath,
			Integer orderId, String patientName, String specialName,
			OSSConfigure ossConfig) throws Exception {

		Map<String, String> map = new HashMap<String, String>();
		String dir = "liuan/vedio/" + orderId + "/";
		String _filename = UUID.randomUUID().toString().replace("-", "");
		File file = new File(filePath);
		OSSClient ossClient = new OSSClient(ossConfig.getEndpoint(),
				ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret());
		ObjectMetadata objectMeta = new ObjectMetadata();
		objectMeta.setContentLength(file.length());
		String content_type = contentType(file.getName().substring(
				file.getName().lastIndexOf(".") + 1));
		System.out.println("\r\n==========content_type:" + content_type);
		objectMeta.setContentType(content_type);
		InputStream input = new FileInputStream(file);
		PutObjectResult result = ossClient.putObject(ossConfig.getBucketName(),
				dir + file.getName(), input, objectMeta);
		System.out.println("\r\n==========上传参数" + result.toString());
		String returl = ossConfig.getAccessUrl() + "/" + dir + file.getName();
		input.close();

		String filename = (StringUtils.isNotBlank(patientName) ? patientName
				: "defalt") + "_" + specialName + "_" + file.getName();
		System.out.println("\r\n==========上传视频地:" + returl);
		map.put("returl", returl);
		map.put("filename", filename);
		return map;
	}

	public static InputStream blob2stream(Blob blob) throws Exception {
		InputStream is = new ByteArrayInputStream(blob2ByteArr(blob));
		return is;
	}

	public static byte[] blob2ByteArr(Blob blob) throws Exception {

		byte[] b = null;
		try {
			if (blob != null) {
				long in = 0;
				b = blob.getBytes(in, (int) (blob.length()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("fault");
		}

		return b;
	}

	public static void uploadEditorImage(InputStream content, long filelength,
			String fileName) {
		OSSConfigure ossConfig = new OSSConfigure();
		OSSClient client = new OSSClient(ossConfig.getEndpoint(),
				ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret());
		ObjectMetadata meta = new ObjectMetadata();
		meta.setContentLength(filelength);
		client.putObject(ossConfig.getBucketName(), fileName, content, meta);
	}

	/*
	 * public static void main(String[] args) { OSSClient client = new
	 * OSSClient("oss-cn-beijing.aliyuncs.com", "qWnj059oBi9lOg37",
	 * "JpWJ3WxmxTjrcEOd47jpx4Khm85Vwu"); ObjectListing objectListing = client
	 * .listObjects(new ListObjectsRequest("tupian201604")); for
	 * (OSSObjectSummary objectSummary : objectListing .getObjectSummaries()) {
	 * System.out.println(" - " + objectSummary.getKey() + "  " + "(size = " +
	 * objectSummary.getSize() + ")"); } }
	 */
	
	/**
	 * 专家二维码生成
	 * 
	 * @param filename
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static String genErweima(String filename, String url)
			throws Exception {
		BufferedImage image = QRCodeUtil.generateQRCode(url, 184, 184);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageOutputStream imageOutputStream = ImageIO
				.createImageOutputStream(baos);
		ImageIO.write(image, "jpg", imageOutputStream);
		// 输出数组
		InputStream is = new ByteArrayInputStream(baos.toByteArray());
		return uploadInputStream_new(is, new OSSConfigure(), filename);
	}

	public static String uploadInputStream_new(InputStream is,
			OSSConfigure ossConfig, String filename) throws Exception {
		String rootDir=year_sdf.format(new Date())+"/"+month_sdf.format(new Date())+"/"+day_sdf.format(new Date());
		String returl = "";
		String dir = rootDir+"/system/erweima/";
		filename = filename + "_" + UUID.randomUUID().toString().replace("-", "");
		OSSClient ossClient = new OSSClient(ossConfig.getEndpoint(),
				ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret());
		ObjectMetadata objectMeta = new ObjectMetadata();
		objectMeta.setContentType("image/jpeg");
		ossClient.putObject(ossConfig.getBucketName(), dir + filename + ".jpg",
				is, objectMeta);
		returl = ossConfig.getAccessUrl() + "/" + dir + filename + ".jpg";
		is.close();
		ossClient.shutdown();
		return returl;
	}

	/**
	 * Description: 判断OSS服务文件上传时文件的contentType
	 * 
	 * @Version1.0
	 * @param FilenameExtension
	 *            文件后缀
	 * @return String
	 */
	public static String contentType(String FilenameExtension) {
		if (FilenameExtension.equals("zip") || FilenameExtension.equals("ZIP")) {
				return "application/zip";
		}
		if (FilenameExtension.equals("rar") || FilenameExtension.equals("RAR")) {
			return "application/rar";
		}
		if (FilenameExtension.equals("dcm") || FilenameExtension.equals("DCM")) {
			return "x-lml/x-evm";
		}
		if (FilenameExtension.equals("BMP") || FilenameExtension.equals("bmp")) {
			return "image/bmp";
		}
		if (FilenameExtension.equals("GIF") || FilenameExtension.equals("gif")) {
			return "image/gif";
		}
		if (FilenameExtension.equals("JPEG")
				|| FilenameExtension.equals("jpeg")
				|| FilenameExtension.equals("JPG")
				|| FilenameExtension.equals("jpg")
				|| FilenameExtension.equals("PNG")
				|| FilenameExtension.equals("png")) {
			return "image/jpeg";
		}
		if (FilenameExtension.equals("HTML")
				|| FilenameExtension.equals("html")) {
			return "text/html";
		}
		if (FilenameExtension.equals("TXT") || FilenameExtension.equals("txt")) {
			return "text/plain";
		}
		if (FilenameExtension.equals("VSD") || FilenameExtension.equals("vsd")) {
			return "application/vnd.visio";
		}
		if (FilenameExtension.equals("PPTX")
				|| FilenameExtension.equals("pptx")
				|| FilenameExtension.equals("PPT")
				|| FilenameExtension.equals("ppt")) {
			return "application/vnd.ms-powerpoint";
		}
		if (FilenameExtension.equals("DOCX")
				|| FilenameExtension.equals("docx")
				|| FilenameExtension.equals("DOC")
				|| FilenameExtension.equals("doc")) {
			return "application/msword";
		}
		if (FilenameExtension.equals("XML") || FilenameExtension.equals("xml")) {
			return "text/xml";
		}
		if (FilenameExtension.equals("MP4") || FilenameExtension.equals("mp4")) {
			return "video/mpeg";
		}
		if (FilenameExtension.equals("AVI") || FilenameExtension.equals("avi")) {
			return "video/x-msvideo";
		}
		if (FilenameExtension.equals("WEBM")
				|| FilenameExtension.equals("webm")) {
			return "video/webm";
		}
		if (FilenameExtension.equals("MKV") || FilenameExtension.equals("mkv")) {
			return "video/x-matroska";
		}
		if (FilenameExtension.equals("MOV") || FilenameExtension.equals("mov")) {
			return "video/quicktime";
		}
		if (FilenameExtension.equals("RM") || FilenameExtension.equals("rm")) {
			return "application/vnd.rn-realmedia";
		}
		if (FilenameExtension.equals("OGG") || FilenameExtension.equals("ogg")) {
			return "application/vnd.rn-realmedia";
		}
		if (FilenameExtension.equals("OGV") || FilenameExtension.equals("ogv")) {
			return "application/vnd.rn-realmedia";
		}
		if (FilenameExtension.equalsIgnoreCase("wmv")) {
			return "video/x-ms-wmv";
		}
		
		return "text/html";
	}

	// 实时传输流，上传文件
	public static Map<String, Object> uploadInputStreamAppend(
			RemoteConsultation order, MobileSpecial special, InputStream in,
			OSSConfigure ossConfig, String filename, Long position) {
		Map<String, Object> map = new HashMap<String, Object>();
		OSSClient client = new OSSClient(ossConfig.getEndpoint(),
				ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret());
		if (!StringUtils.isNotBlank(filename)) {
			String dir = "liuan/vedio/" + order.getId() + "/";
			filename = dir
					+ (StringUtils.isNotBlank(order.getPatientName()) ? order
							.getPatientName() : "defalt") + "_"
					+ special.getSpecialName() + "_" + sdf.format(new Date())
					+ ".webm";
		}
		try {
			if (position == null) {
				position = 0L;
			}
			AppendObjectResult appendObjectResult = client
					.appendObject(new AppendObjectRequest(ossConfig
							.getBucketName(), filename, in)
							.withPosition(position));
			map.put("nextPosition", appendObjectResult.getNextPosition());
			map.put("filename", filename);
		} catch (Exception oe) {
			oe.printStackTrace();
		}
		return map;
	}

	/*
	 * public static void main(String[] args) throws Exception { //
	 * uploadFile_in("test",new // OSSConfigure(),
	 * "ftp://FTPTEST:123456@115.28.90.43:21/mediaupload/281094/20151102142611.jpg"
	 * ); // String ret=uploadFile_in(new // OSSConfigure(),
	 * "http://218.22.197.122:8889/Images/CT/2006-1-1/8/1.2.392.200036.9116.2.5.1.48.1221423033.1286761599.719671.dcm"
	 * ,
	 * "\\Images\\CT\\2006-1-1\\8\\1.2.392.200036.9116.2.5.1.48.1221423033.1286761599.719671.dcm"
	 * );
	 * 
	 * 
	 * String ret=uploadFile_in(new OSSConfigure(),
	 * "ftp://zjh:123456@218.22.197.122:8889/Images\\CT\\2006-1-1\\8\\1.2.392.200036.9116.2.5.1.48.1221423033.1286676667.467598.dcm"
	 * ,
	 * "\\Images\\CT\\2006-1-1\\8\\1.2.392.200036.9116.2.5.1.48.1221423033.1286676667.467598.dcm"
	 * ); System.out.println("==返回的http路径=="+ret);
	 * 
	 * // 删除测试
	 * 
	 * System.out.println("=============开始删除================="); deleteFile(new
	 * OSSConfigure(),
	 * "http://tupian201604.oss-cn-beijing.aliyuncs.com/20160428/16/xiaxia_test.jpg"
	 * ); System.out.println("=============删除结束=================");
	 * 
	 * // uploadInputStreamAppend(null,new OSSConfigure(),null);
	 * 
	 * String dir = "liuan/testappend/"; OSSConfigure ossConfig=new
	 * OSSConfigure(); OSSClient client = new OSSClient(ossConfig.getEndpoint(),
	 * ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret());
	 * client.deleteObject(ossConfig.getBucketName(), dir+"test.txt");
	 * 
	 * }
	 */

}
