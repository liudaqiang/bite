package com.lq.bite;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.lq.bite.dao.AllIcoDao;
import com.lq.bite.entity.CleanBite;
import com.lq.bite.utils.RedisAPI;

@Component
public class MyApplicationRunner implements ApplicationRunner {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private AllIcoDao allIcoDao;
	@Autowired
	private CustomPropertiesConfig customPropertiesConfig;

	@Override
	public void run(ApplicationArguments arg0) throws Exception {
		logger.warn("-------------项目启动-------------------");
		Object objAllIco = RedisAPI.getObj("allIco");
		logger.warn(objAllIco + "");
		logger.warn("-------------执行所有ico存入缓存操作-------------------");
		if (objAllIco == null) {
			List<CleanBite> allIco = allIcoDao.getAll();
			logger.warn(allIco.size() + "");
			RedisAPI.setObj("allIco", allIco, 86400);
		}
		RedisAPI.setStr("publicKey", customPropertiesConfig.getPublicKey(), 86400);
		RedisAPI.setStr("privateKey", customPropertiesConfig.getPrivateKey(), 86400);
	}
}