package xmakeUtils;


import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.ProcessAdapter;
import com.intellij.execution.process.ProcessEvent;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import toolWindow.XMakeToolWindowFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.File;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class XMakeUtils {

    private String xmakeProgram = "";


    public String getXMakeProgram() {

        if (!xmakeProgram.equals("")) {
            return xmakeProgram;
        }


        // if platform Windows, return "xmake"
        if (SystemInfo.isWindows) {
            xmakeProgram = "xmake";
            return xmakeProgram;
        }

        // get possible xmake file location
        String homeEnv = "";
        if (System.getenv("HOME") != null) {
            homeEnv = System.getenv("HOME");
        }


        var programs = Arrays.asList("xmake", homeEnv + "/.local/bin/xmake", "/usr/local/bin/xmake", "/usr/bin/xmake");

        for (var program : programs) {
            Path path = Paths.get(program);
            if (program.equals("xmake") || Files.exists(path)) {
                // if has file xmake  check if has --version command
                ProcessBuilder processBuilder = new ProcessBuilder(List.of(program, "--version"));
                try {
                    var process = processBuilder.start();
                    if (process.waitFor() == 0) {
                        xmakeProgram = program;
                        break;
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }

        return xmakeProgram;
    }

    public void setXmakeProgram(String xmakeProgram) {
        this.xmakeProgram = xmakeProgram;
    }
}
