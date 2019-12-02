import com.atguigu.ct.producer.Application;
import com.atguigu.ct.producer.bean.User;
import com.atguigu.ct.producer.controller.UserController;
import com.atguigu.ct.producer.mapper.UserMapper;
import com.atguigu.ct.producer.service.UserService;
import com.atguigu.ct.producer.service.UserServiceImpl;
import com.atguigu.ct.producer.utils.RedissLockUtil;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.org.apache.bcel.internal.generic.NEW;
import jodd.util.StringUtil;
import org.apache.hadoop.security.SaslOutputStream;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mortbay.util.ajax.JSON;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Wrapper;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class SampleTest {
    @Autowired
        private UserMapper userMapper;
    @Autowired
    private UserService userService;
    @Autowired
  private RedisTemplate redisTemplate;

    @Autowired
    private UserController userController;
    @Test
    public void testSelect() {
        System.out.println(("----- selectAll method test ------"));
        List<User> userList = userMapper.selectList(null);
        userList.forEach(System.out::println);
    }

    @Test
    public void saveStudent(){
        User user = new User();
        user.setAge(12);
        user.setName("adads");
        user.setEmail("dasd44");
        boolean save = userService.save(user);
        Assert.assertTrue(save);
    }
        @Test
        public void del111(){
            Map<String,Object> map=new HashMap<>();
            map.put("age",12);
            int i = userMapper.deleteByMap(map);
            System.out.println(i);

        }
    @Test
    public void del12(){
       testCountDownLatch();
        System.out.println(1);
    }

    @Test
    public void insert1(){
        int i = new Random().nextInt(10);
        for (int a=0;a<10;a++){
            User user = new User();
            user.setEmail("dadsa"+i);
            user.setName("wanhgn"+i);
            user.setAge(i);
            int insert = userMapper.insert(user);
            System.out.println(user.getId());
        }
    }

    /**
     * 测试1000并发
     *
     */

    @Test
    public void  test1000(){
        int i = new Random().nextInt(10);

        parallelTesk(5000, new Runnable() {
            @Override
            public void run() {
                User user = new User();
                user.setEmail("dadsa"+i);
                user.setName("wanhgn"+i);
                user.setAge(i);
                int insert = userMapper.insert(user);
            }
        });
    }

    /**
     * 高并发测试：
     * 创建threadNum个线程；
     * 执行任务task
     * @param threadNum 线程数量
     * @param task 任务
     */
    public static void parallelTesk(int threadNum, Runnable task){

        // 1. 定义闭锁来拦截线程
        final CountDownLatch startGate = new CountDownLatch(1);
        final CountDownLatch endGate  = new CountDownLatch(threadNum);

        // 2. 创建指定数量的线程
        for (int i = 0; i <threadNum; i++) {
            Thread t = new Thread(() -> {
                try {
                    startGate.await();
                    try {
                        task.run();
                    } finally {
                        endGate.countDown();
                    }
                } catch (InterruptedException e) {

                }
            });

            t.start();
        }

        // 3. 线程统一放行，并记录时间！
        long start =  System.nanoTime();

        startGate.countDown();
        try {
            endGate.await();
        } catch (InterruptedException e) {
        }

        long end = System.nanoTime();
        System.out.println("cost times :" +(end - start));
    }





@Test
    public void test12(){
    QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
    Map<String,Object> map=new HashMap<>();
    List<Long> list=new ArrayList<>();
    list.add(40L);
    list.add(41L);
;    map.put("id",list);
    QueryWrapper<User> userQueryWrapper1 = userQueryWrapper.allEq(map);

    int delete = userMapper.delete(userQueryWrapper1);
    System.out.println(delete);
}

    @Test
    public void test13(){
        AbstractWrapper  a =new QueryWrapper<>();
        a.ge("age",20);
        List list = userMapper.selectList(a);
        System.out.println(list.size());
    }

    @Test
    public void  test15(){
//        User byId = this.userService.getById(60);
////        byId.setAge(66);
////        boolean b = this.userService.updateById(byId);
//
//
//        UpdateWrapper<User> wrapper = new UpdateWrapper<>();
//        wrapper.eq("id",byId.getId());
//        wrapper.set("age",6218);
//        boolean update = this.userService.update(byId, wrapper);
//        System.out.println(update);
        IPage<User> userPage = new Page<>(1, 2);
        QueryWrapper<User> all=new QueryWrapper();
        all.eq("age",67);

        IPage<User> iPage = userMapper.selectPage(userPage,  null);
        System.out.println(iPage.getTotal());
    }

    @Test
    public void  test1111(){
        Page<User> userPage = new Page<>(1, 1);
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        User user = new User();
        user.setAge(67);
        queryWrapper.setEntity(user);
        IPage<User> iPage = userMapper.selectPage(userPage, queryWrapper);
        System.out.println(iPage.getRecords().toString());
        System.out.println(iPage.getTotal());
    }


    /**
     * 可重入的分布式锁  created By 六脉神剑
     * @return
     */
    @Test
    public void testRedis(){
        String key="六脉神剑";
        // 通过key获取value
        String value = (String) redisTemplate.opsForValue().get(key);
        if (StringUtil.isEmpty(value)) {
            String lockKey="一剑断落水";
            try {
                boolean locked = RedissLockUtil.tryLock(lockKey, TimeUnit.SECONDS, 5, 10);
                if (locked) {
                    System.out.println("我是获得锁的线程，求叫我大佬");
                    User byId = userService.getById(1);
                    System.out.println("睡一下,然后把从数据库查到的数据放到redis中,生产环境中没必要睡，我这个是测试");
                    Thread.sleep(500);
                    redisTemplate.opsForValue().set(key,"byId");
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


    @Test
    public void TestLock(){

        for (int i=0;i<=10;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("我是线程"+Thread.currentThread().getName());
                    userController.testRedis();
                }
            }).start();
        }
        System.out.println("aaaa");
    }


    @Test
    public void testCountDownLatch() {

        int threadCount = 100;

        final CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {

            new Thread(new Runnable() {

                @Override
                public void run() {

                    System.out.println("线程" + Thread.currentThread().getId() + "开始出发");

                    try {
                        userController.testRedis();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    System.out.println("线程" + Thread.currentThread().getId() + "已到达终点");

                    latch.countDown();
                }
            }).start();
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("10个线程已经执行完毕！开始计算排名");
    }


    @Test
    public void TestBitMap(){

        List<String> goodsId = new ArrayList<String>();
        goodsId.add("201912010001");
        goodsId.add("20191201002");
        goodsId.add("20191201003");
        goodsId.add("20191201004");

        long index = Math.abs((long) (goodsId.get(3).hashCode()));
        System.out.println(index);
        redisTemplate.opsForValue().setBit("goodsIdBit",index,true);
        Boolean aBoolean = redisTemplate.opsForValue().getBit("goodsIdBit", index);
        System.out.println(aBoolean);


    }


}
