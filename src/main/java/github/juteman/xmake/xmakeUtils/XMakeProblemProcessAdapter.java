package github.juteman.xmake.xmakeUtils;

import com.intellij.execution.process.ProcessAdapter;
import com.intellij.execution.process.ProcessEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.SystemInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import github.juteman.xmake.toolWindow.XMakeToolWindowFactory;

import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XMakeProblemProcessAdapter extends ProcessAdapter {
    private OutputStreamWriter _outputStreamWriter;
    private XMakeConsoleHandler handler;
    private Project project;

    public XMakeProblemProcessAdapter(XMakeConsoleHandler handler, Project project) {
        this.handler = handler;
        this.project = project;
    }


    @Nullable
    private XMakeProblem parseProblem(String info) {
        if (SystemInfo.isWindows) {
            String infoUtf8 = new String(StandardCharsets.UTF_8.encode(info).array());
            Pattern pattern = Pattern.compile("(.*?)\\(([0-9]*)\\): (.*?) .*?: (.*)");
            Matcher matcher = pattern.matcher(infoUtf8);
            if (matcher.find()) {
                var file = matcher.group(1);
                var line = matcher.group(2);
                var kind = matcher.group(3);
                var message = matcher.group(4);
                return new XMakeProblem(file, line, "0", kind, message);
            }
        } else {
            Pattern pattern = Pattern.compile("(.*?)\\(([0-9]*)\\): (.*?) .*?: (.*)");
            Matcher matcher = pattern.matcher(info);
            var file = matcher.group(2);
            var line = matcher.group(3);
            var column = matcher.group(4);
            var kind = matcher.group(5);
            var message = matcher.group(6);
            return new XMakeProblem(file, line, column, kind, message);
        }

        return null;
    }


    @Override
    public void processTerminated(@NotNull ProcessEvent event) {
        String content = handler.getOutputContent();
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                ArrayList<XMakeProblem> problems = new ArrayList<>();
                var lines = content.split("\n");
                for (String line : lines) {
                    var problem = parseProblem(line);
                    if (problem != null) {
                        problems.add(problem);
                    }
                }
                Objects.requireNonNull(XMakeToolWindowFactory.getXMakeToolWindowProblemPanel(project)).setProblems(problems);
            }
        });
    }


}
