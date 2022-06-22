# networkingJava
<b>Overview</b><br>
This Module describes a very basic one-way Client and Server setup where a Client connects, sends messages to the server and the server shows them using a socket connection. There’s a lot of low-level stuff that needs to happen for these things to work but the Java API networking package (java.net) takes care of all of that, making network programming very easy for programmers.<br>
<b>Establish a Socket Connection (Client)</b><br>
To connect to network location (IP Address) and port. The java.net.Socket class represents a Socket. To open a socket: <br>

Socket socket = new Socket(“127.0.0.1/localhost”, 5500)
<ul>
<li>The first argument – IP address of Server. ( 127.0.0.1  is the IP address of localhost, where code will run on the single stand-alone machine).</li>
  <li>The second argument – Port. (Just a number representing which application to run on a server. For example, HTTP runs on port 80. Port number can be from 0 to 65535)</li>
</ul>
<b>Communication </b><br>
To communicate over a socket connection, streams are used to both input and output the data.<br>
<b>Closing the connection</b><br>
The socket connection is closed explicitly once the message to the server is sent.
In the program, the Client keeps reading input from a user and sends it to the server until “Over” is typed.
<b>Establish a Socket Connection (Server)</b><br>
To write a server application two sockets are needed. <br>
<ul>
<li>A ServerSocket which waits for the client requests (when a client makes a new Socket())</li>
<li>A plain old Socket socket to use for communication with the client.</li>
  </ul>
  <b>Communication</b><br>
getOutputStream() method is used to send the output through the socket.<br>
<b>Close the Connection </b><br>
After finishing,  it is important to close the connection by closing the socket as well as input/output streams.<br>
<b>Note:</b>
<ul>
<li>Server application makes a ServerSocket on a specific port which is 5500. This starts our Server listening for client requests coming in for port 5500.</li>
  <li>Then Server makes a new Socket to communicate with the client.</li>
  socket = server.accept()
  <li>The accept() method blocks(just sits there) until a client connects to the server.</li>
  <li>Then we take input from the socket using getInputStream() method. Our Server keeps receiving messages until the Client sends “Over”.</li>
  <li>After we’re done we close the connection by closing the socket and the input stream.</li>
  <li>To run the Client and Server application on your machine, compile both of them. Then first run the server application and then run the Client application.</li>
</ul>
<b>To run on Terminal or Command Prompt</b><br>
Open two windows one for Server and another for Client

1. First run the Server application as,  

$ java Server
Server started 
Waiting for a client …

2. Then run the Client application on another terminal as,  

$ java Client
It will show – Connected and the server accepts the client and shows,
Client accepted





