package com.preyearegmi.httpclient.abs;

import com.preyearegmi.httpclient.HTTPResponse;

/**
 * Created by preyea on 2/19/17.
 */
public interface RequestCompleteCallback {

    void onSuccess(HTTPResponse response);
    void onFailed(int responseCode);
    void onConnectionNotEstablished(String message);
}
