package service;


import com.intellij.execution.configuration.EnvironmentVariablesData;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(name = "XMakeProjectSetting")
public class XMakeService implements PersistentStateComponent<XMakeService.State> {


    static class State {
        public String currentPlatform;
        public String currentArchitecture = "";
        public String currentMode = "release";
        public String workingDirectory = "";
        public String androidNDKDirectory = "";
        public String buildOutputDirectory = "";
        public boolean verboseOutput = false;
        public String additionalConfiguration = "";
    }

    @Nullable
    @Override
    public XMakeService.State getState() {
        return null;
    }

    @Override
    public void loadState(@NotNull XMakeService.State state) {

    }
}
