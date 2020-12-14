package github.juteman.xmake.action;

import com.intellij.execution.process.ProcessAdapter;
import com.intellij.execution.process.ProcessEvent;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import github.juteman.xmake.service.XMakeService;
import github.juteman.xmake.toolWindow.XMakeToolWindowFactory;
import github.juteman.xmake.xmakeUtils.XMakeUtils;

import java.util.Objects;


public class BuildAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Project currentProject = event.getProject();
        if (currentProject == null) {
            return;
        }
        XMakeToolWindowFactory.getXMakeConsoleView(currentProject).clear();

        var xmakeService = XMakeService.getInstance(currentProject);
        if (Objects.requireNonNull(xmakeService).getChanged()) {
            Objects.requireNonNull(XMakeUtils.runInConsole(currentProject, xmakeService.getConfigurationCommandLine(), false, false, false))
                    .addProcessListener(new ConfigChangedProcessAdapter(currentProject));
            xmakeService.setChanged(false);
        } else {
            XMakeUtils.runInConsole(currentProject, xmakeService.getBuildCommandLine(), true, true, true);
        }

    }

    private class ConfigChangedProcessAdapter extends ProcessAdapter {
        private final Project project;

        public ConfigChangedProcessAdapter(Project project) {
            this.project = project;
        }

        @Override
        public void processTerminated(@NotNull ProcessEvent event) {
            var xmakeService = XMakeService.getInstance(project);
            XMakeUtils.runInConsole(project, xmakeService.getBuildCommandLine(), false, true, true);
        }
    }
}
