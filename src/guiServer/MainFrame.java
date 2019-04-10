package guiServer;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

import javax.swing.*;

import fileIO.*;

@SuppressWarnings("serial")
class MyFrame extends JFrame {
	
	private JLabel dataInfo;
	private JButton startButton;
	private JButton loadData;	
	String ipAddr = null;
	JTextField numField;
	JTextField portField;
	JTextField nameField;
	JPanel panel;
	int dataCount;
	ExamData[] data;
	
	public MyFrame() {
		// TODO Auto-generated constructor stub
		
		setTitle("���� ���� Server");
		//setSize(600, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel = new JPanel();
		
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		InetAddress addr;
		
		try {
			addr = InetAddress.getLocalHost();
			ipAddr = addr.getHostAddress() + "";
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(this, "IP�ּҸ� ���� �� �����ϴ�.", "ERRER", JOptionPane.ERROR_MESSAGE);
		}
		
		JLabel ipLabel = new JLabel("IP �ּ� : " + ipAddr);
		JLabel portInfo = new JLabel("Port : ");
		JLabel numInfo = new JLabel("���� �� : ");
		JLabel nameInfo = new JLabel("���� �̸� : ");
		
		ipLabel.setPreferredSize(new Dimension(400, 80));
		
		JPanel portPanel = new JPanel();
		portField = new JTextField(10);
		portPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
		portPanel.setPreferredSize(new Dimension(600, 40));
		
		JPanel numPanel = new JPanel();
		numField = new JTextField(5);
		numPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
		numPanel.setPreferredSize(new Dimension(400, 60));
		
		JPanel namePanel = new JPanel();
		nameField = new JTextField(10);
		namePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
		namePanel.setPreferredSize(new Dimension(400, 60));
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		dataInfo = new JLabel("������ �ҷ��� �� ������ �������ּ���");
		loadData = new JButton("���� �ҷ�����");
		loadData.setFont(new Font("�����ٸ����", Font.PLAIN, 16));
		loadData.addActionListener(new ButtonListener(this));
		startButton = new JButton("����");
		startButton.setFont(new Font("�����ٸ����", Font.PLAIN, 16));
		startButton.setEnabled(false);
		startButton.addActionListener(new ButtonListener(this));
		buttonPanel.setPreferredSize(new Dimension(600, 100));
		
		ipLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		ipLabel.setFont(new Font("�����ٸ����", Font.PLAIN, 20));
		portInfo.setFont(new Font("�����ٸ����", Font.PLAIN, 20));
		dataInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
		numInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
		dataInfo.setFont(new Font("�����ٸ����", Font.PLAIN, 30));
		numInfo.setFont(new Font("�����ٸ����", Font.PLAIN, 20));
		nameInfo.setFont(new Font("�����ٸ����", Font.PLAIN, 20));
		
		//������Ʈ ���̱�
		portPanel.add(portInfo);
		portPanel.add(portField);
		numPanel.add(numInfo);
		numPanel.add(numField);
		buttonPanel.add(loadData);
		buttonPanel.add(startButton);
		namePanel.add(nameInfo);
		namePanel.add(nameField);
		panel.add(ipLabel);
		panel.add(portPanel);
		panel.add(numPanel);
		panel.add(namePanel);
		panel.add(dataInfo);
		
		panel.add(Box.createRigidArea(new Dimension(0, 20)));
		panel.add(buttonPanel);
		add(panel);
		pack();
		setVisible(true);
	}
	
	public JFrame getFrame() {
		return this;
	}
	
	private class ButtonListener implements ActionListener {
		
		JFrame frameControl = null;
		
		public ButtonListener(JFrame frame) {
			// TODO Auto-generated constructor stub
			frameControl = frame;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(e.getSource() == loadData) {
				dataCount = 0;
				Random rand = new Random();
				rand.setSeed(System.currentTimeMillis());
				
				try {
					dataCount = Integer.parseInt(numField.getText());
				}catch (NumberFormatException e2) {
					// TODO: handle exception
					dataInfo.setText("������ �������ּ���.");
					return;
				}
				
				if(dataCount <= 0) {
					dataInfo.setText("���׼��� 1 �̻����� �������ּ���.");
					return;
				}
				
				else if(!new File("./data/" + (dataCount) + ".txt").exists()) {
					dataInfo.setText("���׼��� �ʹ� �����ϴ�!");
					return;
				}
				
				int[] randData = new int[dataCount];
				data = new ExamData[dataCount];
				
				for(int i = 0; i < dataCount; i++) {
					randData[i] = 0;
				}
				
				int filesCount = 0;
				
				for(int i = 1; i < 10000; i++) {
					if(!new File("./data/" + i + ".txt").exists()) {
						filesCount = i - 1;
						break;
					}
				}
				
				for(int i = 0; i < dataCount; i++) {
					int temp = rand.nextInt(10000) % filesCount + 1;
					int k = 1;
					for(int j = 0; j < randData.length; j++) {
						if(randData[j] == temp) {
							i--;
							k = 0;
							break;
						}
					}
					
					if(k == 1) {
						if(new File("./data/" + temp + ".txt").exists()) {
							randData[i] = temp;
							data[i] = new ExamData("./data/" + temp + ".txt");
						}
						else {
							JOptionPane.showMessageDialog(getFrame(), "������ �д� ���� ������ �߻��Ͽ����ϴ�.", "���� �б� ����", JOptionPane.ERROR_MESSAGE);
							return;
						}
					}
				}

				dataInfo.setText("�����бⰡ �Ϸ�Ǿ����ϴ�.");
				loadData.setEnabled(false);
				startButton.setEnabled(true);
			}
			
			if(e.getSource() == startButton) {
				
				int port;
				
				try {
					port = Integer.parseInt(portField.getText());
				} catch (NumberFormatException e2) {
					JOptionPane.showMessageDialog(frameControl, "port�� �������ּ���");
					return;
				}
				
				panel.setVisible(false);
				
				TestStartPage startPanel = new TestStartPage(nameField.getText(), frameControl, dataCount, data, ipAddr, port);
				add(startPanel);
				startPanel.setVisible(true);
			}
		}
	}
}

public class MainFrame {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new MyFrame();
	}
}
