package com.tspeiz.modules.pacs.util;

public class ImageOrientationUtil {
	private static final double obliquityThresholdCosineValue = 0.8D;

	  public static final String getMajorAxisFromPatientRelativeDirectionCosine(double x, double y, double z)
	  {
	    String axis = null;

	    String orientationX = x < 0.0D ? "R" : "L";
	    String orientationY = y < 0.0D ? "A" : "P";
	    String orientationZ = z < 0.0D ? "F" : "H";

	    double absX = Math.abs(x);
	    double absY = Math.abs(y);
	    double absZ = Math.abs(z);

	    if ((absX > 0.8D) && (absX > absY) && (absX > absZ))
	      axis = orientationX;
	    else if ((absY > 0.8D) && (absY > absX) && (absY > absZ))
	      axis = orientationY;
	    else if ((absZ > 0.8D) && (absZ > absX) && (absZ > absY)) {
	      axis = orientationZ;
	    }

	    return axis;
	  }

	  public static final String makeImageOrientationLabelFromImageOrientationPatient(double rowX, double rowY, double rowZ, double colX, double colY, double colZ)
	  {
	    String label = null;
	    String rowAxis = getMajorAxisFromPatientRelativeDirectionCosine(rowX, rowY, rowZ);
	    String colAxis = getMajorAxisFromPatientRelativeDirectionCosine(colX, colY, colZ);
	    if ((rowAxis != null) && (colAxis != null)) {
	      if (((rowAxis.equals("R")) || (rowAxis.equals("L"))) && ((colAxis.equals("A")) || (colAxis.equals("P"))))
	        label = "AXIAL";
	      else if (((colAxis.equals("R")) || (colAxis.equals("L"))) && ((rowAxis.equals("A")) || (rowAxis.equals("P"))))
	        label = "AXIAL";
	      else if (((rowAxis.equals("R")) || (rowAxis.equals("L"))) && ((colAxis.equals("H")) || (colAxis.equals("F"))))
	        label = "CORONAL";
	      else if (((colAxis.equals("R")) || (colAxis.equals("L"))) && ((rowAxis.equals("H")) || (rowAxis.equals("F"))))
	        label = "CORONAL";
	      else if (((rowAxis.equals("A")) || (rowAxis.equals("P"))) && ((colAxis.equals("H")) || (colAxis.equals("F"))))
	        label = "SAGITTAL";
	      else if (((colAxis.equals("A")) || (colAxis.equals("P"))) && ((rowAxis.equals("H")) || (rowAxis.equals("F")))) label = "SAGITTAL"; 
	    }
	    else {
	      label = "OBLIQUE";
	    }
	    return label;
	  }

	  public static final String makePatientOrientationFromPatientRelativeDirectionCosine(double x, double y, double z)
	  {
	    StringBuffer buffer = new StringBuffer();

	    String orientationX = x < 0.0D ? "R" : "L";
	    String orientationY = y < 0.0D ? "A" : "P";
	    String orientationZ = z < 0.0D ? "F" : "H";

	    double absX = Math.abs(x);
	    double absY = Math.abs(y);
	    double absZ = Math.abs(z);

	    for (int i = 0; i < 3; i++) {
	      if ((absX > 0.0001D) && (absX > absY) && (absX > absZ)) {
	        buffer.append(orientationX);
	        absX = 0.0D;
	      } else if ((absY > 0.0001D) && (absY > absX) && (absY > absZ)) {
	        buffer.append(orientationY);
	        absY = 0.0D; } else {
	        if ((absZ <= 0.0001D) || (absZ <= absX) || (absZ <= absY)) break;
	        buffer.append(orientationZ);
	        absZ = 0.0D;
	      }
	    }

	    return buffer.toString();
	  }

	  public static String getOrientation(String imageOrientation)
	  {
	    String[] imageOrientationArray = imageOrientation.split("\\\\");
	    float _imgRowCosx = Float.parseFloat(imageOrientationArray[0]);
	    float _imgRowCosy = Float.parseFloat(imageOrientationArray[1]);
	    float _imgRowCosz = Float.parseFloat(imageOrientationArray[2]);
	    float _imgColCosx = Float.parseFloat(imageOrientationArray[3]);
	    float _imgColCosy = Float.parseFloat(imageOrientationArray[4]);
	    float _imgColCosz = Float.parseFloat(imageOrientationArray[5]);

	    String[] imgOrientation = new String[2];
	    imgOrientation[0] = makePatientOrientationFromPatientRelativeDirectionCosine(_imgRowCosx, _imgRowCosy, _imgRowCosz);
	    imgOrientation[1] = makePatientOrientationFromPatientRelativeDirectionCosine(_imgColCosx, _imgColCosy, _imgColCosz);

	    String plane = makeImageOrientationLabelFromImageOrientationPatient(_imgRowCosx, _imgRowCosy, _imgRowCosz, _imgColCosx, _imgColCosy, _imgColCosz);
	    if (plane.equalsIgnoreCase("SAGITTAL")) {
	      imgOrientation[1] = imgOrientation[1].replace("H", "S");
	      imgOrientation[1] = imgOrientation[1].replace("F", "I");
	    }

	    return imgOrientation[0].substring(0, Math.min(imgOrientation[0].length(), 2)) + "\\" + imgOrientation[1].substring(0, Math.min(imgOrientation[1].length(), 2));
	  }
}
