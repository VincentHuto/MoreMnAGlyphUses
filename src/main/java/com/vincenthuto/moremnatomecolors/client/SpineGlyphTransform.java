package com.vincenthuto.moremnatomecolors.client;

public final class SpineGlyphTransform {
    private SpineGlyphTransform() {}

    public static double[] position(int index) {
        return new double[]{-0.2, 0.18 - index * 0.13 , 0.0};
    }

    public static double[] factionPosition(int index) {
        return new double[]{-0.25, 0.18 - index * 0.13, 0.0};
    }
    public static double[] undeadFactionPosition(int index) {
        return new double[]{-0.3, 0.18 - index * 0.13, 0.0};
    }

    public static double[] inventoryPosition(int index) {
        return new double[]{0.06, 0.72 - index * 0.22, 0.5};
    }

    public static double[] factionInventoryPosition(int index) {
        return new double[]{0.06, 0.72 - index * 0.22, 0.5};
    }

    public static double[] undeadFactionInventoryPosition(int index) {
        return new double[]{0.01, 0.72 - index * 0.22, 0.75};
    }

    public static float inventoryScale() {
        return 0.30F;
    }

    public static float runeRotationDegrees() {
        return 0.0F;
    }
}
