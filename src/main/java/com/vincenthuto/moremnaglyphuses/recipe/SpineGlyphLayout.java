package com.vincenthuto.moremnaglyphuses.recipe;

public final class SpineGlyphLayout {
    private SpineGlyphLayout() {}

    public static int[] findSlots(boolean[] occupied) {
        return findSlots(occupied, false);
    }

    public static int[] findSlots(boolean[] occupied, boolean allowLeftColumn) {
        if (occupied.length != 9) return null;
        int[] columns = allowLeftColumn ? new int[]{0, 2} : new int[]{2};
        for (int column : columns) {
            boolean any = false;
            boolean valid = true;
            for (int slot = 0; slot < 9; slot++) {
                if (occupied[slot]) {
                    any = true;
                    if (slot % 3 != column) {
                        valid = false;
                        break;
                    }
                }
            }
            if (any && valid) {
                return new int[]{column, column + 3, column + 6};
            }
        }
        return null;
    }
}
