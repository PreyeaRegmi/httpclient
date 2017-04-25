<h1>A lightweight HTTP Client for android.</h1>
<p>A simple library to handle your common http request in simplest manner.</p>
<h3>What it is all about?</h3>
<p>Network requests can be messy at times in android. So it helps to abstract the network details and promotes clean code.</p>
<h3>Why should I use it when there are so many other http clients?</h3>
<p>Yes there are some great http clients with outstanding performance and features. But sometimes when the size of our apk really matters, when we don't want any additional features and ship any transitive dependency with our app, then this might do the job.</p>
<h3>Details</h3>
<p>This library is built on top of HttpUrlConnection introduced in Java 7. Currently GET and POST http methods are only supported along with MULTIPART FORM UPLOAD. It features a default job scheduler to queue and perform network task in background thread. All files are uploaded in binary as default, so the server to which it is uploaded must handle the files accordingly.</p>
<p>The library is built on the basis of Command Processor Design Pattern. So, Tweak, modify and have fun. Any positive citicism is appreciated.</p>
<h3>Instruction</h3>
<p>The HTTPClient class provides methods for performing various network related task. These methods returns an object of type NetworkTask which later can be enqueued to executor using enqueue method. For example:</p>
