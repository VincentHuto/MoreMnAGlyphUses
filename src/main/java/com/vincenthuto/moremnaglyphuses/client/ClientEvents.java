package com.vincenthuto.moremnaglyphuses.client;

import com.vincenthuto.moremnaglyphuses.MoreMnAGlyphUses;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import com.vincenthuto.moremnaglyphuses.SpineGlyphs;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MoreMnAGlyphUses.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientEvents {
    private ClientEvents() {}

    @SubscribeEvent
    public static void registerGlyphModels(ModelEvent.RegisterAdditional event) {
        SpineGlyphs.CHALK_RUNE_LETTERS.keySet().stream()
                .map(color -> SpineGlyphs.glyphModel(
                        ResourceLocation.fromNamespaceAndPath("mna", "stone_rune_" + color)))
                .forEach(event::register);
    }

    @SubscribeEvent
    public static void addPlayerLayers(EntityRenderersEvent.AddLayers event) {
        PlayerRenderer defaultRenderer = event.getSkin("default");
        PlayerRenderer slimRenderer = event.getSkin("slim");
        add(defaultRenderer);
        add(slimRenderer);
        ArmorStandRenderer armorStandRenderer = event.getRenderer(EntityType.ARMOR_STAND);
        add(armorStandRenderer);
    }

    private static <T extends LivingEntity, M extends HumanoidModel<T>> void add(LivingEntityRenderer<T, M> renderer) {
        if (renderer != null) renderer.addLayer(new ArmorRuneLayer<>(renderer, Minecraft.getInstance().getItemRenderer()));
    }
}
