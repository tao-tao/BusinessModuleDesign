package com.tansun.form.designer.editors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class formServer {
	private ServerSocket serverSocket;
	private Socket socket;
	private int port = 10000;
	private ExecutorService executeService = Executors.newFixedThreadPool(100);
	
	public formServer(){
		try {
			System.out.println("[formServer] is started....");
			serverSocket = new ServerSocket(port);
			while(true){
					socket = serverSocket.accept();
					executeService.submit(new Runnable(){
						public void run(){
							try{
								BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
								PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
								
								String line =  in.readLine();
								System.out.println("Server Received:[" + line + "]");
								
								out.close();
								in.close();
							}catch (Exception e) {
								e.printStackTrace();
							}finally{
								try {
									socket.close();
								} catch (IOException e) {
									System.out.println("close socket error.");
									e.printStackTrace();
								}
							}
						}
					});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != serverSocket){
					serverSocket.close();
					System.out.println("serverSocket close");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws InterruptedException {
		new formServer();
	}  

}


