package fi.dy.masa.malilib.config.option;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

import fi.dy.masa.malilib.gui.config.ConfigTab;
import fi.dy.masa.malilib.listener.EventListener;

public interface ConfigInfo extends BaseInfo
{
    /**
     * @return a list of strings that the config screen search bar is matching against.
     * Each string is tried until a match is found.
     * The match is by default checked by stringInList.contains(searchTerm)
     * where both values are first converted to lower case.
     */
    default List<String> getSearchStrings()
    {
        return Collections.singletonList(this.getDisplayName());
    }

    /**
     * @return a click handler for the config name label widget.
     * If this returns a non-null value, then the hover info will
     * by default get a prefix saying "Click for more information"
     * and the label widget will get this event handler set as the click action.
     */
    @Nullable
    default EventListener getLabelClickHandler()
    {
        return null;
    }

    /**
     * If this config contains any nested options, adds the nested options to the provided list.
     * This is intended for config groups which are currently open/expanded, to add their
     * contained nested options to the list for the config screen.
     * @param nestingLevel the current nesting level on the config screen
     * @param expandAlways if true, then the nested configs should be added
     *                     even if the config is not in an expanded/open state
     */
    default void addNestedOptionsToList(List<ConfigOnTab> list, ConfigTab tab, int nestingLevel, boolean expandAlways)
    {
        // NO-OP
    }

    /**
     * @return true if the value has been changed from the default value
     */
    boolean isModified();

    /**
     * Resets the config value back to the default value
     */
    void resetToDefault();
}
