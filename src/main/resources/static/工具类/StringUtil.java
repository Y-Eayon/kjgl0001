package com.zt.common;

public class StringUtil {
	public static boolean empty(Object obj){
		boolean flag = true;// true 是空的
		if(obj != null && !"".equals(obj.toString())){
			flag = false;
		}
		return flag;
		
	}

}
