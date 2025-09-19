package com.pharos.testnfcintent;

public enum M1KeyTypeEnum {
    KEYTYPE_A("Key Type A"),
    KEYTYPE_B("Key Type B");

    private final String displayName;

    M1KeyTypeEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    public static M1KeyTypeEnum from(String text) {
        if (text == null) return null;
        for (M1KeyTypeEnum e : values()) {
            if (e.displayName.equalsIgnoreCase(text) || e.name().equalsIgnoreCase(text)) {
                return e;
            }
        }
        return null;
    }
}
