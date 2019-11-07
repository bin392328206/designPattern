package com.atguigu.ct.weibo.dao;

import com.atguigu.ct.weibo.contanis.Constants;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.ipc.Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HbaseDao {

    //发布微博

    public static void  publishWeiBo(String uid,String content) throws IOException{
        //获取Connection对象
        Connection connection = ConnectionFactory.createConnection(Constants.CONFIFIGURATION);
        //第一部分 ：操作微博内容表

        //1.获取微博内容表对象

        Table contentTable= connection.getTable(TableName.valueOf(Constants.CONTENT_TABLE));

        //获取当前时间戳
        long ts = System.currentTimeMillis();

        //获取rowkey
        String rowKey=uid+ts;

        //创建put对象
        Put contentPut=new Put(Bytes.toBytes(rowKey));
        //给put赋值

        contentPut.addColumn(Bytes.toBytes(Constants.CONTENT_TABLE_CF),Bytes.toBytes("content"),Bytes.toBytes(content));
        //执行拆入操作
        contentTable.put(contentPut);

        //第二部 操作微博收件箱表

        // 获取用户关系表对象
        Table reaTable  = connection.getTable(TableName.valueOf(Constants.RELATION_TABLE));


            List<Put> list=new ArrayList<>();
         Get get = new Get(Bytes.toBytes(uid));
        Result result = reaTable.get(get);

        Scan scan = new Scan();
        Cell[] cells = result.rawCells();
        for (Cell cell : cells) {
            //构建微博收件箱的对象
        Put inBoxPut=new Put(CellUtil.cloneQualifier(cell));

        inBoxPut.addColumn(Bytes.toBytes(Constants.INBOX_TABLE_CF),Bytes.toBytes(uid),Bytes.toBytes(rowKey));
        list.add(inBoxPut);

        }

        //批量新增到Hbase的收件箱表


        //获取当前发微博用户的粉丝 列祖数据

    }


}
