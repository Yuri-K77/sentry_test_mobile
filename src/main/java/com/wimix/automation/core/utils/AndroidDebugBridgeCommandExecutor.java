package com.wimix.automation.core.utils;

import com.wimix.automation.core.configuration.SentryConfig;
import lombok.SneakyThrows;

import java.io.*;

public class AndroidDebugBridgeCommandExecutor {

    /**
     * Extracts crash logs from android device/emulator according to package name and saves them into:
     * <pre>target/crash_log/crash_log.txt</pre>
     * <p> Each crash log is separated by some spaces from other one in file.
     * <p> If there were no crashes according to the package name, then the folder & file is not created.
     */
    public static void saveCrashLogToFile() {
        try {
            Process process = Runtime.getRuntime().exec("adb logcat *:E -d");

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            StringBuilder stringBuilder = new StringBuilder();
            boolean isCrashLine = false;

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                bufferedReader.mark(100000);

                if (line.contains("FATAL EXCEPTION: main") & !isCrashLine) {
                    if (bufferedReader.readLine().contains("Process: " + SentryConfig.getPackage())) {
                        isCrashLine = true;
                        bufferedReader.reset();
                    }
                }
                if (isCrashLine & line.contains("AndroidRuntime:")) {
                    stringBuilder
                            .append(line)
                            .append("\n");
                } else {
                    if (isCrashLine & !line.contains("AndroidRuntime:")) {
                        stringBuilder
                                .append("\n")
                                .append("\n");//line breaks between crashes
                    }
                    isCrashLine = false;
                }
            }

            bufferedReader.close();

            if (!stringBuilder.isEmpty()) {//if any data about the application crash is found(stringBuilder is NOT empty), then create new directory and file
                stringBuilder.setLength(stringBuilder.length() - 3);//delete empty lines in the end
                File crashLogDir = new File("target/crash_log");
                //noinspection ResultOfMethodCallIgnored
                crashLogDir.mkdir();
                FileWriter fileWriter = new FileWriter(new File(crashLogDir, "crash_log.txt").getAbsolutePath());
                fileWriter.write(String.valueOf(stringBuilder));
                fileWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    public static void setLocationEnabled(boolean status) {
        if (status) {
            Runtime.getRuntime().exec("adb shell settings put secure location_mode 3");
        } else {
            Runtime.getRuntime().exec("adb shell settings put secure location_mode 0");
        }
    }

    @SneakyThrows
    public static void hideKeyboard() {
        Runtime.getRuntime().exec("adb shell pm disable-user com.android.inputmethod.latin");
    }
}