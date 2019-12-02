package com.atguigu.ct.analysis.io;

import com.atguigu.ct.analysis.kv.AnalysisKey;
import com.atguigu.ct.analysis.kv.AnalysisValue;
import com.atguigu.ct.common.util.JDBCUtil;
import org.apache.hadoop.fs.Path;

import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.output.FileOutputCommitter;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class MySQLBeanOutputFormat extends OutputFormat<AnalysisKey, AnalysisValue> {



    protected static class  MySQLRecordWriter extends RecordWriter<AnalysisKey, AnalysisValue>{

        private Connection connection = null;
        Map<String, Integer> userMap = new HashMap<String, Integer>();
        Map<String, Integer> dateMap = new HashMap<String, Integer>();


        public MySQLRecordWriter() {
            // 获取资源
            connection = JDBCUtil.getConnection();
            PreparedStatement pstat = null;
            ResultSet rs = null;

            try {

                String queryUserSql = "select id, tel from ct_user";
                pstat = connection.prepareStatement(queryUserSql);
                rs = pstat.executeQuery();
                while ( rs.next() ) {
                    Integer id = rs.getInt(1);
                    String tel = rs.getString(2);
                    userMap.put(tel, id);
                }

                rs.close();

                String queryDateSql = "select id, year, month, day from ct_date";
                pstat = connection.prepareStatement(queryDateSql);
                rs = pstat.executeQuery();
                while ( rs.next() ) {
                    Integer id = rs.getInt(1);
                    String year = rs.getString(2);
                    String month = rs.getString(3);
                    if ( month.length() == 1 ) {
                        month = "0" + month;
                    }
                    String day = rs.getString(4);
                    if ( day.length() == 1 ) {
                        day = "0" + day;
                    }
                    dateMap.put(year + month + day, id);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if ( rs != null ) {
                    try {
                        rs.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if ( pstat != null ) {
                    try {
                        pstat.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        public void write(AnalysisKey analysisKey, AnalysisValue analysisValue) throws IOException, InterruptedException {
            PreparedStatement pstat = null;
            try {
                String insertSQL = "insert into ct_call ( telid, dateid, sumcall, sumduration ) values ( ?, ?, ?, ? )";
                pstat = connection.prepareStatement(insertSQL);

                pstat.setInt(1, userMap.get(analysisKey.getTel()));
                pstat.setInt(2, dateMap.get(analysisKey.getDate()));
                pstat.setInt(3, Integer.parseInt(analysisValue.getSumCall()) );
                pstat.setInt(4, Integer.parseInt(analysisValue.getSumDuration()));
                pstat.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if ( pstat != null ) {
                    try {
                        pstat.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        public void close(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
            if ( connection != null ) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    @Override
    public RecordWriter<AnalysisKey, AnalysisValue> getRecordWriter(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        return new MySQLRecordWriter();
    }

    @Override
    public void checkOutputSpecs(JobContext jobContext) throws IOException, InterruptedException {

    }

    public static  Path getOutputPath(JobContext jobContext){
        String name=jobContext.getConfiguration().get(FileOutputFormat.OUTDIR);
        return name==null? null: new Path(name);
    }

    @Override
    public OutputCommitter getOutputCommitter(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        if (taskAttemptContext==null){
            Path output=getOutputPath(taskAttemptContext);
            OutputCommitter fileOutputCommitter = new FileOutputCommitter(output, taskAttemptContext);
            return fileOutputCommitter;
        }
        return null;


    }
}
