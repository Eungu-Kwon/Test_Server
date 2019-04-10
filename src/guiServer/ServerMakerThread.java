package guiServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import javax.swing.JTable;

import fileIO.ExamData;

public class ServerMakerThread extends Thread {
	
	ServerSocket serversocket = null;
	ExamData[] data;
	int studentCount = 0;
	int dataCount;
	int port;
	String testName;
	JTable table;
	@SuppressWarnings("rawtypes")
	Vector vc;
	
	public ServerMakerThread(String _testName, int _dataCount, ExamData[] _data, JTable _table, int _port) {
		// TODO Auto-generated constructor stub
		data = _data;
		testName = _testName;
		dataCount = _dataCount;
		table = _table;
		port = _port;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			MakeServerThread();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Connection ERROR IO");
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void MakeServerThread() throws IOException {
		serversocket = null;
		Socket clientSocket = null;
		
		//Make Server Socket
		try {
			serversocket = new ServerSocket(port);
			
		}catch (IOException e) {
			System.exit(1);
		}
		
		vc = new Vector();
		while(true) {
			MakeServer testServer = null;
			try {
				clientSocket = serversocket.accept();
				vc.addElement(clientSocket);
				testServer = new MakeServer(testName, dataCount, data, serversocket, clientSocket, table, studentCount);
				testServer.start();
				studentCount++;
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				System.err.println("IO ERRER");
				return;
			}
		}
	}
	
	public ServerSocket getServerSocket() {
		return serversocket;
	}
	
	public int getHowManyStudent() {
		return studentCount;
	}
	
	public void closeAll() {
		for(int i = 0; i < vc.size(); i++) {
			Socket s = (Socket)vc.elementAt(i);
			try {
				s.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//무시하고 다음!
			}
		}
	}
}