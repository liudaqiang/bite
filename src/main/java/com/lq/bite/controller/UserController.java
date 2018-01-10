package com.lq.bite.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lq.bite.entity.User;
import com.lq.bite.service.UserService;

@RestController
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping("/getUsers")  
    public List<User> getUsers() {  
        List<User> users=userService.getAll();  
        return users;  
    }  
      
    @RequestMapping("/getUser")  
    public User getUser(Integer id) {  
        User user=userService.getOne(id);  
        return user;  
    }  
      
    @RequestMapping("/add")  
    public void save(User user) {  
    	userService.insert(user);  
    }  
      
    @RequestMapping(value="update")  
    public void update(User user) {  
    	userService.update(user);  
    }  
      
    @RequestMapping(value="/delete/{id}")  
    public void delete(@PathVariable("id") Integer id) {  
    	userService.delete(id);  
    }  
}
