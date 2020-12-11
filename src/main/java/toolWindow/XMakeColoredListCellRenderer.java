package toolWindow;

import com.intellij.ui.ColoredListCellRenderer;
import com.intellij.ui.SimpleTextAttributes;
import icons.XMakeIcons;
import org.jetbrains.annotations.NotNull;
import xmakeUtils.XMakeProblem;

import javax.swing.*;

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
