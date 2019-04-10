package fileIO;

import java.io.File;
import java.util.Arrays;

public class ReadAndSetData {
	
	String subject;
	int subDir;
	File[] subDirList;				//과목 폴더
	File[] specificSubject;			//세부 분야 폴더
	int specificSubjectidx;
	
	public ReadAndSetData() {
		// TODO Auto-generated constructor stub
		SearchDir();
	}

	private void SearchDir() {
		File path = new File("data/");
		if(path.exists() == false) {
			System.err.println("Dir Not Exist");
			return;
		}
		
		subDirList = path.listFiles();
		Arrays.sort(subDirList);
	}
	
	public File[] getSubjects() {
		return subDirList;
	}
	
	public void setSubject(int _subDir) {
		subDir = _subDir;
		specificSubject = subDirList[subDir].listFiles();
		Arrays.sort(specificSubject);
	}
	
	public File[] getSpecificSubjectList() {
		return specificSubject;
	}
	
	public void setSpecificSubject(int idx) {
		specificSubjectidx = idx;
	}
	
	public File[] getSpecificSubject() {
		File[] f = specificSubject[specificSubjectidx].listFiles();
		Arrays.sort(f);
		return f;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ReadAndSetData r = new ReadAndSetData();
		r.SearchDir();
	}
}
