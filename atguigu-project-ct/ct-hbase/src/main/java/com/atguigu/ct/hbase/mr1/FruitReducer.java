package com.atguigu.ct.hbase.mr1;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;

import javax.sound.midi.Soundbank;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FruitReducer extends TableReducer<Text, NullWritable, NullWritable> {

    @Override
    protected void reduce(Text key, Iterable<NullWritable> valuess, Context context) throws IOException, InterruptedException {
        ////从 HDFS 中读取的数据
        String lineValue = key.toString();
//读取出来的每行数据使用\t 进行分割，存于 String 数组
        String[] values = lineValue.split(" ");
//根据数据中值的含义取值
        String rowKey = values[0];
        String name = values[1];
        String color = values[2];
//初始化 rowKey
//初始化 put 对象
        Put put = new Put(Bytes.toBytes(rowKey));
//参数分别:列族、列、值
        put.add(Bytes.toBytes("info"), Bytes.toBytes("name"),
                Bytes.toBytes(name));
        put.add(Bytes.toBytes("info"), Bytes.toBytes("color"),
                Bytes.toBytes(color));
        context.write(NullWritable.get(), put);
    }

    public static void main(String[] args) {
        List<String> list=new ArrayList<>();
        list.add("a");
        list.add("a");
        list.add("b");
        list.add("b");
        list.add("c");
        list.add("d");
        for (String s : list) {
            System.out.println(s);
        }
        Map<String, List<String>>  map= list.stream().collect(Collectors.groupingBy(String::toString));
    }
}
