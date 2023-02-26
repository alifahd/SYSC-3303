import java.io.*;
import java.net.*;

public class IntermediateHost 
{
	//initialize variables
	private DatagramPacket sendPacket, receivePacket;
	private DatagramSocket receiveSocket, sendSocket, sendReceiveSocket;
	private int port_C;
	private int len_C;
	private InetAddress address_C;
	
	//initialize a contructor
	public IntermediateHost() 
	{
		try 
		{
			
			sendReceiveSocket = new DatagramSocket();

	     	receiveSocket = new DatagramSocket(23);  
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
	private void printByteMsg(byte[] msg, int len) 
	{				
		System.out.println("\nIntermediate: Packet in bytes - represented as HEX:");
		for (int i = 1; i <= len; i++) 
		{
			if (Integer.toHexString(msg[i-1]).length() == 1)
			{
				System.out.print("0");
			}
			
			System.out.print(Integer.toHexString(msg[i-1]) + " ");
			if (i % 4 == 0) 
			{
				System.out.print("\n");
			}
		}
		
	}
	
	
	/*
	 * This method will be receiving packets from the client class
	 * 
	 * @params byte[] dataByte
	 */
	private void receiveClientPacket(byte[] dataByte) 
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
		
		//initialize variables to allow communication with client
		address_C = receivePacket.getAddress();
		port_C = receivePacket.getPort();
		len_C = receivePacket.getLength();

		System.out.println("\nIntermediate Host: Packet received:");
		System.out.println("From host: " + address_C);
		System.out.println("Host port: " + port_C);
		System.out.println("Length: " + len_C);
		System.out.println("\nIntermediate Host: Packet as a string:");  
		System.out.println(new String(dataByte,0,len_C));
		this.printByteMsg(dataByte, len_C);		
	}
	
	
	/*
	 * This method will send packets to the client class
	 * 
	 * @params byte[] dataByte
	 */
	private void sendClientPacket(byte[] dataByte) 
	{
		sendPacket = new DatagramPacket(dataByte, receivePacket.getLength(),address_C, port_C);
		System.out.println("Intermediate: Forwarding packet to CLIENT:");
		System.out.println("To host: " + sendPacket.getAddress());
		System.out.println("Destination host port: " + sendPacket.getPort());
		System.out.println("Length: " + sendPacket.getLength());
		this.printByteMsg(sendPacket.getData(), sendPacket.getLength());

		try {
			sendSocket = new DatagramSocket();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		//send the datagram packet to the server via send/receive socket
		try {
			sendSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("\nIntermediate: packet sent to CLIENT.\n");
	
		//close socket
		sendSocket.close();		
	}
	
	/*
	 * This method will be used to send packets to the server system
	 * 
	 * @params byte[] dataByte
	 */
	private void sendServerPacket(byte[] dataByte) {
		
		sendPacket = new DatagramPacket(dataByte, receivePacket.getLength(),receivePacket.getAddress(), 69); 
		
		System.out.println("Intermediate: Forwarding packet to SERVER:");
		System.out.println("To host: " + sendPacket.getAddress());
		System.out.println("Destination host port: " + sendPacket.getPort());
		System.out.println("Length: " + sendPacket.getLength());
		System.out.println("\nIntermediate: Packet as a string:");  
		System.out.println(new String(sendPacket.getData(), 0, sendPacket.getLength()));
		this.printByteMsg(sendPacket.getData(), sendPacket.getLength());
	
		
		try {
			sendReceiveSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("\nIntermediate: Packet sent to Server.");	
	}
	
	/*
	 * This method will be used receive packets from the server class
	 * 
	 * @params byte[] dataByte
	 */
	private void receiveServerPacket(byte[] dataByte)
	{
		try 
		{
			sendReceiveSocket.receive(receivePacket);
		} catch(IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println("Intermediate: Packet received:");
		System.out.println("From host: " + receivePacket.getAddress());
		System.out.println("Host port: " + receivePacket.getPort());
		System.out.println("Length: " + receivePacket.getLength());
		this.printByteMsg(dataByte, receivePacket.getLength());		
	}
	
	/*
	 * This method will be used to receive packets from the client and pass to the server
	 * 
	 * @params N/A
	 */
	public void receiveAndPass() 
	{
		byte dataByte[] = new byte[100];
		receivePacket = new DatagramPacket(dataByte, dataByte.length);
		
		while(true) 
		{
			System.out.println("\n#################################################\n");
			
			//will call all the functions to wait for client to receive the packet server sent
			this.receiveClientPacket(dataByte);
			this.sendServerPacket(dataByte);
			//will wait for the server to receive the packet client sent
			this.receiveServerPacket(dataByte);
			this.sendClientPacket(dataByte);	
		} 
	}
	
	
	/*
	 * Main method
	 */
	public static void main(String[] args) 
	{
		IntermediateHost iH = new IntermediateHost();
		iH.receiveAndPass();

	}

}