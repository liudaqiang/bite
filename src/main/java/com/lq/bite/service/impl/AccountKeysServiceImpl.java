package com.lq.bite.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lq.bite.dao.AccountKeysDao;
import com.lq.bite.entity.AccountKeys;
import com.lq.bite.service.AccountKeysService;
@Service
public class AccountKeysServiceImpl implements AccountKeysService{
	@Autowired
	private AccountKeysDao accountKeysDao;
	@Override
	public List<AccountKeys> getAll() {
		return accountKeysDao.getAll();
	}

	@Override
	public AccountKeys getOne(Integer id) {
		return accountKeysDao.getOne(id);
	}

	@Override
	public void insert(AccountKeys t) {
		accountKeysDao.insert(t);
	}

	@Override
	public void update(AccountKeys t) {
		accountKeysDao.update(t);
	}

	@Override
	public void delete(Integer id) {
		accountKeysDao.delete(id);
	}

	@Override
	public List<AccountKeys> get(AccountKeys t) {
		return accountKeysDao.get(t);
	}

	@Override
	public void deleteByPublicKey(AccountKeys accountKeys) {
		accountKeysDao.deleteByPublicKey(accountKeys);
	}
	
}
