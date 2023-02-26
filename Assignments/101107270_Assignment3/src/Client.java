import java.io.*;
import java.net.*;

public class Client 
{
	//initialize variables
	private DatagramPacket sendPacket, receivePacket;
	private DatagramSocket sendReceiveSocket;
	
	//initialize constructor
	public Client() 
	{
		try 
		{
			sendReceiveSocket = new DatagramSocket();
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
	private void byteMessage(String fileName, String mode, byte[] arr, char rWe)
	{
		int i = 0;
		
		arr[i] = (byte) 0;
		i++;
		//read
		if(rWe == 'r') 
		{
			arr[i] = (byte) 1; 
		} 
		//write
		else if(rWe == 'w') 
		{ 
			arr[i] = (byte) 2; 
		} 
		//error
		else 
		{
			arr[i] = (byte) 0; 
		}
		i++;
		
		for(byte b : fileName.getBytes()) 
		{
			arr[i] = b;
			i++;
		}
		
		arr[i] = (byte) 0; 
		i++;
		
		for(byte b : mode.getBytes()) 
		{
			arr[i] = b;
			i++;
		}
		
		arr[i] = (byte) 0;
	}
	
	/*
	 * This will print out the message to be converted in HEX
	 * 
	 * @params byte[] arr, int len
	 */
	private void printByteMessage(byte[] msg, int len) 
	{
		System.out.println("\nClient: The packet in bytes are represented as HEX:");
		for (int i = 1; i <= len; i++) 
		{
			if (Integer.toHexString(msg[i-1]).length() == 1) 
			{
				System.out.print("0");
			}
			System.out.print(Integer.toHexString(msg[i-1]) + " ");
			if (i % 4 == 0) 
			{
				System.out.println("");
			}
		}
	}
	
	
	// Sends Datagram packets to the server 
	// Prints out the packets details for each
	/*
	 * This will send out the datagram packets to the server and print out the details
	 * of each packet
	 * 
	 * @params byte[] msg
	 */
	private void sendPacket(byte[] dataByte)
	{
		try 
		{
			sendPacket = new DatagramPacket(dataByte, dataByte.length,InetAddress.getLocalHost(), 23);
		} catch (UnknownHostException e) 
		{
			e.printStackTrace();
				System.exit(1);
			}
				
		System.out.println("\nClient: Sending packet...");
		System.out.println("To host: " + sendPacket.getAddress());
		System.out.println("Destination host port: " + sendPacket.getPort());
		System.out.println("Length: " + sendPacket.getLength());

		try 
		{
			sendReceiveSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("Client: Packet sent.");	
	}
	
	
	// Receive a Datagram packet on the sendReceive socket
	// and print out the packets details 
	/*
	 * This will receive the datagram packet through the sendReceive socket and will then print
	 * out the details of the packets
	 * 
	 * @params N/A
	 */
	private void receivePacket() 
	{	
		byte dataByte[] = new byte[100];
		receivePacket = new DatagramPacket(dataByte, dataByte.length);

		try 
		{
			
			sendReceiveSocket.receive(receivePacket);
		} catch(IOException e) 
		{
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
		System.out.println("Client: Packet received:");
		System.out.println("From host: " + receivePacket.getAddress());
		System.out.println("Host port: " + receivePacket.getPort());
		System.out.println("Length: " + receivePacket.getLength());
		this.printByteMessage(dataByte, receivePacket.getLength());
		System.out.println("\n");		
	}
	
	/*
	 * This will send and receive datagram packets, will loop 11 times to send 5 and receive 5
	 * and get 1 error 
	 * 
	 * @params N/A
	 */
	public void sendAndReceive() 
	{
		String fileName = "test.txt";
		String mode = "netascii";
		char wRe = 'r';

		//send 11 packets to the server 
		for (int i = 0; i < 11; i++) 
		{
			//read
			if (i % 2 == 0) { 	
				wRe = 'r';
			}
			//write
			else 
			{ 			
				wRe = 'w';
			}
			//error
			if (i == 10) 
			{		
				wRe = 'e';
			}
			
			System.out.println("\n#################################################\n");
			System.out.println("Client: Preparing to send packet #" + (i + 1));
		
			byte[] msg = new byte[4 + fileName.length() + mode.length()];
			this.byteMessage(fileName, mode, msg, wRe);
		
			System.out.println("\nClient: Packet as a string:");  
			System.out.println(new String(msg,0,msg.length));
			this.printByteMessage(msg, msg.length);
		
			this.sendPacket(msg);
			
			this.receivePacket();	
		}
		sendReceiveSocket.close();  
	}
	
	/*
	 * Main method
	 */
	public static void main(String[] args) 
	{
		Client c = new Client();
		c.sendAndReceive();
	}

}
