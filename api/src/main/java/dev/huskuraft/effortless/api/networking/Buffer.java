package dev.huskuraft.effortless.api.networking;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import dev.huskuraft.effortless.api.core.Item;
import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.api.core.Items;
import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.math.Vector2d;
import dev.huskuraft.effortless.api.math.Vector2i;
import dev.huskuraft.effortless.api.math.Vector3d;
import dev.huskuraft.effortless.api.math.Vector3i;
import dev.huskuraft.effortless.api.text.Text;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public final class Buffer extends WrappedByteBuf {

    public Buffer(ByteBuf source) {
        super(source);
    }

    public static Buffer newBuffer() {
        return new Buffer(Unpooled.buffer());
    }

    public <T> T readNullable(BufferReader<T> reader) {
        if (readBoolean()) return read(reader);
        return null;
    }

    public UUID readUUID() {
        return new UUID(readLong(), readLong());
    }

    public <T extends Enum<T>> T readEnum(Class<T> clazz) {
        return clazz.getEnumConstants()[readVarInt()];
    }

    public String readString() {
        return Utf8String.read(this, 1024);
    }

    public Text readText() {
        return Text.text(readString())
                .withBold(readNullable(Buffer::readBoolean))
                .withItalic(readNullable(Buffer::readBoolean))
                .withUnderlined(readNullable(Buffer::readBoolean))
                .withStrikethrough(readNullable(Buffer::readBoolean))
                .withObfuscated(readNullable(Buffer::readBoolean))
                .withColor(readNullable(Buffer::readInt));
    }

    public int readVarInt() {
        return VarInt.read(this);
    }

    public long readVarLong() {
        return VarLong.read(this);
    }

    // use Registries
    public Item readItem() {
        // FIXME: 4/5/24
        return Items.AIR;
    }

    public ItemStack readItemStack() {
        // FIXME: 4/5/24
        return ItemStack.empty();
    }

    public <T> T read(BufferReader<T> reader) {
        return reader.read(this);
    }

    public <T> List<T> readList(BufferReader<T> reader) {
        var i = readInt();
        var list = new ArrayList<T>();

        for (int j = 0; j < i; ++j) {
            list.add(read(reader));
        }
        return Collections.unmodifiableList(list);
    }

    public <K, V> Map<K, V> readMap(BufferReader<K> keyReader, BufferReader<V> valueReader) {
        var i = readInt();
        var map = new LinkedHashMap<K, V>();

        for (int j = 0; j < i; ++j) {
            map.put(read(keyReader), read(valueReader));
        }
        return Collections.unmodifiableMap(map);
    }

    public <T> void writeNullable(T value, BufferWriter<T> writer) {
        writeBoolean(value != null);
        if (value != null) write(value, writer);
    }

    public void writeUUID(UUID uuid) {
        writeLong(uuid.getMostSignificantBits());
        writeLong(uuid.getLeastSignificantBits());
    }

    public <T extends Enum<T>> void writeEnum(Enum<T> value) {
        writeVarInt(value.ordinal());
    }

    public void writeString(String value) {
        Utf8String.write(this, value, 1024);
    }

    public void writeText(Text value) {
        writeString(value.getString());
        writeNullable(value.isBold(), Buffer::writeBoolean);
        writeNullable(value.isItalic(), Buffer::writeBoolean);
        writeNullable(value.isUnderlined(), Buffer::writeBoolean);
        writeNullable(value.isStrikethrough(), Buffer::writeBoolean);
        writeNullable(value.isObfuscated(), Buffer::writeBoolean);
        writeNullable(value.getColor(), Buffer::writeInt);
    }

    public void writeVarInt(int value) {
        VarInt.write(this, value);
    }

    public void writeVarLong(long value) {
        VarLong.write(this, value);
    }


    public void writeItem(Item value) {

    }

    // TODO: 7/12/23 extract
    public void writeItemStack(ItemStack value) {

    }

    public <T> void write(T value, BufferWriter<T> writer) {
        writer.write(this, value);
    }

    public <T> void writeList(Collection<T> collection, BufferWriter<T> writer) {
        writeInt(collection.size());
        for (var object : collection) {
            write(object, writer);
        }
    }

    public <K, V> void writeMap(Map<K, V> map, BufferWriter<K> keyWriter, BufferWriter<V> valueWriter) {
        writeInt(map.size());
        for (var entry : map.entrySet()) {
            keyWriter.write(this, entry.getKey());
            valueWriter.write(this, entry.getValue());
        }
    }

    public Vector3d readVector3d() {
        return new Vector3d(readDouble(), readDouble(), readDouble());
    }

    public void writeVector3d(Vector3d vector) {
        writeDouble(vector.x());
        writeDouble(vector.y());
        writeDouble(vector.z());
    }

    public Vector2d readVector2d() {
        return new Vector2d(readDouble(), readDouble());
    }

    public void writeVector2d(Vector2d vector) {
        writeDouble(vector.x());
        writeDouble(vector.y());
    }

    public Vector3i readVector3i() {
        return new Vector3i(readInt(), readInt(), readInt());
    }

    public void writeVector3i(Vector3i vector) {
        writeInt(vector.x());
        writeInt(vector.y());
        writeInt(vector.z());
    }

    public Vector2i readVector2i() {
        return new Vector2i(readInt(), readInt());
    }

    public void writeVector2i(Vector2i vector) {
        writeInt(vector.x());
        writeInt(vector.y());
    }

    public ResourceLocation readResourceLocation() {
        return ResourceLocation.of(readString(), readString());
    }

    public void writeResourceLocation(ResourceLocation resourceLocation) {
        writeString(resourceLocation.getNamespace());
        writeString(resourceLocation.getPath());
    }

}
