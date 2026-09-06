package com.vincenthuto.moremnatomecolors.client;

import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public final class ArmorGlyphTransform {
    public static final Map<String, Profile> PROFILES = Map.of(
            "mage_robes", new Profile(0.0, 0.16, 0.20, 0.21, 0.18F, 180.0F),
            "bone_armor_chest", new Profile(0.0, 0.16, 0.20, 0.20, 0.18F, 180.0F),
            "council_armor_chest", new Profile(0.0, 0.16, 0.20, 0.20, 0.18F, 180.0F),
            "demon_armor_chest", new Profile(0.0, 0.16, 0.20, 0.20, 0.18F, 180.0F),
            "fey_armor_chest", new Profile(0.0, 0.16, 0.20, 0.20, 0.18F, 180.0F));

    private ArmorGlyphTransform() {}

    public static Profile profile(ItemStack stack) {
        ResourceLocation id = ForgeRegistries.ITEMS.getKey(stack.getItem());
        return id == null ? null : PROFILES.get(id.getPath());
    }

    public record Profile(double x, double firstY, double spacing, double z, float scale, float roll) {
        public double y(int index) {
            return firstY + spacing * index;
        }
    }
}
