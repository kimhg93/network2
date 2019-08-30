package kr.co.itcen.network.echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class EchoClient1 {
	private static String SERVER_IP = "192.168.1.31";
	private static final int SERVER_PORT = 8001;
	public static void main(String[] args) {
		Socket socket = null;
		Scanner sc = null;
		try {
			//1. scanner 생성(표준입력, 키보드 연결)
			sc = new Scanner(System.in);
			
			//2. 소켓 생성
			socket = new Socket();
			
			//3. 서버 연결	
			socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));			
			log("Connected");
			
			//3. IOStream 생성하기
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"), true);
			
			
			while(true) {			
				//5. 키보드 입력 받기
				System.out.print("toServer > ");				
				String line = sc.next();
				
				if(line.equals("exit")) {
					break;
				}	
				//6. 데이터 쓰기(전송)
				pw.println(line);
				
				//7. 데이터 읽기(수신)
				String data = br.readLine();				
				if(data == null) {
					// 정상종료 : remote socket이 close() 메소드를 호출해서 정상적으로 소켓을 닫은경우
					log("Closed by client");
					break;
				}					
				//8.콘솔 출력
				log("FromServer > "+data);
			}			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(sc!=null) {
					sc.close();
				}				
				if(socket!=null&&socket.isClosed()==false) {
					socket.close();
				}				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static void log(String str) {
		System.out.println("[Echo client] "+str);
	}
}
