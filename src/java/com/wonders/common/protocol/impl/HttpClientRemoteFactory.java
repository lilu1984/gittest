package com.wonders.common.protocol.impl;

import java.io.File;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.wonders.common.protocol.IHttpClientRemote;

public class HttpClientRemoteFactory {
	
	private static Logger logger = Logger.getLogger(HttpClientRemoteFactory.class);

	private final static IHttpClientRemote clientRemote = new IHttpClientRemote(){

		public boolean sendRemote(String filePath, String url) {
			if (StringUtils.isEmpty(filePath) || StringUtils.isEmpty(url)) return false;
			File targetFile = new File(filePath);
			PostMethod filePost = new PostMethod(url);
			try {
				Part[] parts = { new FilePart(targetFile.getName(), targetFile) };
				filePost.setRequestEntity(new MultipartRequestEntity(parts, filePost.getParams()));
				HttpClient client = new HttpClient();
				client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
				int status = client.executeMethod(filePost);
				if (status == HttpStatus.SC_OK) {
					return true;
				} else {
					return false;
				}
			} catch (Exception ex) {
				logger.error(ex.getMessage());
				throw new RuntimeException(ex.getMessage());
			} finally {
				filePost.releaseConnection();
			}
		}
	};
	
	public static IHttpClientRemote getInstance(){
		return clientRemote;
	}
}
