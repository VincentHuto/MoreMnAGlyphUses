package com.vincenthuto.moremnaglyphuses.client;

import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public final class ArmorGlyphTransform {
    public static final Map<String, Profile> PROFILES = Map.of(
            "mage_robes", new Profile(0.0, 0.16, 0.20, 0.21, 0.18F, 180.0F),
            "bone_armor_chest", new Profile(0.0, 0.16, 0.20, 0.20, 0.18F, 180.0F),
            "council_armor_chest", new Profile(
                    new double[]{0.20, -0.2, 0.0}, new double[]{0.25, 0.25, 0.40}, 0.20, 0.18F, 180.0F),
            "demon_armor_chest", new Profile(0.0, 0.16, 0.20, 0.20, 0.18F, 180.0F),
            "fey_armor_chest", new Profile(
                    new double[]{0.06, 0.0, 0.0}, new double[]{0.16, 0.36, 0.56}, 0.20, 0.18F, 180.0F));

    private ArmorGlyphTransform() {}

    public static Profile profile(ItemStack stack) {
        ResourceLocation id = ForgeRegistries.ITEMS.getKey(stack.getItem());
        return id == null ? null : PROFILES.get(id.getPath());
    }

    public record Profile(double[] x, double[] y, double z, float scale, float roll) {
        public Profile(double x, double firstY, double spacing, double z, float scale, float roll) {
            this(new double[]{x, x, x},
                    new double[]{firstY, firstY + spacing, firstY + spacing * 2}, z, scale, roll);
        }

        public double x(int index) {
            return x[index];
        }

        public double y(int index) {
            return y[index];
        }
    }
}
