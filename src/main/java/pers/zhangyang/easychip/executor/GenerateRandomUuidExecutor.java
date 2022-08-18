package pers.zhangyang.easychip.executor;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easychip.yaml.MessageYaml;
import pers.zhangyang.easylibrary.base.ExecutorBase;
import pers.zhangyang.easylibrary.util.MessageUtil;
import pers.zhangyang.easylibrary.util.VersionUtil;

import java.util.UUID;

public class GenerateRandomUuidExecutor extends ExecutorBase {
    public GenerateRandomUuidExecutor(@NotNull CommandSender sender, String commandName, @NotNull String[] args) {
        super(sender, commandName, args);
    }

    @Override
    protected void run() {

        if (!(sender instanceof Player)) {
            MessageUtil.notPlayer(sender);
            return;
        }
        Player player = (Player) sender;


        UUID uuid = UUID.randomUUID();

        String uuidString = uuid.toString();
        TextComponent textComponent = new TextComponent(uuidString);
        if (VersionUtil.getMinecraftBigVersion() == 1 && VersionUtil.getMinecraftMiddleVersion() >= 16) {
            textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, uuidString));
        }

        Bukkit.getConsoleSender().sendMessage(uuidString);

        String hover = MessageYaml.INSTANCE.getNonemptyString("message.component.generateRandomUuid");
        if (hover != null) {
            hover = ChatColor.translateAlternateColorCodes('&', hover);
            if (VersionUtil.getMinecraftBigVersion() == 1 && VersionUtil.getMinecraftMiddleVersion() < 16) {
                textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{new TextComponent(hover)}));
            } else {
                textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hover)));
            }
        }

        player.spigot().sendMessage(textComponent);
    }
}
