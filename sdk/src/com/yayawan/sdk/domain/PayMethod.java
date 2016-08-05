package com.yayawan.sdk.domain;

/**
 * 支付方式
 * @author wjy
 *
 */
public class PayMethod {

    public String payName;
   // public int iconId;
    public int mentid;
    public PayMethod(String payName,  int mentid) {
        super();
        this.payName = payName;
        //this.iconId = iconId;
        this.mentid = mentid;
    }
    @Override
    public String toString() {
        return "PayMethod [payName=" + payName + ", iconId=" 
                + ", mentid=" + mentid + "]";
    }
    
    
}
