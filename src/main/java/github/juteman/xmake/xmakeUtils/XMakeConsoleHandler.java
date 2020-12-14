package github.juteman.xmake.xmakeUtils;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.KillableColoredProcessHandler;
import com.intellij.execution.process.ProcessTerminatedListener;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.util.Key;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.diagnostic.Logger;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class XMakeConsoleHandler extends KillableColoredProcessHandler {

    private String outputContent;
    private ConsoleView consoleView;
    private boolean showExitCode;
    private OutputStreamWriter outputStreamWriter;
    private Logger logger;

    public XMakeConsoleHandler(ConsoleView consoleView, @NotNull GeneralCommandLine commandLine, boolean showExitCode) throws ExecutionException {
        super(commandLine);
        outputContent = "";
        this.consoleView = consoleView;
        this.showExitCode = showExitCode;
        this.outputStreamWriter = null;
        logger = Logger.getInstance(XMakeConsoleHandler.class.getName());
        if (showExitCode) {
            ProcessTerminatedListener.attach(this);
        }
    }

    @Override
    public void coloredTextAvailable(@NotNull String text, @NotNull Key attributes) {
        append(text, attributes);
        super.coloredTextAvailable(text, attributes);
    }

    private void append(@NotNull String text, @NotNull Key<?> attributes)
    {
        this.consoleView.print(text, ConsoleViewContentType.getConsoleViewType(attributes));
        outputContent += text;
    }

    public void input(String inputString)
    {
        try {
            getProcessInputWriter().append(inputString);
        } catch (IOException e) {
            logger.error(e);
        }
    }

    public void inputWithFlush(String inputString)
    {
        try {
            getProcessInputWriter().append(inputString);
            getProcessInputWriter().flush();
        } catch (IOException e) {
            logger.error(e);
        }
    }

    public void flush()
    {
        try {
            getProcessInputWriter().flush();
        } catch (IOException e) {
            logger.error(e);
        }
    }


    public String getOutputContent() {
        return outputContent;
    }

    private OutputStreamWriter getProcessInputWriter()
    {
        if(outputStreamWriter == null)
        {
            outputStreamWriter = new OutputStreamWriter(getProcessInput());
        }
        return outputStreamWriter;
    }
}
