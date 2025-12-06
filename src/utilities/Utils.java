package utilities;

import java.io.File;

public class Utils {
	public static File check(String fileName) {
        // Basic null/empty validation
        if (fileName == null || fileName.isEmpty()) {
            System.out.println("Invalid file name.");
            return null;
        }

        // Clean input (remove quotes, trim spaces)
        fileName = fileName.replace("\"", "").trim();
        File file = new File(fileName);

        // 1. Check: current working directory
        if (!file.exists()) {

            // 2. Check relative to user working directory (IntelliJ / terminal)
            File userDirRelative = new File(System.getProperty("user.dir"), fileName);
            if (userDirRelative.exists())
                return userDirRelative;

            // 3. Check /res folder under working directory
            File resRelative = new File(System.getProperty("user.dir") + File.separator + "res", fileName);
            if (resRelative.exists())
                return resRelative;

            // 4. Check JAR execution directory (if running from a packaged jar)
            try {
                String jarDir = new File(Utils.class.getProtectionDomain()
                        .getCodeSource()
                        .getLocation()
                        .toURI())
                        .getParent();

                File jarRelative = new File(jarDir, fileName);
                if (jarRelative.exists())
                    return jarRelative;

            } catch (Exception e) {
                // JAR path may not be retrievable when running from an IDE
            }
        }

        // If still not found, print full absolute path for debugging
        if (!file.exists()) {
            System.out.println("❌ File not found: " + file.getAbsolutePath());
            return null;
        }

        return file;
    }
}
