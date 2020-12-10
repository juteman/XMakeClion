package service;


import com.intellij.execution.configuration.EnvironmentVariablesData;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xmakeUtils.XMakeUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@State(name = "XMakeProjectSetting", storages = {@com.intellij.openapi.components.Storage("$WORKSPACE_FILE$")})
public class XMakeService implements PersistentStateComponent<XMakeService.State> {

    static class State {
        public String currentPlatform = XMakeUtils.getPlatform();
        public String currentArchitecture = "";
        public String currentMode = "release";
        public String workingDirectory = "";
        public String androidNDKDirectory = "";
        public String buildOutputDirectory = "";
        public boolean verboseOutput = false;
        public String additionalConfiguration = "";
    }

    private final Project project;
    private final List<String> platforms;
    private final List<String> architectures;
    private final List<String> modes;
    private GeneralCommandLine buildCommandLine;
    private boolean isChanged;
    State data = new State();


    public XMakeService(Project inProject) {
        project = inProject;
        platforms = Arrays.asList("macosx", "linux", "windows", "android", "iphoneos", "watchos", "mingw");
        modes = Arrays.asList("release", "debug");
        architectures = getArchitecturesByPlatform(data.currentPlatform);
        isChanged = false;
        ensureState();
    }

    @Nullable
    @Override
    public XMakeService.State getState() {
        return data;
    }

    private void ensureState() {
        if (data.workingDirectory.equals("")) {
            data.workingDirectory = project.getBasePath();
        }
        if (data.currentArchitecture.equals("") && !architectures.isEmpty()) {
            data.currentArchitecture = architectures.get(0);
        }
    }

    @Override
    public void loadState(@NotNull XMakeService.State state) {
        if (data != state) {
            data = state;
            isChanged = true;
        }
        ensureState();
    }


    public GeneralCommandLine getBuildCommandLine() {
        var parameters = new ArrayList<String>();
        if (data.verboseOutput) {
            parameters.add("-v");
        } else {
            parameters.add("-w");
        }
        return makeCommandLine(parameters);
    }

    public GeneralCommandLine getReBuildCommandLine() {
        var parameters = new ArrayList<String>();
        parameters.add("-r");
        if (data.verboseOutput) {
            parameters.add("-v");
        } else {
            parameters.add("-w");
        }
        return makeCommandLine(parameters);
    }

    public GeneralCommandLine getCleanCommandLine() {
        var parameters = new ArrayList<String>();
        parameters.add("c");
        if (data.verboseOutput) {
            parameters.add("-v");
        }
        return makeCommandLine(parameters);
    }

    public GeneralCommandLine getCleanConfigurationCommandLine() {
        var parameters = new ArrayList<String>();
        parameters.add("f");
        parameters.add("-c");
        if (data.verboseOutput) {
            parameters.add("-v");
        }
        if (!data.buildOutputDirectory.equals("")) {
            parameters.add("-o");
            parameters.add(data.buildOutputDirectory);
        }
        return makeCommandLine(parameters);
    }

    public GeneralCommandLine getConfigurationCommandLine() {
        var parameters = new ArrayList<String>(Arrays.asList("f", "-p", data.currentPlatform, "-a", data.currentArchitecture, "-m", data.currentMode));
        if (data.currentPlatform.equals("android") && !data.androidNDKDirectory.equals("")) {
            parameters.add("--ndk=\"${data.androidNDKDirectory}\"");
        }
        if (data.verboseOutput) {
            parameters.add("-v");
        }
        if (!data.buildOutputDirectory.equals("")) {
            parameters.add("-o");
            parameters.add(data.buildOutputDirectory);
        }
        return makeCommandLine(parameters);
    }

    public GeneralCommandLine getQuickStartCommandLine() {
        var parameters = new ArrayList<String>(Arrays.asList("f", "-y"));
        if (data.verboseOutput) {
            parameters.add("-v");
        }

        return makeCommandLine(parameters);
    }

    public GeneralCommandLine makeCommandLine(List<String> parameters) {
        return new GeneralCommandLine(XMakeUtils.getXMakeProgram()).withParameters(parameters)
                .withCharset(StandardCharsets.UTF_8)
                .withWorkDirectory(data.workingDirectory)
                .withEnvironment(EnvironmentVariablesData.DEFAULT.getEnvs())
                .withRedirectErrorStream(true);
    }

    public List<String> getTargets() {
        var targets = Arrays.asList("default", "all");
        var results = XMakeUtils.runCommandResult(Arrays.asList(XMakeUtils.getXMakeProgram(), "l", "-c",
                "import(\"core.project.config\"); " +
                        "import(\"core.project.project\"); " +
                        "config.load(); for name, _ in pairs((project.targets())) do print(name) end"),
                data.workingDirectory);
        var resultLine = results.split("\n");
        for (var line : resultLine) {
            if (!line.trim().equals("")) {
                targets.add(line);
            }
        }

        return targets;
    }

    private List<String> getArchitecturesByPlatform(String platform) {
        switch (platform) {
            case "macosx":
            case "linux":
            case "mingw":
                return Arrays.asList("x86_64", "i386");
            case "windows":
                return Arrays.asList("x86", "x64");
            case "iphoneos":
                return Arrays.asList("arm64", "armv7", "armv7s", "x86_64", "i386");
            case "watchos":
                return Arrays.asList("armv7s", "i386");
            case "android":
                return Arrays.asList("armv7-a", "armv5te", "armv6", "armv8-a", "arm64-v8a");
            default:
                return Collections.emptyList();
        }
    }

    @Nullable
    public static XMakeService getInstance(Project inProject) {
        return ServiceManager.getService(inProject, XMakeService.class);
    }


}
