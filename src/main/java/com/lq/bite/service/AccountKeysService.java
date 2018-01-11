package com.lq.bite.service;

import com.lq.bite.base.BaseService;
import com.lq.bite.entity.AccountKeys;

public interface AccountKeysService extends BaseService<AccountKeys>{
	public void deleteByPublicKey(AccountKeys accountKeys);
}
