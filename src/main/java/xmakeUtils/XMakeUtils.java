package xmakeUtils;


import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.*;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
import java.util.Objects;
import java.util.regex.Pattern;

public class XMakeUtils {

    private static String xmakeProgram = "";


    public static String getXMakeProgram() {

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

    public static String getPlatform() {
        if (SystemInfo.isWindows) {
            return "windows";
        } else if (SystemInfo.isMac) {
            return "macosx";
        } else {
            return "linux";
        }
    }

    /**
     * run command with argument
     *
     * @param argv             argument list
     * @param workingDirectory command working directory
     * @return error code. 0 for ok.
     */
    public static int runCommand(@NotNull List<String> argv, @Nullable String workingDirectory) {
        int code = -1;

        var processBuilder = new ProcessBuilder(argv);

        if (workingDirectory != null) {
            processBuilder.directory(new File(workingDirectory));
        }

        try {
            var process = processBuilder.start();
            code = process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return code;
    }

    /**
     * run command get result
     *
     * @param argv             command line
     * @param workingDirectory run command line directory
     * @return return command line result
     */
    public static String runCommandResult(@NotNull List<String> argv, @Nullable String workingDirectory) {
        BufferedReader bufferedReader = null;
        StringBuilder result = new StringBuilder();

        var processBuilder = new ProcessBuilder(argv);

        if (workingDirectory != null) {
            processBuilder.directory(new File(workingDirectory));
        }

        processBuilder.environment().put("COLORTERM", "nocolor");

        try {
            var process = processBuilder.start();
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line = bufferedReader.readLine();
            while (line != null) {
                result.append(line).append("\n");
                line = bufferedReader.readLine();
            }

            if (process.waitFor() != 0) {
                result = new StringBuilder();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return result.toString();
    }

    public void setXmakeProgram(String inXMakeProgram) {
        xmakeProgram = inXMakeProgram;
    }


    /**
     * run command in idea console
     *
     * @param project      current project
     * @param commandLine  command line to run
     * @param showConsole  if show console when run
     * @param showProblem  if show problem when run
     * @param showExitCode if show exit state code
     * @return xmake console handler
     */
    public ProcessHandler runInConsole(Project project, GeneralCommandLine commandLine, Boolean showConsole, Boolean showProblem, Boolean showExitCode) {
        try {
            var handler = new XMakeConsoleHandler(XMakeToolWindowFactory.getXMakeConsoleView(project), commandLine, showExitCode);
            // if show console, make tool window show
            if (showConsole) {
                XMakeToolWindowFactory.getXMakeToolWindow(project).show(new Runnable() {
                    @Override
                    public void run() {
                        Objects.requireNonNull(XMakeToolWindowFactory.getXMakeToolWindowOutputPanel(project)).showPanel();
                    }
                });
            }

            if (showProblem) {
                // set XMake problem process adapter to listen
                // TODO implement XMakeProblemProcessAdapter
                handler.addProcessListener(new XMakeProblemProcessAdapter(handler, project));
            }

            handler.startNotify();

            return handler;
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }
}
