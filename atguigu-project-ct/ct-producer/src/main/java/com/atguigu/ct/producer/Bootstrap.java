package com.atguigu.ct.producer;

import com.atguigu.ct.common.bean.Producer;
import com.atguigu.ct.producer.bean.LocalFileProducer;
import com.atguigu.ct.producer.io.LocalFileDataIn;
import com.atguigu.ct.producer.io.LocalFileDataOut;

/**
 * 启动对象
 */
public class Bootstrap {
    public static void main(String[] args) throws Exception {

        if ( args.length < 2 ) {
            System.out.println("系统参数不正确，请按照指定格式传递：java -jar Produce.jar path1 path2 ");
            System.exit(1);
        }

        // 构建生产者对象
        Producer producer = new LocalFileProducer();

//        producer.setIn(new LocalFileDataIn("E:\\资料\\java\\学习资料\\大数据\\尚硅谷\\尚硅谷大数据技术之电信客服综合案例\\2.资料\\辅助文档\\contact.log"));
//        // 生产数据
//        producer.setOut(new LocalFileDataOut("E:\\资料\\java\\学习资料\\大数据\\尚硅谷\\尚硅谷大数据技术之电信客服综合案例\\2.资料\\辅助文档\\call.log"));
        // 关闭生产者对象
        producer.setIn(new LocalFileDataIn(args[0]));
        producer.setOut(new LocalFileDataOut(args[1]));
        producer.produce();
        producer.close();

    }
}
