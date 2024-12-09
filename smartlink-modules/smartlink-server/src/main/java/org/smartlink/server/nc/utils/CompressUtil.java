package org.smartlink.server.nc.utils;


import cn.hutool.core.codec.Base64Encoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.Deflater;
import java.util.zip.GZIPOutputStream;

/**
 * 压缩工具类
 */
public class CompressUtil {

	private static int buffSize = 1024;

	/**
	 * deflaterCompress
	 * 	默认压缩
	 *
	 * @param source 原文
	 * @return
	 * @throws IOException
	 * @throws
	 * @throws Exception
	 */
	public static String deflaterCompress(String source) throws Exception {
		String value = null;

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		Deflater compressor = new Deflater();

		try {

			byte[] input = source.getBytes(StandardCharsets.UTF_8);
			// 设置压缩登记
			compressor.setLevel(Deflater.DEFAULT_COMPRESSION);
			compressor.setInput(input);
			compressor.finish();
			final byte[] buf = new byte[buffSize];

			while (!compressor.finished()) {
				int count = compressor.deflate(buf);
				bos.write(buf, 0, count);
			}
			value = Base64Encoder.encode(bos.toByteArray());

		} finally {
			bos.close();
			compressor.end();
		}

		return value;
	}

	/**
	 * gzipCompress
	 * 	基于gzip压缩
	 *
	 * @param source 原文
	 * @return
	 * @throws IOException
	 * @throws
	 * @throws Exception
	 */
	public static String gzipCompress(String source) throws Exception {
		String value = null;

		ByteArrayOutputStream out = null;

		try {
			out = new ByteArrayOutputStream();
			GZIPOutputStream gzip = new GZIPOutputStream(out);
			byte[] input = source.getBytes(StandardCharsets.UTF_8);
			gzip.write(input);
			gzip.close();
			value = Base64Encoder.encode(out.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception("压缩异常 " + e.getMessage());
		}finally {
			if(out != null){
				out.close();
			}
		}

		return value;
	}

}
