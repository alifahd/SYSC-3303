// Server.java
// This class is the server side of a simple echo server based on
// UDP/IP. The server receives from a the host a packet containing a request, then echoes a response back to the host.

import java.io.*;
import java.net.*;

public class Server {

   private DatagramPacket sendPacket, receivePacket;
   private DatagramSocket sendSocket, receiveSocket;

   public Server()
   {
      try {
         // Construct a datagram socket and bind it to any available 
         // port on the local host machine.
         sendSocket = new DatagramSocket();

         // Construct a datagram socket and bind it to port 69 
         receiveSocket = new DatagramSocket(69);
         
      } catch (SocketException se) {
         se.printStackTrace();
         System.exit(1);
      } 
   }

   public void receiveAndEcho() throws Exception
   {
      // Construct a DatagramPacket for receiving packets up 
      // to 100 bytes long (the length of the byte array).

      byte data[] = new byte[100];
      receivePacket = new DatagramPacket(data, data.length);
      System.out.println("Server: Waiting for Packet.\n");

      // Block until a datagram packet is received from receiveSocket.
      try {        
         System.out.println("Waiting..."); // so we know we're waiting
         receiveSocket.receive(receivePacket);
      } catch (IOException e) {
         System.out.print("IO Exception: likely:");
         System.out.println("Receive Socket Timed Out.\n" + e);
         e.printStackTrace();
         System.exit(1);
      }
      
      System.out.println(receivePacket);

      // Process the received datagram.
      System.out.println(receivePacket);
      System.out.println("Server: Packet received:");
      System.out.println("From host: " + receivePacket.getAddress());
      System.out.println("Host port: " + receivePacket.getPort());
      int len = receivePacket.getLength();
      System.out.println("Length: " + len);
      System.out.print("Containing: " );

      // Form a String from the byte array.
      String received = new String(data,0,len);   
      System.out.println(received + "\n");

      //output in byte form
      System.out.print("Byte Form: ");   
      for(int i = 0; i <len; i++) {
    	  if((byte)data[i] == (byte)0 && (byte)data[i+1] == (byte)0) {
              System.out.print((byte)data[i]+"\n");   
    		  break;
    	  }
          System.out.print((byte)data[i] + " ");    	  
      }
      
      //generates response based on request
      byte[] response = new byte[100];
      if((byte)data[0] == (byte)0 && (byte)data[1] == (byte)1) {
          System.out.print("Read Request: VALID\n");
          response[0] = (byte)0;
          response[1] = (byte)3;
          response[2] = (byte)0;
          response[3] = (byte)1;
      }else if((byte)data[0] == (byte)0 && (byte)data[1] == (byte)2) {
          System.out.print("Write Request: VALID\n" );
          response[0] = (byte)0;
          response[1] = (byte)4;
          response[2] = (byte)0;
          response[3] = (byte)0;  	  
      }else {
          throw new Exception("INVALID REQUEST\n");	  
      }
      
      // Slow things down (wait 5 seconds)
      try {
          Thread.sleep(5000);
      } catch (InterruptedException e ) {
          e.printStackTrace();
          System.exit(1);
      }
 
      // Create a new datagram packet containing the string received from the client.

      sendPacket = new DatagramPacket(response, receivePacket.getLength(),
                               receivePacket.getAddress(), receivePacket.getPort());

      System.out.println( "\nServer: Sending packet:");
      System.out.println("To host: " + sendPacket.getAddress());
      System.out.println("Destination host port: " + sendPacket.getPort());
      len = sendPacket.getLength();
      System.out.println("Length: " + len);
      System.out.print("Containing: ");
      for(int i = 0; i <4; i++) {
          System.out.print((byte)response[i] + " ");    	  
      }
        
      // Send the datagram packet to the client via the send socket. 
      try {
         sendSocket.send(sendPacket);
      } catch (IOException e) {
         e.printStackTrace();
         System.exit(1);
      }

      System.out.println("\nServer: packet sent");

      // We're finished, so close the sockets.
      sendSocket.close();
   }

   public static void main( String args[] )
   {
	   Server c = new Server();
	   while(true) {
		      try {
				c.receiveAndEcho();
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}		   
	   }
   }
}

