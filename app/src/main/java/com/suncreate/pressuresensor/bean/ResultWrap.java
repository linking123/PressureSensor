package com.suncreate.pressuresensor.bean;

/**
 * Created by chenzhao on 16-6-6.
 */
public class ResultWrap extends Base {
    private int resultFlag;
    private String resultErrorMsg;

    public boolean OK() {
        return resultFlag == 1;
    }

    public int getResultFlag() {
        return resultFlag;
    }

    public void setResultFlag(int resultFlag) {
        this.resultFlag = resultFlag;
    }

    public String getResultErrorMsg() {
        return resultErrorMsg;
    }

    public void setResultErrorMsg(String resultErrorMsg) {
        this.resultErrorMsg = resultErrorMsg;
    }

    @Override
    public String toString() {
        return "ResultWrap{" +
                "resultFlag=" + resultFlag +
                ", resultErrorMsg='" + resultErrorMsg + '\'' +
                '}';
    }
}
