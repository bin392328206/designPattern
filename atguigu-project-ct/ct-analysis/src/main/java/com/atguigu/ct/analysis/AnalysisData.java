package com.atguigu.ct.analysis;

import com.atguigu.ct.analysis.tool.AnalysisTextTool;
import org.apache.hadoop.util.ToolRunner;

public class AnalysisData {

    public static void main(String[] args) throws Exception {

        int run = ToolRunner.run(new AnalysisTextTool(), args);


    }
}
