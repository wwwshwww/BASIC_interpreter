package newLang1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Main1 {

	public static void main(String[] args) {
		String path = "test1.bas";
		if(args.length > 0) {
			path = args[0];
		}
		
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path)), StandardCharsets.UTF_8));) {
			String str;
			List<String> lines = new ArrayList<>();
			while((str = reader.readLine()) != null) {
				lines.add(str);
				System.out.println(str);
			}
		}catch(FileNotFoundException e) {
			System.out.println(path + " : not found");
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

}
