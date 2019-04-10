package fileIO;

import java.io.File;

//나중에 Real을 지우고 앞에 public을 붙여 정식 클래스가 되고자 하는 야망을 가진 클래스
public class FileReader {
	
	ReadAndSetData dataManager;
	File[] exams;
	public FileReader() {
		// TODO Auto-generated constructor stub
		dataManager = new ReadAndSetData();
		dataManager.setSubject(0);						//과목의 인텍스 설정
		//dataManager.setSpecificSubject(0);
		
		exams = dataManager.getSpecificSubject();
	}
	
	public File[] getSubjects() {
		return dataManager.getSubjects();
	}
	
	public void setSubject(int i) {
		dataManager.setSubject(i);
	}
	
	public File[] getSpecificSubjects() {
		return dataManager.getSpecificSubjectList();
	}
	
	public File[] getExamData(int i) {
		dataManager.setSpecificSubject(i);
		exams = dataManager.getSpecificSubject();
		return exams;
	}
}
