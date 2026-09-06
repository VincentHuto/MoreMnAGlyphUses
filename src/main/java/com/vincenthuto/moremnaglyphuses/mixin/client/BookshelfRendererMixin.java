package com.vincenthuto.moremnaglyphuses.mixin.client;

import com.mna.blocks.tileentities.renderers.BookshelfRenderer;
import com.mna.items.ItemInit;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.vincenthuto.moremnaglyphuses.SpineGlyphs;
import com.vincenthuto.moremnaglyphuses.client.SpineGlyphTransform;
import com.vincenthuto.moremnaglyphuses.client.SpineRuneRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BookshelfRenderer.class)
public abstract class BookshelfRendererMixin {
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;renderStatic(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;IILcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/level/Level;I)V"))
    private void moremnaglyphuses$renderSpineGlyphs(ItemRenderer renderer, ItemStack book,
            ItemDisplayContext context, int light, int overlay, PoseStack pose,
            MultiBufferSource buffers, Level level, int seed) {
        renderer.renderStatic(book, context, light, overlay, pose, buffers, level, seed);
        var material = SpineGlyphs.readMaterial(book);
        if (material != null && material != SpineGlyphs.Material.GLYPH) {
            var glyphIds = SpineGlyphs.readIds(book);
            for (int i = 0; i < glyphIds.size(); i++) {
                double[] position = position(book, i);
                pose.pushPose();
                pose.translate(position[0], position[1], position[2]);
                SpineRuneRenderer.render(glyphIds.get(i), material, pose, buffers, light, 0.12F);
                pose.popPose();
            }
            return;
        }
        if (material == SpineGlyphs.Material.GLYPH) {
            var glyphIds = SpineGlyphs.readIds(book);
            for (int i = 0; i < glyphIds.size(); i++) {
                double[] position = position(book, i);
                pose.pushPose();
                pose.translate(position[0], position[1], position[2]);
                pose.mulPose(Axis.YP.rotationDegrees(90.0F));
                pose.scale(0.12F, 0.12F, 0.12F);
                SpineRuneRenderer.renderGlyph(glyphIds.get(i), pose, buffers, light, overlay, renderer);
                pose.popPose();
            }
            return;
        }
        if (SpineGlyphs.hasMaterialKey(book)) return;
        var glyphIds = SpineGlyphs.readIds(book);
        for (int i = 0; i < glyphIds.size(); i++) {
            ItemStack glyph = SpineGlyphs.resolve(glyphIds.get(i));
            if (glyph.isEmpty()) continue;
            double[] position = position(book, i);
            pose.pushPose();
            pose.translate(position[0], position[1], position[2]);
            pose.mulPose(Axis.YP.rotationDegrees(-90.0F));
            pose.scale(0.12F, 0.12F, 0.12F);
            renderer.renderStatic(glyph, ItemDisplayContext.FIXED, light, overlay, pose, buffers, level, seed + i + 1);
            pose.popPose();
        }
    }

    private static double[] position(ItemStack book, int index) {
        return isFactionTome(book)
                ? book.is(ItemInit.GRIMOIRE_UNDEAD.get()) ? SpineGlyphTransform.undeadFactionPosition(index)
                          : SpineGlyphTransform.factionPosition(index)
                : SpineGlyphTransform.position(index);
    }

    private static boolean isFactionTome(ItemStack stack) {
        return stack.is(ItemInit.GRIMOIRE_COUNCIL.get())
                || stack.is(ItemInit.GRIMOIRE_DEMON.get())
                || stack.is(ItemInit.GRIMOIRE_FEY.get())
                || stack.is(ItemInit.GRIMOIRE_UNDEAD.get());
    }
}
