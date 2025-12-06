package appDomain;

import java.io.File;

import utilities.Utils;

public class AppDriver {
	public static void main(String[] fakearg) {
		String[] args = {"java", "-jar" , "WordTracker.jar" , "test1.txt" , "-pf", "-foutput.txt "};
		File file = null;
		WordTracker wordTracker = new WordTracker();
		
		if (args == null || args.length == 0) return;

        for (String arg : args) {
            if (arg == null || arg.isEmpty()) continue;

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
            if (lower.equals("java") || lower.equals("-jar") || lower.endsWith(".jar"))
                continue;
            
            if (lower.startsWith("-p") || lower.startsWith("-f")) {
            	continue;
            }
            
            if (lower.endsWith("txt") || !lower.startsWith("-f")) {
            	file = Utils.check(lower);
            	
            	wordTracker.loadTree();
            	
            	wordTracker.constructsFromFile(file);
            	
            	wordTracker.saveTree();
            }
        }
	}
}
