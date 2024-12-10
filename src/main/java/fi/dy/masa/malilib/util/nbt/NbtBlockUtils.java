package fi.dy.masa.malilib.util.nbt;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.llamalad7.mixinextras.lib.apache.commons.tuple.Pair;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;

import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.SignText;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.recipe.Recipe;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.event.Vibrations;

import fi.dy.masa.malilib.util.Constants;
import fi.dy.masa.malilib.util.NBTUtils;

public class NbtBlockUtils
{
    /**
     * Get the Block Entity Type from the NBT Tag.
     *
     * @param nbt
     * @return
     */
    public static @Nullable BlockEntityType<?> getBlockEntityTypeFromNbt(@Nonnull NbtCompound nbt)
    {
        if (nbt.contains(fi.dy.masa.malilib.util.NbtKeys.ID, Constants.NBT.TAG_STRING))
        {
            RegistryEntry<BlockEntityType<?>> entry = Registries.BLOCK_ENTITY_TYPE.getEntry(Identifier.tryParse(nbt.getString(fi.dy.masa.malilib.util.NbtKeys.ID))).orElse(null);

            if (entry != null && entry.hasKeyAndValue())
            {
                return entry.value();
            }
        }

        return null;
    }

    /**
     * Write the Block Entity ID tag.
     *
     * @param type
     * @param nbtIn
     * @return
     */
    public static NbtCompound setBlockEntityTypeToNbt(BlockEntityType<?> type, @Nullable NbtCompound nbtIn)
    {
        NbtCompound nbt = new NbtCompound();
        Identifier id = BlockEntityType.getId(type);

        if (id != null)
        {
            if (nbtIn != null)
            {
                nbtIn.putString(NbtKeys.ID, id.toString());
                return nbtIn;
            }
            else
            {
                nbt.putString(NbtKeys.ID, id.toString());
            }
        }

        return nbt;
    }

    /**
     * Read the Crafter's "locked slots" from NBT
     *
     * @param nbt
     * @return
     */
    public static Set<Integer> getDisabledSlotsFromNbt(@Nonnull NbtCompound nbt)
    {
        Set<Integer> list = new HashSet<>();

        if (nbt.contains(fi.dy.masa.malilib.util.NbtKeys.DISABLED_SLOTS, Constants.NBT.TAG_INT_ARRAY))
        {
            int[] is = nbt.getIntArray(fi.dy.masa.malilib.util.NbtKeys.DISABLED_SLOTS);

            for (int j : is)
            {
                list.add(j);
            }
        }

        return list;
    }

    /**
     * Get the Beacon's Effects from NBT.
     *
     * @param nbt
     * @return
     */
    public static Pair<RegistryEntry<StatusEffect>, RegistryEntry<StatusEffect>> getBeaconEffectsFromNbt(@Nonnull NbtCompound nbt)
    {
        RegistryEntry<StatusEffect> primary = null;
        RegistryEntry<StatusEffect> secondary = null;

        if (nbt.contains(fi.dy.masa.malilib.util.NbtKeys.PRIMARY_EFFECT, Constants.NBT.TAG_STRING))
        {
            Identifier id = Identifier.tryParse(nbt.getString(fi.dy.masa.malilib.util.NbtKeys.PRIMARY_EFFECT));
            if (id != null)
            {
                primary = Registries.STATUS_EFFECT.getEntry(id).orElse(null);
            }
        }
        if (nbt.contains(fi.dy.masa.malilib.util.NbtKeys.SECONDARY_EFFECT, Constants.NBT.TAG_STRING))
        {
            Identifier id = Identifier.tryParse(nbt.getString(fi.dy.masa.malilib.util.NbtKeys.SECONDARY_EFFECT));
            if (id != null)
            {
                secondary = Registries.STATUS_EFFECT.getEntry(id).orElse(null);
            }
        }

        return Pair.of(primary, secondary);
    }

