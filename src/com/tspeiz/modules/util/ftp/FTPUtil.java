package com.tspeiz.modules.util.ftp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import com.tspeiz.modules.common.bean.UploadStatus;
import com.tspeiz.modules.util.PropertiesUtil;

public class FTPUtil {
	private Logger log = Logger.getLogger(FTPUtil.class);
	private FTPClient ftpClient = new FTPClient();

	public FTPUtil() {
		this.ftpClient.addProtocolCommandListener(new PrintCommandListener(
				new PrintWriter(System.out)));
	}

	// 连接ftp服务器
	public boolean connect() throws IOException {
		ftpClient.connect(PropertiesUtil.getString("ftp_ip"), Integer.parseInt(PropertiesUtil.getString("ftp_port")));
		if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
			if (ftpClient.login(PropertiesUtil.getString("ftp_user"), PropertiesUtil.getString("ftp_pass"))) {
				return true;
			}
		}
		return false;
	}
	
	public static List<String> listFTPFiles(String dir) throws Exception{
		List<String> filenames=new ArrayList<String>();
		FTPUtil ftputil=new FTPUtil();
		if(ftputil.connect()){
			ftputil.ftpClient.enterLocalPassiveMode(); 
			try {
				ftputil.ftpClient.changeWorkingDirectory(dir);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("====开始获取数据===");
			FTPFile[] fs = ftputil.ftpClient.listFiles();
			System.out.println("====获取数据结束===");
			for (FTPFile ff : fs) {
				byte[] bytes = ff.getName().getBytes("iso-8859-1");
				String fn = new String(bytes, "utf8");
				if((fn.equalsIgnoreCase(".")||fn.equalsIgnoreCase("..")))continue;
				if(!fn.contains(".dcm"))continue;
				filenames.add(fn);
			}
			ftputil.ftpClient.logout();
			ftputil.ftpClient.disconnect();
		}
		return filenames;
	}
	
	public static void main(String[] args) {
		try {
			listFTPFiles("Images\\DSA\\2016\\01\\2016-01-01\\156762\\");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	/**
	 * 上传文件到FTP服务器，支持断点续传
	 * 
	 * @param local
	 *            本地上传的文件
	 * @param remote
	 *            远程文件路径
	 * @return 上传结果
	 * @throws IOException
	 */
	public UploadStatus upload(MultipartFile local, String remote)
			throws IOException {
		ftpClient.enterLocalPassiveMode();// 模式设置
		// 设置以二进制流的方式传输
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		ftpClient.setBufferSize(1024 * 1024);
		UploadStatus result;
		String remoteFileName = remote;
		if (remote.contains("/")) {
			remoteFileName = remote.substring(remote.lastIndexOf("/") + 1);
			String directory = remote.substring(0, remote.lastIndexOf("/") + 1);
			if (!directory.equalsIgnoreCase("/")
					&& !ftpClient.changeWorkingDirectory(directory)) {
				// 如果远程目录不存在，则递归创建远程服务器目录
				int start = 0;
				int end = 0;
				if (directory.startsWith("/")) {
					start = 1;
				} else {
					start = 0;
				}
				end = directory.indexOf("/", start);
				while (true) {
					String subDirectory = remote.substring(start, end);
					if (!ftpClient.changeWorkingDirectory(subDirectory)) {
						if (ftpClient.makeDirectory(subDirectory)) {
							ftpClient.changeWorkingDirectory(subDirectory);
						} else {
							System.out.println("创建目录失败");
							return UploadStatus.Create_Directory_Fail;
						}
					}

					start = end + 1;
					end = directory.indexOf("/", start);
					// 检查所有目录是否创建完毕
					if (end <= start) {
						break;
					}
				}
			}
		}

		// 检查远程是否存在文件
		FTPFile[] files = ftpClient.listFiles(remoteFileName);
		if (files.length == 1) {
			long remoteSize = files[0].getSize();
			long localSize = local.getSize();
			if (remoteSize == localSize) {
				return UploadStatus.File_Exits;
			} else if (remoteSize > localSize) {
				return UploadStatus.Remote_Bigger_Local;
			}
			// 尝试移动文件内读取指针,实现断点续传
			InputStream is = local.getInputStream();
			if (is.skip(remoteSize) == remoteSize) {
				ftpClient.setRestartOffset(remoteSize);
				if (ftpClient.storeFile(remote, is)) {
					return UploadStatus.Upload_From_Break_Success;
				}
			}
			// 如果断点续传没有成功，则删除服务器上文件，重新上传
			if (!ftpClient.deleteFile(remoteFileName)) {
				return UploadStatus.Delete_Remote_Faild;
			}
			is = local.getInputStream();
			if (ftpClient.storeFile(remote, is)) {
				result = UploadStatus.Upload_New_File_Success;
			} else {
				result = UploadStatus.Upload_New_File_Failed;
			}
			is.close();
		} else {
			InputStream is = local.getInputStream();
			if (ftpClient.storeFile(remoteFileName, is)) {
				result = UploadStatus.Upload_New_File_Success;
			} else {
				result = UploadStatus.Upload_New_File_Failed;
			}
			is.close();
		}
		return result;
	}

	/**
	 * 下载文件
	 * 
	 * @param remote
	 *            远程文件路径
	 * @param local
	 *            本地文件路径
	 * @return 是否成功
	 * @throws IOException
	 */
	public boolean download(String remote, String local) throws IOException {
		ftpClient.enterLocalPassiveMode();
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		boolean result;
		File f = new File(local);
		FTPFile[] files = ftpClient.listFiles(remote);
		if (files.length != 1) {
			log.info("远程文件不唯一");
			return false;
		}
		long lRemoteSize = files[0].getSize();
		if (f.exists()) {
			OutputStream out = new FileOutputStream(f, true);
			log.info("本地文件大小为:" + f.length());
			if (f.length() >= lRemoteSize) {
				log.info("本地文件大小大于远程文件大小，下载中止");
				return false;
			}
			ftpClient.setRestartOffset(f.length());
			result = ftpClient.retrieveFile(remote, out);
			out.close();
		} else {
			OutputStream out = new FileOutputStream(f);
			result = ftpClient.retrieveFile(remote, out);
			out.close();
		}
		return result;
	}

	// 下载文件
	public void downFileByte(String fileName, String docid,OutputStream os)
			throws IOException {
			ftpClient.changeWorkingDirectory("mediaupload/" + docid + "/");//转移到FTP服务器目录 
	        FTPFile[] fs = ftpClient.listFiles(); 
	        for(FTPFile ff:fs){ 
	            if(ff.getName().equals(fileName)){ 
	               // File localFile = new File(ff.getName());          
	              // ;  
	            	
	                ftpClient.retrieveFile(ff.getName(), os); 
	               // os.close(); 
	            } 
	        } 
	}

	// 删除文件至FTP通用方法
	public boolean deleteFileFtp(String fileName,String docid) throws IOException {
		String[] str = fileName.split(PropertiesUtil.getString("ftp_ip")+":"+PropertiesUtil.getString("ftp_port")+"/");
		if(str.length>0)
		{
			fileName=str[str.length-1];
			log.info(fileName);
		}
		//ftpClient.changeWorkingDirectory("mediaupload/" + docid + "/");//转移到FTP服务器目录
		return ftpClient.deleteFile(fileName);

	}

	// 断开ftp服务器
	public void disconnect() throws IOException {
		if (ftpClient.isConnected()) {
			ftpClient.disconnect();
		}
	}
	
	//获取指定ftp文件流
	public static InputStream gainRetriFile(String filename) throws Exception{
		FTPUtil ftputil=new FTPUtil();
		InputStream in=null;
		if(ftputil.connect()){
			ftputil.ftpClient.enterLocalPassiveMode();  
			in=ftputil.ftpClient.retrieveFileStream(filename);
			if (in != null) {
				in.close();
			}
			if (!ftputil.ftpClient.completePendingCommand()) {   
				ftputil.ftpClient.logout();  
				ftputil.ftpClient.disconnect();  
	        }  
		}
		return in;
	}

	public FTPClient getFtpClient() {
		return ftpClient;
	}

	public void setFtpClient(FTPClient ftpClient) {
		this.ftpClient = ftpClient;
	}
	
	/*public static void main(String[] args) throws Exception {
		//downFile();
		URL u=new URL("ftp://FTPTEST:123456@115.28.90.43:21/mediaupload/281094/20151102142611.jpg"); 
		InputStream in = u.openStream();
		System.out.println(in);
		File file=new File("C:\\Users\\kx\\Desktop\\20151102142611.jpg");
		System.out.println(file.length());
		
		//FTPUtil ftpUtil = new FTPUtil();
		//boolean b = ftpUtil.connect("115.28.90.43", 21, "FTPTEST", "123456");
		//System.out.println(b);
	String ftp	= "ftp://FTPTEST:123456@115.28.90.43:21/mediaupload/281094/20151102115908.jpeg";
	String[] str = ftp.split("115.28.90.43:21");
	System.out.println(str.length);
	for(String str1:str)
	{
		System.out.println(str[str.length-1]);
	}
	
	}*/
}
