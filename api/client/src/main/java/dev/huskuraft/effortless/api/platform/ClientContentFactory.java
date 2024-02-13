package dev.huskuraft.effortless.api.platform;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.api.input.KeyBinding;
import dev.huskuraft.effortless.api.input.KeyCodes;
import dev.huskuraft.effortless.api.input.OptionKeys;
import dev.huskuraft.effortless.api.renderer.RenderStateFactory;
import dev.huskuraft.effortless.api.sound.SoundFactory;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.api.texture.TextureFactory;

public interface ClientContentFactory extends ContentFactory {

    SearchTree<ItemStack> searchItemStack(SearchBy searchBy);

	default <T> SearchTree<T> searchByText(List<T> list, Function<T, Stream<Text>> keyExtractor) {
		return null;
	}

	@Deprecated
	<T> SearchTree<T> search(List<T> list, Function<T, Stream<Text>> keyExtractor);

    KeyBinding getOptionKeyBinding(OptionKeys key);

    KeyBinding newKeyBinding(String name, String category, KeyCodes key);

    RenderStateFactory getRenderStateFactory();

    TextureFactory getTextureFactory();

    SoundFactory getSoundFactory();

    static ClientContentFactory getInstance() {
        return ClientEntrance.getInstance().getContentFactory();
    }

}
