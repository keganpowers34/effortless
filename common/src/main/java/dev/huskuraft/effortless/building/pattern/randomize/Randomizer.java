package dev.huskuraft.effortless.building.pattern.randomize;

import java.util.Collection;
import java.util.UUID;

import dev.huskuraft.effortless.api.core.Item;
import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.building.BuildStage;
import dev.huskuraft.effortless.building.pattern.Transformer;

public interface Randomizer<T> extends Transformer {

    static Category extract(Object object) {
        if (object instanceof Item) {
            return Category.ITEM;
        }
        throw new IllegalArgumentException("Invalid object: " + object);
    }

    Order getOrder();

    Target getTarget();

    Category getCategory();

    Collection<Chance<T>> getChances();

    Producer<T> asProducer(long seed);

    enum Order {
        SEQUENCE("sequence"),
        RANDOM("random");

        private final String name;

        Order(String name) {
            this.name = name;
        }

        public Text getDisplayName() {
            return Text.translate("effortless.randomizer.order.%s".formatted(name));
        }

        public String getName() {
            return name;
        }
    }

    enum Target {
        SINGLE("single"),
        GROUP("group");

        private final String name;

        Target(String name) {
            this.name = name;
        }

        public Text getDisplayName() {
            return Text.translate("effortless.randomizer.target.%s".formatted(name));
        }

        public String getName() {
            return name;
        }
    }

    enum Category {
        ITEM("item", ItemStack.class);

        private final String name;
        private final Class<?> clazz;

        Category(String name, Class<?> clazz) {
            this.name = name;
            this.clazz = clazz;
        }

        public Text getDisplayName() {
            return Text.translate("effortless.randomizer.category.%s".formatted(name));
        }

        public String getName() {
            return name;
        }
    }

    @Override
    Randomizer<T> withId(UUID id);

    @Override
    Randomizer<T> withName(Text name);

    @Override
    default Randomizer<T> finalize(Player player, BuildStage stage) {
        return this;
    }
}
