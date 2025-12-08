package appDomain;

public class AppDriver {
	public static void main(String[] fakearg) {
		String[] args = {"java", "-jar" , "WordTracker.jar" , "test2.txt" , "-pf", "-foutput.txt "};
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
            	
            	wordTracker.loadTree();
            	
            	wordTracker.constructsFromFile(lower);
            	
            	wordTracker.saveTree();
            }
        }
	}
}
