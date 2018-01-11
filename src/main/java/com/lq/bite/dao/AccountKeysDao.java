package com.lq.bite.dao;

import com.lq.bite.base.BaseDao;
import com.lq.bite.entity.AccountKeys;


public interface AccountKeysDao extends BaseDao<AccountKeys>{
	public void deleteByPublicKey(AccountKeys accountKeys);
}
