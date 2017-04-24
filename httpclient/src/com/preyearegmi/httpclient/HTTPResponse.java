package com.preyearegmi.httpclient;

/**
 * Created by preyea on 2/19/17.
 */
 public final class HTTPResponse {

    int responseCode;
    String responseBody;

    public HTTPResponse() {
        responseCode=-1;
        responseBody="";
    }

    public HTTPResponse(int code, String body) {
        responseCode = code;
        responseBody = body;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }
}
