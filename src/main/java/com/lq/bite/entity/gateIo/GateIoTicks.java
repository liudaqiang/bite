package com.lq.bite.entity.gateIo;
/**
 * 合约市场 K 线图
 * @author Administrator
 *
 */
public class GateIoTicks {
	private String t;//时间戳
	private String o;//开盘价
	private String v;//交易量，只有市场行情的 K 线数据里有该值
	private String h;//最高价
	private String c;//收盘价
	private String l;//最低价
	public String getT() {
		return t;
	}
	public void setT(String t) {
		this.t = t;
	}
	public String getO() {
		return o;
	}
	public void setO(String o) {
		this.o = o;
	}
	public String getV() {
		return v;
	}
	public void setV(String v) {
		this.v = v;
	}
	public String getH() {
		return h;
	}
	public void setH(String h) {
		this.h = h;
	}
	public String getC() {
		return c;
	}
	public void setC(String c) {
		this.c = c;
	}
	public String getL() {
		return l;
	}
	public void setL(String l) {
		this.l = l;
	}
	@Override
	public String toString() {
		return "GateIoTicks [t=" + t + ", o=" + o + ", v=" + v + ", h=" + h + ", c=" + c + ", l=" + l + "]";
	}
	
	
}
