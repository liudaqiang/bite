package com.lq.bite.service;

import java.util.List;

import com.lq.bite.entity.User;

public interface UserService {
	
	List<User> getAll();  
    
    User getOne(Integer id);  
  
    void insert(User user);  
  
    void update(User user);  
  
    void delete(Integer id);  
}
