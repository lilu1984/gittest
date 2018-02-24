package com.wonders.tdsc.common.util;

import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ConvertImageUtil {
    public static void convert(String oldImage, String newImage) throws Exception {
    	try{
	        File fi = new File(oldImage); // ��ͼ�ļ�
	        File fo = new File(newImage); // ��Ҫת������Сͼ�ļ�
	        int nw = 126; // �����Ϊ126
	        int nh = 170; // �����Ϊ170
	        AffineTransform transform = new AffineTransform();
	        BufferedImage bis = ImageIO.read(fi);
	        
	        int w = bis.getWidth();
	        int h = bis.getHeight();
	
	        double sx = (double) nw / w;
	        double sy = (double) nh / h;
	        // �ж��Ǻ���ͼ�λ��Ǽ���ͼ��
	        if (w > h) // ����ͼ��
	        {
	            if ((int) (sx * h) > nh) // �Ƚϸ߲����ϸ߶�Ҫ��,�Ͱ��߶ȱ���
	            {
	                sx = sy;
	                nw = (int) (w * sx);
	            } else {
	                sy = sx;
	                nh = (int) (h * sy);
	            }
	        } else {
	            if ((int) (sy * w) > nw) {
	                sy = sx;
	                nh = (int) (h * sy);
	            } else {
	                sx = sy;
	                nw = (int) (w * sx);
	            }
	        }
	
	        transform.setToScale(sx, sy);
	        AffineTransformOp ato = new AffineTransformOp(transform, null);
	        BufferedImage bid = new BufferedImage(nw, nh, BufferedImage.TYPE_3BYTE_BGR);
	        ato.filter(bis, bid);
	        ImageIO.write(bid, "jpeg", fo);
    	}catch(Exception ex){
    		throw ex;
    	}
    }
}
