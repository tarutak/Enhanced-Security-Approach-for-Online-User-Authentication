/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import constants.Constants;
import java.util.Date;



public class QRCode implements Constants {

    public static String createQRCode(String qrCodeData) {
        try {
            if(!new File(uploads).exists()) {
                new File(uploads).mkdirs();
            }
            Map hintMap = new HashMap();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            String fileName = format.format(new Date()) + ".png";
            BitMatrix matrix = new MultiFormatWriter().encode(new String(qrCodeData.getBytes(charset), charset), BarcodeFormat.QR_CODE, w, h, hintMap);
            MatrixToImageWriter.writeToFile(matrix, ext, new File(uploads, fileName));
            return fileName;
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
        return null;
    }

    public static String readQRCode(String filePath) {
        try {
            Map hintMap = new HashMap();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(ImageIO.read(new FileInputStream(filePath)))));
            Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap, hintMap);
            return qrCodeResult.getText();
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
        return null;
        
    }
}
