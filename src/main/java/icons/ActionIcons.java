package icons;

import com.intellij.openapi.util.IconLoader;
import javax.swing.*;

public interface ActionIcons {
    Icon run = IconLoader.getIcon("/icons/run.png", ActionIcons.class);
    Icon build = IconLoader.getIcon("/icons/build.png", ActionIcons.class);
}
