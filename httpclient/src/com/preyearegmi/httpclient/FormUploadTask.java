package com.preyearegmi.httpclient;

import com.preyearegmi.httpclient.abs.NetworkTask;
import com.preyearegmi.httpclient.abs.RequestCompleteCallback;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.Map;

/**
 * Created by Nova on 2/25/2017.
 */
public final class FormUploadTask implements NetworkTask {
    private HttpURLConnection httpURLConnection = null;
    private URL urlObj = null;
    private Map<String, String> header = null;
    private Map<String, String> body = null;
    private RequestCompleteCallback requestCompleteCallback;
    private String[] myFiles = null;
    private String twoHyphens;
    private String boundary;
    private String crlf;
    private boolean noData;


    protected FormUploadTask(URL url, Map<String, String> head, Map<String, String> bdy, String[] files, RequestCompleteCallback listener) {
        urlObj = url;
        header = head;
        body = bdy;
        requestCompleteCallback = listener;
        myFiles = files;
        twoHyphens = "--";
        boundary = Long.toString(System.currentTimeMillis());
        crlf = "\r\n";
        noData = true;

    }

    @Override
    public void run() {
        try {
            String request = "";
            httpURLConnection = (HttpURLConnection) urlObj.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);


            //Set header if provided
            if (header != null)
                for (Map.Entry me : header.entrySet()) {
                    httpURLConnection.setRequestProperty(me.getKey().toString(), me.getValue().toString());
                }

            httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            httpURLConnection.connect();
            DataOutputStream outputStream = new DataOutputStream(httpURLConnection.getOutputStream());

            //Append files
            if (myFiles != null) {
                noData = false;
                int index = 1;
                for (String filePath : myFiles) {
                    File file=generateFileFromPath(filePath);
                    outputStream.writeBytes(crlf + twoHyphens + boundary + crlf);
                    outputStream.writeBytes("Content-Disposition: form-data; name=\"file" + (index++) + "\";" + " filename=\"" + file.getName() + "\"" + crlf);
                    outputStream.writeBytes("Content-Transfer-Encoding: binary" + crlf);

                    byte[] data = Files.readAllBytes(file.toPath());
                    outputStream.write(data, 0, data.length);
                }
            }

            //Append form input data
            if (body != null) {
                noData = false;
                for (Map.Entry me : body.entrySet()) {
                    outputStream.writeBytes(crlf + twoHyphens + boundary + crlf);
                    outputStream.writeBytes("Content-Disposition: form-data; name=\"" + me.getKey().toString() + "\"" + crlf);
                    outputStream.writeBytes("Content-Type: text/plain" + crlf);
                    outputStream.writeBytes(crlf);
                    outputStream.writeBytes(me.getValue().toString());
                }
            }

            if (noData)
                throw new IllegalArgumentException("No data to upload");
            outputStream.flush();

            StringBuilder responseString = new StringBuilder();
            HTTPResponse response = new HTTPResponse();

            int rcode = httpURLConnection.getResponseCode();

            if (rcode > 199 && rcode < 300) {
                InputStreamReader inputStream = new InputStreamReader(httpURLConnection.getInputStream());
                BufferedReader br = new BufferedReader(inputStream);
                String line = null;
                while ((line = br.readLine()) != null) {
                    responseString.append(line);
                }
                br.close();
                response.setResponseCode(rcode);
                response.setResponseBody(responseString.toString());
                httpURLConnection.disconnect();
                if (requestCompleteCallback != null)
                {
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

    private File generateFileFromPath(String filePath) {
        File file;
        try
        {
            file=new File(filePath);
        }
        catch(Exception e)
        {
            throw new IllegalArgumentException("Invalid file for given path");
        }
        return file;
    }
}
