package com.gxchain.common.ws.client;


import com.gxchain.common.ws.client.constant.WSConstants;
import com.gxchain.common.ws.client.impl.GxchainApiRestClientImpl;
import com.gxchain.common.ws.client.impl.GxchainWebSocketClientImpl;

/**
 * gxchain client 工厂类
 *
 * @author liruobin
 * @since 2018/7/3 上午10:15
 */
public class GxchainClientFactory {
    private static GxchainClientFactory clientFactory = new GxchainClientFactory();

    public static GxchainClientFactory getInstance() {
        return clientFactory;
    }
    /**
     * 创建webSocket client
     *
     * @param wsUrl
     * @return
     */
    public GxchainWebSocketClient newWebSocketClient(String wsUrl) {
        return new GxchainWebSocketClientImpl(wsUrl);
    }

    /**
     * 创建webSocket client
     *
     * @return
     */
    public GxchainWebSocketClient newWebSocketClient() {
        return new GxchainWebSocketClientImpl(WSConstants.WS_URL);
    }

    /**
     * 创建http client
     * @param url
     * @return
     */
    public GxchainApiRestClient newRestCLient(String url) {
        return new GxchainApiRestClientImpl(url);
    }
}
