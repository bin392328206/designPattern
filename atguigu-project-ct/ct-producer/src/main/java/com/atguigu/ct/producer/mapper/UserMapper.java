package com.atguigu.ct.producer.mapper;

import com.atguigu.ct.producer.bean.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    //自定义方法
    int listCount();

    IPage<User> selectPageVo(Page page, @Param("state") Integer state);
}


