package configuration;

import com.intellij.execution.configuration.EnvironmentVariablesComponent;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.layout.CCFlags;
import org.jetbrains.annotations.NotNull;
import com.intellij.ui.RawCommandLineEditor;
import service.XMakeService;

import javax.swing.*;

public class XMakeSettingsEditor extends SettingsEditor<XMakeRunConfiguration> {

    private final DefaultComboBoxModel<String> targetsModel;
    private final RawCommandLineEditor runArguments;
    private final EnvironmentVariablesComponent environmentVariables;
    private Project project;
    private JPanel xmakePanel;
    private ComboBox<String> targetsComboBox;

    public XMakeSettingsEditor(Project inProject)
    {
        super();
        project = inProject;
        targetsModel = new DefaultComboBoxModel<>();
        runArguments =  new RawCommandLineEditor();
        environmentVariables = new EnvironmentVariablesComponent();

    }


    @Override
    protected void resetEditorFrom(@NotNull XMakeRunConfiguration configuration) {
        targetsModel.removeAllElements();
        var xmakeService = XMakeService.getXMakeService(project);
        for (var target : xmakeService.getTargets()) {
            targetsModel.addElement(target);
        }
        targetsModel.setSelectedItem(configuration.getRunTarget());

        // reset run arguments
        runArguments.setText(configuration.getRunArguments());

        // reset environment variables
        environmentVariables.setEnvData(configuration.getRunEnvironment());
    }

    @Override
    protected void applyEditorTo(@NotNull XMakeRunConfiguration configuration) throws ConfigurationException {
        configuration.setRunTarget(targetsModel.getSelectedItem().toString());
        configuration.setRunArguments(runArguments.getText());
        configuration.setRunEnvironment(environmentVariables.getEnvData());
    }

    @Override
    @NotNull
    protected JComponent createEditor() {
        return xmakePanel;
    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
        var xmakeService = XMakeService.getXMakeService(project);
        for (var target : xmakeService.getTargets()) {
            targetsModel.addElement(target);
        }
        targetsComboBox = new ComboBox<>(targetsModel);
    }
}
