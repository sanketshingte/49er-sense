import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.jdbc.PreparedStatement;


public class Light implements Runnable {
	ServerSocket serverSocket;
	static Connection conn;
	static Statement stmt;
	int mainfloor_light,firstfloor_light,mainfloor_light1,firstfloor_light1;
	int mainfloor_status, firstfloor_status,mainfloor_status1,firstfloor_status1;
	
	public Light(Connection conn) throws IOException{
		this.conn = conn;
		serverSocket = new ServerSocket(6694);
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
					System.out.println("Error from light");
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
				String sql = "SELECT * FROM Light"; 
				ResultSet rs = stmt.executeQuery(sql);
				while(rs.next()){
					mainfloor_light = rs.getInt("mainfloor_brightness");
					firstfloor_light = rs.getInt("firstfloor_brightness");
					mainfloor_status = rs.getInt("mainfloor_status");
					firstfloor_status = rs.getInt("firstfloor_status");
					
				}
				//mfs+mfl+ffs+ffl
				String mfs = Integer.toString(mainfloor_status)+" ";
				String mfl = Integer.toString(mainfloor_light)+" ";
				String ffs = Integer.toString(firstfloor_status)+" ";
				String ffl = Integer.toString(firstfloor_light);
				
				//System.out.println("data_user_temp="+data_user_temp);
				if(mainfloor_light1 != mainfloor_light){
					System.out.println("Main floor light brightness is set: "+mainfloor_light+"%");
					System.out.println("**********************************************");
					mfl = Integer.toString(mainfloor_light)+" ";
					toRPi(mfs+mfl+ffs+ffl);
					//stmt.executeUpdate("insert into Light " + "values  ('"+30+"','"+29+"')");
				}
				if(firstfloor_light1 != firstfloor_light){
					System.out.println("First floor light brightness is set: "+firstfloor_light+"%");
					System.out.println("**********************************************");
					ffl = Integer.toString(firstfloor_light);
					toRPi(mfs+mfl+ffs+ffl);
				}
				if(mainfloor_status1 != mainfloor_status){
					if(mainfloor_status == 0)
						System.out.println("Main Floor lights are OFF ");
					else System.out.println("Main floor lights are ON");
					System.out.println("**********************************************");
					mfs = Integer.toString(mainfloor_status)+" ";
					toRPi(mfs+mfl+ffs+ffl);
				}
				if(firstfloor_status1 != firstfloor_status){
					if(firstfloor_status == 0)
						System.out.println("First Floor lights are OFF ");
					else System.out.println("First floor lights are ON");
					System.out.println("**********************************************");
					ffs = Integer.toString(firstfloor_status)+" ";
					toRPi(mfs+mfl+ffs+ffl);
				}
				mainfloor_light1= mainfloor_light;
				firstfloor_light1= firstfloor_light;
				mainfloor_status1 = mainfloor_status;
				firstfloor_status1 = firstfloor_status;
				
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
