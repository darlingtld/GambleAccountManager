package com.gamble;

public enum ModeEnum {
    MASTER("master"),
    BASIC("basic"),
    MODE_135_6810("135-6810"),
    MODE_147_258("147-258"),
    MODE_147_369("147-369"),
    MODE_246_579("246-579"),
    MODE_258_369("258-369");

    private String mode;

    ModeEnum(String mode) {
        this.mode = mode;
    }

    public String getMode() {
        return mode;
    }
}
