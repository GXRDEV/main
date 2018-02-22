package com.tspeiz.modules.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.tspeiz.modules.common.entity.newrelease.CustomFileStorage;
import com.tspeiz.modules.common.service.ICommonService;
import com.tspeiz.modules.util.oss.OSSConfigure;
import com.tspeiz.modules.util.oss.OSSManageUtil;

public class WebmToMp4Util {

	private static Logger log = Logger.getLogger(WebmToMp4Util.class);

	private static String ffmpegPath="";
	
	private static ICommonService commonService;
	
	static {
		ffmpegPath = PropertiesUtil.getString("ffmpegPath");
	}
	
	public static void main(String[] args) {

		final String inPath = "http://tupian201604.oss-cn-beijing.aliyuncs.com/liuan/vedio/134/20160630094238940.webm";
		final String outPath = "E:\\video\\ceshi26.mp4";
		if (process(inPath, outPath)) {
			System.out.println("ok");
		}
	}

	public static void convert(String file_url, String localdir, final Integer orderId,final String patientName,final String specialName) {
		
		if(StringUtils.isBlank(ffmpegPath)){
			System.out.println("\r\n===========WebmToMp4Util:没有配置ffmpeg" );
			return;
		}
		File ffmpegfile = new File(ffmpegPath);
		if (!ffmpegfile.exists()) {
			System.out.println("\r\n===========WebmToMp4Util:ffmpeg文件不存在" );
			return ;
		}
		
		final String fileUrl = file_url.replaceAll("\\\\", "/");
		String fname = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
		fname = fname.substring(0, fname.lastIndexOf(".")) +  ".mp4";
		
		final String outfile = localdir + "\\" + fname;

		System.out.println("\r\n===========infile:" + fileUrl);
		System.out.println("\r\n===========outfile:" + outfile);
		File file = new File(outfile);
		if (file.exists()) {
			file.delete();
		}

		// 启动转码线程 从WEBM转码 为 MP4 并上传到服务器，然后删除
		Thread convertThread = new Thread() {
			public void run() {

				Map<String, String> retmap = null;
				log.info("===开始转码==");
				boolean status = process(fileUrl, outfile);
				if (!status) {
					log.error("===转码失败===");
					return;
				}
				// 上传转码文件
				log.info("===开始上传转码文件==");
				try {
					 retmap = OSSManageUtil.uploadFile(outfile, orderId,patientName,specialName, new OSSConfigure());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					log.error("===上传转码文件失败==");
					return;
				}
				File lcoalfile = new File(outfile);
				if (lcoalfile.exists()) {
					System.out.println("====删除成功与否？===" + lcoalfile.delete());
				}
				log.info("===上传转码文件结束==");
				
				if(commonService != null){

					String returl = retmap.get("returl");
					String filename = retmap.get("filename");
					CustomFileStorage cu = new CustomFileStorage();
					cu.setCreateTime(new Timestamp(new Date().getTime()));
					cu.setFileName(filename);
					cu.setFileUrl(returl);
					cu.setOrderId(orderId);
					cu.setStatus(1);
					commonService.saveCustomFileStorage(cu);
				}
			}
		};
		convertThread.start();
	}

	private static boolean process(String inPath, String outPath) {
		int type = checkContentType(inPath);
		boolean status = false;
		if (type == 0) {
			System.out.println("直接将文件转为mp4文件");
			status = processWEBM(inPath, outPath);// 直接将文件转为mpe文件
		}
		return status;
	}

	private static int checkContentType(String inPath) {
		String type = inPath.substring(inPath.lastIndexOf(".") + 1,
				inPath.length()).toLowerCase();
		// ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）
		if (type.equals("avi")) {
			return 0;
		} else if (type.equals("mpg")) {
			return 0;
		} else if (type.equals("wmv")) {
			return 0;
		} else if (type.equals("3gp")) {
			return 0;
		} else if (type.equals("mov")) {
			return 0;
		} else if (type.equals("mp4")) {
			return 0;
		} else if (type.equals("asf")) {
			return 0;
		} else if (type.equals("asx")) {
			return 0;
		} else if (type.equals("flv")) {
			return 0;
		} else if (type.equals("webm")) {
			return 0;
		}

		return 9;
	}

	// ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）
	private static boolean processWEBM(String inPath, String outPath) {

		try {

			String command = ffmpegPath + " -i " + inPath + " " + outPath;
			System.out.print("\r\n===========command:" + command);
			Process proc = Runtime.getRuntime().exec(command);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					proc.getErrorStream()));

			String line = null;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}

			int exitVal = proc.waitFor();
			System.out.println("Process exitVal:" + exitVal);

			return true;
		} catch (Exception ee) {
			System.out.println("Error exec!");
		}

		return false;
	}

	// ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）
	private static boolean processWEBM_1(String oldfilepath) {

		List<String> commend = new ArrayList<String>();
		commend.add("D:\\ffmpeg\\bin\\ffmpeg");
		commend.add("-i");
		commend.add(oldfilepath);
		commend.add("E:\\video\\ceshi17.mp4");

		try {

			ProcessBuilder builder = new ProcessBuilder(commend);
			builder.command(commend);
			builder.start();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	
	public static void setFfmpegPath(String ffmpegPath) {
		WebmToMp4Util.ffmpegPath = ffmpegPath;
	}

	public static void setCommonService(ICommonService commonService) {
		WebmToMp4Util.commonService = commonService;
	}
}
