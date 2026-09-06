package com.vincenthuto.moremnaglyphuses.client;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ArmorGlyphTransformTest {
    @Test
    void factionProfilesFollowTheirArmorSpines() {
        var council = ArmorGlyphTransform.PROFILES.get("council_armor_chest");
        assertEquals(0.16, council.x(0));
        assertEquals(-0.16, council.x(1));
        assertEquals(0.0, council.x(2));
        assertEquals(0.25, council.y(0));
        assertEquals(0.25, council.y(1));
        assertEquals(0.34, council.y(2));

        var fey = ArmorGlyphTransform.PROFILES.get("fey_armor_chest");
        assertEquals(0.06, fey.x(0));
        assertEquals(0.0, fey.x(1));
        assertEquals(0.0, fey.x(2));

        for (String id : new String[]{"bone_armor_chest", "demon_armor_chest"}) {
            var unchanged = ArmorGlyphTransform.PROFILES.get(id);
            assertEquals(0.0, unchanged.x(0));
            assertEquals(0.0, unchanged.x(1));
            assertEquals(0.0, unchanged.x(2));
        }
    }

    @Test
    void everyProfileRendersOutsideTheChestArmorSurface() {
        ArmorGlyphTransform.PROFILES.forEach((id, profile) -> {
            assertTrue(profile.z() > 3.0 / 16.0, id + " is inside the chest armor");
            assertEquals(180.0F, profile.roll(), id + " is upside down");
        });
    }
}
