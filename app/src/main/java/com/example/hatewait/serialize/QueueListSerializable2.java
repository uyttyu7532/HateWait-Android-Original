package com.example.hatewait.serialize;

import java.io.Serializable;
import java.util.List;

public class QueueListSerializable2 implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String INFO="STRQUE";
	public int autonum;
	public List<QueueInfoVo2> qivo;
	@Override
	public String toString() {
		return "QueueListSerializable [ INFO=" + INFO + ", autonum=" + autonum + ", qivo=" + qivo + "]";
	}
	public static String getInfo() {
		return INFO;
	}
	public int getAutonum() {
		return autonum;
	}
	public void setAutonum(int autonum) {
		this.autonum = autonum;
	}
	public List<QueueInfoVo2> getQivo() {
		return qivo;
	}
	public void setQivo(List<QueueInfoVo2> qivo) {
		this.qivo = qivo;
	}
}
