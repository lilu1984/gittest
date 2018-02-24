package com.wonders.tdsc.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;

public class UrlUtils {
    private static final Logger logger = Logger.getLogger(UrlUtils.class);

    /**
     * 访问指定的URL
     * 
     * @param str
     * @return 页面body信息
     */
    public static String visitURL(String strProcessURL) {
        String returnInfo = "";
        try {
            //            strProcessURL = URLEncoder.encode(strProcessURL, "UTF-8");

            //connect server
            URL objURL = new URL(strProcessURL);
            //open connect
            HttpURLConnection objConn = (HttpURLConnection) objURL.openConnection();
            objConn.setDoOutput(true);
            objConn.connect();
            //get server return message
            StringBuffer strTempBuf = new StringBuffer();
            byte[] byteArray = new byte[2048];
            int nReadCount = -1;
            BufferedReader reader = new BufferedReader(new InputStreamReader(objConn.getInputStream()));
            char[] buffer = new char[1024];
            StringBuffer stringBuffer = new StringBuffer();
            String pageContent = "";
            int count;
            while ((count = reader.read(buffer, 0, 1024)) > -1) {
                stringBuffer.append(buffer, 0, count);
            }
            pageContent = stringBuffer.toString();
            if (pageContent.indexOf("<body>")>0){
                pageContent= pageContent.substring(pageContent.indexOf("<body>")+6, pageContent.length());
            }else if (pageContent.indexOf("<BODY>")>0){
                pageContent= pageContent.substring(pageContent.indexOf("<BODY>")+6, pageContent.length());
            }
            if (pageContent.indexOf("</body>")>0){
                pageContent= pageContent.substring(0, pageContent.indexOf("</body>"));
            }else if (pageContent.indexOf("</BODY>")>0){
                pageContent= pageContent.substring(0, pageContent.indexOf("</BODY>"));
            }
            returnInfo = pageContent;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnInfo;

    }

    /**
     * 访问指定的URL
     * 
     * @param str
     */
    public static void readURLToFile(String strProcessURL) {
        try {
            StringBuffer strFileInfoBuf = new StringBuffer();

            //connect server
            URL objURL = new URL(strProcessURL);
            //open connect
            HttpURLConnection objConn = (HttpURLConnection) objURL.openConnection();
            objConn.setDoOutput(true);
            objConn.connect();
            //get server return message
            StringBuffer strTempBuf = new StringBuffer();
            byte[] byteArray = new byte[2048];
            int nReadCount = -1;
            BufferedInputStream objOutput = new BufferedInputStream(objConn.getInputStream());
            FileOutputStream objFileIn = new FileOutputStream("c:\\test.txt");
            while ((nReadCount = objOutput.read(byteArray)) != -1) {
                objFileIn.write(byteArray, 0, nReadCount);
            }
            objFileIn.close();
            objOutput.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}