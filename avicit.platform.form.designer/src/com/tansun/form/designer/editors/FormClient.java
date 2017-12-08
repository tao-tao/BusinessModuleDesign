package com.tansun.form.designer.editors;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class FormClient {
	private Socket socket;
	private int port = 10000;
	private String hostIp = "127.0.0.1";
	
	public FormClient(){
		try {
			String message = null;
			BufferedReader in = null;
			PrintWriter out = null;
			BufferedReader line = null;
				socket = new Socket(hostIp, port);
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);
				line = new BufferedReader(new InputStreamReader(System.in));
				System.out.println(in.readLine());
				message = "dlfdlfkdlfj";
				out.println(message);
			line.close();
			out.close();
			in.close();
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new FormClient();

	}

}