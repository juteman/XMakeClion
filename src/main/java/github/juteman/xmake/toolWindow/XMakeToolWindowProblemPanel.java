package github.juteman.xmake.toolWindow;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.ColoredListCellRenderer;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.ui.components.JBScrollPane;
import github.juteman.xmake.icons.XMakeIcons;
import org.jetbrains.annotations.NotNull;
import github.juteman.xmake.xmakeUtils.XMakeProblem;
import com.intellij.ui.components.JBList;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import static java.util.Collections.emptyList;

public class XMakeToolWindowProblemPanel extends SimpleToolWindowPanel {
    List<XMakeProblem> problems;
    private ActionToolbar actionToolbar;
    private JBList<XMakeProblem> problemList;
    private JBScrollPane problemPane;
    private Project project;

    public class XMakeColoredListCellRenderer extends ColoredListCellRenderer<XMakeProblem> {

        @Override
        protected void customizeCellRenderer(@NotNull JList<? extends XMakeProblem> jList, XMakeProblem xmakeProblem, int index, boolean selected, boolean hasFocus) {
            var file = xmakeProblem.getFile();
            if (file == null) {
                return;
            }

            if (xmakeProblem.getKind().equals("warning")) {
                setIcon(XMakeIcons.WARNING);
            } else if (xmakeProblem.getKind().equals("error")) {
                setIcon(XMakeIcons.ERROR);
            } else {
                setIcon(XMakeIcons.WARNING);
            }

            setToolTipText(xmakeProblem.getMessage() == null? "" : xmakeProblem.getMessage());

            append("${file}(${value.line ?: \"0\"}): ${value.message ?: \"\"}", SimpleTextAttributes.REGULAR_ATTRIBUTES);
        }
    }


    private class XMakeProblemMouseAdapter extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getClickCount() == 1 || e.getClickCount() == 2)
            {
                int index   = problemList.locationToIndex(e.getPoint());
                // TODO: Listen to mouse click event and jump to error line
            }
        }
    }

    public XMakeToolWindowProblemPanel(Project project) {
        super(false);
        this.project = project;
        problems = emptyList();
        problemList = new JBList<>(emptyList());
        initProblemList();
        problemPane = new JBScrollPane(problemList);
        problemPane.setBorder(null);
        var _actionManager = ActionManager.getInstance();
        actionToolbar = _actionManager.createActionToolbar("XMake ToolBar", (ActionGroup) _actionManager.getAction("XMake.Menu"), false);
        setToolbar(actionToolbar.getComponent());
        actionToolbar.setTargetComponent(this);
        setContent(problemPane);
        problemList.addMouseListener(new XMakeProblemMouseAdapter());
    }

    private void initProblemList() {
        problemList.setEmptyText("There are no compiling problems to display.");
        problemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        problemList.setCellRenderer(new XMakeColoredListCellRenderer());
    }

    public List<XMakeProblem> getProblems() {
        return problems;
    }

    public void setProblems(List<XMakeProblem> problems) {
        if (!ApplicationManager.getApplication().isDispatchThread()) {
            throw new IllegalArgumentException("Not a dispatch thread");
        }
        this.problems = problems;
        problemList.setListData((XMakeProblem[]) problems.toArray());
    }
}
