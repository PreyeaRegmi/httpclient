package com.preyearegmi.httpclient;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.preyearegmi.httpclient.abs.FileDownloadCallback;
import com.preyearegmi.httpclient.abs.NetworkTask;
import com.preyearegmi.httpclient.abs.RequestCompleteCallback;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * Created by preyea on 2/16/17.
 */
public final class HTTPClient {

    private static Handler handler = null;

    static Handler getMainThreadHandler() {
        if (handler == null)
            handler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                }
            };
        return handler;
    }

    public static synchronized NetworkTask request(String url, METHODTYPE methodType, Map<String, String> header,
                                                   String body, RequestCompleteCallback listener) {
        URL urlObj;
        try {
            urlObj = new URL(url);
        } catch (MalformedURLException ex) {
            throw new IllegalArgumentException("Invalid Url: " + url);
        }

        switch (methodType) {
            case POST:
                return new PostTask(urlObj, header, body, listener);
            case GET:
                return new GetTask(urlObj, header, listener);
            default:
                throw new IllegalArgumentException("Undefined http method");
        }
    }

    public static synchronized NetworkTask getFile(String url, Map<String, String> header, FileDownloadCallback callback) {
        URL urlObj;
        try {
            urlObj = new URL(url);
        } catch (MalformedURLException ex) {
            throw new IllegalArgumentException("Invalid Url: " + url);
        }

        return new GetFileTask(urlObj, header, callback);

    }

    public static synchronized NetworkTask formUpload(String url, Map<String, String> header, Map<String, String> body, String[] filePaths, RequestCompleteCallback callback) {
        URL urlObj;
        try {
            urlObj = new URL(url);
        } catch (MalformedURLException ex) {
            throw new IllegalArgumentException("Invalid Url: " + url);
        }
        return new FormUploadTask(urlObj, header, body, filePaths, callback);
    }

    public static void enqueue(NetworkTask command) {
        if (command instanceof GetTask || command instanceof PostTask || command instanceof GetFileTask || command instanceof FormUploadTask)
            TaskExecutor.getTaskExecutor().execute(command);
        else
            throw new IllegalArgumentException("External task is forbidden, please use task provided by HTTPClient.request method");
    }
}
