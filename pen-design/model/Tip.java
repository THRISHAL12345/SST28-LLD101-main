package model;

import model.enums.TipType;

public class Tip {

    private TipType type;

    public Tip(TipType type) {
        this.type = type;
    }

    public TipType getType() {
        return type;
    }
}