package com.btb.util;

import java.io.File;

public class FileUtil {
	/**
	 * 可以避免空格和非法字符
	 * @param path
	 * @return
	 */
	public static File getFile(String path) {
		return new File(StringUtil.decode(path));
	}
}
