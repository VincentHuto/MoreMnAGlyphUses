package com.vincenthuto.moremnatomecolors;

import com.vincenthuto.moremnatomecolors.recipe.DecorateTomeRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

@Mod(MoreMnATomeColors.MODID)
public final class MoreMnATomeColors {
    public static final String MODID = "moremnatomecolors";
    private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MODID);
    public static final RegistryObject<RecipeSerializer<DecorateTomeRecipe>> DECORATE_TOME =
            RECIPE_SERIALIZERS.register("decorate_tome", () -> new net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer<>(DecorateTomeRecipe::new));

    public MoreMnATomeColors(FMLJavaModLoadingContext context) {
        RECIPE_SERIALIZERS.register(context.getModEventBus());
        MinecraftForge.EVENT_BUS.addListener(this::addTooltip);
    }

    private void addTooltip(ItemTooltipEvent event) {
        var glyphs = SpineGlyphs.resolve(event.getItemStack());
        if (glyphs.isEmpty()) return;
        var material = SpineGlyphs.readMaterial(event.getItemStack());
        String heading = material == SpineGlyphs.Material.CHALK ? "tooltip.moremnatomecolors.chalk_spine_runes"
                : material == SpineGlyphs.Material.METAL ? "tooltip.moremnatomecolors.metal_spine_runes"
                : "tooltip.moremnatomecolors.spine_glyphs";
        event.getToolTip().add(Component.translatable(heading).withStyle(ChatFormatting.GRAY));
        glyphs.forEach(glyph -> event.getToolTip().add(Component.literal("  ").append(glyph.getHoverName()).withStyle(ChatFormatting.DARK_GRAY)));
    }
}
