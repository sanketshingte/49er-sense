import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Tempsimulator {

	/**
	 * @param args
	 */
	static int user_temp, user_mode=0;
	static int okrunflag=0;
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		//int port = 5001;
		Database1 db1 = new Database1();
		db1.dbconn();
	    
		//Scanner read = new Scanner(System.in);
		//Temphumidity temp = new Temphumidity();
		//Runnable r1 = new Temphumidity();
		//Thread t1 = new Thread(r1);
		/*Runnable r6 = new GreetingServer();
		Thread t6 = new Thread(r6);
		t6.start();*/
		Runnable r6 = new DiscoverIp(db1.conn);
		Thread t6 = new Thread(r6);
		t6.start();
		
		while(okrunflag == 0){
			//okrunflag =0;
			System.out.println(".");
		}
		
		System.out.println("Discovered IP of RPI");
		Runnable r1 = new Temp(db1.conn);
		Thread t1 = new Thread(r1);
		t1.start();
		
		Runnable r2 = new Light(db1.conn);
		Thread t2 = new Thread(r2);
		t2.start();
		
		Runnable r3 = new Secure(db1.conn);
		Thread t3 = new Thread(r3);
		t3.start();
		
		Runnable r4 = new Lock(db1.conn);
		Thread t4 = new Thread(r4);
		t4.start();
		
		Runnable r5 = new Garage(db1.conn);
		Thread t5 = new Thread(r5);
		t5.start();
		
		/*Runnable r7 = new PollRPis(db1.conn);
		Thread t7 = new Thread(r7);
		t7.start();*/
		
		/*
		while(true){
			System.out.println("\n-------------------------------------");
			System.out.println("Enter the temperature you want to set");
			user_temp = read.nextInt();
			System.out.println("For heat mode: press 0");
			System.out.println("For cool mode: press 1");
			user_mode = read.nextInt();
			//System.out.println("Enter the humidity you want to set");
			//user_hum = read.nextInt();
			
			t2.run();
		}*/
	}

}
