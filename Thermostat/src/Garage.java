import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.net.*;
import java.io.*;

public class Garage implements Runnable {
	
	ServerSocket serverSocket;
	static Connection conn;
	static Statement stmt;
	int car_door1,car_door2;
	int car_door11=-1,car_door21=-1;
	
	
	public Garage(Connection conn) throws IOException{
		this.conn = conn;
		serverSocket = new ServerSocket(6691);
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
				String sql = "SELECT * FROM Garage"; 
				ResultSet rs = stmt.executeQuery(sql);
				int count =0;
				while(rs.next()){
					car_door1 = rs.getInt("door1");
					car_door2 = rs.getInt("door2");
				}
				String car1stat = Integer.toString(car_door1)+" ";
				String car2stat = Integer.toString(car_door2);
				/*while (count <1){
					count++;
					toRPi(car1stat+car2stat);
				}*/
				//System.out.println("door1 Value="+car_door11);
				//System.out.println("data_user_temp="+data_user_temp);
				if(car_door11 != car_door1){
					if(car_door1 == 1){
						System.out.println("Garage door1 is locked");
						//toRPi("Garage door1 is locked");
						//toRPi("1 1");
						car1stat = "1 ";
					}
					else {
						System.out.println("Garage door1 is unlocked");
						//toRPi("Garage door1 is unlocked");
						car1stat = "0 ";//toRPi("1 0");
						//stmt.execute("INSERT INTO Garage (door1,door2) "+ "VALUES (2,2)");
					}
					toRPi(car1stat+car2stat);
					System.out.println("**********************************************");
					
				}
				
				if(car_door21 != car_door2){
					if(car_door2 == 1){
						System.out.println("Garage door2 is locked");
						//toRPi("Garage door2 is locked");	
						//toRPi("2 1");
						car2stat = "1";
						}
					else{ System.out.println("Garage door2 is unlocked");
						//toRPi("Garage door2 is unlocked");
						//toRPi("2 0");
						car2stat = "0";
						}
					toRPi(car1stat+car2stat);
					System.out.println("**********************************************");
				}
				//toRPi(car1stat+car2stat);
				car_door11= car_door1;
				car_door21= car_door2;
				
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
/*import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Garage implements Runnable {

	static Connection conn;
	static Statement stmt;
	int car_door1,car_door2;
	int car_door11,car_door21;
	
	public Garage(Connection conn){
		this.conn = conn;
	}
	
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			// TODO Auto-generated method stub
			try{
				//STEP 4: Execute a query
				stmt = conn.createStatement();
				String sql = "SELECT * FROM Garage"; 
				ResultSet rs = stmt.executeQuery(sql);
				while(rs.next()){
					car_door1 = rs.getInt("door1");
					car_door2 = rs.getInt("door2");
				}
	          
				//System.out.println("data_user_temp="+data_user_temp);
				if(car_door11 != car_door1){
					if(car_door1 == 1)
						System.out.println("Garage door1 is locked");
					else System.out.println("Garage door1 is unlocked");
					System.out.println("**********************************************");
				}
				
				if(car_door21 != car_door2){
					if(car_door2 == 1)
						System.out.println("Garage door2 is locked");
					else System.out.println("Garage door2 is unlocked");
					System.out.println("**********************************************");
				}
				
				car_door11= car_door1;
				car_door21= car_door2;
				
			}catch(SQLException se){
				//Handle errors for JDBC
				se.printStackTrace();
			}catch(Exception e){
				//Handle errors for Class.forName
				e.printStackTrace();
			}
		}
		
	}

}*/
