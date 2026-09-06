package com.vincenthuto.moremnatomecolors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.nbt.CompoundTag;
import org.junit.jupiter.api.Test;

class SpineGlyphsTest {
    @Test
    void replacesGlyphsWithoutChangingOtherBookData() {
        CompoundTag book = new CompoundTag();
        book.putString("saved_recipe", "still here");
        SpineGlyphs.writeTag(book, List.of(id("stone_rune_red")));
        SpineGlyphs.writeTag(book, List.of(
                id("stone_rune_blue"), id("stone_rune_green")));

        assertEquals(List.of(
                id("stone_rune_blue"), id("stone_rune_green")), SpineGlyphs.readIds(book));
        assertEquals("still here", book.getString("saved_recipe"));
    }

    @Test
    void ignoresInvalidAndExcessStoredIds() {
        CompoundTag book = new CompoundTag();
        CompoundTag root = new CompoundTag();
        root.putString("unrelated", "kept");
        book.put("moremnatomecolors", root);
        SpineGlyphs.writeTag(book, List.of(
                id("one"), id("two"), id("three"), id("four")));
        book.getCompound("moremnatomecolors").getList("spine_glyphs", 8).add(net.minecraft.nbt.StringTag.valueOf("not an id"));

        assertEquals(3, SpineGlyphs.readIds(book).size());
        assertEquals("kept", book.getCompound("moremnatomecolors").getString("unrelated"));
    }

    @Test
    void storesMaterialWithoutChangingOtherDecorationData() {
        CompoundTag book = new CompoundTag();
        SpineGlyphs.writeTag(book, List.of(id("stone_rune_red")), SpineGlyphs.Material.METAL);

        assertEquals(SpineGlyphs.Material.METAL, SpineGlyphs.readMaterial(book));
        assertEquals(List.of(id("stone_rune_red")), SpineGlyphs.readIds(book));
        assertNull(SpineGlyphs.readMaterial(new CompoundTag()));
    }

    @Test
    void catalystFreeDecorationClearsPreviousMaterial() {
        CompoundTag book = new CompoundTag();
        SpineGlyphs.writeTag(book, List.of(id("stone_rune_red")), SpineGlyphs.Material.METAL);
        SpineGlyphs.writeTag(book, List.of(id("stone_rune_blue")));

        assertEquals(false, SpineGlyphs.hasMaterialKey(book));
        assertEquals(List.of(id("stone_rune_blue")), SpineGlyphs.readIds(book));
    }

    @Test
    void clearingDecorationPreservesAllOtherBookData() {
        CompoundTag book = new CompoundTag();
        book.putString("saved_recipe", "still here");
        SpineGlyphs.writeTag(book, List.of(id("stone_rune_blue")), SpineGlyphs.Material.CHALK);

        SpineGlyphs.clearTag(book);

        assertEquals(false, book.contains(SpineGlyphs.ROOT));
        assertEquals("still here", book.getString("saved_recipe"));
    }

    @Test
    void distinguishesLegacyDataFromUnknownMaterial() {
        CompoundTag legacy = new CompoundTag();
        SpineGlyphs.writeTag(legacy, List.of(id("stone_rune_red")));
        assertEquals(false, SpineGlyphs.hasMaterialKey(legacy));

        legacy.getCompound(SpineGlyphs.ROOT).putString(SpineGlyphs.MATERIAL_KEY, "removed_material");
        assertEquals(true, SpineGlyphs.hasMaterialKey(legacy));
        assertNull(SpineGlyphs.readMaterial(legacy));
    }

    @Test
    void derivesRuneIndicesFromIndependentChalkAndMetalMaps() {
        assertEquals('p', SpineGlyphs.CHALK_RUNE_LETTERS.get("white"));
        assertEquals('h', SpineGlyphs.METAL_RUNE_LETTERS.get("white"));
        SpineGlyphs.CHALK_RUNE_LETTERS.forEach((color, letter) ->
                assertEquals(letter - 'a', SpineGlyphs.runeIndex(id("stone_rune_" + color), SpineGlyphs.Material.CHALK)));
        SpineGlyphs.METAL_RUNE_LETTERS.forEach((color, letter) ->
                assertEquals(letter - 'a', SpineGlyphs.runeIndex(id("stone_rune_" + color), SpineGlyphs.Material.METAL)));
        assertEquals(-1, SpineGlyphs.runeIndex(id("stone_rune_blank")));
        assertEquals(-1, SpineGlyphs.runeIndex(id("missing")));
    }

    @Test
    void supportsOnlyTheFiveRequestedChestArmorIds() {
        for (String path : List.of("mage_robes", "bone_armor_chest", "council_armor_chest",
                "demon_armor_chest", "fey_armor_chest")) {
            assertEquals(true, SpineGlyphs.isArmorTarget(id(path)));
        }
        assertEquals(false, SpineGlyphs.isArmorTarget(id("broken_robes")));
        assertEquals(false, SpineGlyphs.isArmorTarget(id("mage_leggings")));
        assertEquals(false, SpineGlyphs.isArmorTarget(ResourceLocation.tryParse("minecraft:diamond_chestplate")));
    }

    private static ResourceLocation id(String path) {
        return ResourceLocation.tryParse("mna:" + path);
    }
}
