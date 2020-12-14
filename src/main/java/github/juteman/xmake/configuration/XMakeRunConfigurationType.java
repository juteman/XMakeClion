package github.juteman.xmake.configuration;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

import github.juteman.xmake.icons.XMakeIcons;

public class XMakeRunConfigurationType implements ConfigurationType {
    @NotNull
    @Override
    public String getDisplayName() {
        return "XMake";
    }

    @Override
    public String getConfigurationTypeDescription() {
        return "XMake run github.juteman.xmake.configuration type";
    }

    @Override
    public Icon getIcon() {
        return XMakeIcons.setting;
    }

    @NotNull
    @Override
    public String getId() {
        return "XMakeConfiguration";
    }

    @Override
    public ConfigurationFactory[] getConfigurationFactories() {
        return new ConfigurationFactory[]{new XMakeRunConfigurationFactory(this)};
    }
}
