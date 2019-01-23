package newlang3;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Main {

	public static void main(String[] args) throws Exception {
		String path = "test2.bas";
		if(args.length > 0) {
			path = args[0];
		}
		
//		Reader fr = null;
//		try {
//			fr = new FileReader(path);
//		}catch(FileNotFoundException e) {
//			System.out.println(path + " : not found");
//			System.exit(-1);
//		}
//		
//		while(true) {
//			int ci;
//			try {
//				ci = fr.read();
//			}catch(IOException e) {
//				System.out.println("io error");
//				break;
//			}
//			
//			if(ci == -1) break;
//			System.out.print((char)ci);
//		}
//		
//		if(fr != null) {
//			try {
//				fr.close();
//			}catch(IOException e) {
//				
//			}
//		}
//		System.out.println("\n");
		
		InputStream in = null;
		try {
			in = new FileInputStream(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		LexicalAnalyzerImpl ana = new LexicalAnalyzerImpl(in);
		while(true) {
			LexicalUnit unit = ana.get();
			System.out.println(unit);
			if(unit.getType() == LexicalType.EOF) break;
		}
		
	}	
}
