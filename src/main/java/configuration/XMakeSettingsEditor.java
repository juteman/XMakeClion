package configuration;

import com.intellij.execution.configuration.EnvironmentVariablesComponent;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.ui.ComboBox;
import org.jetbrains.annotations.NotNull;
import com.intellij.ui.RawCommandLineEditor;
import javax.swing.*;

public class XMakeSettingsEditor extends SettingsEditor<XMakeRunConfiguration> {

    private final DefaultComboBoxModel<String> targetsModel= new DefaultComboBoxModel<>();
    private final ComboBox<String> targetsComboBox = new ComboBox<>(targetsModel);
    private final RawCommandLineEditor runArguments = new RawCommandLineEditor();
    private final EnvironmentVariablesComponent environmentVariables = new EnvironmentVariablesComponent();


    @Override
    protected void resetEditorFrom(@NotNull XMakeRunConfiguration configuration) {
        targetsModel.removeAllElements();
    }

    @Override
    protected void applyEditorTo(@NotNull XMakeRunConfiguration configuration) throws ConfigurationException {
        configuration.setRunTarget(targetsModel.getSelectedItem().toString());
        configuration.setRunArgument(runArguments.getText());
        configuration.setRunEnvironment(environmentVariables.getEnvData());
    }

    @Override
    @NotNull
    protected JComponent createEditor() {
        return new JPanel();
    }
    
}
