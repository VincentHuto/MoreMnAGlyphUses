package com.vincenthuto.moremnaglyphuses;

import com.mna.items.runes.StoneRune;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.vincenthuto.moremnaglyphuses.mixin.BookshelfTileAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public final class SpineGlyphs {
    public static final String ROOT = MoreMnAGlyphUses.MODID;
    public static final String KEY = "spine_glyphs";
    public static final String MATERIAL_KEY = "spine_rune_material";
    private static final Set<String> ARMOR_TARGETS = Set.of(
            "mage_robes", "bone_armor_chest", "council_armor_chest", "demon_armor_chest", "fey_armor_chest");
    public static final Map<String, Character> CHALK_RUNE_LETTERS = Map.ofEntries(
            Map.entry("gray", 'a'),
            Map.entry("blue", 'b'),
            Map.entry("black", 'c'),
            Map.entry("red", 'd'),
            Map.entry("lime", 'e'),
            Map.entry("purple", 'f'),
            Map.entry("green", 'g'),
            Map.entry("orange", 'h'),
            Map.entry("pink", 'i'),
            Map.entry("cyan", 'j'),
            Map.entry("brown", 'k'),
            Map.entry("magenta", 'm'),
            Map.entry("yellow", 'l'),
            Map.entry("light_gray", 'n'),
            Map.entry("light_blue", 'o'),
            Map.entry("white", 'p'));
    public static final Map<String, Character> METAL_RUNE_LETTERS = Map.ofEntries(
            Map.entry("gray", 'd'),
            Map.entry("blue", 'k'),
            Map.entry("black", 'n'),
            Map.entry("red", 'm'),
            Map.entry("green", 'e'),
            Map.entry("purple", 'j'),
            Map.entry("lime", 'i'),
            Map.entry("orange", 'b'),
            Map.entry("pink", 'f'),
            Map.entry("cyan", 'c'),
            Map.entry("brown", 'a'),
            Map.entry("magenta", 'l'),
            Map.entry("yellow", 'g'),
            Map.entry("light_gray", 'p'),
            Map.entry("light_blue", 'o'),
            Map.entry("white", 'h'));

    public enum Material {
        CHALK("chalk"), METAL("metal");

        private final String id;

        Material(String id) {
            this.id = id;
        }
    }

    private SpineGlyphs() {}

    public static void write(ItemStack stack, List<ResourceLocation> ids) {
        CompoundTag tag = stack.getOrCreateTag();
        writeTag(tag, ids);
    }

    public static void writeTag(CompoundTag tag, List<ResourceLocation> ids) {
        ListTag glyphs = new ListTag();
        ids.stream().limit(3).map(id -> StringTag.valueOf(id.toString())).forEach(glyphs::add);
        CompoundTag root = tag.contains(ROOT, Tag.TAG_COMPOUND) ? tag.getCompound(ROOT) : new CompoundTag();
        root.put(KEY, glyphs);
        root.remove(MATERIAL_KEY);
        tag.put(ROOT, root);
    }

    public static void write(ItemStack stack, List<ResourceLocation> ids, Material material) {
        CompoundTag tag = stack.getOrCreateTag();
        writeTag(tag, ids, material);
    }

    public static void writeTag(CompoundTag tag, List<ResourceLocation> ids, Material material) {
        writeTag(tag, ids);
        tag.getCompound(ROOT).putString(MATERIAL_KEY, material.id);
    }

    public static void clear(ItemStack stack) {
        if (stack.hasTag()) clearTag(stack.getTag());
    }

    public static void clearTag(CompoundTag tag) {
        tag.remove(ROOT);
    }

    public static List<ResourceLocation> readIds(ItemStack stack) {
        return stack.hasTag() ? readIds(stack.getTag()) : List.of();
    }

    public static List<ResourceLocation> readIds(CompoundTag tag) {
        if (!tag.contains(ROOT, Tag.TAG_COMPOUND)) return List.of();
        CompoundTag root = tag.getCompound(ROOT);
        ListTag glyphs = root.getList(KEY, Tag.TAG_STRING);
        List<ResourceLocation> result = new ArrayList<>(Math.min(3, glyphs.size()));
        for (int i = 0; i < glyphs.size() && result.size() < 3; i++) {
            ResourceLocation id = ResourceLocation.tryParse(glyphs.getString(i));
            if (id != null) result.add(id);
        }
        return result;
    }

    public static List<ItemStack> resolve(ItemStack stack) {
        List<ItemStack> result = new ArrayList<>(3);
        for (ResourceLocation id : readIds(stack)) {
            ItemStack glyph = resolve(id);
            if (!glyph.isEmpty()) result.add(glyph);
        }
        return result;
    }

    public static ItemStack resolve(ResourceLocation id) {
        Item item = ForgeRegistries.ITEMS.getValue(id);
        return item instanceof StoneRune ? new ItemStack(item) : ItemStack.EMPTY;
    }

    public static Material readMaterial(ItemStack stack) {
        return stack.hasTag() ? readMaterial(stack.getTag()) : null;
    }

    public static boolean hasMaterialKey(ItemStack stack) {
        return stack.hasTag() && hasMaterialKey(stack.getTag());
    }

    public static boolean hasMaterialKey(CompoundTag tag) {
        return tag.contains(ROOT, Tag.TAG_COMPOUND)
                && tag.getCompound(ROOT).contains(MATERIAL_KEY, Tag.TAG_STRING);
    }

    public static Material readMaterial(CompoundTag tag) {
        if (!tag.contains(ROOT, Tag.TAG_COMPOUND)) return null;
        String id = tag.getCompound(ROOT).getString(MATERIAL_KEY);
        for (Material material : Material.values()) if (material.id.equals(id)) return material;
        return null;
    }

    public static int runeIndex(ResourceLocation id) {
        return runeIndex(id, Material.CHALK);
    }

    public static int runeIndex(ResourceLocation id, Material material) {
        if (id == null || !"mna".equals(id.getNamespace())) return -1;
        String prefix = "stone_rune_";
        if (!id.getPath().startsWith(prefix)) return -1;
        Map<String, Character> letters = material == Material.METAL ? METAL_RUNE_LETTERS : CHALK_RUNE_LETTERS;
        Character letter = letters.get(id.getPath().substring(prefix.length()));
        return letter == null || letter < 'a' || letter > 'p' ? -1 : letter - 'a';
    }

    public static boolean isArmorTarget(ResourceLocation id) {
        return id != null && "mna".equals(id.getNamespace()) && ARMOR_TARGETS.contains(id.getPath());
    }

    public static boolean isArmorTarget(ItemStack stack) {
        return isArmorTarget(ForgeRegistries.ITEMS.getKey(stack.getItem()));
    }

    public static boolean isDecoratable(ItemStack stack) {
        return isArmorTarget(stack) || BookshelfTileAccessor.moremnaglyphuses$getDirectDisplayItems().contains(stack.getItem());
    }
}
