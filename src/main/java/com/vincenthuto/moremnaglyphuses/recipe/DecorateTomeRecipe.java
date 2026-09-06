package com.vincenthuto.moremnaglyphuses.recipe;

import com.mna.items.runes.StoneRune;
import com.mna.items.ItemInit;
import com.vincenthuto.moremnaglyphuses.MoreMnAGlyphUses;
import com.vincenthuto.moremnaglyphuses.SpineGlyphs;
import java.util.ArrayList;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

public final class DecorateTomeRecipe extends CustomRecipe {
    public DecorateTomeRecipe(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        return isClearRecipe(container) || glyphSlots(container) != null;
    }

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess access) {
        if (isClearRecipe(container)) {
            ItemStack result = container.getItem(4).copy();
            result.setCount(1);
            SpineGlyphs.clear(result);
            return result;
        }
        int[] slots = glyphSlots(container);
        if (slots == null) return ItemStack.EMPTY;
        ItemStack result = container.getItem(4).copy();
        result.setCount(1);
        var ids = new ArrayList<ResourceLocation>(slots.length);
        for (int slot : slots) ids.add(ForgeRegistries.ITEMS.getKey(container.getItem(slot).getItem()));
        SpineGlyphs.Material material = material(container.getItem(3));
        if (material == null) SpineGlyphs.write(result, ids);
        else SpineGlyphs.write(result, ids, material);
        return result;
    }

    private static boolean isClearRecipe(CraftingContainer container) {
        if (container.getWidth() != 3 || container.getHeight() != 3) return false;
        ItemStack book = container.getItem(4);
        if (book.isEmpty() || book.getTagElement(SpineGlyphs.ROOT) == null
                || !SpineGlyphs.isDecoratable(book)) return false;
        for (int slot = 0; slot < 9; slot++) {
            if (slot != 4 && !container.getItem(slot).isEmpty()) return false;
        }
        return true;
    }

    private static int[] glyphSlots(CraftingContainer container) {
        ItemStack catalystStack = container.getItem(3);
        SpineGlyphs.Material material = material(catalystStack);
        boolean hasCatalyst = material != null;
        if (container.getWidth() != 3 || container.getHeight() != 3
                || container.getItem(4).isEmpty()
                || (!catalystStack.isEmpty() && !hasCatalyst && !(catalystStack.getItem() instanceof StoneRune))
                || !SpineGlyphs.isDecoratable(container.getItem(4))) return null;
        boolean[] occupied = new boolean[9];
        for (int slot = 0; slot < 9; slot++) {
            if (slot == 4 || (slot == 3 && hasCatalyst)) continue;
            ItemStack stack = container.getItem(slot);
            if (!stack.isEmpty()) {
                ResourceLocation id = ForgeRegistries.ITEMS.getKey(stack.getItem());
                if (!(stack.getItem() instanceof StoneRune) || SpineGlyphs.runeIndex(id) < 0) return null;
                occupied[slot] = true;
            }
        }
        return SpineGlyphLayout.findSlots(occupied, !hasCatalyst);
    }

    private static SpineGlyphs.Material material(ItemStack stack) {
        if (stack.is(ItemInit.WIZARD_CHALK.get())) return SpineGlyphs.Material.CHALK;
        if (stack.is(ItemInit.RUNESMITH_HAMMER.get())) return SpineGlyphs.Material.METAL;
        return null;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
        NonNullList<ItemStack> remaining = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);
        ItemStack catalyst = container.getItem(3);
        if (material(catalyst) != null && catalyst.getDamageValue() + 1 < catalyst.getMaxDamage()) {
            ItemStack damaged = catalyst.copy();
            damaged.setCount(1);
            damaged.setDamageValue(catalyst.getDamageValue() + 1);
            remaining.set(3, damaged);
        }
        return remaining;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= 3 && height >= 3;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return MoreMnAGlyphUses.DECORATE_TOME.get();
    }
}
