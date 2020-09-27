package fi.dy.masa.malilib.gui.config;

import java.util.List;
import javax.annotation.Nullable;
import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.option.ConfigInfo;
import fi.dy.masa.malilib.util.StringUtils;

public abstract class BaseConfigGroup implements ConfigInfo
{
    protected final String nameTranslationKey;
    protected final String commentTranslationKey;
    protected final Object[] commentArgs;
    protected ImmutableList<ConfigInfo> configs = ImmutableList.of();

    public BaseConfigGroup(String name, String modId, List<ConfigInfo> configs)
    {
        this(modId + ".config_group.name." + name, modId + ".config_group.comment." + name);

        this.configs = ImmutableList.copyOf(configs);
    }

    public BaseConfigGroup(String nameTranslationKey, String commentTranslationKey, Object... commentArgs)
    {
        this.nameTranslationKey = nameTranslationKey;
        this.commentTranslationKey = commentTranslationKey;
        this.commentArgs = commentArgs;
    }

    /**
     * Sets the list of contained configs, overriding any old values
     * @param configs
     * @return
     */
    public BaseConfigGroup setConfigs(ConfigInfo... configs)
    {
        this.configs = ImmutableList.copyOf(configs);
        return this;
    }

    /**
     * Returns the list of configs contained within this group
     * @return
     */
    public ImmutableList<ConfigInfo> getConfigs()
    {
        return this.configs;
    }

    @Override
    public String getName()
    {
        return StringUtils.translate(this.getConfigNameTranslationKey());
    }

    @Override
    public String getConfigNameTranslationKey()
    {
        return this.nameTranslationKey;
    }

    @Override
    @Nullable
    public String getCommentTranslationKey()
    {
        return this.commentTranslationKey;
    }

    @Override
    public boolean isModified()
    {
        for (ConfigInfo config : this.configs)
        {
            if (config.isModified())
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public void resetToDefault()
    {
        for (ConfigInfo config : this.configs)
        {
            config.resetToDefault();
        }
    }
}