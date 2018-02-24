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
	        File fi = new File(oldImage); // 大图文件
	        File fo = new File(newImage); // 将要转换出的小图文件
	        int nw = 126; // 定义宽为126
	        int nh = 170; // 定义高为170
	        AffineTransform transform = new AffineTransform();
	        BufferedImage bis = ImageIO.read(fi);
	        
	        int w = bis.getWidth();
	        int h = bis.getHeight();
	
	        double sx = (double) nw / w;
	        double sy = (double) nh / h;
	        // 判断是横向图形还是坚向图形
	        if (w > h) // 横向图形
	        {
	            if ((int) (sx * h) > nh) // 比较高不符合高度要求,就按高度比例
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
