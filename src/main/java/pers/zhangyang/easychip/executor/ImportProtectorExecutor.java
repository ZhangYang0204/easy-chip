package pers.zhangyang.easychip.executor;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easychip.domain.Protector;
import pers.zhangyang.easychip.yaml.MessageYaml;
import pers.zhangyang.easychip.yaml.ProtectorYaml;
import pers.zhangyang.easylibrary.base.ExecutorBase;
import pers.zhangyang.easylibrary.util.MessageUtil;
import pers.zhangyang.easylibrary.util.PlayerUtil;

import java.util.List;

public class ImportProtectorExecutor extends ExecutorBase {
    public ImportProtectorExecutor(@NotNull CommandSender sender, String commandName, @NotNull String[] args) {
        super(sender, commandName, args);
    }

    @Override
    protected void run() {
        if (args.length != 1) {
            return;
        }

        if (!(sender instanceof Player)) {
            MessageUtil.notPlayer(sender);
        }
        Player player = (Player) sender;


        Protector chip = ProtectorYaml.INSTANCE.getProtector("protector." + args[0]);

        if (chip == null) {
            List<String> list = MessageYaml.INSTANCE.getStringList("message.chat.notExistProtectorWhenImportProtector");
            MessageUtil.sendMessageTo(sender, list);
            return;
        }


        ItemStack itemStack = chip.getItemStack().clone();
        if (PlayerUtil.checkSpace(player, itemStack) < itemStack.getAmount()) {
            List<String> list = MessageYaml.INSTANCE.getStringList("message.chat.notEnoughSpaceWhenImportProtector");
            MessageUtil.sendMessageTo(sender, list);
            return;
        }

        PlayerUtil.addItem(player, itemStack, itemStack.getAmount());


        List<String> list = MessageYaml.INSTANCE.getStringList("message.chat.importProtector");
        MessageUtil.sendMessageTo(sender, list);
    }


}