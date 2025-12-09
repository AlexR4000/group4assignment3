package appDomain;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * Application entry point. Parses command-line arguments (input file,
 * report option -pf/-pl/-po, optional -f output file) and runs the
 * WordTracker process.
 */
public class AppDriver {
	public static void main(String[] args) {
//		String[] args = {"java", "-jar" , "WordTracker.jar" , "test2.txt" , "-pf", "-foutput.txt "};
		if (args == null || args.length == 0) {
            System.out.println("Usage: java -jar WordTracker.jar <input.txt> -pf|-pl|-po [-f <output.txt>]");
            return;
        }

		String inputFile = null;
		String modeFlag = null;
		String outputFile = null;

		for (int i = 0; i < args.length; i++) {
            if (args[i] == null) continue;
            String token = args[i].trim();

            // Normalize special characters to avoid parsing issues
            token = token.replace('–', '-')        // long dash
                     .replace('—', '-')        // em dash
                     .replace("\"", "")        // remove quotes
                     .replace("“", "")
                     .replace("”", "")
                     .trim()
                     .replace("\\", "/");      // unify slashes

            String lower = token.toLowerCase();

            // Ignore arguments related to Java execution itself
            if (lower.equals("java") || lower.equals("-jar") || lower.endsWith(".jar")) {
                continue;
            }
            
            // report flags
            if (lower.equals("-pf") || lower.equals("-pl") || lower.equals("-po")) {
            	modeFlag = lower;
                continue;
            }
            
         // -foutput.txt or -f output.txt
            if (lower.startsWith("-f")) {
                if (lower.length() > 2) {
                    outputFile = token.substring(2);
                } else {
                    // token is exactly "-f", look ahead
                    if (i + 1 < args.length) {
                        outputFile = args[++i];
                    }
                }
                continue;
            }
            
         // anything that looks like a filename (not starting with -) is input
            if (!lower.startsWith("-") && inputFile == null) {
                inputFile = token;
                continue;
            }
        }
            
            if (inputFile == null || modeFlag == null) {
                System.out.println("Missing required arguments.");
                System.out.println("Usage: java -jar WordTracker.jar <input.txt> -pf|-pl|-po [-f<output.txt>]");
                return;
            }
            
            WordTracker tracker = new WordTracker();
            	
        	tracker.loadTree();
        	
        	tracker.constructsFromFile(inputFile);
        	
        	tracker.saveTree();
        	
        	// Prepare output stream: either System.out or a file
            PrintStream out = System.out;
            boolean toFile = false;
            try {
                if (outputFile != null && !outputFile.trim().isEmpty()) {
                    out = new PrintStream(new FileOutputStream(outputFile, false), true);
                    toFile = true;
                }
                
             // generate the chosen report
                switch (modeFlag) {
                    case "-pf":
                        tracker.generateReport("pf", out);
                        break;
                    case "-pl":
                        tracker.generateReport("pl", out);
                        break;
                    case "-po":
                        tracker.generateReport("po", out);
                        break;
                    default:
                        System.out.println("Unknown report flag.");
                        break;
                }
            } catch (FileNotFoundException e) {
                System.out.println("Unable to open output file: " + e.getMessage());
                e.printStackTrace();
            } finally {
                if (toFile && out != null) out.close();
            }
	}
}
