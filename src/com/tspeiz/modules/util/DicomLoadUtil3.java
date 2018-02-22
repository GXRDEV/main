package com.tspeiz.modules.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
/*import org.dcm4che3.io.DicomInputStream;
import org.dcm4che3.io.DicomInputStream.IncludeBulkData;*/
import org.dcm4che3.util.SafeClose;

import com.tspeiz.modules.util.DicomInputStream.IncludeBulkData;

public class DicomLoadUtil3 {
	private static Logger log = Logger.getLogger(DicomLoadUtil.class);
	private static SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
	private static SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat _sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/* @Test
	    public void testPart10BigEndian() throws Exception {
	        Attributes attrs = readFromResource("US-RGB-8-epicard", IncludeBulkData.NO);
	        assertEquals(3, attrs.getInt(Tag.SamplesPerPixel, 0));
	    }*/

	public static Map<String, Object> gainDicomSeries2(String httpurl) {
		log.info("===============开始解析图片=====");
		Map<String, Object> map = new HashMap<String, Object>();
		DicomInputStream dis = null;
		InputStream stream = null;
		try {
			
			URL u = new URL(httpurl);
			stream = u.openStream();
			
			Attributes attrs = readFromResource(stream, IncludeBulkData.NO);
			
			//dis = new DicomInputStream(stream);
			//Attributes attrs = dis.readFileMetaInformation();
			
			
			log.info("===============解析图片完成=====");
			map.put("seriesNum",  attrs.getString(Tag.SeriesNumber));
			map.put("patient_id",attrs.getString(Tag.PatientID));
			map.put("study_id", attrs.getString(Tag.StudyID));
			map.put("series_id", attrs.getString(Tag.SeriesInstanceUID));
			map.put("instance_id", attrs.getString(Tag.SOPInstanceUID));
			
		} catch (Exception e) {
			e.printStackTrace();
		}  finally {
			SafeClose.close(stream);
			SafeClose.close(dis);
		}
	
		return map;
	}

	 public static Map<String, Object> LoadDicomObject(File file) {
			Map<String, Object> map = new HashMap<String, Object>();
			DicomInputStream dis = null;
			
			try {		
				Attributes attrs = readFromResource(file, IncludeBulkData.NO);
				//dis = new DicomInputStream(file);
	            //Attributes attrs = dis.readFileMetaInformation();
				
				//System.out.println( "\n==============" + dobj.toString() + "\n");
				//map.put("Patient_ID_Issuer", dobj.getString(0x00100021));// 序列号
				//map.put("other_patient_id", dobj.getString(0x00101000));// 患者id
				//map.put("other_patient_id_sequence", dobj.getString(0x00101002));// 患者姓名
				
				map.put("seriesNum", attrs.getString(Tag.SeriesNumber));// 序列号
				map.put("patient_id", attrs.getString(Tag.PatientID));// 患者id
				map.put("Patient_Name", attrs.getString(Tag.PatientName));// 患者姓名
				map.put("study_id", attrs.getString(Tag.StudyID));// 检查id
				map.put("series_id", attrs.getString(Tag.SeriesInstanceUID));// 序列id
				map.put("instance_id", attrs.getString(Tag.SOPInstanceUID));// 实例id
				String redate=attrs.getString(Tag.StudyDate);
				try {
					redate=fmt.format(sdf.parse(redate))+" 00:00:00";
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				map.put("REPORT_DATE",redate);// 检查时间
				map.put("Modality", attrs.getString(Tag.Modality));// 检查类型
				//System.out.println(dobj.getString(0x00100010));
				//System.out.println(gb2312ToUtf8(dobj.getString(0x00100010)));
				//System.out.println(utf8Togb2312(dobj.getString(0x00100010)));
			
				System.out.println( "\n==============" +JSONObject.fromObject(map).toString() + "\n");			
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.error("===============解析dcm文件失败:" + e.getMessage().toString());
			} finally {
				 SafeClose.close(dis);
			}
			return map;
		}

	  private static Attributes readFromResource(File file, 
	            IncludeBulkData includeBulkData)
	            throws Exception {
		    Attributes fmi = null;
	        DicomInputStream in = new DicomInputStream(
	        		file);
	        try {
	            in.setIncludeBulkData(includeBulkData);
	            in.setAddBulkDataReferences(includeBulkData == IncludeBulkData.URI); 
	            fmi= in.readDataset(-1, -1);
	        }finally {
	        	SafeClose.close(in);
	        }
	        
	        return fmi;
	    }
	  
	  private static Attributes readFromResource(InputStream stream, 
	            IncludeBulkData includeBulkData)
	            throws Exception {
	        DicomInputStream in = new DicomInputStream(
	        		stream);
	        Attributes fmi = null;

	        try {
	            in.setIncludeBulkData(includeBulkData);
	            in.setAddBulkDataReferences(includeBulkData == IncludeBulkData.URI);
	            fmi= in.readDataset(-1,-1);
	        
	        } finally {
	        	SafeClose.close(in);
	        }
	        return fmi;
	    }
	  
}
