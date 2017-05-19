package com.zhiyuninfo.dm.client;

public class DeviceAttribute {
	public String name;
	public String type;
	
	public final static String STRING_LIST_TYPE="StringList";
	public final static String STRING_TYPE="String";
	public final static String INT_TYPE="Int";
	public final static String DOUBLE_TYPE="Double";
	public final static String BOOLEAN_TYPE="Boolean";
	public final static String BYTE_TYPE="Byte";
	public final static String SHORT_TYPE="Short";
	public final static String FLOAT_TYPE="Float";
	public final static String FILE_TYPE="File";
	
	public DeviceAttribute(String name,String type){
		this.name=name;
		this.type=type;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DeviceAttribute other = (DeviceAttribute) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
}
