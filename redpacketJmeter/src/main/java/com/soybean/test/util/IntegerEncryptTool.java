package com.soybean.test.util;

import java.util.Random;

/**
 * Description: 这里用一句话描述这个类的作用
 * Company    : 上海黄豆网络科技有限公司
 * @author     : pangzt
 * Date       : 2018/3/23
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 * v1.0.0        2018/3/23        pangzt         新增              1001
 ********************************************************************/

public class IntegerEncryptTool {

	private static final char[][] charGroups = {
			{'z', 'k', 'n', 'x', 's', 'w', 't', '9', '7'},
			{'j', 'i', 'o', 'b', '5', 'h', '3', 'r', '8'},
			{'f', 'c', 'm', '0', '6', 'p', 'e', 'y', 'q'},
			{'l', 'a', '4', '2', '1', 'd', 'g', 'v', 'u'}
	};

	public static String Encrypt(int value) {
		Random random = new Random(value);
		if (value == 0) {
			char[] zeroChars = charGroups[0];
			return zeroChars[random.nextInt(zeroChars.length)] + "";
		}
		StringBuilder builder = new StringBuilder(16);
		for (int i = 30; i >= 0; i -= 2) {
			int segValue = (value >> i) & 3;
			char[] chars = charGroups[segValue];
			builder.append(chars[random.nextInt(chars.length)]);
		}
		return builder.toString();
	}

	public static int Decrypt(String text) {
		if ("".equals(text) || null == text) {
			throw new IllegalArgumentException(text);
		}
		char[] textArray = text.toCharArray();
		int value = 0;
		for (int i = text.length() - 1; i >= 0; --i) {
			char ch = textArray[i];
			int segValue = -1;
			for (int j = 0; j < 4; ++j) {
				if (indexOf(charGroups[j], ch) != -1) {
					segValue = j;
					break;
				}
			}
			if (segValue < 0) {
				throw new IllegalArgumentException("Invalid character in cipher: text");
			}
			value |= segValue << ((textArray.length - i - 1) * 2);
		}
		return value;
	}

	public static int indexOf(char[] a,char key) {
		for (int i = 0; i < a.length; i++) {
			if (a[i] == key) {
				return i;
			}
		}
		return -1;
	}

	public static void main(String[] args) {
		System.out.println(IntegerEncryptTool.Decrypt("wx9nweqpsk45c3wy"));
	}

}
