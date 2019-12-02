package com.atguigu.ct.weibo.design.pattern.creation.singleton.mei;

import lombok.Data;

public enum Response {

    SUCCESS(1, "success"),
    PARAM_MISSING(2, "param is missing"),
    INVALID_xxxx(3, "xxxx is invalid");

    private SendResponse sendResponse;

    private Response(int errCode, String errInfo) {
        this.sendResponse = new SendResponse();
        this.sendResponse.setMsgId(0);
        this.sendResponse.setErrCode(errCode);
        this.sendResponse.setErrInfo(errInfo);
    }

    public SendResponse getSendResponse() {
        return sendResponse;
    }

}
