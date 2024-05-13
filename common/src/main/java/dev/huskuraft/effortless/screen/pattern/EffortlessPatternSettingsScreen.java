package dev.huskuraft.effortless.screen.pattern;

import java.util.function.Consumer;

import dev.huskuraft.effortless.EffortlessClient;
import dev.huskuraft.effortless.api.gui.AbstractPanelScreen;
import dev.huskuraft.effortless.api.gui.button.Button;
import dev.huskuraft.effortless.api.gui.text.TextWidget;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.text.ChatFormatting;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.building.pattern.Pattern;
import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;
import dev.huskuraft.effortless.screen.transformer.EffortlessItemRandomizerEditScreen;
import dev.huskuraft.effortless.screen.transformer.EffortlessTransformerEditScreen;
import dev.huskuraft.effortless.screen.transformer.EffortlessTransformerPresetsSelectScreen;
import dev.huskuraft.effortless.screen.transformer.TransformerList;

public class EffortlessPatternSettingsScreen extends AbstractPanelScreen {

    private final Consumer<Pattern> applySettings;
    private final Pattern defaultSettings;
    private Pattern lastSettings;
    private TextWidget titleTextWidget;
    private TransformerList entries;
    private Button upButton;
    private Button downButton;
    private Button editButton;
    private Button deleteButton;
    private Button clearButton;
    private Button addButton;
    private Button enableButton;
    private Button saveButton;

    public EffortlessPatternSettingsScreen(Entrance entrance) {
        super(entrance, Text.translate("effortless.pattern.simple.title").withStyle(ChatFormatting.DARK_GRAY), PANEL_WIDTH_EXPANDED, PANEL_HEIGHT_270);
        this.applySettings = pattern -> {
            getEntrance().getStructureBuilder().setPattern(getEntrance().getClient().getPlayer() , lastSettings);
        };
        this.defaultSettings = getEntrance().getStructureBuilder().getContext(getEntrance().getClient().getPlayer()).pattern();
        this.lastSettings = getEntrance().getStructureBuilder().getContext(getEntrance().getClient().getPlayer()).pattern();
    }

    @Override
    protected EffortlessClient getEntrance() {
        return (EffortlessClient) super.getEntrance();
    }

    @Override
    public void onCreate() {
        setPanelHeight(lastSettings.enabled() ? PANEL_HEIGHT_270 : PANEL_TITLE_HEIGHT_1 + PANEL_BUTTON_ROW_HEIGHT_2);
        setPanelWidth(lastSettings.enabled() ? PANEL_WIDTH_EXPANDED : PANEL_WIDTH);

        this.titleTextWidget = addWidget(new TextWidget(getEntrance(), getLeft() + getWidth() / 2, getTop() + PANEL_TITLE_HEIGHT_1 - 10, getScreenTitle().withColor(0x00404040), TextWidget.Gravity.CENTER));

        this.enableButton = addWidget(Button.builder(getEntrance(), lastSettings.enabled() ? Text.translate("effortless.pattern.button.disable") : Text.translate("effortless.pattern.button.enable"), button -> {
            this.lastSettings = lastSettings.withEnabled(!lastSettings.enabled());
            recreate();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), PANEL_TITLE_HEIGHT_1 + PANEL_BUTTON_ROW_HEIGHT_1, 0f, 0f, 1f).build());

        this.saveButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.done"), button -> {
            this.lastSettings = lastSettings.withEnabled(!lastSettings.transformers().isEmpty() && lastSettings.enabled());
            applySettings.accept(lastSettings);
            detach();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 0f, 0f, 1f).build());

        this.entries = addWidget(new TransformerList(getEntrance(), getLeft() + AbstractPanelScreen.PADDINGS, getTop() + PANEL_TITLE_HEIGHT_1 + PANEL_BUTTON_ROW_HEIGHT_1N, getWidth() - AbstractPanelScreen.PADDINGS * 2 - 8 /* scrollbar */, getHeight() - PANEL_TITLE_HEIGHT_1 - PANEL_BUTTON_ROW_HEIGHT_1N - PANEL_BUTTON_ROW_HEIGHT_2));
        this.entries.reset(lastSettings.transformers());
        this.entries.setAlwaysShowScrollbar(true);

        this.upButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.up"), button -> {
            if (entries.hasSelected()) {
                entries.moveUpSelected();
            }
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 0f, 0.25f).build());
        this.downButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.down"), button -> {
            if (entries.hasSelected()) {
                entries.moveDownSelected();
            }
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 0.25f, 0.25f).build());

        this.editButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.edit"), button -> {
            if (entries.hasSelected()) {
                var item = entries.getSelected().getItem();
                switch (item.getType()) {
                    case ARRAY, MIRROR, RADIAL -> {
                        new EffortlessTransformerEditScreen(
                                getEntrance(),
                                transformer -> {
                                    entries.replaceSelect(transformer);
                                    onReload();
                                },
                                item
                        ).attach();
                    }
                    case ITEM_RAND -> {
                        new EffortlessItemRandomizerEditScreen(
                                getEntrance(),
                                transformer -> {
                                    entries.replaceSelect(transformer);
                                    onReload();
                                },
                                (ItemRandomizer) item
                        ).attach();
                    }
                }

            }
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 0f, 0.25f).build());

        this.deleteButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.delete"), button -> {
            if (entries.hasSelected()) {
                entries.deleteSelected();
            }
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 0.25f, 0.25f).build());

        this.clearButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.clear"), button -> {
            entries.clear();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 0.5f, 0.25f).build());

        this.addButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.add"), button -> {
            new EffortlessTransformerPresetsSelectScreen(
                    getEntrance(),
                    transformer -> {
                        entries.insertSelected(transformer.withRandomId().withName(Text.empty()));
                        onReload();
                    }
            ).attach();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 0.75f, 0.25f).build());

    }

    @Override
    public void onReload() {
        lastSettings = new Pattern(lastSettings.id(), lastSettings.enabled(), entries.items());
        entries.setVisible(lastSettings.enabled());

        upButton.setActive(entries.hasSelected() && entries.indexOfSelected() > 0);
        downButton.setActive(entries.hasSelected() && entries.indexOfSelected() < entries.children().size() - 1);
        editButton.setActive(entries.hasSelected());
        deleteButton.setActive(entries.hasSelected());
        addButton.setActive(entries.items().size() < 4);

        upButton.setVisible(getEntrance().getClient().getWindow().isAltDown() && lastSettings.enabled());
        downButton.setVisible(getEntrance().getClient().getWindow().isAltDown() && lastSettings.enabled());
        editButton.setVisible(!getEntrance().getClient().getWindow().isAltDown() && lastSettings.enabled());
        deleteButton.setVisible(!getEntrance().getClient().getWindow().isAltDown() && lastSettings.enabled());
        clearButton.setVisible(lastSettings.enabled());
        addButton.setVisible(lastSettings.enabled());

    }

}
