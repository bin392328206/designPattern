package com.atguigu.ct.producer.io;

import com.atguigu.ct.common.bean.DataIn;
import com.atguigu.ct.common.bean.DataOut;

import java.io.*;

/**
 * 本地文件数据输出
 */
public class LocalFileDataOut  implements DataOut  {

    private PrintWriter writer = null;

    public void setPath(String path) {
        try {
            writer = new PrintWriter( new OutputStreamWriter( new FileOutputStream(path), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public LocalFileDataOut(String path){
        setPath(path);
    }

    public void close() throws IOException {
        if ( writer != null ) {
            writer.close();
        }
    }


    public void write(Object data) throws Exception {
        write(data.toString());
    }

    public void write(String data) throws Exception {
        writer.println(data);
        writer.flush();
    }
}
