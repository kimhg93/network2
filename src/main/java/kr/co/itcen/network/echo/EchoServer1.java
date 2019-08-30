package kr.co.itcen.network.echo;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer1 {
	private static final int PORT = 8001;
	public static void main(String[] args) {
		// 1. 서버소켓 생성
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket();
			
			//2. Binding: Socket에 SocketAddress(IPAddress + Port)를 바인딩 한다.
			InetAddress inetAddress = InetAddress.getLocalHost();			
			InetSocketAddress inetSocketAddress = new InetSocketAddress(inetAddress,PORT);
			serverSocket.bind(inetSocketAddress);
			log("binding "+inetAddress.getHostAddress()+":"+PORT);
			
			//3. accept: 클라이언트로 부터 연결요청(connect)을 기다린다.
			while(true) {
				Socket socket = serverSocket.accept(); //Blocking
				new EchoServerReceiveThread(socket).start();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			//8. server socket 자원정리
			try {
				if (serverSocket != null && serverSocket.isClosed() == false) {
					serverSocket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	
	public static void log(String str) {
		System.out.println("[Echo sever#"+ Thread.currentThread().getId() + "]" +str);
	}

}
