package HateWait.vo;

import java.io.Serializable;
import java.util.List;

public class QueueListSerializable implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String INFO="STRQUE";
	public int autonum;
	public List<QueueInfoVo> qivo;
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
	public List<QueueInfoVo> getQivo() {
		return qivo;
	}
	public void setQivo(List<QueueInfoVo> qivo) {
		this.qivo = qivo;
	}
}
