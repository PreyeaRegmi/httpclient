package com.preyearegmi.httpclient;

import com.preyearegmi.httpclient.abs.NetworkTask;
import com.preyearegmi.httpclient.abs.RequestCompleteCallback;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by preyea on 2/19/17.
 */
public final class PostTask implements NetworkTask {

    HttpURLConnection httpURLConnection = null;
    URL urlObj = null;
    Map<String, String> header = null;
    String body = null;
    OutputStreamWriter outputStream;
    InputStreamReader inputStream;
    RequestCompleteCallback requestCompleteCallback;


    protected PostTask(URL url, Map<String, String> head, String bdy, RequestCompleteCallback listener) {
        urlObj = url;
        header = head;
        body = bdy;
        requestCompleteCallback = listener;
    }

    @Override
    public void run() {
        try {
            httpURLConnection = (HttpURLConnection) urlObj.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            //Set header if provided
            if (header != null)
                for (Map.Entry me : header.entrySet()) {
                    System.out.print(me.getKey() + ": ");
                    System.out.println(me.getValue());
                    httpURLConnection.setRequestProperty(me.getKey().toString(), me.getValue().toString());
                }

            httpURLConnection.connect();

            //Send body if provided
            if (body == null)
                body = "";
            outputStream = new OutputStreamWriter(httpURLConnection.getOutputStream());
            outputStream.write(body);
            outputStream.flush();


            StringBuilder sb = new StringBuilder();

            HTTPResponse response = new HTTPResponse();

            int rcode = httpURLConnection.getResponseCode();

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
