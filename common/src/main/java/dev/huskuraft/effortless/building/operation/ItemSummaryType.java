package dev.huskuraft.effortless.building.operation;

public enum ItemSummaryType {
    BLOCKS_PLACED,
    BLOCKS_DESTROYED,
    BLOCKS_INTERACTED,

    BLOCKS_PLACE_INSUFFICIENT,
    BLOCKS_BREAK_INSUFFICIENT,

    BLOCKS_NOT_PLACEABLE,
    BLOCKS_NOT_BREAKABLE,
    BLOCKS_NOT_INTERACTABLE,


    BLOCKS_PLACE_NOT_WHITELISTED,
    BLOCKS_BREAK_NOT_WHITELISTED,
    BLOCKS_PLACE_BLACKLISTED,
    BLOCKS_BREAK_BLACKLISTED;


    public boolean isSuccess() {
        return this == BLOCKS_PLACED || this == BLOCKS_DESTROYED || this == BLOCKS_INTERACTED;
    }

}
