package com.yayawan.sdk.domain;
/**
 * 快捷支付信息
 * @author wjy
 *
 */
public class BankInfo {

    
    public String id;
    public String bank_id;
    public String lastno;
    public String bindvalid;
    public String bankname;
    @Override
    public String toString() {
        return "BankInfo [id=" + id + ", bank_id=" + bank_id + ", lastno="
                + lastno + ", bindvalid=" + bindvalid + ", bankname="
                + bankname + "]";
    }
    
    
    
}
