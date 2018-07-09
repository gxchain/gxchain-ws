package com.gxchain.common.ws.client;

import com.google.gson.reflect.TypeToken;
import com.gxchain.common.ws.client.util.WsGsonUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

import java.lang.reflect.Type;

@Slf4j
public class GxchainWebSocketListener<T> extends WebSocketListener {
    private GxchainApiCallback<T> callback;

    private Type eventClass;

    private TypeToken<T> typeToken;

    private boolean closing = false;


    public GxchainWebSocketListener(GxchainApiCallback<T> callback, Type eventClass) {
        this.callback = callback;
        this.eventClass = eventClass;
    }

    public GxchainWebSocketListener(GxchainApiCallback<T> callback) {
        this.callback = callback;
        this.typeToken = new TypeToken<T>() {
        };
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        T event;
        if (eventClass == null) {
            event = WsGsonUtil.fromJson(text, typeToken.getType());
        } else {
            event = WsGsonUtil.fromJson(text, eventClass);
        }
        callback.onResponse(webSocket, event);
    }

    @Override
    public void onClosing(final WebSocket webSocket, final int code, final String reason) {
        closing = true;
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        if (!closing) {
            callback.onFailure(t);
        }
    }
}
