package XMLcreate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;

public class XMLFile {
	
	public static File file;
	private Vector<String> words = new Vector<String>();
	String filePath;

	public XMLFile(String filePath) {
		openFile(filePath);
	}



	public void openFile(String filePath) {
		file = new File(filePath);
			try {
				boolean success = file.createNewFile();
			} catch (IOException e) {
				return;
		}
	}
		//파일의 단어를 읽는 함수
	public void readFile() {
		try {
			Scanner fscanner = new Scanner(new
			FileReader(file));
			while(fscanner.hasNext()) {
				String word = fscanner.nextLine();
				words.add(word.trim());
			}
			fscanner.close();
		} catch (FileNotFoundException e) {
		
			return;
		}
	}

	public void wirteFile(String word, File file, boolean flag) {
			
		FileWriter fw = null;
			
		if(flag) {
			try {			
				fw = new FileWriter(file, true);			
			} catch (IOException e1) {			
				e1.printStackTrace();			
			}
		}
		
		else {
			try {			
				fw = new FileWriter(file, false);			
			} catch (IOException e1) {			
				e1.printStackTrace();			
			}
		}
		try {
			
			fw.write(word + '\n');
			fw.flush();					
			fw.close();
			
		} catch (IOException e) {
			return;
		}
	}

}

