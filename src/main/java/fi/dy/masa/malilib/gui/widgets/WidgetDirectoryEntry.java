package fi.dy.masa.malilib.gui.widgets;

import java.io.File;
import javax.annotation.Nullable;
import fi.dy.masa.malilib.gui.interfaces.IDirectoryNavigator;
import fi.dy.masa.malilib.gui.interfaces.IFileBrowserIconProvider;
import fi.dy.masa.malilib.gui.interfaces.IGuiIcon;
import fi.dy.masa.malilib.gui.widgets.WidgetFileBrowserBase.DirectoryEntry;
import fi.dy.masa.malilib.gui.widgets.WidgetFileBrowserBase.DirectoryEntryType;
import fi.dy.masa.malilib.render.RenderUtils;
import fi.dy.masa.malilib.util.FileUtils;

public class WidgetDirectoryEntry extends WidgetListEntryBase<DirectoryEntry>
{
    protected final IDirectoryNavigator navigator;
    protected final DirectoryEntry entry;
    protected final boolean isOdd;
    @Nullable protected final IFileBrowserIconProvider iconProvider;

    public WidgetDirectoryEntry(int x, int y, int width, int height, boolean isOdd, DirectoryEntry entry,
            int listIndex, IDirectoryNavigator navigator, @Nullable IFileBrowserIconProvider iconProvider)
    {
        super(x, y, width, height, entry, listIndex);

        this.isOdd = isOdd;
        this.entry = entry;
        this.navigator = navigator;
        this.iconProvider = iconProvider;
    }

    public DirectoryEntry getDirectoryEntry()
    {
        return this.entry;
    }

    @Override
    protected boolean onMouseClickedImpl(int mouseX, int mouseY, int mouseButton)
    {
        if (this.entry.getType() == DirectoryEntryType.DIRECTORY)
        {
            this.navigator.switchToDirectory(new File(this.entry.getDirectory(), this.entry.getName()));
        }
        else
        {
            return super.onMouseClickedImpl(mouseX, mouseY, mouseButton);
        }

        return true;
    }

    @Override
    public void render(int mouseX, int mouseY, boolean selected)
    {
        @Nullable IGuiIcon icon = this.iconProvider != null ? this.iconProvider.getIconForEntry(this.entry) : null;
        int xOffset = 0;

        if (icon != null)
        {
            xOffset += this.iconProvider.getEntryIconWidth(this.entry) + 2;
            icon.renderAt(this.x, this.y + (this.height - icon.getHeight()) / 2, this.zLevel + 1, false, false);
        }

        // Draw a lighter background for the hovered and the selected entry
        if (selected || this.isMouseOver(mouseX, mouseY))
        {
            RenderUtils.drawRect(this.x, this.y, this.width, this.height, 0x70FFFFFF);
        }
        else if (this.isOdd)
        {
            RenderUtils.drawRect(this.x, this.y, this.width, this.height, 0x20FFFFFF);
        }
        // Draw a slightly lighter background for even entries
        else
        {
            RenderUtils.drawRect(this.x, this.y, this.width, this.height, 0x38FFFFFF);
        }

        // Draw an outline if this is the currently selected entry
        if (selected)
        {
            RenderUtils.drawOutline(this.x, this.y, this.width, this.height, 0xEEEEEEEE);
        }

        int yOffset = (this.height - this.fontHeight) / 2 + 1;
        this.drawString(this.x + xOffset + 2, this.y + yOffset, 0xFFFFFFFF, this.getDisplayName());

        super.render(mouseX, mouseY, selected);
    }

    protected String getDisplayName()
    {
        if (this.entry.getType() == DirectoryEntryType.DIRECTORY)
        {
            return this.entry.getDisplayName();
        }
        else
        {
            return FileUtils.getNameWithoutExtension(this.entry.getDisplayName());
        }
    }
}
