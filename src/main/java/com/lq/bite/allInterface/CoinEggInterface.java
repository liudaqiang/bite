package com.lq.bite.allInterface;

public class CoinEggInterface {
	//获取当前最新行情
	public static final String TICKER = "https://api.coinegg.com/api/v1/ticker/";
	//市场深度
	public static final String DEPTH = "https://api.coinegg.com/api/v1/depth/";
	//最近的市场交易
	public static final String ORDERS = "https://api.coinegg.com/api/v1/orders/";
	//账户信息
	public static final String BALANCE = "https://api.coinegg.com/api/v1/balance/";
	//挂单查询
	public static final String TRADE_LIST = "https://api.coinegg.com/api/v1/trade_list/";
	//查询订单信息
	public static final String TRADE_VIEW = "https://api.coinegg.com/api/v1/trade_view/";
	//取消订单
	public static final String TRADE_CANCEL = "https://api.coinegg.com/api/v1/trade_cancel/";
	//下单
	public static final String TRADE_ADD = "https://api.coinegg.com/api/v1/trade_add/";
}
