package appDomain;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * Application entry point for the WordTracker program.
 *
 * <p>
 * Expected usage:
 * <pre>
 * java -jar WordTracker.jar &lt;input.txt&gt; -pf|-pl|-po [-f &lt;output.txt&gt;]
 * </pre>
 * where:
 * <ul>
 *   <li>{@code -pf} prints words with filenames only</li>
 *   <li>{@code -pl} prints words with filenames and line numbers</li>
 *   <li>{@code -po} prints words with filenames, line numbers and frequency</li>
 *   <li>{@code -f &lt;output.txt&gt;} optionally redirects the report to the given file</li>
 * </ul>
 * </p>
 *
 * <p>
 * The main method parses command-line arguments, loads the persistent tree
 * (if present), updates it from the provided input file, saves the tree back
 * to disk, and prints the requested report either to standard output or to a
 * specified output file.
 * </p>
 */
public class AppDriver {
	
	/**
     * Program entry point.
     *
     * <p>
     * This method parses the command line for:
     * <ul>
     *   <li>input filename (first token that does not start with "-")</li>
     *   <li>report flag: {@code -pf}, {@code -pl} or {@code -po}</li>
     *   <li>optional output directive: {@code -foutput.txt} or {@code -f output.txt}</li>
     * </ul>
     * It then runs the WordTracker load/construct/save cycle and prints the requested
     * report to either {@code System.out} or the user-specified file.
     * </p>
     *
     * @param args command-line arguments as described above
     */
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
