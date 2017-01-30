import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;


public class PollRPis implements Runnable {
	static Connection conn;
	static Statement stmt;
	ServerSocket serverSocket;
	
	public PollRPis(Connection conn) throws IOException{
		
		this.conn = conn;
		serverSocket = new ServerSocket(9875);
	}

	
public void run() {
	DiscoverIp dip = new DiscoverIp();
		
		while(true){	
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			ArrayList<String> allIps = dip.getIpString();
			//ArrayList<String> allIps = DiscoverIp.getIpString();
			String currentIp;
			System.out.println("Array size ="+allIps.size());
			if(allIps.size() >0){
				for(int i=0;i<allIps.size();i++){
					currentIp = allIps.get(i);
					try {
						Socket s = new Socket(currentIp, 9874);
						 DataOutputStream out = new DataOutputStream(s.getOutputStream());
				         out.writeUTF("I am server. Hello");
				       
					        DataInputStream in = new DataInputStream(s.getInputStream());
					        System.out.println("RPI says " + in.readUTF());
					        
					        	
					        s.close();
					        
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//PrintWriter p = new PrintWriter(s.getOutputStream());
			        
					
				}
				
			}
			
			
		}
		
		
}


}