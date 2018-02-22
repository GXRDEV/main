package com.tspeiz.modules.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.dcm4che2.data.DicomElement;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.io.DicomInputStream;
import org.dcm4che2.util.CloseUtils;

import com.tspeiz.modules.util.ftp.FTPUtil;

public class DicomLoadUtil {
	private static Logger log = Logger.getLogger(DicomLoadUtil.class);
	private static SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
	private static SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat _sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static String gainDicomSeries(String httpurl) {
		log.info("===============开始解析图片=====");
		DicomObject dobj = null;
		try {
			// URL u=new URL(httpurl);
			FTPUtil ftputil = new FTPUtil();
			InputStream in = null;
			if (ftputil.connect()) {
				ftputil.getFtpClient().enterLocalPassiveMode();
				in = ftputil.getFtpClient().retrieveFileStream(httpurl);
				if (in != null) {
					dobj = loadDicomObject(in);
					in.close();
				}
				// ftputil.getFtpClient().completePendingCommand();
				if (!ftputil.getFtpClient().completePendingCommand()) {
					log.info("====关闭ftp===");
					ftputil.getFtpClient().logout();
					ftputil.getFtpClient().disconnect();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("===============解析图片完成=====");
		return dobj.getString(0x00200011);
	}

	public static Map<String, Object> gainDicomSeries2(String httpurl) {
		log.info("===============开始解析图片=====");
		Map<String, Object> map = new HashMap<String, Object>();
		DicomObject dobj = null;
		try {
			URL u = new URL(httpurl);
			InputStream in = u.openStream();
			if (in != null) {
				dobj = loadDicomObject(in);
				in.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("===============解析图片完成=====");
		map.put("seriesNum", dobj.getString(0x00200011));
		map.put("patient_id", dobj.getString(0x00100020));
		map.put("study_id", dobj.getString(0x0020000D));
		map.put("series_id", dobj.getString(0x0020000E));
		map.put("instance_id", dobj.getString(0x00080018));
		return map;
	}

	public static String gainDicomSeries(InputStream in) {
		log.info("===============开始解析图片=====");
		DicomObject dobj = null;
		try {
			dobj = loadDicomObject(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("===============解析图片完成=====");
		return dobj.getString(0x00200011);
	}

	private static DicomObject loadDicomObject(InputStream is) {
		DicomInputStream in = null;
		DicomObject obj = null;
		try {
			// in = new DicomInputStream(file);
			in = new DicomInputStream(is);
			obj = in.readDicomObject();
			in.close();
			return obj;
		} catch (IOException e) {
			exit(e.getMessage());
			throw new RuntimeException();
		}
	}

	private static void exit(String msg) {
		System.err.println(msg);
		System.err.println("Try 'dcm2jpg -h' for more information.");
		//System.exit(1);
	}

	@SuppressWarnings("unchecked")
	public static void main(String args[]) throws Exception {
		File src = new File(
				"C:\\Users\\kx\\Desktop\\1-1.dcm");
		load(src);
		/*
		 * System.out.println("==========="); File src2 = new
		 * File("C:\\Users\\kx\\Desktop\\test1.dcm"); LoadDicomObject(src2);
		 */
	}
	
	public static void load(File file){
		DicomObject dcmObj = null;
		DicomInputStream in = null;
		try {
			in = new DicomInputStream(file);
			if (in != null) {
				dcmObj = in.readDicomObject();
				in.close();
		/* DicomElement wcDcmElement = dcmObj.get(2625616);
		 System.out.println(wcDcmElement.toString());
         DicomElement wwDcmElement = dcmObj.get(2625617);
         System.out.println(wwDcmElement.toString());
         DicomElement rowDcmElement = dcmObj.get(2621456);
         System.out.println(rowDcmElement.toString());
         DicomElement colDcmElement = dcmObj.get(2621457);
         System.out.println(colDcmElement.toString());
         DicomElement imgOrientation = dcmObj.get(2097207);
         System.out.println(imgOrientation.toString());
         DicomElement imgPosition = dcmObj.get(2097202);
         System.out.println(imgPosition.toString());
         DicomElement sliceThick = dcmObj.get(1572944);
         System.out.println(sliceThick.toString());
         DicomElement frameOfRefUID = dcmObj.get(2097234);
         System.out.println(frameOfRefUID.toString());
         DicomElement pixelSpacingEle = dcmObj.get(2621488);
         System.out.println(pixelSpacingEle.toString());
         DicomElement totalFramesEle = dcmObj.get(2621448);
         System.out.println(totalFramesEle.toString());
         DicomElement imageType = dcmObj.get(524296);
         System.out.println(imageType.toString());
         System.out.println(dcmObj.getString(0x00080008));
         String sex = dcmObj.getString(1048640);
         System.out.println("===sex=="+sex);
*/			
		Iterator<DicomElement> eles= dcmObj.iterator();
		while(eles.hasNext()){
			DicomElement ele=eles.next();
			System.out.println(JSONObject.fromObject(ele).toString());
		}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
         
	}

	public static Map<String, Object> LoadDicomObject(File file) {
		DicomObject dobj = null;
		DicomInputStream in = null;
		Map<String, Object> map = new HashMap<String, Object>();
		
	
		try {
			in = new DicomInputStream(file);
			if (in != null) {
				dobj = in.readDicomObject();
				in.close();
				
				//System.out.println( "\n==============" + dobj.toString() + "\n");
				//map.put("Patient_ID_Issuer", dobj.getString(0x00100021));// 序列号
				//map.put("other_patient_id", dobj.getString(0x00101000));// 患者id
				//map.put("other_patient_id_sequence", dobj.getString(0x00101002));// 患者姓名
		
				map.put("seriesNum", dobj.getString(0x00200011));// 序列号
				map.put("patient_id", dobj.getString(0x00100020));// 患者id
				map.put("Patient_Name", dobj.getString(0x00100010));// 患者姓名
				map.put("study_id", dobj.getString(0x0020000D));// 检查id
				map.put("series_id", dobj.getString(0x0020000E));// 序列id
				map.put("instance_id", dobj.getString(0x00080018));// 实例id
				String redate=dobj.getString(0x00080020);
				try {
					redate=fmt.format(sdf.parse(redate))+" 00:00:00";
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				map.put("REPORT_DATE",redate);// 检查时间
				map.put("Modality", dobj.getString(0x00080060));// 检查类型
				System.out.println(dobj.getString(0x00100010));
				System.out.println(gb2312ToUtf8(dobj.getString(0x00100010)));
				System.out.println(utf8Togb2312(dobj.getString(0x00100010)));
				
				System.out.println( "\n==============" +JSONObject.fromObject(map).toString() + "\n");
				
			}
		} catch (IOException e) {
			// exit(e.getMessage());
			log.error("===============解析dcm文件失败:" + e.getMessage().toString());
			throw new RuntimeException();
		} finally {
			CloseUtils.safeClose(in);
		}
		return map;
	}

	public static String gb2312ToUtf8(String str) {
		String urlEncode = "";
		try {
			urlEncode = URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return urlEncode;
	}
	
	public static String utf8Togb2312(String str){

        StringBuffer sb = new StringBuffer();

        for ( int i=0; i<str.length(); i++) {

            char c = str.charAt(i);

            switch (c) {

               case '+' :

                   sb.append( ' ' );

               break ;

               case '%' :

                   try {

                        sb.append(( char )Integer.parseInt (

                        str.substring(i+1,i+3),16));

                   }

                   catch (NumberFormatException e) {

                       throw new IllegalArgumentException();

                  }

                  i += 2;
                  break ;
               default :
                  sb.append(c);
                  break ;
             }
        }
        String result = sb.toString();
        String res= null ;
        try {
             byte [] inputBytes = result.getBytes( "8859_1" );
            res= new String(inputBytes, "UTF-8" );
        }
        catch (Exception e){}
        return res;

  }

}
