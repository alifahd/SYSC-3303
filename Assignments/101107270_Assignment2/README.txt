Introduction to UDP Program

Setup:
-Run Server.java, run IntermediateHost.java, run Client.java. You can see outputs
in different consoles

Client.java
-creates a read or write request (or even the invalid one)
-passes it to intermediate host on port 23
-Not looping for some reason (error)

IntermediateHost.java
-revceives requset/response from client/server and passes it to the other.
-connection between client and server

Server.java
-check for validity of request and either terminates or sends a response