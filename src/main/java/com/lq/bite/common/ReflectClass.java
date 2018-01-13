package com.lq.bite.common;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.lq.bite.entity.CleanBite;
/**
 * 将各网站数据解析成相同格式
 * @author l.q
 *
 */
public class ReflectClass {
	/**
	 * 解析币蛋返回
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public List<CleanBite> reflectCoinEgg(Object obj)throws Exception {
		List<CleanBite> cecbList = new ArrayList<>();
		List<String> ceoNameList = new ArrayList<>();
		if (obj == null)
			return null;
		//查询出全部字段
		Field[] fields = obj.getClass().getDeclaredFields();
		for (int j = 0; j < fields.length; j++) {
			CleanBite cecb = new CleanBite();
			fields[j].setAccessible(true);
			// 字段名
			if("uid".equals(fields[j].getName())){
				continue;
			}
			String [] fieldNameAndType = fields[j].getName().split("_");
			//这里获取是不是之前就有的
			if(ceoNameList.contains(fieldNameAndType[0])){
				int index = ceoNameList.indexOf(fieldNameAndType[0]);
				if(index != -1){
					cecb = cecbList.get(index);
				}
			}
			
			cecb.setBiteName(fieldNameAndType[0]);
			if(fieldNameAndType[1].equals("balance")){
				cecb.setBlance(fields[j].get(obj).toString());
			}else if(fieldNameAndType[1].equals("lock")){
				cecb.setLock(fields[j].get(obj).toString());
			}
			if(!ceoNameList.contains(fieldNameAndType[0]) || ceoNameList.size() == 0){
				cecbList.add(cecb);
				ceoNameList.add(fieldNameAndType[0]);
			}
			
		}
		return cecbList;
	}
}
