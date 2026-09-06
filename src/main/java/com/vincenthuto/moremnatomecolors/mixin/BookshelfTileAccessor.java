package com.vincenthuto.moremnatomecolors.mixin;

import com.mna.blocks.tileentities.BookshelfTile;
import java.util.List;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BookshelfTile.class)
public interface BookshelfTileAccessor {
    @Accessor(value = "directDisplayItems", remap = false)
    static List<Item> moremnatomecolors$getDirectDisplayItems() {
        throw new AssertionError();
    }
}
