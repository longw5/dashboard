package com.hxqh;

import java.util.concurrent.ConcurrentHashMap;

public class TestUtil {

	public static void main(String[] args) {
		
		
		ConcurrentHashMap<Object, Object> map = new ConcurrentHashMap<>();
		
		map.put("aa", 111);
		map.put("bb", 111);
		map.put("cc", 111);
		
		Object remove1 = map.remove("dd");
		Object remove2 = map.remove("cc");
		
		System.out.println(remove1);
		System.out.println(map);
	}
	
}
