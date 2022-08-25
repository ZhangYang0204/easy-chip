package pers.zhangyang.easychip.executor;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easychip.domain.Fortifier;
import pers.zhangyang.easychip.yaml.FortifierYaml;
import pers.zhangyang.easychip.yaml.MessageYaml;
import pers.zhangyang.easylibrary.base.ExecutorBase;
import pers.zhangyang.easylibrary.util.MessageUtil;
import pers.zhangyang.easylibrary.util.PlayerUtil;

import java.util.List;

public class ImportFortifierExecutor extends ExecutorBase {
    public ImportFortifierExecutor(@NotNull CommandSender sender, String commandName, @NotNull String[] args) {
        super(sender, commandName, args);
    }

    @Override
    protected void run() {
        if (args.length != 2) {
            return;
        }

        if (!(sender instanceof Player)) {
            MessageUtil.notPlayer(sender);
        }
        Player player = (Player) sender;
        int amount;
        try {
            amount = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            MessageUtil.invalidArgument(player, args[0]);
            return;
        }

        Fortifier fortifier = FortifierYaml.INSTANCE.getFortifier("fortifier." + args[0]);

        if (fortifier == null) {
            List<String> list = MessageYaml.INSTANCE.getStringList("message.chat.notExistFortifierWhenImportFortifier");
            MessageUtil.sendMessageTo(sender, list);
            return;
        }


        ItemStack itemStack = fortifier.getItemStack().clone();
        if (PlayerUtil.checkSpace(player, itemStack) < itemStack.getAmount() * amount) {
            List<String> list = MessageYaml.INSTANCE.getStringList("message.chat.notEnoughSpaceWhenImportFortifier");
            MessageUtil.sendMessageTo(sender, list);
            return;
        }

        PlayerUtil.addItem(player, itemStack, itemStack.getAmount() * amount);


        List<String> list = MessageYaml.INSTANCE.getStringList("message.chat.importFortifier");
        MessageUtil.sendMessageTo(sender, list);
    }


}