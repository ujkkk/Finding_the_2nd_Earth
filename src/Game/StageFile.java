package Game;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;

public class StageFile {
	
	public static File file;
	int n;
	String filePath;

	public StageFile() {
		openFile();
	}

	public void openFile() {
		file = new File("src/Game/stage.txt");
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
				n = Integer.parseInt(word.trim());
			}
			fscanner.close();
		} catch (FileNotFoundException e) {
		
			return;
		}
	}

		
	public void wirteFile(String word, File file) {
			
		FileWriter fw = null;
			
		try {			
			fw = new FileWriter(file, false);			
		} catch (IOException e1) {			
			e1.printStackTrace();			
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

