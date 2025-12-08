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
	public static void main(String[] fakearg) {
		String[] args = {"java", "-jar" , "WordTracker.jar" , "test2.txt" , "-pf", "-foutput.txt "};
		WordTracker wordTracker = new WordTracker();
		
		if (args == null || args.length == 0) return;
		
		String inputFile = null;
		String reportFlag = null;
		String outputFile = null;

		for (int i = 0; i < args.length; i++) {
            String raw = args[i];
            if (raw == null) continue;
            String arg = raw.trim();

            // Normalize special characters to avoid parsing issues
            arg = arg.replace('–', '-')        // long dash
                     .replace('—', '-')        // em dash
                     .replace("\"", "")        // remove quotes
                     .replace("“", "")
                     .replace("”", "")
                     .trim()
                     .replace("\\", "/");      // unify slashes

            String lower = arg.toLowerCase();

            // Ignore arguments related to Java execution itself
            if (lower.equals("java") || lower.equals("-jar") || lower.endsWith(".jar")) {
                continue;
            }
            
            // report flags
            if (lower.equals("-pf") || lower.equals("-pl") || lower.equals("-po")) {
                reportFlag = lower;
                continue;
            }
            
            // -foutput.txt (no space)
            if (lower.startsWith("-f") && lower.length() > 2) {
                outputFile = raw.substring(2); // preserve original casing/path characters
                continue;
            }
            
            // -f output.txt (separate)
            if (lower.equals("-f")) {
                if (i + 1 < args.length) {
                    outputFile = args[i + 1];
                    i++; // skip next token
                }
                continue;
            }
            
         // anything that looks like a filename (not starting with -) is input
            if (!lower.startsWith("-") && inputFile == null) {
                inputFile = raw;
                continue;
            }
        }
            
            if (inputFile == null || reportFlag == null) {
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
                    File outFile = new File(outputFile.trim());
                    out = new PrintStream(new FileOutputStream(outFile, false), true);
                    toFile = true;
                }
                
             // generate the chosen report
                switch (reportFlag) {
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
                }
            } catch (FileNotFoundException e) {
                System.out.println("Unable to open output file: " + e.getMessage());
            } finally {
                if (toFile && out != null) out.close();
            }
	}
}
