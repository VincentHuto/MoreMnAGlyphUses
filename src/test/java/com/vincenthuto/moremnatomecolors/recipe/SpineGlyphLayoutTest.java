package com.vincenthuto.moremnatomecolors.recipe;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class SpineGlyphLayoutTest {
    @Test
    void acceptsAnyOccupiedPositionsInRightColumn() {
        assertArrayEquals(new int[]{2, 5, 8}, SpineGlyphLayout.findSlots(new boolean[]{false, false, true, false, false, true, false, false, false}));
        assertArrayEquals(new int[]{2, 5, 8}, SpineGlyphLayout.findSlots(new boolean[]{false, false, true, false, false, true, false, false, true}));
        assertArrayEquals(new int[]{2, 5, 8}, SpineGlyphLayout.findSlots(new boolean[]{false, false, false, false, false, false, false, false, true}));
    }

    @Test
    void catalystFreeLayoutAlsoAcceptsTheLeftColumn() {
        assertArrayEquals(new int[]{0, 3, 6}, SpineGlyphLayout.findSlots(
                new boolean[]{true, false, false, true, false, false, true, false, false}, true));
        assertNull(SpineGlyphLayout.findSlots(
                new boolean[]{true, false, false, true, false, false, true, false, false}, false));
    }

    @Test
    void rejectsMixedColumnsAndNonColumnSlots() {
        assertArrayEquals(new int[]{0, 3, 6}, SpineGlyphLayout.findSlots(
                new boolean[]{true, false, false, false, false, false, true, false, false}, true));
        assertNull(SpineGlyphLayout.findSlots(new boolean[]{true, false, false, false, false, true, false, false, false}));
        assertNull(SpineGlyphLayout.findSlots(new boolean[]{false, true, false, false, false, false, false, false, false}));
        assertNull(SpineGlyphLayout.findSlots(new boolean[]{true, false, false, true, false, false, true, false, false}));
    }
}
