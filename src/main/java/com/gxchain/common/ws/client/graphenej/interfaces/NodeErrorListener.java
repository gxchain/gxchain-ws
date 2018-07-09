package com.gxchain.common.ws.client.graphenej.interfaces;

import com.gxchain.common.ws.client.graphenej.models.BaseResponse;

/**
 * Interface to be implemented by any listener to network errors.
 */
public interface NodeErrorListener {
    void onError(BaseResponse.Error error);
}
