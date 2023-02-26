//IntermediateHost.java
//This class is the connection to the server and client side of the
//UDP/IP program. The host receives from client/server and passes it to the other

import java.io.*;
import java.net.*;

public class IntermediateHost {

	private DatagramPacket sendPacket, receivePacket;
	private DatagramSocket receiveSocket;
	private DatagramSocket sendReceiveSocket;

public IntermediateHost()
{
   try {
      // Construct a datagram socket and bind it to port 23
	  receiveSocket = new DatagramSocket(23);
      sendReceiveSocket = new DatagramSocket();

   } catch (SocketException se) {
      se.printStackTrace();
      System.exit(1);
   } 
}

public void receiveAndEcho()
{
   // Construct a DatagramPacket for receiving packets up 
   // to 100 bytes long (the length of the byte array).

   byte data[] = new byte[100];
   receivePacket = new DatagramPacket(data, data.length);
   System.out.println("Host: Waiting for Packet.\n");

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

   // Process the received datagram.
   System.out.println(receivePacket);
   System.out.println("Host: Packet received:");
   System.out.println("From host: " + receivePacket.getAddress());
   System.out.println("Host port: " + receivePacket.getPort());
   int len = receivePacket.getLength();
   byte tempData[] = new byte[len];
   for (int i = 0; i < len; i++) {
 	  tempData[i] = data[i];
   }
   data = tempData;
   System.out.println("Length: " + len);
   System.out.print("Containing: " );

   // Form a String from the byte array.
   String received = new String(data,0,len);   
   System.out.println(received + "\n");
   
   // Slow things down (wait 5 seconds)
   try {
       Thread.sleep(5000);
   } catch (InterruptedException e ) {
       e.printStackTrace();
       System.exit(1);
   }

   // Create a new datagram packet

   sendPacket = new DatagramPacket(data, receivePacket.getLength(),
                            receivePacket.getAddress(), 69);

   System.out.println( "Host: Sending packet:");
   System.out.println("To host: " + sendPacket.getAddress());
   System.out.println("Destination host port: " + sendPacket.getPort());
   len = sendPacket.getLength();
   System.out.println("Length: " + len);
   System.out.print("Containing: ");
   System.out.println(new String(sendPacket.getData(),0,len));
     
   // Send the datagram packet to the client via the send socket. 
   try {
	  sendReceiveSocket.send(sendPacket);
   } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
   }

   System.out.println("Host: packet sent");

}

	public static void main( String args[] ) {
		IntermediateHost c = new IntermediateHost();
		while(true) {
			   c.receiveAndEcho();		
		}
	}
}

