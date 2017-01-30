import java.net.*;
import java.io.*;

public class GreetingServer implements Runnable {
	ServerSocket serverSocket;
	int port = 5001;
	int i = 0;
	public GreetingServer() throws IOException {
		serverSocket = new ServerSocket(port);
		// TODO Auto-generated constructor stub
	}

	public void run() {
		// TODO Auto-generated method stub
		//ServerSocket serverSocket;
		   
		   //int port;
		  /* public GreetingServer() {
		      
		     // serverSocket.setSoTimeout(10000);
		   }*/

		      while(true) {
		         try {
		            System.out.println("Waiting for client on port " + 
		               serverSocket.getLocalPort() + "...");
		            Socket server = serverSocket.accept();
		            
		            System.out.println("Just connected to " + server.getRemoteSocketAddress());
		            //DataInputStream in = new DataInputStream(server.getInputStream());
		            
		            //System.out.println(in.readUTF());
		            if (i<2){
		            DataOutputStream out = new DataOutputStream(server.getOutputStream());
		            System.out.println("FUCK YOU");
		            out.writeUTF("Thank you for connecting to "+i+""+ server.getLocalSocketAddress()
		               + "\nGoodbye!");
		            i++;   
		            }
		          //  server.close();
		            
		         }/*catch(SocketTimeoutException s) {
		            System.out.println("Socket timed out!");
		            break;
		         }*/catch(IOException e) {
		            e.printStackTrace();
		            break;
		         }
		      }
		   
	}

}
