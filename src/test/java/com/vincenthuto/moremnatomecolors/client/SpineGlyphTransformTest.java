package com.vincenthuto.moremnatomecolors.client;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class SpineGlyphTransformTest {
    @Test
    void anchorsGlyphsOnTheClosedBookSpineInTopToBottomOrder() {
        assertArrayEquals(new double[]{-0.2, 0.18, 0.0}, SpineGlyphTransform.position(0), 0.0001);
        assertArrayEquals(new double[]{-0.2, 0.05, 0.0}, SpineGlyphTransform.position(1), 0.0001);
        assertArrayEquals(new double[]{-0.2, -0.08, 0.0}, SpineGlyphTransform.position(2), 0.0001);
    }

    @Test
    void exposesAnIndependentFactionTomeTransform() {
        assertArrayEquals(new double[]{-0.25, 0.18, 0.0}, SpineGlyphTransform.factionPosition(0), 0.0001);
        assertArrayEquals(new double[]{-0.25, 0.05, 0.0}, SpineGlyphTransform.factionPosition(1), 0.0001);
        assertArrayEquals(new double[]{-0.25, -0.08, 0.0}, SpineGlyphTransform.factionPosition(2), 0.0001);
        assertArrayEquals(new double[]{-0.3, 0.18, 0.0}, SpineGlyphTransform.undeadFactionPosition(0), 0.0001);
    }

    @Test
    void exposesIndependentInventoryTransforms() {
        assertArrayEquals(new double[]{0.06, 0.72, 0.5}, SpineGlyphTransform.inventoryPosition(0), 0.0001);
        assertArrayEquals(new double[]{0.06, 0.50, 0.5}, SpineGlyphTransform.inventoryPosition(1), 0.0001);
        assertArrayEquals(new double[]{0.06, 0.28, 0.5}, SpineGlyphTransform.inventoryPosition(2), 0.0001);
        assertArrayEquals(new double[]{0.06, 0.72, 0.5}, SpineGlyphTransform.factionInventoryPosition(0), 0.0001);
        assertArrayEquals(new double[]{0.01, 0.72, 0.75}, SpineGlyphTransform.undeadFactionInventoryPosition(0), 0.0001);
        org.junit.jupiter.api.Assertions.assertEquals(0.30F, SpineGlyphTransform.inventoryScale(), 0.0001F);
    }

    @Test
    void rotatesGroundRunesUprightOnTheSpine() {
        assertEquals(0.0F, SpineGlyphTransform.runeRotationDegrees(), 0.0001F);
    }
}
