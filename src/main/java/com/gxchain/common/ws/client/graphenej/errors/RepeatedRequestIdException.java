package com.gxchain.common.ws.client.graphenej.errors;

/**
 * Created by nelson on 6/27/17.
 */

public class RepeatedRequestIdException extends Exception {

    public RepeatedRequestIdException(String message){
        super(message);
    }
}
