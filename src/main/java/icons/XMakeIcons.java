package icons;

import com.intellij.openapi.util.IconLoader;
import javax.swing.*;

public interface XMakeIcons {
    Icon xmake = IconLoader.getIcon("/icons/xmake_13x13.png", XMakeIcons.class);
    Icon setting = IconLoader.getIcon("/icons/settings.png", XMakeIcons.class);
    Icon WARNING = IconLoader.getIcon("/icons/warning.png", XMakeIcons.class);
    Icon ERROR = IconLoader.getIcon("/icons/error.png", XMakeIcons.class);
    Icon FILE = IconLoader.getIcon("/icons/xmake.png", XMakeIcons.class);
}
