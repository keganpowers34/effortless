package dev.huskuraft.effortless.core;

public abstract class BlockData {

    public abstract BlockData mirror(Axis axis);

    public abstract BlockData rotate(Revolve revolve);

    public abstract boolean isAir();

    public abstract Item getItem();

    public abstract boolean isReplaceable(Player player, BlockInteraction interaction);

    public abstract boolean isReplaceable();

    public abstract boolean isDestroyable();
}