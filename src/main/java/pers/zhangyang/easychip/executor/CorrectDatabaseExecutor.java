package pers.zhangyang.easychip.executor;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easychip.service.CommandService;
import pers.zhangyang.easychip.service.impl.CommandServiceImpl;
import pers.zhangyang.easychip.yaml.MessageYaml;
import pers.zhangyang.easylibrary.base.ExecutorBase;
import pers.zhangyang.easylibrary.util.MessageUtil;
import pers.zhangyang.easylibrary.util.TransactionInvocationHandler;

import java.util.List;

public class CorrectDatabaseExecutor extends ExecutorBase {
    public CorrectDatabaseExecutor(@NotNull CommandSender sender, String commandName, @NotNull String[] args) {
        super(sender, commandName, args);
    }

    @Override
    protected void run() {
        if (args.length != 0) {
            return;
        }

        CommandService commandService = (CommandService) new TransactionInvocationHandler(new CommandServiceImpl()).getProxy();
        commandService.correctDatabase();

        List<String> list = MessageYaml.INSTANCE.getStringList("message.chat.correctDatabase");
        MessageUtil.sendMessageTo(this.sender, list);
    }
}
