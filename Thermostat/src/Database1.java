import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class Database1 {
	
    /***********************************************************/
    //JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver"; 
    static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/users";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "student";
    static Connection conn = null;
    static Statement stmt = null;
    /***********************************************************/

    public static ArrayList<Double> data_curr_temp = new ArrayList<Double>();
    public static ArrayList<Integer> data_user_temp = new ArrayList<Integer>();
    //public static ArrayList<Integer> dest = new ArrayList<Integer>();

public void dbconn(){
    try{
          //STEP 2: Register JDBC driver
    	System.out.println("connecting db1");
          Class.forName("com.mysql.jdbc.Driver");

          //STEP 3: Open a connection
          //System.out.println("connecting to ");
          System.out.println("connecting db2");
          conn = DriverManager.getConnection(DB_URL, USER, PASS);
          /*
          //STEP 4: Execute a query
          stmt = conn.createStatement();
          System.out.println("connecting db");
          String sql = "SELECT * FROM mytable"; 	//Where id ="+ idnum +" ";
          ResultSet rs = stmt.executeQuery(sql);
          System.out.println("connecting db3");
          while(rs.next()){
         
        	  data_curr_temp.add((double)rs.getInt("User Temp"));
        	  //data_user_temp.add(rs.getInt("Source"));
              //dest.add(rs.getInt("Destination"));
          
          }
          rs.close();*/
       }catch(SQLException se){
          //Handle errors for JDBC
          se.printStackTrace();
       }catch(Exception e){
          //Handle errors for Class.forName
          e.printStackTrace();
       }finally{
          //finally block used to close resources
          /*try{
             if(stmt!=null)
                conn.close();
          }catch(SQLException se){
          }// do nothing
          try{
             if(conn!=null)
                conn.close();
          }catch(SQLException se){
             se.printStackTrace();
          }*///end finally try
       }//end try
    }//end

}