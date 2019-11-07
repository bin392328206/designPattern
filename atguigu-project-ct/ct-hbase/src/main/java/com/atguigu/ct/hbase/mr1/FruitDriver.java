package com.atguigu.ct.hbase.mr1;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import sun.management.Agent;

public class FruitDriver implements Tool {

    private static final String ZK_CONNECT_KEY = "hbase.zookeeper.quorum";
    private static final String ZK_CONNECT_VALUE = "hadoop1:2181,hadoop2:2181,hadoop3:2181";

    private  Configuration configuration=null;
    @Override
    public void setConf(Configuration conf) {
        configuration=conf;
    }

    @Override
    public Configuration getConf() {
        return configuration;
    }

    @Override
    public int run(String[] args) throws Exception {

        Configuration conf = new Configuration();
        conf.set(ZK_CONNECT_KEY, ZK_CONNECT_VALUE);
        conf.set("fs.defaultFS", "hdfs://192.168.62.138:9000");
        conf.addResource("config/core-site.xml");
        conf.addResource("config/hdfs-site.xml");
        System.setProperty("HADOOP_USER_NAME", "hadoop1");
        //获取job对象
        Job instance = Job.getInstance(configuration);
        //设置驱动
        instance.setJarByClass(FruitDriver.class);

        //设置输出kv类型
        instance.setMapperClass(FruitMapper.class);
        instance.setMapOutputKeyClass(Text.class);
        instance.setMapOutputValueClass(NullWritable.class);

        //设置reducer类
        TableMapReduceUtil.initTableReducerJob("fruit",FruitReducer.class,instance);
        Path inPath = new
                Path("hdfs://192.168.62.138:9000/input_fruit/fruit.tsv");
        //设置输入输出类
        //设置 Reduce 数量，最少 1 个
        instance.setNumReduceTasks(1);
        FileInputFormat.setInputPaths(instance,inPath);
        //提交任务
        boolean b = instance.waitForCompletion(true);
        return b ?0:1;
    }

    public static void main(String[] args) throws Exception {
        try {
            Configuration entries = new Configuration();
            int run = ToolRunner.run(entries, new FruitDriver(), args);
            System.exit(run);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
