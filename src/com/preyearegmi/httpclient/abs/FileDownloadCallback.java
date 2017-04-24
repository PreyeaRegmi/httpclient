package com.preyearegmi.httpclient.abs;

/**
 * Created by Nova on 2/19/2017.
 */
public interface FileDownloadCallback {

    void onSuccess(byte[] response);
    void onFailed(int responseCode);
    void onConnectionNotEstablished(String message);
}
