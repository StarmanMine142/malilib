package fi.dy.masa.malilib;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.fabricmc.api.ModInitializer;
import fi.dy.masa.malilib.event.InitializationHandler;

public class MaLiLib implements ModInitializer
{
    public static final Logger LOGGER = LogManager.getLogger(MaLiLibReference.MOD_ID);

    @Override
    public void onInitialize()
    {
        InitializationHandler.getInstance().registerInitializationHandler(new MaLiLibInitHandler());
    }

    public static void debugLog(String key, Object... args)
    {
        if (MaLiLibReference.DEBUG_MODE || MaLiLibConfigs.Debug.DEBUG_MESSAGES.getBooleanValue())
        {
            LOGGER.info(key, args);
        }
    }
}
