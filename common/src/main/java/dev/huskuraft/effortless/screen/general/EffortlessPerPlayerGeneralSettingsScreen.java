package dev.huskuraft.effortless.screen.general;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import dev.huskuraft.effortless.EffortlessClient;
import dev.huskuraft.effortless.api.core.Item;
import dev.huskuraft.effortless.api.core.PlayerInfo;
import dev.huskuraft.effortless.api.gui.AbstractPanelScreen;
import dev.huskuraft.effortless.api.gui.AbstractWidget;
import dev.huskuraft.effortless.api.gui.button.Button;
import dev.huskuraft.effortless.api.gui.text.TextWidget;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.text.ChatFormatting;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.screen.item.EffortlessItemsScreen;
import dev.huskuraft.effortless.screen.player.PlayerInfoList;
import dev.huskuraft.effortless.screen.settings.SettingOptionsList;
import dev.huskuraft.effortless.session.config.GeneralConfig;

public class EffortlessPerPlayerGeneralSettingsScreen extends AbstractPanelScreen {

    private final PlayerInfo playerInfo;
    private final BiConsumer<PlayerInfo, GeneralConfig> consumer;
    private GeneralConfig defaultConfig;
    private GeneralConfig originalConfig;
    private GeneralConfig config;
    private GeneralConfig globalConfig;
    private AbstractWidget resetButton;
    private AbstractWidget saveButton;

    public EffortlessPerPlayerGeneralSettingsScreen(Entrance entrance, PlayerInfo playerInfo, GeneralConfig config, BiConsumer<PlayerInfo, GeneralConfig> consumer) {
        super(entrance, Text.translate("effortless.general_settings.title"), PANEL_WIDTH_EXPANDED, PANEL_HEIGHT_270);
        this.playerInfo = playerInfo;
        this.defaultConfig = GeneralConfig.NULL;
        this.originalConfig = config;
        this.config = config;
        this.globalConfig = getEntrance().getSessionManager().getServerSessionConfigOrEmpty().getGlobalConfig();
        this.consumer = consumer;
    }