    /**
     * Get the Beehive data from NBT.
     * @param nbt
     * @return
     */
    public static Pair<List<BeehiveBlockEntity.BeeData>, BlockPos> getBeesDataFromNbt(@Nonnull NbtCompound nbt)
    {
        List<BeehiveBlockEntity.BeeData> bees = new ArrayList<>();
        BlockPos flower = BlockPos.ORIGIN;

        if (nbt.contains(fi.dy.masa.malilib.util.NbtKeys.FLOWER))
        {
            flower = NBTUtils.readBlockPosFromIntArray(nbt, fi.dy.masa.malilib.util.NbtKeys.FLOWER);
        }
        if (nbt.contains(fi.dy.masa.malilib.util.NbtKeys.BEES, Constants.NBT.TAG_LIST))
        {
            BeehiveBlockEntity.BeeData.LIST_CODEC.parse(NbtOps.INSTANCE, nbt.get(fi.dy.masa.malilib.util.NbtKeys.BEES)).resultOrPartial().ifPresent(bees::addAll);
        }

        return Pair.of(bees, flower);
    }

    /**
     * Get the Skulk Sensor Vibration / Listener data from NBT.
     *
     * @param nbt
     * @param registry
     * @return
     */
    public static Pair<Integer, Vibrations.ListenerData> getSkulkSensorVibrationsFromNbt(@Nonnull NbtCompound nbt, @Nonnull DynamicRegistryManager registry)
    {
        AtomicReference<Vibrations.ListenerData> data = new AtomicReference<>(null);
        int lastFreq = -1;

        if (nbt.contains(fi.dy.masa.malilib.util.NbtKeys.VIBRATION, Constants.NBT.TAG_INT))
        {
            lastFreq = nbt.getInt(fi.dy.masa.malilib.util.NbtKeys.VIBRATION);
        }
        if (nbt.contains(fi.dy.masa.malilib.util.NbtKeys.LISTENER, Constants.NBT.TAG_COMPOUND))
        {
            Vibrations.ListenerData.CODEC.parse(registry.getOps(NbtOps.INSTANCE), nbt.getCompound(fi.dy.masa.malilib.util.NbtKeys.LISTENER)).resultOrPartial().ifPresent(data::set);
        }

        return Pair.of(lastFreq, data.get());
    }

    /**
     * Get the End Gateway's Exit Portal from NBT.
     * @param nbt
     * @return
     */
    public static Pair<Long, BlockPos> getExitPortalFromNbt(@Nonnull NbtCompound nbt)
    {
        long age = -1;
        BlockPos pos = BlockPos.ORIGIN;

        if (nbt.contains(fi.dy.masa.malilib.util.NbtKeys.AGE, Constants.NBT.TAG_LONG))
        {
            age = nbt.getLong(fi.dy.masa.malilib.util.NbtKeys.AGE);
        }
        if (nbt.contains(fi.dy.masa.malilib.util.NbtKeys.EXIT, Constants.NBT.TAG_INT_ARRAY))
        {
            pos = NBTUtils.readBlockPosFromIntArray(nbt, fi.dy.masa.malilib.util.NbtKeys.EXIT);
        }

        return Pair.of(age, pos);
    }

    /**
     * Get a Sign's Text from NBT.
     *
     * @param nbt
     * @param registry
     * @return
     */
    public static Pair<Pair<SignText, SignText>, Boolean> getSignTextFromNbt(@Nonnull NbtCompound nbt, @Nonnull DynamicRegistryManager registry)
    {
        AtomicReference<SignText> front = new AtomicReference<>(null);
        AtomicReference<SignText> back = new AtomicReference<>(null);
        boolean waxed = false;

        if (nbt.contains(fi.dy.masa.malilib.util.NbtKeys.FRONT_TEXT))
        {
            SignText.CODEC.parse(registry.getOps(NbtOps.INSTANCE), nbt.getCompound(fi.dy.masa.malilib.util.NbtKeys.FRONT_TEXT)).resultOrPartial().ifPresent(front::set);
        }
        if (nbt.contains(fi.dy.masa.malilib.util.NbtKeys.BACK_TEXT))
        {
            SignText.CODEC.parse(registry.getOps(NbtOps.INSTANCE), nbt.getCompound(fi.dy.masa.malilib.util.NbtKeys.BACK_TEXT)).resultOrPartial().ifPresent(back::set);
        }
        if (nbt.contains(fi.dy.masa.malilib.util.NbtKeys.WAXED))
        {
            waxed = nbt.getBoolean(fi.dy.masa.malilib.util.NbtKeys.WAXED);
        }

        return Pair.of(Pair.of(front.get(), back.get()), waxed);
    }

