package configuration;

import com.intellij.execution.configuration.EnvironmentVariablesComponent;
import com.intellij.execution.configuration.EnvironmentVariablesData;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.components.JBLabel;
import org.jetbrains.annotations.NotNull;
import com.intellij.ui.RawCommandLineEditor;
import org.jetbrains.annotations.Nullable;
import service.XMakeService;

import javax.swing.JPanel;
import javax.swing.*;

public class XMakeSettingsEditor extends SettingsEditor<XMakeRunConfiguration> {

    private DefaultComboBoxModel<String> targetsModel;
    private RawCommandLineEditor runArguments;
    private EnvironmentVariablesComponent environmentVariables;
    private Project project;
    private JPanel xmakePanel;
    private JBLabel targetLabel;
    private ComboBox<String> targetsComboBox;
    private XMakeService xmakeService;

    public XMakeSettingsEditor(Project inProject) {
        super();
        project = inProject;
        xmakeService = XMakeService.getInstance(project);
    }


    @Override
    protected void resetEditorFrom(@NotNull XMakeRunConfiguration configuration) {
        targetsModel.removeAllElements();
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
        targetsModel = new DefaultComboBoxModel<>();
        targetsComboBox = new ComboBox<>(targetsModel);
    }


}
