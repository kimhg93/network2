package kr.co.itcen.network.echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class EchoServer {
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
			Socket socket = serverSocket.accept(); //Blocking
			InetSocketAddress inetRemoteSocketAddress = (InetSocketAddress)socket.getRemoteSocketAddress();
			
			log("connected form client "+
					inetRemoteSocketAddress.getAddress().getHostAddress()+":"+
					inetRemoteSocketAddress.getPort());
			
			try {
			//4. IOStream 생성				
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
				PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"), true);
				
				while(true) {
					//5. 데이터 읽기(수신)
					String data = br.readLine();				
					if(data == null) {
						// 정상종료 : remote socket이 close() 메소드를 호출해서 정상적으로 소켓을 닫은경우
						log("closed by client");
						break;
					}
					
					log("FromClient > "+data);
					
					//6. 데이터 쓰기(송신)
					//socket.connect(inetRemoteSocketAddress);
					pw.println(data);
				}							
				
			} catch(SocketException e) {
				System.out.println("[TCPServer] abnormal closed by client");
			} catch(IOException e) {
				e.printStackTrace();
			} finally {
				// 7. socket 자원정리
				if(socket!=null&&socket.isClosed()==false) {
					socket.close();
				}
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
		System.out.println("[Echo sever] "+str);
	}

}
