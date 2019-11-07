package com.atguigu.ct.weibo.utils;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

public class HbaseUtil {


    private Configuration conf = HBaseConfiguration.create();
    //微博内容表的表名
    private static final byte[] TABLE_CONTENT =
            Bytes.toBytes("weibo:content");
    //用户关系表的表名
    private static final byte[] TABLE_RELATIONS =
            Bytes.toBytes("weibo:relations");
    //微博收件箱表的表名
    private static final byte[] TABLE_RECEIVE_CONTENT_EMAIL =
            Bytes.toBytes("weibo:receive_content_email");
    public void initNamespace() {
        HBaseAdmin admin = null;
        try {
            admin = new HBaseAdmin(conf);
//命名空间类似于关系型数据库中的 schema，可以想象成文件夹
            NamespaceDescriptor weibo = NamespaceDescriptor
                    .create("weibo")
                    .addConfiguration("creator", "Jinji")
                    .addConfiguration("create_time",
                            System.currentTimeMillis() + "")
                    .build();

            admin.createNamespace(weibo);
        } catch (MasterNotRunningException e) {
            e.printStackTrace();
        } catch (ZooKeeperConnectionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != admin) {
                try {
                    admin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
