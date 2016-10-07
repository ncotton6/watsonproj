package watson.cleaner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;

/**
 * Parser - Loops through a directory searching for 
 * simple HTML files. Uses JSOUP to clean these
 * files to just simple HTML
 * @author Phil Lopez - pgl5711@rit.edu
 */
public class Parser 
{
	public static String origLoc;
	public static String outLoc;
	public static int numFiles = 0;
	public static int validHTML = 0;
	public static List<String> ignoreList =	new ArrayList<String>(
			Arrays.asList(new String[]{"pdf","jpg","png","gif"}));
	
    public static void main( String[] args ){    	
    	origLoc = args[0];
    	outLoc = args[1];
    	
    	File dir = new File(origLoc);

    	Parser p = new Parser();
    	long s = new Date().getTime();
    	p.parseFilesInDir(dir);
    	long e = new Date().getTime();
    	
    	System.out.println("Time taken: " + (e-s));
    	System.out.println("Found: " + numFiles);
    	System.out.println("Valid HTML: " + validHTML);
    }
    
    /**
     * Parses a directory for HTML and other folders
     * @param dir
     */
    public void parseFilesInDir(File dir){
    	for(File f : dir.listFiles()){
    		if(f.isDirectory()){
    			System.out.println("Parsing Directory: ");
    			parseFilesInDir(f);
    		}
    		else{
	    		numFiles++;
	    		System.out.println("Found file: " + f.getName());
	    		if(!ignoreFile(f)){
	    			saveCleanHTML(f);
	    		}
    		}
    	}
    }
    
    /**
     * Saves a given file to the output folder
     * after grabbing the "body" element via Jsoup
     * @param f
     */
    public void saveCleanHTML(File f){
		try {
			if(isHTMLFile(f)){
				Document doc = Jsoup.parse(f, "UTF-8");
				// Get the content of the body tag
				for(Element e : doc.getElementsByTag("body")){
					// Clean the HTML
					String cleanHTML = Jsoup.clean(e.html(), Whitelist.basic());
					File newFile = new File(getBasicFileName(f));
					PrintWriter pw = new PrintWriter(newFile);
					pw.write(cleanHTML);
					pw.flush();
					pw.close();
					validHTML++;
				}
			}
		} catch (IOException e) {
			System.out.println("JSOUP Err");
			e.printStackTrace();
		}
    }
       
    /**
     * Creates a unique output file name
     * @param f
     * @return
     */
    public String getBasicFileName(File f){
    	return outLoc + "\\" + f.getName() + "_" + validHTML + ".html";
    }
    
    /**
     * Creates a new file name based on the given old files name
     * @param f
     * @param isDir
     * @return
     */
    public String getNewFileName(File f, boolean isDir){
    	String fName = f.getAbsolutePath().replace(origLoc, outLoc);
    	if(isDir){
    		return fName;
    	}
    	return fName + ".html";
    }
    
    public void createNewDir(File f){
    	File newFile = new File(getNewFileName(f, true));
    	newFile.mkdir();
    }
    
    public boolean ignoreFile(File f){
    	String[] fn = f.getName().split("\\.");
    	if(fn.length > 1){
    		return ignoreList.contains(fn[fn.length-1].toLowerCase());
    	}
    	return false;
    }
    
    /**
     * Simply checks if a file contains "body" or "BODY"
     * @param f
     * @return
     */
    public boolean isHTMLFile(File f){
    	
    	try {
			BufferedReader reader = new BufferedReader(new FileReader(f));
			String result = "";
			String line;
			while((line = reader.readLine()) != null){
				result+=line;
			}
			reader.close();
			if(result.contains("body") || result.contains("BODY")){
				return true;
			}
			else{
				System.out.println("Invalid html");
				return false;
			}
		} 
    	catch (IOException e) {
			e.printStackTrace();
		}
    	return false;
    	
    }
}
