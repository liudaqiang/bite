package com.lq.bite.allApi;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.lq.bite.allInterface.GateIoInterface;
import com.lq.bite.entity.gateIo.GateIoTicks;
import com.lq.bite.utils.HttpRequestUtils;
import com.lq.bite.utils.StringUtils;

import wxPusher.WxPusherMessage;

public class GateIoApi {
	/**
	 * 合约市场 K 线图 5min
	 * @param contract
	 * @param limit
	 * @param interval
	 * @return
	 * @throws Exception
	 */
	public static List<GateIoTicks> getCandlesticks5M(String contract,String limit,String interval)throws Exception{
		String path = GateIoInterface.HOST+GateIoInterface.PREFIX+GateIoInterface.CANDLESTICKS;
		String param = "contract="+contract;
		if(StringUtils.isNotBlank(limit)){
			param = param+"&limit="+limit;
		}
		if(StringUtils.isNotBlank(interval)){
			param = param+"&interval="+interval;
		}
		String sendGETRequest = HttpRequestUtils.sendGETRequest(path, param, "UTF-8");
		List<GateIoTicks> list = JSONObject.parseArray(sendGETRequest, GateIoTicks.class);
		if(list.size() <= 0 ){
			return null;
		}
		String time = list.get(0).getT();//时间字符串
		String jylStr =  list.get(0).getV();//交易量
		jylStr = Double.valueOf(jylStr)/10000+"";
		System.out.println("交易量："+jylStr);
		if(Double.valueOf(jylStr) < 30){
			return null;
		}
		String spj = list.get(0).getC();//收盘价
		String kpj = list.get(0).getO();//开盘价
		Double zf = (Double.valueOf(spj)-Double.valueOf(kpj))/Double.valueOf(kpj);
		DecimalFormat df = new DecimalFormat("######0.0000"); 
		String zfStr = df.format(zf*100);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    String sd = sdf.format(new Date(Long.parseLong(String.valueOf(time))*1000));      // 时间戳转换成时间
	    System.out.println("格式化结果：" + sd);
		StringBuffer sb = new StringBuffer();
		sb.append("获取数据时间:"+sd);
		sb.append("<br />");
		sb.append("交易量:"+jylStr+"BTC");
		sb.append("<br />");
		sb.append("开盘价:"+kpj);
		sb.append("<br />");
		sb.append("收盘价:"+spj);
		sb.append("<br />");
		sb.append("涨跌幅:"+zfStr+"%");
		List<String> topics = new ArrayList<>(); 
		topics.add("1787");
		String retStr = WxPusherMessage.setMessage("5分钟交易数据异常波动", sb.toString(), 2, topics, null);
		return list;
	}
	/**
	 * 合约市场 K 线图1min
	 * @param contract
	 * @param limit
	 * @param interval
	 * @return
	 * @throws Exception
	 */
	public static List<GateIoTicks> getCandlesticks1M(String contract,String limit,String interval)throws Exception{
		String path = GateIoInterface.HOST+GateIoInterface.PREFIX+GateIoInterface.CANDLESTICKS;
		String param = "contract="+contract;
		if(StringUtils.isNotBlank(limit)){
			param = param+"&limit="+limit;
		}
		if(StringUtils.isNotBlank(interval)){
			param = param+"&interval="+interval;
		}
		String sendGETRequest = HttpRequestUtils.sendGETRequest(path, param, "UTF-8");
		List<GateIoTicks> list = JSONObject.parseArray(sendGETRequest, GateIoTicks.class);
		
		if(list.size() <= 0 ){
			return null;
		}
		String time = list.get(0).getT();//时间字符串
		String jylStr =  list.get(0).getV();//交易量
		jylStr = Double.valueOf(jylStr)/10000+"";
		String spj = list.get(0).getC();//收盘价
		String kpj = list.get(0).getO();//开盘价
		Double zf = (Double.valueOf(spj)-Double.valueOf(kpj))/Double.valueOf(kpj);
		DecimalFormat df = new DecimalFormat("######0.0000"); 
		System.out.println("涨幅："+zf*100);
		if(Double.valueOf(jylStr) < 9.4){
			return null;
		}
		String zfStr = df.format(zf*100);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    String sd = sdf.format(new Date(Long.parseLong(String.valueOf(time))*1000));      // 时间戳转换成时间
	    System.out.println("格式化结果：" + sd);
		StringBuffer sb = new StringBuffer();
		sb.append("获取数据时间:"+sd);
		sb.append("<br />");
		sb.append("交易量:"+jylStr+"BTC");
		sb.append("<br />");
		sb.append("开盘价:"+kpj);
		sb.append("<br />");
		sb.append("收盘价:"+spj);
		sb.append("<br />");
		sb.append("涨跌幅:"+zfStr+"%");
		List<String> topics = new ArrayList<>(); 
		topics.add("1787");
		String retStr = WxPusherMessage.setMessage("1分钟交易数据异常波动", sb.toString(), 2, topics, null);
		System.out.println(retStr);
		return list;
	}
}
