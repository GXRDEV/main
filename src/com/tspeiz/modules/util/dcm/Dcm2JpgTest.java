package com.tspeiz.modules.util.dcm;

import java.io.File;
import java.io.IOException;

public class Dcm2JpgTest {
	public static void main(String[] args) {
		int i=0;
		while(true){
			i++;
			try {
				File src = new File("C:\\Users\\kx\\Desktop\\test.dcm");
				File dest = new File("C:\\Users\\kx\\Desktop\\test.jpeg");
					Thread.sleep(500);
					Dcm2Jpeg dcm2jpg = new Dcm2Jpeg();
					System.out.println("===i:"+i);
					dcm2jpg.convert(src, dest,i);
					break;
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		/*try{
		 URL url = new URL("http://tupian201604.oss-cn-beijing.aliyuncs.com/liuan/Images/DX/2016/01/2016-01-01/156786/20160601152259486.dcm");  
         HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
         conn.setRequestMethod("GET");  
         conn.setConnectTimeout(5 * 1000);  
         InputStream inStream = conn.getInputStream();//通过输入流获取图片数据  
         Dcm2Jpeg dcm2jpg = new Dcm2Jpeg();
         File dest = new File("C:\\Users\\kx\\Desktop\\test.jpeg");
         ImageInputStream iis = ImageIO.createImageInputStream(inStream);
 		BufferedImage bi;
 		OutputStream out = null;
 		byte[] data = new byte[1024];  
 		out = new BufferedOutputStream(new FileOutputStream(dest));
		while(inStream.read(data)!= -1){ 
			out.write(data);     
		}
 		try {
 			
 			
 		} finally {
 			CloseUtils.safeClose(iis);
 			CloseUtils.safeClose(out);
 		}
		}catch(Exception e){
			e.printStackTrace();
		}
		*/
	}

}