    @Override
    public void onCreate() {
        var titleTextWidget = addWidget(new TextWidget(getEntrance(), getLeft() + getWidth() / 2, getTop() + PANEL_TITLE_HEIGHT_1 - 10, getScreenTitle().withColor(0x00404040), TextWidget.Gravity.CENTER));
        var playerNameTextWidget = addWidget(new TextWidget(getEntrance(), getLeft() + getWidth() / 2, getTop() + PANEL_TITLE_HEIGHT_1 * 2 - 10, Text.text(playerInfo.getName()).withStyle(ChatFormatting.DARK_GRAY), TextWidget.Gravity.CENTER));

        var playerEntries = addWidget(new PlayerInfoList(getEntrance(), getLeft() + PADDINGS_H, getTop() + PANEL_TITLE_HEIGHT_1, getWidth() - PADDINGS_H * 2, 36, true));
        playerEntries.setShowScrollBar(false);
        playerEntries.setRenderSelection(false);
        playerEntries.reset(List.of(playerInfo));

        var entries = addWidget(new SettingOptionsList(getEntrance(), getLeft() + PADDINGS_H, getTop() + PANEL_TITLE_HEIGHT_1 + playerEntries.getHeight() + INNER_PADDINGS_V, getWidth() - PADDINGS_H * 2 - 8, getHeight() - PANEL_TITLE_HEIGHT_1 - playerEntries.getHeight() - INNER_PADDINGS_V - PANEL_BUTTON_ROW_HEIGHT_1, false, true));
        entries.setAlwaysShowScrollbar(true);
//        bindEntry(
//                entries.addSwitchEntry(Text.translate("effortless.global_general_settings.use_commands"), null, null, null),
//                value -> {
//                    this.config = new GeneralConfig(value, config.allowUseMod(), config.allowBreakBlocks(), config.allowPlaceBlocks(), config.maxReachDistance(), config.maxBlockBreakVolume(), config.maxBlockPlaceVolume(), config.maxBoxSideLengthPerBreak(), config.maxBoxSideLengthPerPlace(), config.whitelistedItems(), config.blacklistedItems());
//                },
//                () -> globalConfig.useCommands(),
//                () -> config.useCommands()
//        );

        bindEntry(
                entries.addSwitchEntry(Text.translate("effortless.global_general_settings.allow_use_mod"), null, null, null),
                (value) -> {
                    this.config = new GeneralConfig(config.useCommands(), value, config.allowBreakBlocks(), config.allowPlaceBlocks(), config.allowInteractBlocks(), config.maxReachDistance(), config.maxBlockPlaceVolume(), config.maxBlockBreakVolume(), config.whitelistedItems(), config.blacklistedItems());
                },
                () -> globalConfig.allowUseMod(),
                () -> config.allowUseMod()
        );
        bindEntry(
                entries.addSwitchEntry(Text.translate("effortless.global_general_settings.allow_break_blocks"), null, null, null),
                (value) -> {
                    this.config = new GeneralConfig(config.useCommands(), config.allowUseMod(), value, config.allowPlaceBlocks(), config.allowInteractBlocks(), config.maxReachDistance(), config.maxBlockBreakVolume(), config.maxBlockPlaceVolume(), config.whitelistedItems(), config.blacklistedItems());
                },
                () -> globalConfig.allowBreakBlocks(),
                () -> config.allowBreakBlocks()
        );
        bindEntry(
                entries.addSwitchEntry(Text.translate("effortless.global_general_settings.allow_place_blocks"), null, null, null),
                (value) -> {
                    this.config = new GeneralConfig(config.useCommands(), config.allowUseMod(), config.allowBreakBlocks(), value, config.allowInteractBlocks(), config.maxReachDistance(), config.maxBlockBreakVolume(), config.maxBlockPlaceVolume(), config.whitelistedItems(), config.blacklistedItems());
                },
                () -> globalConfig.allowPlaceBlocks(),
                () -> config.allowPlaceBlocks()
        );
        bindEntry(
                entries.addSwitchEntry(Text.translate("effortless.global_general_settings.allow_interact_blocks"), null, null, null),
                (value) -> {
                    this.config = new GeneralConfig(config.useCommands(), config.allowUseMod(), config.allowBreakBlocks(), config.allowPlaceBlocks(), value, config.maxReachDistance(), config.maxBlockBreakVolume(), config.maxBlockPlaceVolume(), config.whitelistedItems(), config.blacklistedItems());
                },
                () -> globalConfig.allowInteractBlocks(),
                () -> config.allowInteractBlocks()
        );
        bindEntry(
                entries.addIntegerEntry(Text.translate("effortless.global_general_settings.max_reach_distance"), null, null, GeneralConfig.MAX_REACH_DISTANCE_RANGE_START, GeneralConfig.MAX_REACH_DISTANCE_RANGE_END, null),
                (value) -> {
                    this.config = new GeneralConfig(config.useCommands(), config.allowUseMod(), config.allowBreakBlocks(), config.allowPlaceBlocks(), config.allowInteractBlocks(), value, config.maxBlockBreakVolume(), config.maxBlockPlaceVolume(), config.whitelistedItems(), config.blacklistedItems());
                },
                () -> globalConfig.maxReachDistance(),
                () -> config.maxReachDistance()
        );
        bindEntry(
                entries.addIntegerEntry(Text.translate("effortless.global_general_settings.max_block_break_volume"), null, null, GeneralConfig.MAX_BOX_VOLUME_PER_BREAK_RANGE_START, GeneralConfig.MAX_BOX_VOLUME_PER_BREAK_RANGE_END, null),
                (value) -> {
                    this.config = new GeneralConfig(config.useCommands(), config.allowUseMod(), config.allowBreakBlocks(), config.allowPlaceBlocks(), config.allowInteractBlocks(), config.maxReachDistance(), value, config.maxBlockPlaceVolume(), config.whitelistedItems(), config.blacklistedItems());
                },
                () -> globalConfig.maxBlockBreakVolume(),
                () -> config.maxBlockBreakVolume()
        );
        bindEntry(
                entries.addIntegerEntry(Text.translate("effortless.global_general_settings.max_block_place_volume"), null, null, GeneralConfig.MAX_BOX_VOLUME_PER_PLACE_RANGE_START, GeneralConfig.MAX_BOX_VOLUME_PER_PLACE_RANGE_END, null),
                (value) -> {
                    this.config = new GeneralConfig(config.useCommands(), config.allowUseMod(), config.allowBreakBlocks(), config.allowPlaceBlocks(), config.allowInteractBlocks(), config.maxReachDistance(), config.maxBlockBreakVolume(), value, config.whitelistedItems(), config.blacklistedItems());
                },
                () -> globalConfig.maxBlockPlaceVolume(),
                () -> config.maxBlockPlaceVolume()
        );
        bindEntry(
                entries.addTab(Text.translate("effortless.global_general_settings.whitelisted_items"), null, null, null, (entry, value) -> {
                    entry.getButton().setOnPressListener(button1 -> {
                        new EffortlessItemsScreen(getEntrance(), Text.translate("effortless.global_general_settings.whitelisted_items"), value.stream().map(Item::fromId).toList(), (value1) -> {
                            entry.setItem(value1.stream().map(Item::getId).toList());
                        }).attach();
                    });
                    entry.getButton().setMessage(Text.translate("effortless.global_general_settings.items", value == null ? null : value.size()));
                }),
                (value) -> {
                    this.config = new GeneralConfig(config.useCommands(), config.allowUseMod(), config.allowBreakBlocks(), config.allowPlaceBlocks(), config.allowInteractBlocks(), config.maxReachDistance(), config.maxBlockBreakVolume(), config.maxBlockPlaceVolume(), value, config.blacklistedItems());
                },
                () -> globalConfig.whitelistedItems(),
                () -> config.whitelistedItems()

        );

        bindEntry(
                entries.addTab(Text.translate("effortless.global_general_settings.blacklisted_items"), null, null, null, (entry, value) -> {
                    entry.getButton().setOnPressListener(button1 -> {
                        new EffortlessItemsScreen(getEntrance(), Text.translate("effortless.global_general_settings.blacklisted_items"), value.stream().map(Item::fromId).toList(), (value1) -> {
                            entry.setItem(value1.stream().map(Item::getId).toList());
                        }).attach();
                    });
                    entry.getButton().setMessage(Text.translate("effortless.global_general_settings.items", value == null ? null : value.size()));
                }),
                (value) -> {
                    this.config = new GeneralConfig(config.useCommands(), config.allowUseMod(), config.allowBreakBlocks(), config.allowPlaceBlocks(), config.allowInteractBlocks(), config.maxReachDistance(), config.maxBlockBreakVolume(), config.maxBlockPlaceVolume(), config.whitelistedItems(), value);
                },
                () -> globalConfig.blacklistedItems(),
                () -> config.blacklistedItems()
        );

        addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.cancel"), button -> {
            detach();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 0f, 0f, 1 / 3f).build());

        this.resetButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.reset"), button -> {
            config = defaultConfig;
            recreate();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 0f, 1 / 3f, 1 / 3f).build());

        this.saveButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.save"), button -> {
            consumer.accept(playerInfo, config);
            detach();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 0f, 2 / 3f, 1 / 3f).build());

        this.resetButton.setActive(false);
    }

    @Override
    public void onReload() {
        this.resetButton.setActive(!config.equals(defaultConfig));
    }

    @Override
    protected EffortlessClient getEntrance() {
        return (EffortlessClient) super.getEntrance();
    }

    private <T> void bindEntryState(SettingOptionsList.SettingsEntry<T> entry, Consumer<T> setter, Supplier<T> globalGetter, Supplier<T> playerGetter) {
        var isGlobal = playerGetter.get() == null;
        entry.children().forEach(child -> child.setActive(!isGlobal));
        entry.getAltButton().setActive(true);
        entry.getTitleTextWidget().setMessage(entry.getTitleTextWidget().getMessage().withStyle(isGlobal ? ChatFormatting.ITALIC : ChatFormatting.RESET, isGlobal ? ChatFormatting.GRAY : ChatFormatting.WHITE));
        entry.setConsumer(null);
        entry.setItem(isGlobal ? globalGetter.get() : playerGetter.get());
        entry.setConsumer(setter);
        entry.getAltButton().setMessage(isGlobal ? "O" : "X");
        entry.getAltButton().setTooltip(isGlobal ? "Click to Override" : "Click to Use Global");
    }

    private <T> void bindEntry(SettingOptionsList.SettingsEntry<T> entry, Consumer<T> setter, Supplier<T> globalGetter, Supplier<T> playerGetter) {
        bindEntryState(entry, setter, globalGetter, playerGetter);

        entry.getAltButton().setOnPressListener(button -> {
            setter.accept(playerGetter.get() == null ? globalGetter.get() : null);
            bindEntryState(entry, setter, globalGetter, playerGetter);
        });


    }

}
