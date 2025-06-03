package com.xiaoyu.deepseek;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = DeepSeek.MODID, bus = EventBusSubscriber.Bus.MOD)

public class DeepSeekConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.ConfigValue<String> DEEPSEEK_APIKEY = BUILDER
            .comment("DeepSeek API Key")
            .translation("config.deepseek.apikey") // 添加翻译键
            .define("DeepSeekAPIKey", "");

    public static final ModConfigSpec.ConfigValue<String> DEEPSEEK_MODEL = BUILDER
            .comment("\nModel to use: deepseek-chat or deepseek-reasoner")
            .comment("模型：deepseek-chat和deepseek-reasoner")
            .translation("config.deepseek.model") // 添加翻译键
            .define("DeepSeeKModel", "deepseek-chat");

    private static final ModConfigSpec.BooleanValue DEEPSEEK_DEBUG_MODE = BUILDER
            .comment("\nEnable debug logging")
            .comment("调试模式")
            .translation("config.deepseek.debug") // 添加翻译键
            .define("DebugMode", false);

    private static final ModConfigSpec.BooleanValue SHOW_QUESTIONS_IN_CHAT = BUILDER
            .comment("\nShow player questions in chat")
            .comment("在聊天栏显示玩家提问的问题")
            .translation("config.deepseek.showquestions") // 添加翻译键
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
