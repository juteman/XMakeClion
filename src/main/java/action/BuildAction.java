package action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import service.XMakeService;
import toolWindow.XMakeToolWindowFactory;

public class BuildAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Project currentProject =  event.getProject();
        if(currentProject == null)
        {
            return;
        }
        XMakeToolWindowFactory.getXMakeConsoleView(currentProject).clear();

        var xmakeService = XMakeService.getInstance(currentProject);
        var commandLine = xmakeService.getBuildCommandLine();

        var cleanCommand = xmakeService.getCleanCommandLine();
    }
}
