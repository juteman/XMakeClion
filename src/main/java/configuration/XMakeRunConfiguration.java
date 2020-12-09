package configuration;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configuration.EnvironmentVariablesData;
import com.intellij.execution.configurations.*;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import service.XMakeService;

import java.util.ArrayList;
import java.util.Arrays;

public class XMakeRunConfiguration extends RunConfigurationBase {

    private String runTarget;
    private String runArguments;
    private EnvironmentVariablesData runEnvironment;
    private Project project;

    protected XMakeRunConfiguration(Project inProject, ConfigurationFactory factory, String name) {
        super(inProject, factory, name);
        // Init class field
        runTarget = "Default";
        runArguments = "";
        project = inProject;
    }

    public GeneralCommandLine getRunCommandLine() {
        var parameters = new ArrayList<String>();
        parameters.add("run");
        if (runTarget.equals("all")) {
            parameters.add("-a");
        } else if (!runTarget.equals("") && !runTarget.equals("default")) {
            parameters.add(runTarget);
        }
        if (!runArguments.equals("")) {
            var arguments = runArguments.split(" ");
            parameters.addAll(Arrays.asList(arguments));
        }

        return XMakeService.getXMakeService(project).makeCommandLine(parameters);
    }

    @NotNull
    @Override
    public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        return new XMakeSettingsEditor(project);
    }

//    @Override
//    public void writeExternal(@NotNull Element element) {
//        super.writeExternal(element);
//        element.setAttribute("runTarget", runTarget);
//        element.setAttribute("runArguments", runArguments);
//        runEnvironment.writeExternal(element);
//    }

    @Override
    public void checkConfiguration() throws RuntimeConfigurationException {

    }

    @Nullable
    @Override
    public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment executionEnvironment) throws ExecutionException {
        return null;
    }

    public String getRunTarget() {
        return runTarget;
    }

    public void setRunTarget(String runTarget) {
        this.runTarget = runTarget;
    }

    public String getRunArguments() {
        return runArguments;
    }

    public void setRunArguments(String runArguments) {
        this.runArguments = runArguments;
    }

    public EnvironmentVariablesData getRunEnvironment() {
        return runEnvironment;
    }

    public void setRunEnvironment(EnvironmentVariablesData runEnvironment) {
        this.runEnvironment = runEnvironment;
    }



}
