package com.gxchain.common.ws.client.impl;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.gxchain.common.ws.client.GxchainApiCallback;
import com.gxchain.common.ws.client.GxchainWebSocketClient;
import com.gxchain.common.ws.client.GxchainWebSocketListener;
import com.gxchain.common.ws.client.graphenej.RPC;
import com.gxchain.common.ws.client.graphenej.models.ApiCall;
import com.gxchain.common.ws.client.graphenej.models.BaseResponse;
import com.gxchain.common.ws.client.graphenej.models.WitnessResponse;
import com.gxchain.common.ws.client.graphenej.objects.Transaction;
import com.gxchain.common.ws.client.util.BeanUtils;
import com.gxchain.common.ws.client.util.WsGsonUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * gxchain webSocket client
 *
 * @author liruobin
 * @since 2018/7/3 上午10:14
 */
@AllArgsConstructor
@Slf4j
public class GxchainWebSocketClientImpl implements GxchainWebSocketClient {
    private String wsUrl;
    private OkHttpClient client;

    private WebSocket webSocket;

    private GxchainWebSocketListener listener;

    private boolean isConnect = false;

    private boolean isLogin = false;
    /**
     * 广播api id
     */
    private Integer broadcastApiId;

    private int seq = 6;

    Map<Integer, Response> socketMap = new HashMap();

    public GxchainWebSocketClientImpl(String wsUrl) {
        Dispatcher d = new Dispatcher();
        d.setMaxRequestsPerHost(100);
        this.client = new OkHttpClient.Builder().dispatcher(d).pingInterval(60, TimeUnit.SECONDS).build();
        this.wsUrl = wsUrl;
    }

    @Override
    public void close() {
        final int code = 1000;
        listener.onClosing(webSocket, code, null);
        webSocket.close(code, null);
        listener.onClosed(webSocket, code, null);
        client.dispatcher().executorService().shutdown();
    }

    @Override
    public void resetConnect() {
        final int code = 1000;
        isConnect = false;
        listener.onClosing(webSocket, code, null);
        webSocket.close(code, null);
        listener.onClosed(webSocket, code, null);
    }

    /**
     * 创建socket连接
     *
     * @param wsUrl
     * @param listener
     * @return
     */
    private void createNewWebSocket(String wsUrl, GxchainWebSocketListener<?> listener) {
        Request request = new Request.Builder().url(wsUrl).build();
        webSocket = client.newWebSocket(request, listener);
        this.listener = listener;
        this.isConnect = true;
    }

    /**
     * 发送
     *
     * @param apiCall
     */
    private void send(ApiCall apiCall) {
        if (webSocket == null) {
            throw new NullPointerException("webSocket is null,create webSocket first please");
        }
        webSocket.send(apiCall.toJsonString());
    }

    /**
     * 创建socket连接
     *
     * @param callback
     */
    private void connect(GxchainApiCallback<WitnessResponse<JsonElement>> callback) {
        if (isConnect) {
            return;
        }
        createNewWebSocket(wsUrl, new GxchainWebSocketListener<>(callback, new TypeToken<WitnessResponse<JsonElement>>() {
        }.getType()));
    }

    /**
     * 登录
     */
    private synchronized void login() {
        if (isLogin) {
            return;
        }
        Response response = new Response();
        socketMap.put(1, response);

        ArrayList<Serializable> loginParams = new ArrayList<>();
        loginParams.add("");//用户名 默认为空
        loginParams.add("");//密码 默认为空
        ApiCall apiCall = new ApiCall(1, RPC.CALL_LOGIN, loginParams, RPC.VERSION, 1);
        send(apiCall);
    }

    /**
     * 获取network broadcast 的 api id
     */
    private synchronized void networkBroadcast() {
        if (broadcastApiId != null) {
            return;
        }
        Response response = new Response();
        socketMap.put(3, response);

        ArrayList<Serializable> emptyParams = new ArrayList<>();
        ApiCall apiCall = new ApiCall(1, RPC.CALL_NETWORK_BROADCAST, emptyParams, RPC.VERSION, 3);
        send(apiCall);
        latchAwait(response.latch);
    }

    /**
     * 发送广播
     *
     * @param blockTransaction
     */
    private void broadcast(Transaction blockTransaction, int seq) {
        if (!isLogin) {
            login();
        }
        if (broadcastApiId == null) {
            networkBroadcast();
        }

        ArrayList<Serializable> params = new ArrayList<>();
        params.add(blockTransaction);
        ApiCall apiCall = new ApiCall(broadcastApiId, RPC.BROADCAST_TRANSACTION, params, RPC.VERSION, seq);
        send(apiCall);
    }


    @Override
    public WitnessResponse<JsonElement> broadcastTransaction(Transaction blockTransaction) {


        this.connect((WebSocket webSocket, WitnessResponse<JsonElement> witnessResponse) -> {
            log.info(WsGsonUtil.toJson(witnessResponse));

            Response response = socketMap.get((int) witnessResponse.getId());

            BeanUtils.copyProperties(witnessResponse, response.witnessResponse);
            if (witnessResponse.error != null) {
                log.error(WsGsonUtil.toJson(witnessResponse.error));
                response.latch.countDown();
                return;
            }
            if (witnessResponse.getId() == 1) {
                isLogin = true;
                response.latch.countDown();
            } else if (witnessResponse.getId() == 3) {
                broadcastApiId = witnessResponse.getResult().getAsInt();
                response.latch.countDown();
            } else if (witnessResponse.getId() >= 6) {
                log.info(witnessResponse.getId() + " broadcast success");
                response.latch.countDown();
            }
        });

        Response response = new Response();
        int seq = seqIncr();
        socketMap.put(seq, response);
        broadcast(blockTransaction, seq);

        latchAwait(response.latch);

        if (response.witnessResponse.getId() < 6) {
            response.witnessResponse.error = new BaseResponse.Error("time out, broadcast fail");
        }
        socketMap.remove(seq);
        return response.witnessResponse;
    }

    /**
     * 最多阻塞4秒
     *
     * @param latch
     */
    private void latchAwait(CountDownLatch latch) {
        try {
            latch.await(4, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.warn("阻塞超时:{}", e);
        }
    }

    private synchronized int seqIncr() {
        if (Integer.MAX_VALUE == seq) {
            seq = 7;
        } else {
            seq++;
        }
        return seq;
    }

    class Response {
        WitnessResponse<JsonElement> witnessResponse = new WitnessResponse();
        CountDownLatch latch = new CountDownLatch(1);
    }
}
