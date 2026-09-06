package com.vincenthuto.moremnaglyphuses.mixin;

import com.mna.blocks.tileentities.BookshelfTile;
import com.vincenthuto.moremnaglyphuses.SpineGlyphs;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BookshelfTile.class)
public abstract class BookshelfTileMixin {
    @Shadow(remap = false) private ItemStack[] displayItems;

    @Inject(method = "getUpdateTag", at = @At("RETURN"))
    private void moremnaglyphuses$writeGlyphs(CallbackInfoReturnable<CompoundTag> cir) {
        BookshelfTile shelf = (BookshelfTile) (Object) this;
        ListTag displays = cir.getReturnValue().getList("displayItems", Tag.TAG_COMPOUND);
        for (int i = 0; i < Math.min(displays.size(), shelf.getContainerSize()); i++) {
            CompoundTag decoration = shelf.getItem(i).getTagElement(SpineGlyphs.ROOT);
            if (decoration != null) displays.getCompound(i).put(SpineGlyphs.ROOT, decoration.copy());
        }
    }

    @Inject(method = "parseItemsList", at = @At("TAIL"), remap = false)
    private void moremnaglyphuses$readGlyphs(CompoundTag tag, CallbackInfo ci) {
        ListTag displays = tag.getList("displayItems", Tag.TAG_COMPOUND);
        for (int i = 0; i < Math.min(displays.size(), displayItems.length); i++) {
            CompoundTag entry = displays.getCompound(i);
            if (entry.contains(SpineGlyphs.ROOT, Tag.TAG_COMPOUND)) {
                displayItems[i].getOrCreateTag().put(SpineGlyphs.ROOT, entry.getCompound(SpineGlyphs.ROOT).copy());
            }
        }
    }
}
