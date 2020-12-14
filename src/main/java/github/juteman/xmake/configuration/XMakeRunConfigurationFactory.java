package github.juteman.xmake.configuration;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class XMakeRunConfigurationFactory extends ConfigurationFactory {
    private static final String FACTORY_NAME = "XMake github.juteman.xmake.configuration factory";

    protected XMakeRunConfigurationFactory(ConfigurationType type) {
        super(type);
    }

    @NotNull
    @Override
    public RunConfiguration createTemplateConfiguration(@NotNull Project project) {
        return new XMakeRunConfiguration(project, this, "XMake");
    }

    @NotNull
    @Override
    public String getName() {
        return FACTORY_NAME;
    }
}
