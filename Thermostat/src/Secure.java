import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Secure implements Runnable {
	ServerSocket serverSocket;
	static Connection conn;
	static Statement stmt;
	int disarm,arm_away,arm_stay;
	int disarm1,arm_away1,arm_stay1;
	
	public Secure(Connection conn) throws IOException{
		this.conn = conn;
		serverSocket = new ServerSocket(6692);
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
					System.out.println("Error from security");
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
				String sql = "SELECT * FROM Security"; 
				ResultSet rs = stmt.executeQuery(sql);
				while(rs.next()){
					disarm = rs.getInt("disarm");
					arm_away = rs.getInt("arm_away");
					arm_stay = rs.getInt("arm_stay");
				}
				//String ds = Integer.toString(disarm)+" ";
				//String aa = Integer.toString(arm_away);
			//	String as = Integer.toString(arm_stay)+" ";
				//System.out.println("data_user_temp="+data_user_temp);
				if(disarm1 != disarm){
					if(disarm == 1)
						System.out.println("System is Disarmed");
						System.out.println("**********************************************");
						//ds = Integer.toString(disarm)+" ";
						toRPi("1 "+"0 "+"0");
				}
				
				if(arm_away1 != arm_away){
					if(arm_away == 1)
						System.out.println("System is put on Arm-Away");
						System.out.println("**********************************************");
						//aa = Integer.toString(arm_away);
						toRPi("0 "+"0 "+"1");
				}
				
				if(arm_stay1 != arm_stay){
					if(arm_stay == 1)
						System.out.println("System is put on Arm-Stay");
						System.out.println("**********************************************");
						//aa = Integer.toString(arm_stay)+" ";
						toRPi("0 "+"1 "+"0");
				}
				
				disarm1= disarm;
				arm_away1= arm_away;
				arm_stay1= arm_stay;
				
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
