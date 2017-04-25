<h1 id="a-lightweight-http-client-for-android">A lightweight HTTP Client for android.</h1>

<p>A simple library to handle your common http request in simplest manner.</p>

<h3 id="what-it-is-all-about">What it is all about?</h3>

<p>Network requests can be messy at times in android. So it helps to abstract the network details and promotes clean code.</p>

<h3 id="why-should-i-use-it-when-there-are-so-many-other-http-clients">Why should I use it when there are so many other http clients?</h3>

<p>Yes there are some great http clients with outstanding performance and features. But sometimes when the size of our apk really matters, when we don’t want any additional features and ship any transitive dependency with our app, then this might do the job.</p>

<h3 id="details">Details</h3>

<p>This library is built on top of HttpUrlConnection introduced in Java 7. Currently GET and POST http methods are only supported along with MULTIPART FORM UPLOAD. It features a default job scheduler to queue and perform network task in background thread. All files are uploaded in binary as default, so the server to which it is uploaded must handle the files accordingly.</p>

<p>The four methods are provided in HTTPClient Class to carry out following operation:</p>

<ul>
<li>NetworkTask request(Params…)  /* To perform GET or POST request. */</li>
<li>NetworkTask getFile(Params…)  /* To download any resource as byte array from the URI */</li>
<li>NetworkTask formUpload(Params…)  /* To perform Multipart Form Upload */</li>
<li>enqueue(NetworkTask obj)  /* Pushes the respective network task to the queue and assign a thread from pool to perform it on the background.*/</li>
</ul>

<p>The library is built on the basis of Command Processor Design Pattern. So, Tweak, modify and have fun. Any positive citicism is appreciated.</p>

<h3 id="instruction">Instruction</h3>

<p>The HTTPClient class provides methods for performing various network related task. These methods returns an object of type NetworkTask which later can be enqueued to executor using enqueue method. For example:</p>

<pre><code>  /*Create the  NetworkTask object provided by respective HTTPClient method and save it. */

    NetworkTask networkTask=HTTPClient.getFile(URL, null, new FileDownloadCallback() {
        /*Callback for network task*/
        @Override
        public void onSuccess(byte[] response) {
                /*Do your thing here!*/
        }

        @Override
        public void onFailed(int responseCode) {
            /*Handle all error code here except 200-299.*/
        }

        @Override
        public void onConnectionNotEstablished(String message) {
           /*When any IO Exception is raised*/
        }
    });

    /*Push the networkTask object to be performed on background.*/
    HTTPClient.enqueue(networkTask);
</code></pre>
