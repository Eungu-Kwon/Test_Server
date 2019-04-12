package guiServer;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import fileIO.*;

class MakeServer extends Thread {
	ExamData[] data;
	int dataCount;
	int myNumber;
	String testName;
	JTable table;
	
	ServerSocket serversocket = null;
	Socket clientSocket = null;
	PrintWriter out;
	BufferedReader in;
	
	int score = 0;
	
	public MakeServer(String _testName, int dataNum, ExamData[] tests, ServerSocket serverS, Socket clientS, JTable _table, int count)  throws IOException{
		// TODO Auto-generated constructor stub
		data = tests;
		testName = _testName;
		dataCount = dataNum;
		serversocket = serverS;
		clientSocket = clientS;
		table = _table;
		myNumber = count;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		//Connect Sever and Client
		try {
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		} catch (IOException e) {
			// TODO: handle exception
			System.exit(1);
		}
		
		String name;
		String stuName;
		
		try {
			name = in.readLine();
			stuName = in.readLine();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			System.out.println("학생 정보 읽기 실패");
			return;
		}
		
		String[] student = {"" + (myNumber + 1), name, stuName, "응시중", "NON"};
		
		setTableData(myNumber, student);
		
		//Send to Client
		out.println(testName);
		out.println(dataCount + "");
		
		//Send Exam Data
		for(int i = 0; i < data.length; i++) {
			
			out.println(data[i].getProblem());
			
			for(int j = 0; j < data[i].HowManyProblems(); j++) {
				out.println(data[i].getNumbers(j));
			}
			
			for(int j = 0; j < 5 - data[i].HowManyProblems(); j++) {
				out.println("NONEANSWER");
			}
			
			if(!data[i].getPicturePath().equals("null")) {
				try {
					BufferedImage img = ImageIO.read(new File(data[i].getPicturePath()));
					out.println("hasPicture");
					ImageIO.write(img, "PNG", clientSocket.getOutputStream());
					out.println("EndOfImg");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					out.println("null");
					System.err.println("파일 읽기 오류");
				}
			}
			else {
				out.println("null");
			}
		}
		
		//read from Client
		for(int i = 0; i < data.length; i++) {
			
			String a;
			
			try {
				a = in.readLine();
				int b = Integer.parseInt(a) - 1;
				score += data[i].isCorrect(b);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.err.println("Connection ERROR IO");
				student[3] = "연결 오류";
				updateTableData(myNumber, student, 0);
				out.close();
				return;
			} catch (NumberFormatException e) {
				// TODO: handle exception
				System.err.println("Score load error");
				student[3] = "연결 오류";
				updateTableData(myNumber, student, 0);
				out.close();
				return;
			}
		}
		
		out.println(score + "");
		student[3] = "제출 완료";
		updateTableData(myNumber, student, score);
		
		out.close();
		try {
			in.close();
			clientSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Closing Error");
			return;
		}
	}
	
	public void setTableData(int i, String[] tableData) {
		DefaultTableModel tableModel = (DefaultTableModel)table.getModel();
		tableModel.insertRow(i, tableData);
		if(i == 0) tableModel.removeRow(i + 1);
	}
	
	public void updateTableData(int i, String[] tableData, int myScore){
		DefaultTableModel tableModel = (DefaultTableModel)table.getModel();
		
		tableData[4] = myScore + "";
		tableModel.insertRow(i, tableData);
		tableModel.removeRow(i + 1);
	}
}

@SuppressWarnings("serial")
public class TestStartPage extends JPanel {
	
	int studentCount;
	ServerMakerThread serverMaker;
	JFrame frameCtrl;
	JTable table;
	JButton closeServer;
	JButton saveFile;
	
	public TestStartPage(String testName, JFrame _frameCtrl, int dataCount, ExamData[] data, String ipAddr, int port) {
		// Make Table & Server Maker
		frameCtrl = _frameCtrl;
		frameCtrl.setSize(500, 600);
		frameCtrl.setTitle(testName + " ip : " + ipAddr + " / " + port);
		
		//Make Table
		JPanel panel = new JPanel();
		String[] columnNames = {"No.", "이름", "학번", "상태", "점수"};
		DefaultTableModel dt = new DefaultTableModel(columnNames, 30);
		table = new JTable(dt);
		
		//2번열 가운데 정렬
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		DefaultTableCellRenderer dtcrRight = new DefaultTableCellRenderer();
		dtcrRight.setHorizontalAlignment(SwingConstants.RIGHT);
		TableColumnModel tcm = table.getColumnModel();
		tcm.getColumn(0).setCellRenderer(dtcr);
		tcm.getColumn(1).setCellRenderer(dtcr);
		tcm.getColumn(2).setCellRenderer(dtcr);
		tcm.getColumn(3).setCellRenderer(dtcr);
		tcm.getColumn(4).setCellRenderer(dtcrRight);
		
		tcm.getColumn(0).setMaxWidth(30);
		tcm.getColumn(1).setPreferredWidth(8);
		tcm.getColumn(3).setPreferredWidth(10);
		tcm.getColumn(4).setPreferredWidth(6);
		
		JScrollPane scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		scrollPane.setPreferredSize(new Dimension(400, 500));
		
		//Make Buttons
		JPanel buttonPanel = new JPanel();
		closeServer = new JButton("서버 종료");
		closeServer.addActionListener(new ButtonListener(this));
		saveFile = new JButton("저장");
		saveFile.addActionListener(new ButtonListener(this));
		saveFile.setEnabled(false);
		
		buttonPanel.add(closeServer);
		buttonPanel.add(saveFile);
		
		panel.add(scrollPane);
		panel.add(buttonPanel);
		add(panel);
		setVisible(true);
		
		serverMaker = new ServerMakerThread(testName, dataCount, data, table, port);
		serverMaker.start();
	}
	
	class ButtonListener implements ActionListener {
		JPanel panel;
		public ButtonListener(JPanel _panel) {
			// TODO Auto-generated constructor stub
			panel = _panel;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
			if(e.getSource() == closeServer) {
				
				int option = JOptionPane.showConfirmDialog(panel, "서버를 종료하시겠습니까?\n클라이언트와의 연결도 끊기게됩니다.", "확인", JOptionPane.OK_CANCEL_OPTION);
				if(option == JOptionPane.YES_OPTION) {
					try {
						studentCount = serverMaker.getHowManyStudent();
						System.out.println(studentCount);
						serverMaker.getServerSocket().close();
						closeServer.setEnabled(false);
						saveFile.setEnabled(true);
						serverMaker.closeAll();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						System.err.println("안끊기네");
						return;
					}
				}
				
			}
			
			else if(e.getSource() == saveFile) {
				try {
					Writer file = new FileWriter("./ExcelData.csv", false);
					DefaultTableModel tableModel = (DefaultTableModel)table.getModel();
					file.write("이름,학번,상태,점수\n");
					for(int i = 0; i < studentCount; i++) {
						for(int j = 1; j <= 4; j++) {
							file.write((String) tableModel.getValueAt(i, j) + ",");
						}
						
						file.write('\n');
					}
					file.close();
					JOptionPane.showMessageDialog(panel, "성공적으로 저장하였습니다.", "저장 완료", JOptionPane.INFORMATION_MESSAGE);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					return;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					return;
				}
				
				
			}
		}
	}
}
