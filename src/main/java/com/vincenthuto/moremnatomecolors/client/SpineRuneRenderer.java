package com.vincenthuto.moremnatomecolors.client;

import com.mna.blocks.BlockInit;
import com.mna.blocks.ritual.ChalkRuneBlock;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.vincenthuto.moremnatomecolors.SpineGlyphs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;

public final class SpineRuneRenderer {
    private SpineRuneRenderer() {}

    public static void render(ResourceLocation glyphId, SpineGlyphs.Material material, PoseStack pose,
            MultiBufferSource buffers, int light, float scale) {
        int runeIndex = SpineGlyphs.runeIndex(glyphId, material);
        if (runeIndex < 0) return;

        BlockState rune = BlockInit.CHALK_RUNE.get().defaultBlockState()
                .setValue(ChalkRuneBlock.RUNEINDEX, runeIndex)
                .setValue(ChalkRuneBlock.METAL, material == SpineGlyphs.Material.METAL)
                .setValue(ChalkRuneBlock.ACTIVATED, false);
        pose.mulPose(Axis.XP.rotationDegrees(90.0F));
        pose.mulPose(Axis.YP.rotationDegrees(0.0F));
        pose.mulPose(Axis.ZP.rotationDegrees(90.0F));
        pose.scale(scale, scale, scale);
        pose.translate(-0.5, 0.0, -0.5);
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(
                rune, pose, buffers, light, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, null);
    }

    public static void renderArmor(ResourceLocation glyphId, SpineGlyphs.Material material, PoseStack pose,
            MultiBufferSource buffers, int light, float scale) {
        int runeIndex = SpineGlyphs.runeIndex(glyphId, material);
        if (runeIndex < 0) return;
        BlockState rune = BlockInit.CHALK_RUNE.get().defaultBlockState()
                .setValue(ChalkRuneBlock.RUNEINDEX, runeIndex)
                .setValue(ChalkRuneBlock.METAL, material == SpineGlyphs.Material.METAL)
                .setValue(ChalkRuneBlock.ACTIVATED, false);
        pose.mulPose(Axis.XP.rotationDegrees(90.0F));
        pose.scale(scale, scale, scale);
        pose.translate(-0.5, 0.0, -0.5);
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(
                rune, pose, buffers, light, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, null);
    }
}
