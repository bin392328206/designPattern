package com.atguigu.ct.producer.controller;

import com.atguigu.ct.producer.bean.User;
import com.atguigu.ct.producer.service.UserServiceImpl;
import com.atguigu.ct.producer.utils.RedissLockUtil;
import jodd.util.StringUtil;
import jodd.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private RedisTemplate redisTemplate;

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


    public void testRedis() {
        List<String> goodsId = new ArrayList<String>();
        goodsId.add("201912010001");
        goodsId.add("20191201002");
        goodsId.add("20191201003");
        goodsId.add("20191201004");

        String key = "六脉神剑";
        // 通过key获取value
        long index = Math.abs((long) (goodsId.get(3).hashCode()));
        Boolean goodsIdBit = redisTemplate.opsForValue().getBit("goodsIdBit",index);
        if (!goodsIdBit) {
            return;
        }
        String value = (String) redisTemplate.opsForValue().get(key);
        if (StringUtil.isEmpty(value)) {
            String lockKey = "一剑断落水";
            try {
                /**
                 * 尝试获取锁
                 * @param lockKey
                 * @param unit 时间单位
                 * @param waitTime 最多等待时间
                 * @param leaseTime 上锁后自动释放锁时间
                 * @return
                 */
                boolean locked = RedissLockUtil.tryLock(lockKey, TimeUnit.SECONDS, 5, 10);
                if (locked) {
                    System.out.println("我是获得锁的线程，求叫我大佬" + Thread.currentThread().getName());
                    User byId = service.getById(1);
                    System.out.println("睡一下,然后把从数据库查到的数据放到redis中,生产环境中没必要睡，我这个是测试");
                    Thread.sleep(5000);
                    redisTemplate.opsForValue().set(key, "byId", 30, TimeUnit.SECONDS);
                    //设置bitMap
                    redisTemplate.opsForValue().setBit("goodsIdBit", index, true);
                    redisTemplate.delete(lockKey);

                } else {
                    // 其它线程进来了没获取到锁便等待50ms后重试
                    System.out.println("我们是没有得到锁的线程，等待一下再试试");
                    Thread.sleep(50);
                    testRedis();
                }
            } catch (Exception e) {

            } finally {
                redisTemplate.delete(lockKey);
            }
        }

    }
}
