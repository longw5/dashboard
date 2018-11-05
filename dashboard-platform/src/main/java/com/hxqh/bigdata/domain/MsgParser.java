package com.hxqh.bigdata.domain;

import java.io.Serializable;

/**
 * 解析器字段描述
 * @author wulong
 */
public class MsgParser implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String name;
	private int length;
	private int start;
	private int end;
	private String value;
	
	public MsgParser() {
		super();
	}

	public MsgParser(String name, int length, int start, int end, String value) {
		super();
		this.name = name;
		this.length = length;
		this.start = start;
		this.end = end;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "MsgParser [name=" + name + ", length=" + length + ", start=" + start + ", end=" + end + ", value="
				+ value + "]";
	}
}
