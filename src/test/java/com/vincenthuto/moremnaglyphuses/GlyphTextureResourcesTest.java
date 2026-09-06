package com.vincenthuto.moremnaglyphuses;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.Test;

class GlyphTextureResourcesTest {
    @Test
    void everySelectableRuneHasATransparentSixteenPixelGlyphTexture() throws Exception {
        for (String color : SpineGlyphs.CHALK_RUNE_LETTERS.keySet()) {
            var resource = getClass().getResource(
                    "/assets/moremnaglyphuses/textures/item/spine_glyphs/" + color + ".png");
            assertNotNull(resource, color);
            BufferedImage image = ImageIO.read(resource);
            var chalkResource = getClass().getResource("/assets/mna/textures/block/rituals/chalk_rune_"
                    + SpineGlyphs.CHALK_RUNE_LETTERS.get(color) + ".png");
            assertNotNull(chalkResource, color + " chalk source");
            BufferedImage chalk = ImageIO.read(chalkResource);
            BufferedImage itemGlyph = ImageIO.read(assertResource(
                    "/assets/mna/textures/item/rituals/stone_rune_" + color + ".png", color));
            BufferedImage blank = ImageIO.read(assertResource(
                    "/assets/mna/textures/item/rituals/stone_rune_blank.png", "blank"));
            assertEquals(16, image.getWidth(), color);
            assertEquals(16, image.getHeight(), color);
            boolean hasTransparentPixel = false;
            boolean hasVisiblePixel = false;
            for (int y = 0; y < 16; y++) {
                for (int x = 0; x < 16; x++) {
                    int alpha = image.getRGB(x, y) >>> 24;
                    assertEquals(chalk.getRGB(x, y) >>> 24, alpha, color + " alpha at " + x + "," + y);
                    hasTransparentPixel |= alpha == 0;
                    hasVisiblePixel |= alpha != 0;
                }
            }
            assertEquals(true, hasTransparentPixel, color);
            assertEquals(true, hasVisiblePixel, color);
            int dominantColor = dominantChangedColor(itemGlyph, blank);
            boolean containsDominantColor = false;
            for (int y = 0; y < 16; y++) for (int x = 0; x < 16; x++)
                containsDominantColor |= (image.getRGB(x, y) & 0xffffff) == dominantColor;
            assertEquals(true, containsDominantColor, color + " dominant item-glyph color");
        }
    }

    private java.net.URL assertResource(String path, String name) {
        var resource = getClass().getResource(path);
        assertNotNull(resource, name);
        return resource;
    }

    private static int dominantChangedColor(BufferedImage glyph, BufferedImage blank) {
        var counts = new HashMap<Integer, Integer>();
        for (int y = 0; y < 16; y++) for (int x = 0; x < 16; x++) {
            int pixel = glyph.getRGB(x, y);
            if (pixel != blank.getRGB(x, y)) counts.merge(pixel & 0xffffff, 1, Integer::sum);
        }
        return counts.entrySet().stream().max(java.util.Map.Entry.comparingByValue()).orElseThrow().getKey();
    }
}
