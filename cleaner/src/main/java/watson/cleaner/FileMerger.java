package watson.cleaner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Combines all of the files in the given directory
 * into one single output file
 *
 */
public class FileMerger {

	public static void main(String args[]) throws IOException{
		
		String dirFrom = args[0];
		String outFileName = args[1];
		
		File dir = new File(dirFrom);
		PrintWriter out = new PrintWriter(new File(outFileName));
		
		for(File f : dir.listFiles()){
			BufferedReader reader = new BufferedReader(new FileReader(f));
			String line;
			while((line = reader.readLine()) != null){
				if(line.contains("jquery")){
					System.out.println(f.getName());
				}
				out.println(line);
			}
			reader.close();
		}
		out.flush();
		out.close();
	}
}
