package fi.dy.masa.malilib.test;

import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.ApiStatus;

import net.minecraft.client.MinecraftClient;

import fi.dy.masa.malilib.MaLiLibConfigs;
import fi.dy.masa.malilib.MaLiLibReference;
import fi.dy.masa.malilib.hotkeys.*;
import fi.dy.masa.malilib.render.InventoryOverlayScreen;

@ApiStatus.Experimental
public class TestInputHandler implements IKeybindProvider
{
    private static final TestInputHandler INSTANCE = new TestInputHandler();

    private final Callbacks callback;

    private TestInputHandler()
    {
        super();
        this.callback = new Callbacks();
        this.init();
    }

    public static TestInputHandler getInstance()
    {
        return INSTANCE;
    }

    public Callbacks getCallback()
    {
        return this.callback;
    }

    public void init()
    {
        MaLiLibConfigs.Test.TEST_INVENTORY_OVERLAY.getKeybind().setCallback(this.callback);
        MaLiLibConfigs.Test.TEST_INVENTORY_OVERLAY_TOGGLE.getKeybind().setCallback(this.callback);
    }

    @Override
    public void addKeysToMap(IKeybindManager manager)
    {
        for (IHotkey hotkey : MaLiLibConfigs.Test.HOTKEY_LIST)
        {
            manager.addKeybindToMap(hotkey.getKeybind());
        }

        for (ConfigTestEnum toggle : ConfigTestEnum.values())
        {
            manager.addKeybindToMap(toggle.getKeybind());
        }
    }

    @Override
    public void addHotkeys(IKeybindManager manager)
    {
        manager.addHotkeysForCategory(MaLiLibReference.MOD_NAME, MaLiLibReference.MOD_ID + ".hotkeys.category.test_hotkeys", MaLiLibConfigs.Test.HOTKEY_LIST);
        manager.addHotkeysForCategory(MaLiLibReference.MOD_NAME, MaLiLibReference.MOD_ID + ".hotkeys.category.test_enum_hotkeys", ImmutableList.copyOf(ConfigTestEnum.values()));
    }

    public static class Callbacks implements IHotkeyCallback
    {
        @Override
        public boolean onKeyAction(KeyAction action, IKeybind key)
        {
            MinecraftClient mc = MinecraftClient.getInstance();

            if (mc.player == null)
            {
                return false;
            }

            if (key == MaLiLibConfigs.Test.TEST_INVENTORY_OVERLAY.getKeybind())
            {
                // No message
                return true;
            }
            else if (key == MaLiLibConfigs.Test.TEST_INVENTORY_OVERLAY_TOGGLE.getKeybind())
            {
                if (mc.currentScreen instanceof InventoryOverlayScreen)
                {
                    mc.setScreen(null);
                }
                else if (MaLiLibConfigs.Test.TEST_INVENTORY_OVERLAY.getBooleanValue() &&
                         MaLiLibConfigs.Test.TEST_INVENTORY_OVERLAY.getKeybind().isKeybindHeld())
                {
                    TestInventoryOverlayHandler.getInstance().refreshInventoryOverlay(mc, true);
                }
                else
                {
                    return false;
                }
            }
            else
            {
                return false;
            }

            return true;
        }
    }
}
