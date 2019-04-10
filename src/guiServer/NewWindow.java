package guiServer;

import java.awt.BorderLayout;
import fileIO.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;

@SuppressWarnings("serial")
class mainPanel extends JFrame {
	
	JFrame frameCtrl;
	JPanel mainPanel;
	
	String ipAddr;
	
	JPanel panel1 = new JPanel();
	JPanel panel2 = new JPanel();
	
	FileReader fr;
	JPanel specificSettingPanel;
	JComboBox<String> subjectList;
	JSpinner[] sSpinner;
	JButton startB;
	
	JTextField testNameField;
	JTextField portInputField;
	
	ExamData[] data;
	
	public mainPanel() {
		// TODO Auto-generated constructor stub
		//setLayout(new BorderLayout());
		mainPanel = new JPanel(new BorderLayout());
		frameCtrl = this;
		setTitle("TestServer");
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		fr =  new FileReader();
		
		JTabbedPane tabbedPane = new JTabbedPane();
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		InetAddress addr;
		
		try {
			addr = InetAddress.getLocalHost();
			ipAddr = addr.getHostAddress() + "";
			System.out.println(ipAddr);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(this, "IP주소를 읽을 수 없습니다.", "ERRER", JOptionPane.ERROR_MESSAGE);
		}
		
		startB = new JButton();
		try {
			Image img = ImageIO.read(new File("startB.png"));
			img = getScaledImage(img, 30, 30);
			startB.setIcon(new ImageIcon(img));
			startB.setToolTipText("서버 시작");
			startB.setBorderPainted(false);
			startB.setMargin(new Insets(0, 0, 0, 0));
			startB.setBackground(new Color(getBackground().getRed(), getBackground().getGreen(), getBackground().getBlue()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			startB.setText("서버 시작");
		}
		startB.addActionListener(new ComboBoxActionListener());
	
		
		startB.setPreferredSize(new Dimension(30, 30));
		buttonPanel.add(startB);
		mainPanel.add(buttonPanel, BorderLayout.PAGE_START);
		//panel1
		panel1.setLayout(new BorderLayout(5, 5));
		
		JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		namePanel.setPreferredSize(new Dimension(300, 30));
		JLabel testName = new JLabel("시험 이름  : ");
		testName.setHorizontalAlignment(JLabel.RIGHT);
		testNameField = new JTextField(10);
		testName.setPreferredSize(new Dimension(70, 20));
		namePanel.add(testName);
		namePanel.add(testNameField);
		
		JPanel portPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		portPanel.setPreferredSize(new Dimension(300, 30));
		JLabel portLabel = new JLabel("Port  : ");
		portInputField = new JTextField(10);
		portLabel.setHorizontalAlignment(JLabel.RIGHT);
		portLabel.setPreferredSize(new Dimension(70, 20));
		portPanel.add(portLabel);
		portPanel.add(portInputField);
		panel1.setPreferredSize(new Dimension(300, 40));
		
		panel1.add(namePanel, BorderLayout.PAGE_START);
		panel1.add(portPanel, BorderLayout.LINE_END);
		
		//panel2
		panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
		
		JPanel subjectSelectPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JLabel subjectLabel = new JLabel("과목 : ");
		subjectList = new JComboBox<String>();
		subjectList.setPreferredSize(new Dimension(180, 30));
		
		for(int i = 0; i < fr.getSubjects().length; i++) {
			subjectList.addItem(fr.getSubjects()[i].getName());
		}
		
		subjectList.addActionListener(new ComboBoxActionListener());
		subjectSelectPanel.add(subjectLabel);
		subjectSelectPanel.add(subjectList);
		subjectSelectPanel.setPreferredSize(new Dimension(200, 50));
		panel2.add(subjectSelectPanel);
		
		//세부과목 문항수 설정
		specificSettingPanel = new JPanel();
		Dimension d = new Dimension(200, fr.getSpecificSubjects().length * 40);
		specificSettingPanel.setPreferredSize(d);
		Border border = BorderFactory.createTitledBorder("세부 영역별 문항 수 설정");
		specificSettingPanel.setBorder(border);
		specificSettingPanel.setLayout(new BoxLayout(specificSettingPanel, BoxLayout.Y_AXIS));
		
		JPanel[] sList = new JPanel[fr.getSpecificSubjects().length];
		JLabel[] sLabel = new JLabel[fr.getSpecificSubjects().length];
		sSpinner = new JSpinner[fr.getSpecificSubjects().length];
		
		for(int i = 0; i < fr.getSpecificSubjects().length; i++) {
			sList[i] = new JPanel();
			sList[i].setLayout(new FlowLayout(FlowLayout.LEFT));
			sLabel[i] = new JLabel(fr.getSpecificSubjects()[i].getName());
			SpinnerNumberModel numModel = new SpinnerNumberModel(0, 0, fr.getExamData(i).length, 1);
			sSpinner[i] = new JSpinner(numModel);
			sList[i].add(sLabel[i]);
			sList[i].add(sSpinner[i]);
			sList[i].setPreferredSize(new Dimension(60, 30));
			specificSettingPanel.add(sList[i]);
		}
		JScrollPane scroll = new JScrollPane(specificSettingPanel);
		scroll.setPreferredSize(new Dimension(200, 200));
		panel2.add(scroll);
		tabbedPane.addTab("일반", panel1);
		tabbedPane.addTab("문항수 설정", panel2);
		mainPanel.add(tabbedPane, BorderLayout.LINE_END);
		
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		add(mainPanel);
		pack();
		setVisible(true);
	}
	
	private Image getScaledImage(Image srcImg, int w, int h){
	    BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2 = resizedImg.createGraphics();

	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2.drawImage(srcImg, 0, 0, w, h, null);
	    g2.dispose();

	    return resizedImg;
	}

	class ComboBoxActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			if(arg0.getSource() == subjectList) {
				int num = subjectList.getSelectedIndex();
				specificSettingPanel.removeAll();
				fr.setSubject(num);
				
				JPanel[] sList = new JPanel[fr.getSpecificSubjects().length];
				JLabel[] sLabel = new JLabel[fr.getSpecificSubjects().length];
				sSpinner = new JSpinner[fr.getSpecificSubjects().length];
				for(int i = 0; i < fr.getSpecificSubjects().length; i++) {
					sList[i] = new JPanel();
					sList[i].setLayout(new FlowLayout(FlowLayout.LEFT));
					sLabel[i] = new JLabel(fr.getSpecificSubjects()[i].getName());
					
					SpinnerNumberModel numModel = new SpinnerNumberModel(0, 0, fr.getExamData(i).length, 1);
					sSpinner[i] = new JSpinner(numModel);
					sList[i].add(sLabel[i]);
					sList[i].add(sSpinner[i]);
					sList[i].setPreferredSize(new Dimension(60, 40));
					specificSettingPanel.add(sList[i]);
					
				}
				specificSettingPanel.setPreferredSize(new Dimension(200, fr.getSpecificSubjects().length * 40));
				specificSettingPanel.updateUI();
			}
			
			else if(arg0.getSource() == startB) {
				
				try {
					@SuppressWarnings("unused")
					int temp = getPort();
				} catch (Exception e) {
					// TODO: handle exception
					JOptionPane.showMessageDialog(frameCtrl, "포트 설정을 확인하세요", "포트 설정 오류", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				int[] testNum = new int[fr.getSpecificSubjects().length];
				int sum = 0;
				for(int i = 0; i < fr.getSpecificSubjects().length; i++) {
					testNum[i] = (int) sSpinner[i].getValue();
					sum += testNum[i];
				}
				
				if(sum == 0) {
					JOptionPane.showMessageDialog(frameCtrl, "문항 수를 설정하세요", "문항 수 설정 오류", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				data = new ExamData[sum];
				int curr = 0;
				
				Random rand = new Random();
				rand.setSeed(System.currentTimeMillis());
				
				for(int i = 0; i < fr.getSpecificSubjects().length; i++) {
					int[] arr = new int[testNum[i]];
					for(int j = 0; j < testNum[i]; j++) {
						arr[j] = rand.nextInt(100) % fr.getExamData(i).length;
						for(int k = 0; k < j; k++) {
							if(arr[k] == arr[j]) {
								j--;
								break;
							}
						}
						
					}
					
					for(int j = 0; j < testNum[i]; j++) {
						String q = fr.getExamData(i)[arr[j]].getAbsolutePath();
						data[curr] = new ExamData(q);
						curr++;
					}
				}
				
				shuffleArray(data);
				
				mainPanel.setVisible(false);
				
				TestStartPage startPanel = new TestStartPage(getTestName(), frameCtrl, curr, data, ipAddr, getPort());
				add(startPanel);
				startPanel.setVisible(true);
			}
		}
		
	}

	private static void shuffleArray(ExamData[] ar) {
		
		Random rnd = new Random();
		rnd.setSeed(System.currentTimeMillis());
		for (int i = ar.length - 1; i > 0; i--) {
			int index = rnd.nextInt(i + 1);
			ExamData a = ar[index];
			ar[index] = ar[i];
			ar[i] = a;
		}
	}

	public String getTestName() {
		return testNameField.getText();
	}
	
	public int getPort() {
		return Integer.parseInt(portInputField.getText());
	}
}

/*@SuppressWarnings("serial")
class MyNewFrame extends JFrame {
	public MyNewFrame() {
		// TODO Auto-generated constructor stub
		setTitle("TestServer");
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		//add(new mainPanel(this));
		pack();
		setVisible(true);
	}
}*/

public class NewWindow {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new mainPanel();
	}

}
