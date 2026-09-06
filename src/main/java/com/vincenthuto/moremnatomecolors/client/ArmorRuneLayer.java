package com.vincenthuto.moremnatomecolors.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.vincenthuto.moremnatomecolors.SpineGlyphs;
import com.mojang.math.Axis;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public final class ArmorRuneLayer<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {
    private final ItemRenderer itemRenderer;

    public ArmorRuneLayer(RenderLayerParent<T, M> parent, ItemRenderer itemRenderer) {
        super(parent);
        this.itemRenderer = itemRenderer;
    }

    @Override
    public void render(PoseStack pose, MultiBufferSource buffers, int light, T player,
            float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks,
            float netHeadYaw, float headPitch) {
        ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
        ArmorGlyphTransform.Profile transform = ArmorGlyphTransform.profile(chest);
        if (player.isInvisible() || transform == null || SpineGlyphs.readIds(chest).isEmpty()) return;

        pose.pushPose();
        getParentModel().body.translateAndRotate(pose);
        var ids = SpineGlyphs.readIds(chest);
        var material = SpineGlyphs.readMaterial(chest);
        for (int index = 0; index < ids.size(); index++) {
            pose.pushPose();
            pose.translate(transform.x(), transform.y(index), transform.z());
            pose.mulPose(Axis.ZP.rotationDegrees(transform.roll()));
            if (material != null) {
                SpineRuneRenderer.renderArmor(ids.get(index), material, pose, buffers, light, transform.scale());
            } else if (!SpineGlyphs.hasMaterialKey(chest)) {
                ItemStack glyph = SpineGlyphs.resolve(ids.get(index));
                if (!glyph.isEmpty()) {
                    pose.scale(transform.scale(), transform.scale(), transform.scale());
                    itemRenderer.renderStatic(glyph, ItemDisplayContext.FIXED, light, OverlayTexture.NO_OVERLAY,
                            pose, buffers, player.level(), index + 1);
                }
            }
            pose.popPose();
        }
        pose.popPose();
    }
}
