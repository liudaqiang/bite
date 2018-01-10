package com.lq.bite.dao;

import java.util.List;

import com.lq.bite.entity.User;

public interface UserDao {
	List<User> getAll();  
    
    User getOne(Integer id);  
  
    void insert(User user);  
  
    void update(User user);  
  
    void delete(Integer id);  
}
