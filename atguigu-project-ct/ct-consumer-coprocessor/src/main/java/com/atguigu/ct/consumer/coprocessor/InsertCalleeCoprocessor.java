package com.atguigu.ct.consumer.coprocessor;

import com.atguigu.ct.common.bean.BaseDao;
import com.atguigu.ct.common.constant.Names;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Durability;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.coprocessor.BaseRegionObserver;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.regionserver.wal.WALEdit;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 *
 * 使用协处理器 保存 被叫 用户数据  这样方便查询 增加查询效率
 *
 */
public class InsertCalleeCoprocessor extends BaseRegionObserver {


    @Override
    public void postPut(ObserverContext<RegionCoprocessorEnvironment> e, Put put, WALEdit edit, Durability durability) throws IOException {
            //获取表
        Table table = e.getEnvironment().getTable(TableName.valueOf(Names.TABLE.getValue()));

        //主叫用户的rowkey
        // 1_133_2019_144_1010_1
        String rowKeys= Bytes.toString(put.getRow());

        String[] values = rowKeys.split("_");

        CoprocessorDao coprocessorDao = new CoprocessorDao();
        String call1 = values[1];
        String call2 = values[3];
        String calltime = values[2];
        String duration = values[4];
        String flg = values[5];

        if ("1".equalsIgnoreCase(flg)){
            String calleeRowKey=coprocessorDao.getRegionNum(call2,calltime)+"_"+call2+"_"+calltime+"_"+call1+"_"+duration+"0";
            //保存数据

            Put calleePut = new Put(Bytes.toBytes(calleeRowKey));
            byte[] calleeFamily = Bytes.toBytes(Names.CF_CALLEE.getValue());
            calleePut.addColumn(calleeFamily,Bytes.toBytes("call1"),Bytes.toBytes(call2));
            calleePut.addColumn(calleeFamily, Bytes.toBytes("call2"), Bytes.toBytes(call1));
            calleePut.addColumn(calleeFamily, Bytes.toBytes("calltime"), Bytes.toBytes(calltime));
            calleePut.addColumn(calleeFamily, Bytes.toBytes("duration"), Bytes.toBytes(duration));
            calleePut.addColumn(calleeFamily, Bytes.toBytes("flg"), Bytes.toBytes("0"));
            table.put( calleePut );
            // 关闭表
            table.close();
        }




    }


    private class CoprocessorDao extends BaseDao {

        public int getRegionNum(String tel, String time) {
            return genRegionNum(tel, time);
        }
    }
}
