package com.tspeiz.modules.util.dcm;

import java.io.File;
import java.util.Date;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.dcm4che3.tool.storescu.StoreSCU;

import com.tspeiz.modules.home.CommonVisitController;
import com.tspeiz.modules.util.PropertiesUtil;

public class UploadDcmUtil {
	private static Logger log = Logger.getLogger(UploadDcmUtil.class);
	public static void uploadDirDcms(String dir) {
		String[] args = new String[] {};
		boolean verbose = false;
		String inputFile = "";
		String outputFile = "";
		boolean bSameSeries = false;
		String patientname = "zssure";
		CommandLineParser parser = new BasicParser();
		Options options = new Options();
		options.addOption("h", "help", false, "Print this usage information");
		options.addOption("v", "verbose", false,
				"Print out VERBOSE information");
		options.addOption("f", "file", true, "File to transform and upload");
		options.addOption("d", "directory", true,
				"Transform and Upload all files in the Directory");
		options.addOption("n", "name", true, "Patient Name");
		options.addOption("s", "series", false, "The same series");
		CommandLine commandLine;
		try {
			commandLine = parser.parse(options, args);
			if (commandLine.hasOption('h')) {
				System.out.println("Help Message");
				//System.exit(0);
			}
			if (commandLine.hasOption('v')) {
				verbose = true;
			}
			if (commandLine.hasOption('f')) {
				inputFile = commandLine.getOptionValue('f');
				String id = String.valueOf(new Date().getTime());
				outputFile = inputFile.substring(0, inputFile.lastIndexOf('.'))
						+ id;
			}
			if (commandLine.hasOption('s')) {
				bSameSeries = true;
			}
			if (commandLine.hasOption('n')) {
				patientname = commandLine.getOptionValue('n');
			}
			if (commandLine.hasOption('d')) {
				dir = commandLine.getOptionValue('d');
			}
		} catch (org.apache.commons.cli.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File directory = new File(dir);
		if (directory.isDirectory()) {
			// directory.
			File[] files = directory.listFiles();
			for (File file : files) {
				outputFile = dir + "\\" + file.getName();
				String[] scuArgs = { "-c", "DCM4CHEE@"+PropertiesUtil.getString("DCM_IP")+":11112",
						"--user", "admin", "--user-pass",PropertiesUtil.getString("USER_PASS"), outputFile };
				StoreSCU.main(scuArgs);
				if (outputFile != null) {
					File output = new File(outputFile);
					output.deleteOnExit();// delete file when exit
				}
			}
		}
	}

	public static String uploadSingleDcm(String outputFile) {
		String status="true";
		/*String[] args = new String[] {};
		boolean verbose = false;
		String inputFile = "";
		boolean bSameSeries = false;
		String patientname = "zssure";
		String dir = "";
		CommandLineParser parser = new BasicParser();
		Options options = new Options();
		options.addOption("h", "help", false, "Print this usage information");
		options.addOption("v", "verbose", false,
				"Print out VERBOSE information");
		options.addOption("f", "file", true, "File to transform and upload");
		options.addOption("d", "directory", true,
				"Transform and Upload all files in the Directory");
		options.addOption("n", "name", true, "Patient Name");
		options.addOption("s", "series", false, "The same series");
		CommandLine commandLine;
		try {
			commandLine = parser.parse(options, args);
			if (commandLine.hasOption('h')) {
				System.out.println("Help Message");
				//System.exit(0);
			}
			if (commandLine.hasOption('v')) {
				verbose = true;
			}
			if (commandLine.hasOption('f')) {
				inputFile = commandLine.getOptionValue('f');
				String id = String.valueOf(new Date().getTime());
				outputFile = inputFile.substring(0, inputFile.lastIndexOf('.'))
						+ id;
			}
			if (commandLine.hasOption('s')) {
				bSameSeries = true;
			}
			if (commandLine.hasOption('n')) {
				patientname = commandLine.getOptionValue('n');
			}
			if (commandLine.hasOption('d')) {
				dir = commandLine.getOptionValue('d');
			}
		} catch (org.apache.commons.cli.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*///101.201.199.18
		String[] scuArgs = { "-c", "DCM4CHEE@"+PropertiesUtil.getString("DCM_IP")+":11112", "--user",
				"admin", "--user-pass",PropertiesUtil.getString("USER_PASS"), outputFile };
		//http://47.95.212.72:8088/dcm4chee-web3/
		/*String[] scuArgs = { "-c", "DCM4CHEE@192.168.199.206:11112", "--user",
				"admin", "--user-pass","admin", outputFile };*/
		try{
		StoreSCU.main(scuArgs);
		}catch(Exception e){
			//status="false";
			log.error("========上传dcm文件失败:" + e.getMessage().toString());
			
		}
		// StoreSCP.main(scuArgs);
		if (StringUtils.isNotBlank(outputFile)) {
			System.out.println("=============删除=======");
			File output = new File(outputFile);
			//output.deleteOnExit();// delete file when exit
		}
		return status;
	}

	public static void main(String[] args) {
		//uploadSingleDcm("C:\\apache-tomcat-8.0.33\\webapps\\ROOT\\dcmtemp\\1.2.392.200036.9116.2.5.1.48.1221422019.1465108723.461968.dcm");
		uploadSingleDcm("D:\\PACS\\dcm\\MR\\2016\\01\\2016-01-01\\18189\\test3.dcm");
	}
}
