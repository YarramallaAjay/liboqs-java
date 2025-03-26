package npci.cryptocore;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.Arrays;

public class Common {

    private static final String OS = System.getProperty("os.name").toLowerCase();

    public static void wipe(byte[] array) {
        Arrays.fill(array, (byte) 0);
    }

    public static boolean isWindows() {
        return OS.contains("win");
    }

    public static boolean isMac() {
        return OS.contains("mac");
    }

    public static boolean isLinux() {
        return OS.contains("nux");
    }

    public static void loadNativeLibrary() {
    try {
        System.loadLibrary("oqs-jni");
        System.out.println("Loaded native lib using System.loadLibrary");
    } catch (UnsatisfiedLinkError e) {
        System.err.println("⚠️ System.loadLibrary failed: " + e.getMessage());

        String libName = "liboqs-jni.so";
        if (isMac()) {
            libName = "liboqs-jni.jnilib";
        } else if (isWindows()) {
            libName = "oqs-jni.dll";
        }

        // Step 2: Try loading from ${LIBS_DIR}
        String libsDir = System.getenv("LIBS_DIR"); // or System.getProperty("libs.dir");
        if (libsDir != null) {
            File libFromEnv = new File(libsDir, libName);
            if (libFromEnv.exists()) {
                try {
                    System.load(libFromEnv.getAbsolutePath());
                    System.out.println("Loaded native lib from LIBS_DIR: " + libFromEnv.getAbsolutePath());
                    return;
                } catch (UnsatisfiedLinkError ex) {
                    System.err.println("Failed to load native lib from LIBS_DIR: " + ex.getMessage());
                }
            } else {
                System.err.println("LIBS_DIR set, but file not found at: " + libFromEnv.getAbsolutePath());
            }
        } else {
            System.err.println("LIBS_DIR environment variable not set.");
        }

        // Step 3: Fallback — extract from JAR
        try {
            URL url = KEMs.class.getResource("/" + libName);
            if (url != null) {
                File tmpDir = Files.createTempDirectory("oqs-native-lib").toFile();
                tmpDir.deleteOnExit();

                File nativeLibTmpFile = new File(tmpDir, libName);
                nativeLibTmpFile.deleteOnExit();

                try (InputStream in = url.openStream()) {
                    Files.copy(in, nativeLibTmpFile.toPath());
                    System.load(nativeLibTmpFile.getAbsolutePath());
                    System.out.println("Loaded native lib from JAR resource: " + nativeLibTmpFile.getAbsolutePath());
                }
            } else {
                System.err.println("Native lib resource not found in JAR: /" + libName);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}


    public static <E, T extends Iterable<E>> void print_list(T list) {
        for (Object element : list){
            System.out.print(element);
            System.out.print(" ");
        }
        System.out.println();
    }

    public static String to_hex(byte[] bytes) {
        final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            int v = aByte & 0xFF;
            sb.append(HEX_ARRAY[v >>> 4]);
            sb.append(HEX_ARRAY[v & 0x0F]);
            sb.append(" ");
        }
        return sb.toString();
    }

    public static String chop_hex(byte[] bytes) {
        final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder();
        int num = 8;
        for (int i = 0; i < num; i++) {
            int v = bytes[i] & 0xFF;
            sb.append(HEX_ARRAY[v >>> 4]);
            sb.append(HEX_ARRAY[v & 0x0F]);
            sb.append(" ");
        }
        if (bytes.length > num*2) {
            sb.append("... ");
        }
        for (int i = bytes.length - num; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            sb.append(HEX_ARRAY[v >>> 4]);
            sb.append(HEX_ARRAY[v & 0x0F]);
            sb.append(" ");
        }
        return sb.toString();
    }

}
