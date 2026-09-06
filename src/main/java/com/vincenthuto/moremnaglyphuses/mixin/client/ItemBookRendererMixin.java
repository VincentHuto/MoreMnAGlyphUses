package com.vincenthuto.moremnaglyphuses.mixin.client;

import com.mna.items.ItemInit;
import com.mna.items.renderers.books.ItemBookRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.vincenthuto.moremnaglyphuses.SpineGlyphs;
import com.vincenthuto.moremnaglyphuses.client.SpineGlyphTransform;
import com.vincenthuto.moremnaglyphuses.client.SpineRuneRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemBookRenderer.class)
public abstract class ItemBookRendererMixin {
    @Inject(method = "renderByItem", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;popPose()V", shift = At.Shift.BEFORE))
    private void moremnaglyphuses$renderInventoryGlyphs(ItemStack book, ItemDisplayContext context,
            PoseStack pose, MultiBufferSource buffers, int light, int overlay, CallbackInfo ci) {
        if (context != ItemDisplayContext.GUI) return;
        var material = SpineGlyphs.readMaterial(book);
        if (material != null) {
            var glyphIds = SpineGlyphs.readIds(book);
            for (int i = 0; i < glyphIds.size(); i++) {
                double[] position = inventoryPosition(book, i);
                pose.pushPose();
                pose.translate(position[0], position[1], position[2]);
                SpineRuneRenderer.render(glyphIds.get(i), material, pose, buffers, light,
                        SpineGlyphTransform.inventoryScale());
                pose.popPose();
            }
            return;
        }
        if (SpineGlyphs.hasMaterialKey(book)) return;
        var glyphIds = SpineGlyphs.readIds(book);
        var renderer = Minecraft.getInstance().getItemRenderer();
        for (int i = 0; i < glyphIds.size(); i++) {
            ItemStack glyph = SpineGlyphs.resolve(glyphIds.get(i));
            if (glyph.isEmpty()) continue;
            double[] position = inventoryPosition(book, i);
            pose.pushPose();
            pose.translate(position[0], position[1], position[2]);
            pose.mulPose(Axis.YP.rotationDegrees(-90.0F));
            float scale = SpineGlyphTransform.inventoryScale();
            pose.scale(scale, scale, scale);
            renderer.renderStatic(glyph, ItemDisplayContext.FIXED, light, overlay, pose, buffers, null, i + 1);
            pose.popPose();
        }
    }

    private static double[] inventoryPosition(ItemStack book, int index) {
        if (book.is(ItemInit.GRIMOIRE_UNDEAD.get())) return SpineGlyphTransform.undeadFactionInventoryPosition(index);
        if (book.is(ItemInit.GRIMOIRE_COUNCIL.get()) || book.is(ItemInit.GRIMOIRE_DEMON.get())
                || book.is(ItemInit.GRIMOIRE_FEY.get())) return SpineGlyphTransform.factionInventoryPosition(index);
        return SpineGlyphTransform.inventoryPosition(index);
    }
}
