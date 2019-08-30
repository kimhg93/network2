package kr.co.itcen.network.echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

public class EchoServerReceiveThread extends Thread {
	private Socket socket;	
	
	public EchoServerReceiveThread(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			InetSocketAddress inetRemoteSocketAddress = (InetSocketAddress)socket.getRemoteSocketAddress();
		
			EchoServer.log("connected form client "+
				inetRemoteSocketAddress.getAddress().getHostAddress()+":"+
				inetRemoteSocketAddress.getPort());
		
			//4. IOStream 생성				
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
				PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"), true);
				
				while(true) {
					//5. 데이터 읽기(수신)
					String data = br.readLine();				
					if(data == null) {
						// 정상종료 : remote socket이 close() 메소드를 호출해서 정상적으로 소켓을 닫은경우
						EchoServer.log("closed by client");
						break;
					}
					
					EchoServer.log("FromClient > "+data);
					
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
					try {
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
	}

}
