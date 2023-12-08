package dev.huskuraft.effortless.building.pattern;

import dev.huskuraft.effortless.building.operation.TransformableOperation;
import dev.huskuraft.effortless.building.operation.batch.BatchOperation;
import dev.huskuraft.effortless.text.Text;

import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

public abstract class Transformer {

    private final UUID id;
    private final Text name;

    public Transformer(UUID id, Text name) {
        this.id = id;
        this.name = name;
    }

    public abstract BatchOperation transform(TransformableOperation operation);

    public final UUID getId() {
        return id;
    }

    public final Text getName() {
        return name;
    }

    public abstract Transformers getType();

    public abstract Stream<Text> getSearchableTags();

    public abstract boolean isValid();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transformer that)) return false;

        if (!Objects.equals(id, that.id)) return false;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }


}