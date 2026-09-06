package com.vincenthuto.moremnaglyphuses.client;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ArmorGlyphTransformTest {
    @Test
    void everyProfileRendersOutsideTheChestArmorSurface() {
        ArmorGlyphTransform.PROFILES.forEach((id, profile) -> {
            assertTrue(profile.z() > 3.0 / 16.0, id + " is inside the chest armor");
            assertEquals(180.0F, profile.roll(), id + " is upside down");
        });
    }
}
