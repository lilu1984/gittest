package com.wonders.common.protocol;

/**
 * 向服务端发送文件
 * @author Gordon
 *
 */
public interface IHttpClientRemote {

	boolean sendRemote(String filePath, String url);
}
