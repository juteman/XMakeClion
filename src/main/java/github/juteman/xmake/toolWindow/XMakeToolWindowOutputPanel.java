package github.juteman.xmake.toolWindow;

import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.content.ContentManager;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class XMakeToolWindowOutputPanel extends SimpleToolWindowPanel{

    private final ActionToolbar actionToolbar;
    private final ConsoleView consoleView;
    private Project project;

    public XMakeToolWindowOutputPanel(Project project) {
        super(false);

        this.project = project;
        // Use ActionManager Create github.juteman.xmake.action toolbar by github.juteman.xmake.action group
        var _actionManager = ActionManager.getInstance();
        actionToolbar = _actionManager.createActionToolbar("XMake ToolBar", (ActionGroup) _actionManager.getAction("XMake.Menu"), false);

        // Use TextConsoleBuilder builder make console component
        var builder = TextConsoleBuilderFactory.getInstance().createBuilder(project);
        builder.setViewer(true);
        consoleView = builder.getConsole();

        // Attach tool bar and console component to outputPanel
        setToolbar(actionToolbar.getComponent());
        actionToolbar.setTargetComponent(this);
        setContent(consoleView.getComponent());
    }

    public void showPanel()
    {
        ContentManager contentManager = XMakeToolWindowFactory.getXMakeToolWindow(project).getContentManager();
        contentManager.setSelectedContent(Objects.requireNonNull(contentManager.getContent(0)));
    }

    @NotNull
    public  ConsoleView getConsoleView() {
        return consoleView;
    }

    public ActionToolbar getActionToolbar() {
        return actionToolbar;
    }
}

