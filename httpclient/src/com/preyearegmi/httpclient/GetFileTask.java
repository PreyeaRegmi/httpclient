package com.preyearegmi.httpclient;

import com.preyearegmi.httpclient.abs.FileDownloadCallback;
import com.preyearegmi.httpclient.abs.NetworkTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by Nova on 2/19/2017.
 */
 class GetFileTask implements NetworkTask {

    HttpURLConnection httpURLConnection = null;
    URL urlObj = null;
    Map<String, String> header = null;
    FileDownloadCallback requestCompleteCallback;
    InputStream inputStream;


    protected GetFileTask(URL url, Map<String, String> head, FileDownloadCallback listener) {
        urlObj = url;
        header = head;
        requestCompleteCallback = listener;
    }


    @Override
    public void run() {
        try {
            httpURLConnection = (HttpURLConnection) urlObj.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setDoOutput(true);

            //Set header if provided
            if (header != null)
                for (Map.Entry me : header.entrySet()) {
                    System.out.print(me.getKey() + ": ");
                    System.out.println(me.getValue());
                    httpURLConnection.setRequestProperty(me.getKey().toString(), me.getValue().toString());
                }

            httpURLConnection.connect();

            final int rcode = httpURLConnection.getResponseCode();

            if (rcode > 199 && rcode < 300) {

                inputStream = httpURLConnection.getInputStream();
                final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

                byte[] bArray = new byte[10240];
                int n=-1;


                while ((n = inputStream.read(bArray)) > 0) {
                    byteStream.write(bArray, 0, n);
                }

                httpURLConnection.disconnect();
                if (requestCompleteCallback != null)
                {
                    HTTPClient.getMainThreadHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                               requestCompleteCallback.onSuccess(byteStream.toByteArray());
                            }
                            catch(IllegalStateException ex)
                            {
                                //Enqueue message to the handler
                            }
                        }
                    });
                }

            } else {
                httpURLConnection.disconnect();
                if (requestCompleteCallback != null)
                {
                    HTTPClient.getMainThreadHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                requestCompleteCallback.onFailed(rcode);
                            }
                            catch(IllegalStateException ex)
                            {
                                //Enqueue message to the handler
                            }
                        }
                    });
                }
            }


        } catch (final IOException exception) {
            httpURLConnection.disconnect();
            if (requestCompleteCallback != null)
            {
                HTTPClient.getMainThreadHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            requestCompleteCallback.onConnectionNotEstablished(exception.getMessage());
                        }
                        catch(IllegalStateException ex)
                        {
                            //Enqueue message to the handler
                        }
                    }
                });
            }
        }
    }

}

