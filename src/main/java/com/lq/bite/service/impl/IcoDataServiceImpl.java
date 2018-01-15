package com.lq.bite.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lq.bite.dao.IcoDataDao;
import com.lq.bite.entity.IcoData;
import com.lq.bite.service.IcoDataService;
@Service
public class IcoDataServiceImpl implements IcoDataService{
	@Autowired
	private IcoDataDao icoDataDao;

	@Override
	public List<IcoData> getAll() {
		return icoDataDao.getAll();
	}

	@Override
	public IcoData getOne(Integer id) {
		return icoDataDao.getOne(id);
	}

	@Override
	public void insert(IcoData t) {
		icoDataDao.insert(t);
	}

	@Override
	public void update(IcoData t) {
		icoDataDao.update(t);
	}

	@Override
	public void delete(Integer id) {
		icoDataDao.delete(id);
	}

	@Override
	public List<IcoData> get(IcoData t) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
