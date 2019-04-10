package fileIO;

import java.io.File;

//���߿� Real�� ����� �տ� public�� �ٿ� ���� Ŭ������ �ǰ��� �ϴ� �߸��� ���� Ŭ����
public class FileReader {
	
	ReadAndSetData dataManager;
	File[] exams;
	public FileReader() {
		// TODO Auto-generated constructor stub
		dataManager = new ReadAndSetData();
		dataManager.setSubject(0);						//������ ���ؽ� ����
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
