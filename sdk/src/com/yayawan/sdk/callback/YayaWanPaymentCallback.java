package com.yayawan.sdk.callback;

import java.io.Serializable;

import com.yayawan.sdk.domain.Order;
import com.yayawan.sdk.domain.User;

/**
 * 支付回调
 * 
 * @author wjy
 * 
 */
public interface YayaWanPaymentCallback extends Serializable{
    /**
     * 支付成功后回调
     * 
     * @param paramUser
     *            支付的用户信息
     * @param paramOrder
     *            支付的订单信息
     * @param paramInt
     */
    public abstract void onSuccess(User paramUser, Order paramOrder,
            int paramInt);

    /**
     * 支付失败时的回调
     * @param paramInt
     */
    public abstract void onError(int paramInt);
    /**
     * 退出支付时的回调
     */
    public abstract void onCancel();
}
