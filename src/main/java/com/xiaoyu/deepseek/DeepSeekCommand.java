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
                    context.getSource().sendFailure(Component.literal("§c请输入对话内容！"));
                    return 0;
                }));

        dispatcher.register(Commands.literal("ds")
                .then(Commands.argument("message", StringArgumentType.greedyString())
                        .executes(DeepSeekCommand::handleDeepSeek))
                .executes(context -> {
                    context.getSource().sendFailure(Component.literal("§c请输入对话内容！"));
                    return 0;
                }));
    }

    private static int handleDeepSeek(CommandContext<CommandSourceStack> context) {
        String message = StringArgumentType.getString(context, "message");
        CommandSourceStack source = context.getSource();

        if (DeepSeekConfig.DeepSeekAPIKey.isEmpty()) {
            source.sendFailure(Component.literal("§c未配置DeepSeek API密钥！请检查配置文件。"));
            return 0;
        }

        if (source.getPlayer() instanceof ServerPlayer player) {
            // 显示玩家发送的问题（新增）
            Component questionMsg = Component.literal("§b[玩家] -> DeepSeek: §r" + message);
            player.getServer().getPlayerList().broadcastSystemMessage(questionMsg, false);

            // 异步处理API请求
            Thread apiThread = new Thread(() -> {
                try {
                    String response = DeepSeekAPI.queryDeepSeek(message);
                    player.getServer().execute(() -> {
                        // 显示DeepSeek的回答（修改为更清晰的格式）
                        Component responseMsg = Component.literal("§b[DeepSeek] -> " + player.getScoreboardName() + ": §r" + response);
                        player.getServer().getPlayerList().broadcastSystemMessage(responseMsg, false);
                    });
                } catch (Exception e) {
                    player.getServer().execute(() -> {
                        Component errorMsg = Component.literal("§cDeepSeek请求失败: " + e.getMessage());
                        player.sendSystemMessage(errorMsg);
                        if (DeepSeekConfig.DebugMode) {
                            e.printStackTrace();
                        }
                    });
                }
            });
            apiThread.setName("DeepSeek-API-Thread");
            apiThread.start();

            source.sendSuccess(() -> Component.literal("§a正在向DeepSeek发送请求..."), false);
            return Command.SINGLE_SUCCESS;
        }

        source.sendFailure(Component.literal("§c只有玩家可以使用此命令"));
        return 0;
    }
}
