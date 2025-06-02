package com.xiaoyu.deepseek;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@Mod(DeepSeek.MODID)
public class DeepSeek {
    public static final String MODID = "deepseek";
    public DeepSeek(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, DeepSeekConfig.SPEC);
        NeoForge.EVENT_BUS.addListener(this::registerCommands);
    }
    private void registerCommands(RegisterCommandsEvent event) {
        DeepSeekCommand.register(event.getDispatcher());
    }
}
