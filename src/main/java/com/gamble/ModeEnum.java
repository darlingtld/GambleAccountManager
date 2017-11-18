package com.gamble;

public enum ModeEnum {
    MASTER("master"),
    BASIC("basic"),
    PORT_26000("port_26000"),
    PORT_26001("port_26001") ;

    private String mode;

    ModeEnum(String mode) {
        this.mode = mode;
    }

    public String getMode() {
        return mode;
    }
}
