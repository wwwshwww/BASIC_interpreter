package tameshi;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class ExMain01 {

	public static void main(String[] args) {
		String path = "test1.bas";
		if(args.length > 0) {
			path = args[0];
		}
		
		Reader fr = null;
		try {
			fr = new FileReader(path);
		}catch(FileNotFoundException e) {
			System.out.println(path + " : not found");
			System.exit(-1);
		}
		
		while(true) {
			int ci;
			try {
				ci = fr.read();
			}catch(IOException e) {
				System.out.println("io error");
				break;
			}
			
			if(ci == -1) break;
			System.out.print((char)ci);
		}
		
		if(fr != null) {
			try {
				fr.close();
			}catch(IOException e) {
				
			}
		}

	}

}
