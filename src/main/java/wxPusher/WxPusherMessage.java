package wxPusher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.lq.bite.utils.HttpClientUtil;
import com.lq.bite.utils.HttpRequestUtils;
import com.lq.bite.utils.HttpUtil;

public class WxPusherMessage {
	/**
	 * 发送消息
	 * @param content  文本
	 * @param summary  摘要
	 * @param contentType 内容类型 1表示文字  2表示html(只发送body标签内部的数据即可，不包括body标签) 3表示markdown 
	 * @param uids
	 * @return
	 */
	public static String setMessage(String content,String summary,Integer contentType,List<String> topics,List<String> uids)throws Exception{
		String path = WxPusherConfig.MESSAGE;
		Map<String,Object> params = new HashMap<>();
		params.put("appToken", WxPusherConfig.TOKEN);
		params.put("content", content);
		params.put("summary", summary);
		params.put("topicIds", topics);
		if(uids != null){
			params.put("uids", uids);
		}
		 JSONObject json = new JSONObject(params);
		String post = HttpUtil.doPost(path, json.toJSONString());
		//String post = HttpClientUtil.post(path, params);
		return post;
	}
}
