package com.lq.bite.dao;

import java.util.List;

import com.lq.bite.entity.CleanBite;


public interface AllIcoDao {
	
	void insert(CleanBite t);  
	List<CleanBite> getAll();  
}
