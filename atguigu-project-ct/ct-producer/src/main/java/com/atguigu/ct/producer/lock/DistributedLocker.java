package com.atguigu.ct.producer.lock;

import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

public interface DistributedLocker {
    /**
     * 锁住不设置超时时间
     * @param lockKey
     * @return
     */
    RLock lock(String lockKey);

    /**
     * 锁住设置超时时间
     * @param lockKey
     * @param timeout
     * @return
     */
    RLock lock(String lockKey,int timeout);

    /**
     *
     * @param lockKey
     * @param unit 时间类型
     * @param timeout
     * @return
     */
    RLock lock(String lockKey, TimeUnit unit, int timeout);

    /**
     * 尝试获取锁
     * @param lockKey
     * @param unit
     * @param waitTime
     * @param leaseTime
     * @return
     */
    boolean tryLock(String lockKey, TimeUnit unit, int waitTime, int leaseTime);

    /**
     * 通过lockKey解锁
     * @param lockKey
     */
    void unlock(String lockKey);

    /**
     * 直接通过锁解锁
     * @param lock
     */
    void unlock(RLock lock);
}
