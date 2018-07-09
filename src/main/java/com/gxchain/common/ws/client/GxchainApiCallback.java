package com.gxchain.common.ws.client;


import okhttp3.WebSocket;

@FunctionalInterface
public interface GxchainApiCallback<T> {

    void onResponse(WebSocket webSocket, T response);

    default void onFailure(Throwable cause) {
        cause.printStackTrace();
    }
}
