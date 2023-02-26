// Client.java
// This class is the client side for a program based on
// UDP/IP. The client sends a request to the host, then waits 
// for the host to send a response back to the client.

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Client {

	private DatagramPacket sendPacket, receivePacket;
	private DatagramSocket sendReceiveSocket;

   public Client()
   {
      try {
         // Construct a datagram socket and bind it to any available 
         // port on the local host machine. This socket will be used to
         // send and receive UDP Datagram packets.
         sendReceiveSocket = new DatagramSocket();
      } catch (SocketException se) {   // Can't create the socket.
         se.printStackTrace();
         System.exit(1);
      }
   }

   public void sendAndReceive(int n)
   { 
      String s = "test.txt";
      String m = "octet";
      System.out.println("Client: sending a packet containing:\n" + s +"\n"+m);

      // byte array list for request
      ArrayList<Byte> request = new ArrayList<Byte>();
      request.add((byte)0);
      if(n == 11) {
    	  request.add((byte)3);
      }else if(n % 2 == 0) {
    	  request.add((byte)1);
      }else {
    	  request.add((byte)2);    	  
      }
      byte file[] = s.getBytes();
      //file text
      for(int i = 0; i < file.length; i++) {
    	  request.add(file[i]);
      }
      request.add((byte)0);
      byte mode[] = m.getBytes();
      //mode text
      for(int i = 0; i < mode.length; i++) {
    	  request.add(mode[i]);    	  
      }
      request.add((byte)0);
      byte msg[] = new byte[100];
      //puts request in byte array
      for(int i = 0; i<request.size(); i++) {
    	  msg[i] = request.get(i);
      }
      System.out.println(msg);

      // Construct a datagram packet that is to be sent to port 23.
      try {
         sendPacket = new DatagramPacket(msg, msg.length,
                                         InetAddress.getLocalHost(), 23);
      } catch (UnknownHostException e) {
         e.printStackTrace();
         System.exit(1);
      }

      System.out.println("Client: Sending packet:");
      System.out.println("To host: " + sendPacket.getAddress());
      System.out.println("Destination host port: " + sendPacket.getPort());
      int len = sendPacket.getLength();
      System.out.println("Length: " + len);
      System.out.print("Containing: ");
      System.out.println(new String(sendPacket.getData(),0,len)); // or could print "s"

      // Send the datagram packet to the server via the send/receive socket. 

      try {
         sendReceiveSocket.send(sendPacket);
      } catch (IOException e) {
         e.printStackTrace();
         System.exit(1);
      }

      System.out.println("Client: Packet sent.\n");

      // Construct a DatagramPacket for receiving packets up 
      // to 100 bytes long (the length of the byte array).

      byte data[] = new byte[100];
      receivePacket = new DatagramPacket(data, data.length);

      try {
         // Block until a datagram is received via sendReceiveSocket.  
         sendReceiveSocket.receive(receivePacket);
      } catch(IOException e) {
         e.printStackTrace();
         System.exit(1);
      }

      // Process the received datagram.
      System.out.println("Client: Packet received:");
      System.out.println("From host: " + receivePacket.getAddress());
      System.out.println("Host port: " + receivePacket.getPort());
      len = receivePacket.getLength();
      byte tempData[] = new byte[len];
      for (int i = 0; i < len; i++) {
    	  tempData[i] = data[i];
      }
      data = tempData;
      System.out.println("Length: " + len);
      System.out.print("Containing: ");

      // Form a String from the byte array.
      String received = new String(data,0,len);   
      System.out.println(received);

      // We're finished, so close the socket.
      sendReceiveSocket.close();
   }

   public static void main(String args[])
   {
	   Client c = new Client();
	  for(int i = 1; i <= 11; i++) {
	      c.sendAndReceive(i);		  
	  }
   }
}
