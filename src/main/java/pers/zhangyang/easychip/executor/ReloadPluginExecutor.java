package pers.zhangyang.easychip.executor;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easychip.yaml.MessageYaml;
import pers.zhangyang.easylibrary.base.ExecutorBase;
import pers.zhangyang.easylibrary.util.MessageUtil;

import java.util.List;

public class ReloadPluginExecutor extends ExecutorBase {
    public ReloadPluginExecutor(@NotNull CommandSender sender, String commandName, @NotNull String[] args) {
        super(sender, commandName, args);
    }

    @Override
    protected void run() {
        if (args.length != 0) {
            return;
        }


        List<String> list = MessageYaml.INSTANCE.getStringList("message.chat.reloadPlugin");
        MessageUtil.sendMessageTo(this.sender, list);
    }
}
