import java.io.*;
import java.net.*;

public class Server 
{
	//initialize variables
	private DatagramSocket sendSocket, receiveSocket;
	private DatagramPacket sendPacket, receivePacket;
	
	//initialize constructor
	public Server() 
	{
		try 
		{
			receiveSocket = new DatagramSocket(69);
	          
		} catch (SocketException se) 
		{
			se.printStackTrace();
			System.exit(1);
		} 	    
	}
	
	
	/*
	 * This method will be used to convert the message into a byte array
	 * 
	 * @params String fileName, String mode, byte[] arr, char rWe
	 */
	private void printByteMessage(byte[] msg, int len) 
	{		
		System.out.println("\nServer: Packet in bytes - represented as HEX:");
		for (int i = 1; i <= len; i++) 
		{
			if (Integer.toHexString(msg[i-1]).length() == 1) 
			{
				System.out.print("0");
			}
			// http://www.tutorialspoint.com/java/lang/integer_tohexstring.htm
			System.out.print(Integer.toHexString(msg[i-1]) + " ");
			if (i % 4 == 0) 
			{
				System.out.print("\n");
			}
		}	
	}
	
	/*
	 * This method will be used receive the packets
	 * 
	 * @params byte[] dataByte
	 */
	private void receivePacket(byte dataByte[]) 
	{
		try 
		{        
			System.out.println("Waiting...");
			receiveSocket.receive(receivePacket);
		} catch (IOException e) 
		{
			System.out.print("IO Exception: likely:");
			System.out.println("Receive Socket Timed Out.\n" + e);
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println("\nServer: Packet received:");
		System.out.println("From host: " + receivePacket.getAddress());
		System.out.println("Host port: " + receivePacket.getPort());
		System.out.println("Length: " + receivePacket.getLength());
		System.out.println("\nServer: Packet as a string:" );  
		System.out.println(new String(dataByte, 0, receivePacket.getLength()));
		this.printByteMessage(dataByte, receivePacket.getLength());
	}
	
	
	/*
	 * This method will be used parse any of the incoming packets, to check if there are any errors
	 * 
	 * @params byte[] dataByte
	 */
	private void parsePacket(byte dataByte[]) 
	{
		System.out.println("\nServer: Parsing received packet.");
		
		//makes sure the first byte is 0
		if (dataByte[0] != (byte) 0) 
		{ 
			System.out.println("ERROR - First byte is incorrect.");
			System.exit(1);
		}
		
		//makes sure the second byte is either 1 or 2
		if ((dataByte[1] == (byte) 1) || (dataByte[1] == (byte) 2))
		{ 
			;
		} else 
		{
			System.out.println("ERROR - Second byte is incorrect.");
			System.exit(1);
		}
	
		//make sure the last byte will end up being 0
		if (dataByte[receivePacket.getLength() - 1] != (byte) 0) 
		{
			System.out.println("ERROR - Last byte is incorrect.");
			System.exit(1);
		}
	
		//make sure that the center of the packet is 0
		int center = 0;
		for (int i = 2; i < receivePacket.getLength() - 1; i++) 
		{
			if (dataByte[i] == (byte) 0) 
			{
				center++;
			}	
		}
		
		if (center != 1) 
		{
			System.out.println("ERROR - No 0 byte or to many 0 bytes between text strings.");
			System.exit(1);
		}
		
		System.out.println("Server: Parsing complete.");		
	}
	
	/*
	 * This method will be used to send packets to client
	 * 
	 * @params byte[] dataByte
	 */
	private void sendPacket(byte[] dataByte) 
	{
		
		sendPacket = new DatagramPacket(dataByte, dataByte.length, receivePacket.getAddress(), receivePacket.getPort());

		System.out.println("Server: Sending packet:");
		System.out.println("To host: " + sendPacket.getAddress());
		System.out.println("Destination host port: " + sendPacket.getPort());
		System.out.println("Length: " + sendPacket.getLength());
		this.printByteMessage(dataByte,dataByte.length);
		
		try 
		{
			sendSocket = new DatagramSocket();
		} catch (SocketException e1) 
		{
			e1.printStackTrace();
			System.exit(1);
		}

		try {
			sendSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("\nServer: packet sent.\n");

		//Close the socket.
		sendSocket.close();			
	}
	
	
	// Receives Datagram packets from the client
	// Parses received packets
	// Sends Datagram packets to the client
	
	/*
	 * This method will receive packets from the client and then will parse the packets
	 * and send the packets back to the client if there are not errors from the packets
	 * that were received
	 * 
	 * @params N/A
	 */
	public void receiveAndRespond() 
	{
		byte dataByte[] = new byte[100];
		receivePacket = new DatagramPacket(dataByte, dataByte.length);
		
		while(true) 
		{ 
			System.out.println("\n#################################################\n");
			
			this.receivePacket(dataByte);	
			this.parsePacket(dataByte);
			
			System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
			System.out.println("Server: Preparing packet for response.");
			
			byte[] msg = new byte[4];
			//read request
			if(dataByte[1] == (byte) 1) 
			{
				msg[0] = (byte) 0;
				msg[1] = (byte) 3;
				msg[2] = (byte) 0;
				msg[3] = (byte) 1;
			}
			//write request
			if(dataByte[1] == (byte) 2) 
			{ 
				msg[0] = (byte) 0;
				msg[1] = (byte) 4;
				msg[2] = (byte) 0;
				msg[3] = (byte) 0;
			}
		
			this.sendPacket(msg);			
		}
	}
	
	
	/*
	 * Main method
	 */
	public static void main(String[] args) 
	{
		Server s = new Server();
		s.receiveAndRespond();
	}

}
