package fileIO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Random;
import java.util.Scanner;

public class ExamData {
	Scanner s = null;
	private String problem = null;
	private String[] wAnswers = new String[4];
	private String answer = null;
	private String picPath = null;
	
	int numCount = 0;
	int[] numAnswers;
	
	public ExamData(String path) {
		// TODO Auto-generated constructor stub
		try {
			s = new Scanner(new BufferedReader(new FileReader(path)));
			s.useDelimiter("/");
			
			problem = s.next();
			problem = problem.replace('\n', ' ');
			problem = problem.trim();
			problem = problem.replace("\r", "-*nr-");
			
			
			while(s.hasNext()) {
				String temp = s.next();
				if(temp.charAt(0) == '-') {
					wAnswers[numCount] = temp.substring(1) + "";
					wAnswers[numCount] = wAnswers[numCount].replace('\n', ' ');
					wAnswers[numCount] = wAnswers[numCount].trim();
					numCount++;
					
				}
				
				else if(temp.charAt(0) == '=') {
					answer = temp.substring(1) + "";
					answer = answer.replace('\n', ' ');
					answer = answer.trim();
				}
				else if(temp.substring(0, 4).compareTo("pic=") == 0) {
					picPath = temp.substring(4);
					picPath = picPath.replace('\n', ' ');
					picPath = picPath.trim();
				}
			}
			
			//¹®Á¦ ¼¯±â
			Random rand = new Random();
			rand.setSeed(System.currentTimeMillis());
			
			numAnswers = new int[numCount + 1];
			for(int i = 0; i < numCount + 1; i++) {
				numAnswers[i] = rand.nextInt(1000) % (numCount + 1);
				
				for(int j = 0; j < i; j++) {
					if(numAnswers[i] == numAnswers[j]) {
						numAnswers[i] = rand.nextInt(1000) % (numCount + 1);
						j = -1;
					}
				}
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
	}
	
	public String getProblem() {
		return this.problem;
	}
	
	public String getNumbers(int i) {
		if (numAnswers[i] == 0) return answer;
		else return this.wAnswers[numAnswers[i] - 1];
	}
	
	public int isCorrect(int i) {
		if(i == -1) return 0;
		if (numAnswers[i] == 0) return 1;
		else return 0;
	}
	
	public String getPicturePath() {
		if(picPath == null) return "null";
		if(new File("./picture/" + picPath).exists())
			return "./picture/" + picPath;
		
		else return "null";
	}
	
	public int HowManyProblems() {
		return numCount + 1;
	}
}