    /**
     * Get a Lectern's Book and Page number.
     *
     * @param nbt
     * @param registry
     * @return
     */
    public static Pair<ItemStack, Integer> getBookFromNbt(@Nonnull NbtCompound nbt, @Nonnull DynamicRegistryManager registry)
    {
        ItemStack book = ItemStack.EMPTY;
        int current = -1;

        if (nbt.contains(fi.dy.masa.malilib.util.NbtKeys.BOOK, Constants.NBT.TAG_COMPOUND))
        {
            book = ItemStack.fromNbtOrEmpty(registry, nbt.getCompound(fi.dy.masa.malilib.util.NbtKeys.BOOK));
        }
        if (nbt.contains(fi.dy.masa.malilib.util.NbtKeys.PAGE, Constants.NBT.TAG_INT))
        {
            current = nbt.getInt(fi.dy.masa.malilib.util.NbtKeys.PAGE);
        }

        return Pair.of(book, current);
    }

    /**
     * Get a Skull's Profile Data Component from NBT, and Custom Name.
     *
     * @param nbt
     * @param registry
     * @return
     */
    public static Pair<ProfileComponent, Pair<Identifier, Text>> getSkullDataFromNbt(@Nonnull NbtCompound nbt, @Nonnull DynamicRegistryManager registry)
    {
        AtomicReference<ProfileComponent> profile = new AtomicReference<>(null);
        Identifier note = null;
        Text name = Text.empty();

        if (nbt.contains(fi.dy.masa.malilib.util.NbtKeys.NOTE, Constants.NBT.TAG_STRING))
        {
            note = Identifier.tryParse(nbt.getString(fi.dy.masa.malilib.util.NbtKeys.NOTE));
        }
        if (nbt.contains(fi.dy.masa.malilib.util.NbtKeys.SKULL_NAME, Constants.NBT.TAG_STRING))
        {
            String str = nbt.getString(fi.dy.masa.malilib.util.NbtKeys.SKULL_NAME);

            try
            {
                name = Text.Serialization.fromJson(str, registry);
            }
            catch (Exception ignored) {}
        }
        if (nbt.contains(fi.dy.masa.malilib.util.NbtKeys.PROFILE))
        {
            ProfileComponent.CODEC.parse(NbtOps.INSTANCE, nbt.get(fi.dy.masa.malilib.util.NbtKeys.PROFILE)).resultOrPartial().ifPresent(profile::set);
        }

        return Pair.of(profile.get(), Pair.of(note, name));
    }

    /**
     * Get a Furnaces 'Used Recipes' from NBT.
     *
     * @param nbt
     * @return
     */
    public static Object2IntOpenHashMap<Identifier> getRecipesUsedFromNbt(@Nonnull NbtCompound nbt)
    {
        Object2IntOpenHashMap<Identifier> list = new Object2IntOpenHashMap<>();

        if (nbt.contains(fi.dy.masa.malilib.util.NbtKeys.RECIPES_USED, Constants.NBT.TAG_COMPOUND))
        {
            NbtCompound compound = nbt.getCompound(fi.dy.masa.malilib.util.NbtKeys.RECIPES_USED);

            for (String key : compound.getKeys())
            {
                list.put(Identifier.of(key), compound.getInt(key));
            }
        }

        return list;
    }
}
