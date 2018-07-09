package com.gxchain.common.ws.client;

import com.google.gson.JsonElement;
import com.gxchain.common.ws.client.graphenej.models.WitnessResponse;
import com.gxchain.common.ws.client.graphenej.objects.Asset;
import com.gxchain.common.ws.client.graphenej.objects.Transaction;
import com.gxchain.common.ws.client.graphenej.operations.BaseOperation;

import java.util.List;

/**
 * @author liruobin
 * @since 2018/7/3 上午10:13
 */
public interface GxchainWebSocketClient {

    /**
     * 发送交易广播
     *
     * @param blockTransaction
     * @return
     */
    WitnessResponse<JsonElement> broadcastTransaction(Transaction blockTransaction);

    /**
     * 重置连接
     */
    void resetConnect();

    void close();

}
