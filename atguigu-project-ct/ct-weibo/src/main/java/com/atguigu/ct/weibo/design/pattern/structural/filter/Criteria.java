package com.atguigu.ct.weibo.design.pattern.structural.filter;

import java.util.List;

public interface Criteria {
    public List<Person> meetCriteria(List<Person> persons);
}
