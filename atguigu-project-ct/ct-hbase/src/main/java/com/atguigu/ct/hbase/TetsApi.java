package com.atguigu.ct.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TetsApi {
    public static Configuration conf;

    public static Connection connection;

    static {
//使用 HBaseConfiguration 的单例方法实例化
        conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "192.168.62.138");
        conf.set("hbase.zookeeper.property.clientPort", "2181");
        try {
            connection = ConnectionFactory.createConnection(conf);
        } catch (IOException e) {

        }
    }

    /**
     * 判断表是否存在
     *
     * @param tableName
     * @return
     * @throws Exception
     */

    public static boolean isTableExist(String tableName) throws Exception {
        //Connection connection =

        Admin admin = connection.getAdmin();
        //HBaseAdmin admin = (HBaseAdmin) connection.getAdmin();
        // HBaseAdmin admin = new HBaseAdmin(conf);
        return admin.tableExists(TableName.valueOf(tableName));
    }


    /**
     * 创建表
     *
     * @param tableName
     * @param columnFamily
     * @throws Exception
     */
    public static void createTable(String tableName, String...
            columnFamily) throws
            Exception {
        Admin admin = connection.getAdmin();
//判断表是否存在
        if (isTableExist(tableName)) {
            System.out.println("表" + tableName + "已存在");
//System.exit(0);
        } else {
//创建表属性对象,表名需要转字节
            HTableDescriptor descriptor = new
                    HTableDescriptor(TableName.valueOf(tableName));
//创建多个列族
            for (String cf : columnFamily) {
                descriptor.addFamily(new HColumnDescriptor(cf));
            }
//根据对表的配置，创建表
            admin.createTable(descriptor);
            System.out.println("表" + tableName + "创建成功！");
        }
    }


    /**
     * 删除表
     *
     * @param tableName
     * @throws Exception
     */
    public static void dropTable(String tableName) throws Exception {
        Admin admin = connection.getAdmin();
        if (isTableExist(tableName)) {
            admin.disableTable(TableName.valueOf(tableName));
            admin.deleteTable(TableName.valueOf(tableName));
            System.out.println("表" + tableName + "删除成功！");
        } else {
            System.out.println("表" + tableName + "不存在！");
        }
    }


    /**
     * 向表中插入数据
     *
     * @param tableName
     * @param rowKey
     * @param columnFamily
     * @param column
     * @param value
     * @throws IOException
     */
    public static void addRowData(String tableName, String rowKey,
                                  String columnFamily, String
                                          column, String value) throws IOException {
//创建 HTable 对象
        HTable hTable = new HTable(conf, tableName);
//向表中插入数据
        Put put = new Put(Bytes.toBytes(rowKey));
//向 Put 对象中组装数据
        put.add(Bytes.toBytes(columnFamily), Bytes.toBytes(column),
                Bytes.toBytes(value));
        hTable.put(put);
        hTable.close();
        System.out.println("插入数据成功");
    }

    /**
     * 删除多行数据
     *
     * @param tableName
     * @param rows
     * @throws IOException
     */
    public static void deleteMultiRow(String tableName, String... rows)
            throws IOException {
        HTable hTable = new HTable(conf, tableName);
        List<Delete> deleteList = new ArrayList<Delete>();
        for (String row : rows) {
            Delete delete = new Delete(Bytes.toBytes(row));
            deleteList.add(delete);
        }
        hTable.delete(deleteList);
        hTable.close();
    }

    /**
     * 获取所有数据
     *
     * @param tableName
     * @throws IOException
     */
    public static void getAllRows(String tableName) throws IOException {
        HTable hTable = new HTable(conf, tableName);
//得到用于扫描 region 的对象
        Scan scan = new Scan();
//使用 HTable 得到 resultcanner 实现类的对象
        ResultScanner resultScanner = hTable.getScanner(scan);
        System.out.println(resultScanner);
        for (Result result : resultScanner) {
            Cell[] cells = result.rawCells();
            for (Cell cell : cells) {
//得到 rowkey
                System.out.println(" 行 键 :" +
                        Bytes.toString(CellUtil.cloneRow(cell)));
//得到列族
                System.out.println(" 列 族 " +
                        Bytes.toString(CellUtil.cloneFamily(cell)));
                System.out.println(" 列 :" +
                        Bytes.toString(CellUtil.cloneQualifier(cell)));
                System.out.println(" 值 :" +
                        Bytes.toString(CellUtil.cloneValue(cell)));
            }
        }
    }


    /**
     * 获取一行数据
     *
     * @param tableName
     * @param rowKey
     * @throws IOException
     */
    public static void getRow(String tableName, String rowKey) throws
            IOException {
        HTable table = new HTable(conf, tableName);
        Get get = new Get(Bytes.toBytes(rowKey));
//get.setMaxVersions();显示所有版本
//get.setTimeStamp();显示指定时间戳的版本
        Result result = table.get(get);
        System.out.println(result);
        for (Cell cell : result.rawCells()) {
            System.out.println(" 行 键 :" +
                    Bytes.toString(result.getRow()));
            System.out.println(" 列 族 " +
                    Bytes.toString(CellUtil.cloneFamily(cell)));
            System.out.println(" 列 :" +
                    Bytes.toString(CellUtil.cloneQualifier(cell)));
            System.out.println(" 值 :" +
                    Bytes.toString(CellUtil.cloneValue(cell)));
            System.out.println("时间戳:" + cell.getTimestamp());
        }
    }

    /**
     * 获取某一行指定“列族:列”的数据
     *
     * @param tableName
     * @param rowKey
     * @param family
     * @param qualifier
     * @throws IOException
     */
    public static void getRowQualifier(String tableName, String rowKey,
                                       String family, String
                                               qualifier) throws IOException {
        HTable table = new HTable(conf, tableName);
        Get get = new Get(Bytes.toBytes(rowKey));
        get.addColumn(Bytes.toBytes(family),
                Bytes.toBytes(qualifier));
        Result result = table.get(get);
        System.out.println(result.rawCells().length);
        for (Cell cell : result.rawCells()) {
            System.out.println(" 行 键 :" +
                    Bytes.toString(result.getRow()));
            System.out.println(" 列 族 " +
                    Bytes.toString(CellUtil.cloneFamily(cell)));
            System.out.println(" 列 :" +
                    Bytes.toString(CellUtil.cloneQualifier(cell)));
            System.out.println(" 值 :" +
                    Bytes.toString(CellUtil.cloneValue(cell)));
        }
    }

    public static void scan(String tableName) throws  Exception{
        Table table = connection.getTable(TableName.valueOf(tableName));
        Scan scan = new Scan();
        ResultScanner scanner = table.getScanner(scan);
        for (Result result : scanner) {
            Cell[] cells = result.rawCells();
            for (Cell cell : cells) {

            }
        }
    }


    public static void deleteData(String tableName,String rowKey,String cf,String cn) throws  Exception{
        Table table = connection.getTable(TableName.valueOf(tableName));

        Delete delete = new Delete(Bytes.toBytes(rowKey));

        table.delete(delete);

    }


    public static void main(String[] args) throws Exception {
        String tableName="wangbin";
        String columnFamily="info";
        //createTable(tableName,columnFamily);
        String rowKey=  new Random().nextInt(100)+"";
        //addRowData(tableName,rowKey,columnFamily,"sex","男");
        //getAllRows(tableName);
        //getRow(tableName,"30");
        getRowQualifier(tableName,"30",columnFamily,"name");
    }
}
