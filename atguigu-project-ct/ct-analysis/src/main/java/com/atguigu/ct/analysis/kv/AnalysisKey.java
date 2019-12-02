package com.atguigu.ct.analysis.kv;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class AnalysisKey implements WritableComparable<AnalysisKey> {

    public AnalysisKey() {

    }

    public AnalysisKey( String tel, String date ) {
        this.tel = tel;
        this.date = date;
    }

    private String tel;
    private String date;
    private String tel2;

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(tel);
        out.writeUTF(date);

    }

    @Override
    public void readFields(DataInput in) throws IOException {
        tel = in.readUTF();
        date = in.readUTF();

    }


    @Override
    public int compareTo(AnalysisKey o) {
        int i = tel.compareTo(o.getTel());
        if ( i == 0 ) {
            i = date.compareTo(o.getDate());
        }
        return 0;
    }
}
