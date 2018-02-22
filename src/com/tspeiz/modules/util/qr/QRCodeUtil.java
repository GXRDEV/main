package com.tspeiz.modules.util.qr;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;

import javax.imageio.ImageIO;

import org.json.JSONObject;

import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.Encoder;
import com.google.zxing.qrcode.encoder.QRCode;

public class QRCodeUtil {
	private static final int QUIET_ZONE_SIZE = 4;
	private static final int BLACK = 0xFF000000;
	private static final int WHITE = 0xFFFFFFFF;

	
	public static BufferedImage generateQRCode(String contents,int width,int height)throws Exception{
		BitMatrix bitMatrix = encode(contents, width, height);
		BufferedImage image = toBufferedImage(bitMatrix);
		return image;
	}
	
	

	public static BitMatrix encode(String contents, int width, int height)
			throws WriterException {
		return encode(contents, width, height, null);
	}

	public static BitMatrix encode(String contents, int width, int height,
			Map<EncodeHintType, ?> hints) throws WriterException {
		if (contents.isEmpty()) {
			throw new IllegalArgumentException("Found empty contents");
		}
		if (width < 0 || height < 0) {
			throw new IllegalArgumentException(
					"Requested dimensions are too small: " + width + 'x'
							+ height);
		}
		ErrorCorrectionLevel errorCorrectionLevel = ErrorCorrectionLevel.L;
		int quietZone = QUIET_ZONE_SIZE;
		if (hints != null) {
			ErrorCorrectionLevel requestedECLevel = (ErrorCorrectionLevel) hints
					.get(EncodeHintType.ERROR_CORRECTION);
			if (requestedECLevel != null) {
				errorCorrectionLevel = requestedECLevel;
			}
			Integer quietZoneInt = (Integer) hints.get(EncodeHintType.MARGIN);
			if (quietZoneInt != null) {
				quietZone = quietZoneInt;
			}
		}
		QRCode code = Encoder.encode(contents, errorCorrectionLevel, hints);
		return renderResult(code, width, height, quietZone);
	}

	private static BitMatrix renderResult(QRCode code, int width, int height,
			int quietZone) {
		ByteMatrix input = code.getMatrix();
		if (input == null) {
			throw new IllegalStateException();
		}
		int inputWidth = input.getWidth();
		int inputHeight = input.getHeight();
		int qrWidth = inputWidth;
		int qrHeight = inputHeight;
		int outputWidth = Math.max(width, qrWidth);
		int outputHeight = Math.max(height, qrHeight);
		int multiple = Math.min(outputWidth / qrWidth, outputHeight / qrHeight);
		int leftPadding = (outputWidth - (inputWidth * multiple)) / 2;
		int topPadding = (outputHeight - (inputHeight * multiple)) / 2;
		if (leftPadding >= 0) {
			outputWidth = outputWidth - 2 * leftPadding + 2;
			leftPadding = 2;
		}
		if (topPadding >= 0) {
			outputHeight = outputHeight - 2 * topPadding + 2;
			topPadding = 2;
		}
		BitMatrix output = new BitMatrix(outputWidth, outputHeight);
		for (int inputY = 0, outputY = topPadding; inputY < inputHeight; inputY++, outputY += multiple) {
			for (int inputX = 0, outputX = leftPadding; inputX < inputWidth; inputX++, outputX += multiple) {
				if (input.get(inputX, inputY) == 1) {
					output.setRegion(outputX, outputY, multiple, multiple);
				}
			}
		}
		return output;
	}

	private static BufferedImage toBufferedImage(BitMatrix matrix) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
			}
		}
		return image;
	}
}
