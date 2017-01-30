import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Lock implements Runnable {
	ServerSocket serverSocket;
	static Connection conn;
	static Statement stmt;
	int frontdoor,backdoor,garagedoor;
	int frontdoor1,backdoor1,garagedoor1;
	
	public Lock(Connection conn) throws IOException{
		this.conn = conn;
		serverSocket = new ServerSocket(6693);
		serverSocket.setSoTimeout(10000);
	}
	
	void toRPi(String str) throws IOException{
		//System.out.println("I m he");
				try{
				Socket server = serverSocket.accept();
				//DataInputStream in = new DataInputStream(server.getInputStream());
				
				DataOutputStream out = new DataOutputStream(server.getOutputStream());
			        out.writeUTF(str);
		            	server.close();
				}catch (SocketTimeoutException server){
					//update Database that something is wrong with socket
					System.out.println("Error from lock");
					try{
						stmt.execute("INSERT INTO `status`(permission)" + "VALUES("+0+")");
					}
					catch(SQLException e){
						System.out.println("SQL ERROR");
					}
				}

	}
	
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			// TODO Auto-generated method stub
			try{
				//STEP 4: Execute a query
				stmt = conn.createStatement();
				String sql = "SELECT * FROM Locks"; 
				ResultSet rs = stmt.executeQuery(sql);
				while(rs.next()){
					frontdoor = rs.getInt("front_door");
					backdoor = rs.getInt("back_door");
					garagedoor = rs.getInt("garage_door");
				}
	          
				String fd = Integer.toString(frontdoor)+" ";
				String bd = Integer.toString(backdoor)+" ";
				String gd = Integer.toString(garagedoor);
				
				//System.out.println("data_user_temp="+data_user_temp);
				if(frontdoor1 != frontdoor){
					if(frontdoor == 1)
						System.out.println("Front door is locked");
						
					else System.out.println("Front door is unlocked");
					
					toRPi(Integer.toString(frontdoor)+" "+bd+gd);
					System.out.println("**********************************************");
				}
				
				if(backdoor1 != backdoor){
					if(backdoor == 1)
						System.out.println("Back door is locked");
					else System.out.println("Back door is unlocked");
					toRPi(fd+Integer.toString(backdoor)+" "+gd);
					System.out.println("**********************************************");
				}
				
				if(garagedoor1 != garagedoor){
					if(garagedoor == 1)
						System.out.println("Garage door is locked");
					else System.out.println("Garage door is unlocked");
					toRPi(fd+bd+Integer.toString(garagedoor));
					System.out.println("**********************************************");
				}
				
				frontdoor1= frontdoor;
				backdoor1= backdoor;
				garagedoor1= garagedoor;
				
			}catch(SQLException se){
				//Handle errors for JDBC
				se.printStackTrace();
			}catch(Exception e){
				//Handle errors for Class.forName
				e.printStackTrace();
			}
		}
		
	}


}
