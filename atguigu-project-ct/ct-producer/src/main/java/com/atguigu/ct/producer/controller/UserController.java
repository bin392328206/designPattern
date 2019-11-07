package com.atguigu.ct.producer.controller;

import com.atguigu.ct.producer.bean.User;
import com.atguigu.ct.producer.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserServiceImpl service;

    @ResponseBody
    @RequestMapping("/insert")
    public int insert() {
        User model = new User();
        model.setName("adda");
        model.setAge(321);
        model.setEmail("dds");
        return service.insert(model);
    }

    @ResponseBody
    @RequestMapping("/delete")
    public int delete(int id) {
        return service.delete(id);
    }

    @ResponseBody
    @RequestMapping("/update")
    public int update() {
        User model = new User();
        return service.update(model);
    }

    @ResponseBody
    @RequestMapping("/get")
    public User get(int id) {
        return service.get(id);
    }

    @ResponseBody
    @RequestMapping("/list")
    public List<User> list() {
        List<User> list = service.list();
        return list;
    }

    @ResponseBody
    @RequestMapping("/listCount")
    public int listCount() {
        return service.listCount();
    }
}
