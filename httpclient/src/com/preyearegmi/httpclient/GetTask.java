package com.preyearegmi.httpclient;

import com.preyearegmi.httpclient.abs.NetworkTask;
import com.preyearegmi.httpclient.abs.RequestCompleteCallback;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by preyea on 2/19/17.
 */
public final class GetTask implements NetworkTask {

    HttpURLConnection httpURLConnection = null;
    URL urlObj = null;
    Map<String, String> header = null;
    RequestCompleteCallback requestCompleteCallback;
    InputStreamReader inputStream;


    protected GetTask(URL url, Map<String, String> head, RequestCompleteCallback listener) {
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

            int rcode = httpURLConnection.getResponseCode();

            StringBuilder sb = new StringBuilder();

            HTTPResponse response = new HTTPResponse();

            response.setResponseCode(rcode);

            if (rcode > 199 && rcode < 300) {
                inputStream = new InputStreamReader(httpURLConnection.getInputStream());
                BufferedReader br = new BufferedReader(inputStream);
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();
                response.setResponseCode(rcode);
                response.setResponseBody(sb.toString());
                httpURLConnection.disconnect();
                if (requestCompleteCallback != null) {
//                    HTTPClient.getMainThreadHandler().post(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                requestCompleteCallback.onSuccess(response);
//                            }
//                            catch(IllegalStateException ex)
//                            {
//                                //Enqueue message to the handler
//                            }
//                        }
//                    });
                }
            } else {
                httpURLConnection.disconnect();
                if (requestCompleteCallback != null)
                {
//                    HTTPClient.getMainThreadHandler().post(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                requestCompleteCallback.onFailed(rcode);
//                            }
//                            catch(IllegalStateException ex)
//                            {
//                                //Enqueue message to the handler
//                            }
//                        }
//                    });
                }
            }


        } catch (IOException exception) {
            httpURLConnection.disconnect();
            if (requestCompleteCallback != null)
            {
//                HTTPClient.getMainThreadHandler().post(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            requestCompleteCallback.onConnectionNotEstablished(exception.getMessage());
//                        }
//                        catch(IllegalStateException ex)
//                        {
//                            //Enqueue message to the handler
//                        }
//                    }
//                });
            }
        }
    }
}
