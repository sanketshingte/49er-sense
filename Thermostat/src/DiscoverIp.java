import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;


public class DiscoverIp implements Runnable {
	static Connection conn;
	static Statement stmt;
	static ArrayList<String> Rpi_Ip = new ArrayList<String>();
	
	public DiscoverIp(Connection conn){
		this.conn = conn;
	}
	
	public DiscoverIp(){
		
	}
	
	public ArrayList<String> getIpString(){
		return Rpi_Ip;
	}
	
	void handSHakeRPI(String server_ip){
		
		String myIp = null;
		String[] cmd = {"/home/sanket/Ipscript.sh"};
		try{
			Process p1 = Runtime.getRuntime().exec(cmd);
			BufferedReader stdin = new BufferedReader( new InputStreamReader(p1.getInputStream()));
			myIp = stdin.readLine();
			System.out.println("my ip="+myIp);
		}catch(Exception e){
			
		}
		
		try
		{
			System.out.println("Iam here");
			Socket s;
				s = new Socket(server_ip, 9876);
				//PrintWriter p = new PrintWriter(s.getOutputStream());
				System.out.println("WOW");
				//OutputStream outToServer = s.getOutputStream();
		         DataOutputStream out = new DataOutputStream(s.getOutputStream());
		        // System.out.println("H1");
		         //out.writeUTF("192.168.1.156");
		         out.writeUTF(myIp);
		         //System.out.println("H2");
		         
				//InputStream inFromServer = (InputStream) s.getInputStream();
		        // DataInputStream in = new DataInputStream(s.getInputStream());
		        // System.out.println("Server says " + in.readUTF());
				//p.close();
		        // System.out.println("H3");
				s.close();
			
		}catch(Exception e){}
	}
	
	public void run() {
		
		try {
			stmt = conn.createStatement();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		try{
			stmt.execute("INSERT INTO `status`(permission)" + "VALUES("+1+")"); //have perm to write
		}
		catch(SQLException e){
			System.out.println("SQL ERROR");
		}
		
		
		while(true){	
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String server_ip = null;
			String[] cmd = {"/home/sanket/myShellScript.sh"};
			int size=Rpi_Ip.size();
			try {
				Process p1 = Runtime.getRuntime().exec(cmd);
				BufferedReader stdin = new BufferedReader( new InputStreamReader(p1.getInputStream()));
				//System.out.println(size);
				while((server_ip = stdin.readLine())!= null){
					if(size == 0){	//first time{
						Rpi_Ip.add(server_ip);
						Tempsimulator.okrunflag =1;
					System.out.println(server_ip);
					System.out.println("before Handshake");
					handSHakeRPI(server_ip);
					System.out.println("After Handshake");
					}
					else{
						for (int i = 0; i < Rpi_Ip.size(); i++) {
							//System.out.println("i="+i+"server_ip="+server_ip+"Rpi_Ip.get(i)"+Rpi_Ip.get(i));
							if(Rpi_Ip.get(i) != server_ip){
								continue;
							}
								Rpi_Ip.add(server_ip);
								handSHakeRPI(server_ip);
								System.out.println(server_ip);
								
							
							
						}

						
					}
					
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
			
		}
		
	}
	
	
	
}
