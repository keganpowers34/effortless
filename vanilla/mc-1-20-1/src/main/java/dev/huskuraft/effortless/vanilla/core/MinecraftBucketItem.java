package dev.huskuraft.effortless.vanilla.core;

import java.util.function.Supplier;

import dev.huskuraft.effortless.api.core.Block;
import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.BlockState;
import dev.huskuraft.effortless.api.core.BucketItem;
import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.core.World;
import dev.huskuraft.effortless.api.core.fluid.Fluid;
import dev.huskuraft.effortless.api.text.Text;

public record MinecraftBucketItem(net.minecraft.world.item.BucketItem refs) implements BucketItem {

    @Override
    public Fluid getContent() {
        return getBucketFluid(refs);
    }

    @Override
    public boolean useContent(World world, Player player, BlockPosition blockPosition, BlockInteraction blockInteraction) {
        return refs.emptyContents(player.reference(), world.reference(), MinecraftConvertor.toPlatformBlockPosition(blockPosition), MinecraftConvertor.toPlatformBlockInteraction(blockInteraction));
    }

    @Override
    public void useExtraContent(World world, Player player, BlockPosition blockPosition, ItemStack itemStack) {
        refs.checkExtraContent(player.reference(), world.reference(), itemStack.reference(), MinecraftConvertor.toPlatformBlockPosition(blockPosition));
    }

    @Override
    public ItemStack getDefaultStack() {
        return new MinecraftItem(refs).getDefaultStack();
    }

    @Override
    public Block getBlock() {
        return new MinecraftItem(refs).getBlock();
    }

    @Override
    public ResourceLocation getId() {
        return new MinecraftItem(refs).getId();
    }

    @Override
    public boolean isCorrectToolForDrops(BlockState blockState) {
        return new MinecraftItem(refs).isCorrectToolForDrops(blockState);
    }

    @Override
    public int getMaxStackSize() {
        return new MinecraftItem(refs).getMaxStackSize();
    }

    @Override
    public int getMaxDamage() {
        return new MinecraftItem(refs).getMaxDamage();
    }

    @Override
    public boolean mineBlock(World world, Player player, BlockPosition blockPosition, BlockState blockState, ItemStack itemStack) {
        return new MinecraftItem(refs).mineBlock(world, player, blockPosition, blockState, itemStack);
    }

    @Override
    public Text getName(ItemStack itemStack) {
        return new MinecraftItem(refs).getName(itemStack);
    }


    public static Fluid getBucketFluid(net.minecraft.world.item.BucketItem bucketItem) {
        for (var declaredField : net.minecraft.world.item.BucketItem.class.getDeclaredFields()) {
            // Fluid content (fabric & forge)
            if (declaredField.getType().equals(net.minecraft.world.level.material.Fluid.class)) {
                try {
                    declaredField.trySetAccessible();
                    var fluid = (net.minecraft.world.level.material.Fluid) declaredField.get(bucketItem);
                    if (fluid != null) {
                        return MinecraftFluid.ofNullable(fluid);
                    }
                } catch (IllegalAccessException | SecurityException e) {
                    throw new RuntimeException(e);
                }
            }
            // Supplier<Fluid> fluidSupplier (forge only)
            if (declaredField.getType().equals(Supplier.class)) {
                try {
                    declaredField.trySetAccessible();
                    var fluidSupplier = (Supplier<?>) declaredField.get(bucketItem);
                    var fluid = (net.minecraft.world.level.material.Fluid) fluidSupplier.get();
                    if (fluid != null) {
                        return MinecraftFluid.ofNullable(fluid);
                    }
                } catch (IllegalAccessException | SecurityException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return null;
    }

}
