package tameshi;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;

import newlang3.LexicalType;
import newlang3.LexicalUnit;
import newlang3.ValueImpl;
import newlang3.ValueType;

public class PushbackTameshi {
	public static void main(String[] args) throws IOException {
		String path = "test1.bas";
		if (args.length > 0) {
			path = args[0];
		}

		Reader fr = null;
		PushbackReader pbr = null;
		try {
			fr = new FileReader(path);
		} catch (FileNotFoundException e) {
			System.out.println(path + " : not found");
			System.exit(-1);
		}
		pbr = new PushbackReader(fr);

		for (int i = 0; i < 5; i++) {
			String target = "";
			while (true) {
				int ci = pbr.read();
				if (ci < 0)
					break;
				char c = (char) ci;
				if (ci > 0 && (c >= '0' && c <= '9')) {
					target += c;
					continue;
				}
				if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
					target += c;
					continue;
				}
				//pbr.unread(ci);
				//pbr.read();
				break;
			}
			
			System.out.println(new LexicalUnit(LexicalType.NAME, new ValueImpl(target, ValueType.STRING)));
			
		}
		
		for(int i=0; i<6; i++) {
			int aaa = pbr.read();
			char ch = (char)aaa;
			if(ch == ' ') {
				System.out.println("aaaaaaaaaa!");
			}else {
				System.out.println(ch);
			}
		}

		while (true) {
			int ci;
			try {
				ci = fr.read();
			} catch (IOException e) {
				System.out.println("io error");
				break;
			}

			if (ci == -1)
				break;
			System.out.print((char) ci);
		}

		if (fr != null) {
			try {
				fr.close();
			} catch (IOException e) {

			}
		}

	}
}
