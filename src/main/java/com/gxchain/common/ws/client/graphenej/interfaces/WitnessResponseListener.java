package com.gxchain.common.ws.client.graphenej.interfaces;

import com.gxchain.common.ws.client.graphenej.models.BaseResponse;
import com.gxchain.common.ws.client.graphenej.models.WitnessResponse;

/**
 * Class used to represent any listener to network requests.
 */
public interface WitnessResponseListener {

    void onSuccess(WitnessResponse response);

    void onError(BaseResponse.Error error);
}
