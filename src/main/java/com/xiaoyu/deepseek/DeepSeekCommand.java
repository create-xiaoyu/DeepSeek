package com.xiaoyu.deepseek;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class DeepSeekCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("deepseek")
                .then(Commands.argument("message", StringArgumentType.greedyString())
                        .executes(DeepSeekCommand::handleDeepSeek))
                .executes(context -> {
                    context.getSource().sendFailure(Component.translatable("command.deepseek.usage"));
                    return 0;
                }));

        dispatcher.register(Commands.literal("ds")
                .then(Commands.argument("message", StringArgumentType.greedyString())
                        .executes(DeepSeekCommand::handleDeepSeek))
                .executes(context -> {
                    context.getSource().sendFailure(Component.translatable("command.deepseek.usage"));
                    return 0;
                }));
    }

    private static int handleDeepSeek(CommandContext<CommandSourceStack> context) {
        String message = StringArgumentType.getString(context, "message");
        CommandSourceStack source = context.getSource();

        if (DeepSeekConfig.DeepSeekAPIKey.isEmpty()) {
            source.sendFailure(Component.translatable("command.deepseek.noapikey"));
            return 0;
        }

        if (source.getPlayer() instanceof ServerPlayer player) {
            // 显示玩家问题（使用翻译键）
            if (DeepSeekConfig.ShowQuestionsInChat) {
                Component questionMsg = Component.translatable(
                        "command.deepseek.question",
                        message
                );
                player.getServer().getPlayerList().broadcastSystemMessage(questionMsg, false);
            }

            Thread apiThread = new Thread(() -> {
                try {
                    String response = DeepSeekAPI.queryDeepSeek(message);
                    player.getServer().execute(() -> {
                        // 使用翻译键格式化响应
                        Component responseMsg = Component.translatable(
                                "command.deepseek.response",
                                player.getScoreboardName(),
                                response
                        );
                        player.getServer().getPlayerList().broadcastSystemMessage(responseMsg, false);
                    });
                } catch (Exception e) {
                    player.getServer().execute(() -> {
                        // 使用带参数的翻译键
                        Component errorMsg = Component.translatable(
                                "command.deepseek.error",
                                e.getMessage()
                        );
                        player.sendSystemMessage(errorMsg);
                        if (DeepSeekConfig.DebugMode) {
                            e.printStackTrace();
                        }
                    });
                }
            });
            apiThread.setName("DeepSeek-API-Thread");
            apiThread.start();

            source.sendSuccess(() ->
                            Component.translatable("command.deepseek.sending"),
                    false
            );
            return Command.SINGLE_SUCCESS;
        }

        source.sendFailure(Component.translatable("command.deepseek.playeronly"));
        return 0;
    }
}
