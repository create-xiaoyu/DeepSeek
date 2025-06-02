package com.xiaoyu.deepseek;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = DeepSeek.MODID, bus = EventBusSubscriber.Bus.MOD)

public class DeepSeekConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.ConfigValue<String> DEEPSEEK_APIKEY = BUILDER
            .comment("在此处填写你的DeepSeek API Key")
            .define("DeepSeekAPIKey", "");

    public static final ModConfigSpec.ConfigValue<String> DEEPSEEK_MODEL = BUILDER
            .comment("\n在此处填写DeepSeek模型\n")
            .comment("可选值：\ndeepseek-chat\ndeepseek-reasoner\n")
            .comment("默认值：deepseek-chat")
            .define("DeepSeeKModel", "deepseek-chat");

    private static final ModConfigSpec.BooleanValue DEEPSEEK_DEBUG_MODE = BUILDER
            .comment("\n调试模式\n")
            .comment("默认值：false")
            .define("DebugMode", false);

    private static final ModConfigSpec.BooleanValue SHOW_QUESTIONS_IN_CHAT = BUILDER
            .comment("\n是否在聊天中显示玩家的问题\n")
            .comment("默认值：true")
            .define("ShowQuestionsInChat", true);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static String DeepSeekAPIKey;
    public static String DeepSeeKModel;
    public static Boolean DebugMode;
    public static Boolean ShowQuestionsInChat;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        DeepSeekAPIKey = DEEPSEEK_APIKEY.get();
        DeepSeeKModel = DEEPSEEK_MODEL.get();
        DebugMode = DEEPSEEK_DEBUG_MODE.get();
        ShowQuestionsInChat = SHOW_QUESTIONS_IN_CHAT.get();
    }
}
