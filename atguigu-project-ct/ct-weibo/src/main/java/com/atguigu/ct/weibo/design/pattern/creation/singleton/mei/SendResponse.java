package com.atguigu.ct.weibo.design.pattern.creation.singleton.mei;

import lombok.Data;

@Data
public class SendResponse {

    // 错误码
    private int errCode;

    // 错误信息
    private String errInfo;

    // messageId
    private long msgId;
}
