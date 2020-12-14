package github.juteman.xmake.toolWindow;

import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class XMakeToolWindowFactory implements ToolWindowFactory {

    /**
     * Create XMake tool window Content
     *
     * @param project    current project
     * @param toolWindow current tool window
     */
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        var xmakeToolOutPutWindow = new XMakeToolWindowOutputPanel(project);
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        var outputContent = contentFactory.createContent(xmakeToolOutPutWindow, "Output", false);
        toolWindow.getContentManager().addContent(outputContent);

        var xmakeToolProblemWindow = new XMakeToolWindowProblemPanel(project);
        var problemContent = contentFactory.createContent(xmakeToolProblemWindow, "Problem", false);
        toolWindow.getContentManager().addContent(problemContent);
    }

    public static ToolWindow getXMakeToolWindow(@NotNull Project project) {
        return ToolWindowManager.getInstance(project).getToolWindow("XMake");
    }

    public static XMakeToolWindowOutputPanel getXMakeToolWindowOutputPanel(@NotNull Project project)
    {
        var content = getXMakeToolWindow(project).getContentManager().getContent(0);
        if (content == null)
        {
            return null;
        }
        return (XMakeToolWindowOutputPanel) content.getComponent();
    }


    public static XMakeToolWindowProblemPanel getXMakeToolWindowProblemPanel(@NotNull Project project)
    {
        var content = getXMakeToolWindow(project).getContentManager().getContent(1);
        if (content == null)
        {
            return null;
        }
        return (XMakeToolWindowProblemPanel) content.getComponent();
    }

    public static ConsoleView getXMakeConsoleView(@NotNull Project project)
    {
       return Objects.requireNonNull(getXMakeToolWindowOutputPanel(project)).getConsoleView();
    }
}


