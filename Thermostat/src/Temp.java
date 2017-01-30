import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class Temp implements Runnable {
	ServerSocket serverSocket;
	static Connection conn;
	static Statement stmt;
	int data_user_temp_main,data_user_temp1_main,main_fan,main_mode;
	int data_user_temp_second,data_user_temp1_second,second_fan,second_mode;
	
	public Temp(Connection conn) throws IOException{
		this.conn = conn;
		serverSocket = new ServerSocket(6695);
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
					System.out.println("Error from temp");
					try{
						stmt.execute("INSERT INTO `status`(permission)" + "VALUES("+0+")");
					}
					catch(SQLException e){
						System.out.println("SQL ERROR");
					}
				}

	}
	
	public void run() {
		
		while(true){
			// TODO Auto-generated method stub
			try{
				Temphumidity a = new Temphumidity();
				Tempsimulator b = new Tempsimulator();
				//STEP 4: Execute a query
				stmt = conn.createStatement();
				String sql = "SELECT * FROM Temperature"; 
				ResultSet rs = stmt.executeQuery(sql);
				while(rs.next()){
					data_user_temp_main = rs.getInt("first_temp");
					main_mode = rs.getInt("first_mode");
					data_user_temp_second = rs.getInt("second_temp");
					second_mode = rs.getInt("second_mode");
					main_fan = rs.getInt("first_fan");
					second_fan = rs.getInt("second_fan");
				}
				String tempm = Integer.toString(data_user_temp_main)+" ";
				String mm = Integer.toString(main_mode)+" ";
				String mf = Integer.toString(main_fan)+" ";
				String tempf = Integer.toString(data_user_temp_second)+" ";
				String fm = Integer.toString(second_mode)+" ";
				String ff = Integer.toString(second_fan);
				
				//System.out.println("data_user_temp="+data_user_temp);
				
				/*********Temperature check for main floor *********/
				
				if(data_user_temp1_main != data_user_temp_main)
				{
				if (main_mode == 0 && (data_user_temp_main < a.curr_temp1)){
					while (data_user_temp_main != a.curr_temp1){
						a.curr_temp1--;
						System.out.println("Temp now ="+a.curr_temp1 );
					}
				}
		
				if (main_mode == 1 && (data_user_temp_main > a.curr_temp1)){
					while (data_user_temp_main != a.curr_temp1){
						a.curr_temp1++;
						System.out.println("Temp now(main floor) ="+a.curr_temp1);
					}
				}
				
				data_user_temp1_main= data_user_temp_main;
				
				tempm = Integer.toString(data_user_temp_main)+" ";
				mm = Integer.toString(main_mode)+" ";
				mf = Integer.toString(main_fan)+" ";
				tempf = Integer.toString(data_user_temp_second)+" ";
				fm = Integer.toString(second_mode)+" ";
				ff = Integer.toString(second_fan)+" ";
				toRPi(tempm+mm+mf+tempf+fm+ff);
				System.out.println("Temperature of main floor is set to ="+a.curr_temp1);
				System.out.println("**********************************************");
				//stmt.executeUpdate("insert into Temperature " + "values  ('"+data_user_temp_main+"','"+main_mode+"','"+main_fan+"','"+data_user_temp_second+"','"+second_mode+"','"+second_fan+"','"+a.curr_temp1+"','"+a.curr_temp2+"')");
				}
				
				/*********Temperature check for first floor *********/
				
				if(data_user_temp1_second != data_user_temp_second)
				{
				if (main_mode == 1 && (data_user_temp_second < a.curr_temp2)){
					while (data_user_temp_second != a.curr_temp2){
						a.curr_temp2--;
						System.out.println("Temp now ="+a.curr_temp2 );
					}
				}
		
				if (main_mode == 0 && (data_user_temp_second > a.curr_temp2)){
					while (data_user_temp_second != a.curr_temp2){
						a.curr_temp2++;
						System.out.println("Temp now(first floor) ="+a.curr_temp2);
					}
				}
				
				data_user_temp1_second= data_user_temp_second;
				
				tempm = Integer.toString(data_user_temp_main)+" ";
				mm = Integer.toString(main_mode)+" ";
				mf = Integer.toString(main_fan);
				tempf = Integer.toString(data_user_temp_second)+" ";
				fm = Integer.toString(second_mode);
				ff = Integer.toString(second_fan);
				toRPi(tempm+mm+mf+tempf+fm+ff);
				
				System.out.println("Temperature of first floor is set to ="+a.curr_temp2);
				System.out.println("**********************************************");
				//stmt.executeUpdate("insert into Temperature " + "values  ('"+data_user_temp_main+"','"+main_mode+"','"+main_fan+"','"+data_user_temp_second+"','"+second_mode+"','"+second_fan+"','"+a.curr_temp1+"','"+a.curr_temp2+"')");
				}
				
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
