package com.atguigu.ct.producer.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.atguigu.ct.producer.bean.User;
import com.atguigu.ct.producer.mapper.UserMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService  {

    @Autowired
    UserMapper mapper;

    public int insert(User model) {
        return mapper.insert(model);
    }
    public int delete(int id) {
        return mapper.deleteById(id);
    }
    public int update(User model) {
        //更新全部字段，但可以跟application.yml中的field-strategy字段策略相配合，不更新为null或为空的字段
        return mapper.updateById(model);
        //更新全部字段，且不可为NULL
        //return mapper.updateAllColumnById(model);
    }
    public User get(int id) {
        return mapper.selectById(id);
    }
    public List<User> list() {
        QueryWrapper ew = new QueryWrapper();
        ew.eq("name","wangbin");
        return mapper.selectList(ew);
    }
    //调用自定义方法
    public int listCount(){
        return mapper.listCount();
    }
}
