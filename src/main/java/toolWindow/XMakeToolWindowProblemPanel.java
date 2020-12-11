package toolWindow;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.components.JBScrollPane;
import xmakeUtils.XMakeProblem;
import com.intellij.ui.components.JBList;

import javax.swing.*;
import java.util.List;

import static java.util.Collections.emptyList;

public class XMakeToolWindowProblemPanel extends SimpleToolWindowPanel {
    List<XMakeProblem> problems;
    private JBList<XMakeProblem> problemList;
    private JBScrollPane problemPane;

    public XMakeToolWindowProblemPanel() {
        super(false);
        problems = emptyList();
        problemList = new JBList<>(emptyList());
        problemPane = new JBScrollPane(problemList);
        problemPane.setBorder(null);

    }

    private void initProblemList()
    {
        problemList.setEmptyText("There are no compiling problems to display.");
        problemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        problemList.setCellRenderer(new XMakeColoredListCellRenderer());
    }

    public List<XMakeProblem> getProblems() {
        return problems;
    }

    public void setProblems(List<XMakeProblem> problems) {
         if(!ApplicationManager.getApplication().isDispatchThread())
         {
            throw new IllegalArgumentException("Not a dispatch thread");
         }
         this.problems = problems;

    }
}
