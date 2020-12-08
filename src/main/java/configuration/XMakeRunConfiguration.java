package configuration;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configuration.EnvironmentVariablesData;
import com.intellij.execution.configurations.*;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class XMakeRunConfiguration extends RunConfigurationBase {

    private String runTarget;
    private String runArgument;
    private EnvironmentVariablesData runEnvironment;

    protected XMakeRunConfiguration(Project project, ConfigurationFactory factory, String name) {
        super(project, factory, name);
        // Init class field
        runTarget = "Default";
        runArgument = "";
        runEnvironment = EnvironmentVariablesData.DEFAULT;
    }

    @NotNull
    @Override
    public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        return new XMakeSettingsEditor();
    }

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

    public String getRunArgument() {
        return runArgument;
    }

    public void setRunArgument(String runArgument) {
        this.runArgument = runArgument;
    }

    public EnvironmentVariablesData getRunEnvironment() {
        return runEnvironment;
    }

    public void setRunEnvironment(EnvironmentVariablesData runEnvironment) {
        this.runEnvironment = runEnvironment;
    }
}
