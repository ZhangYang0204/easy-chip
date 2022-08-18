package pers.zhangyang.easychip.executor;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easychip.domain.Chip;
import pers.zhangyang.easychip.yaml.ChipYaml;
import pers.zhangyang.easychip.yaml.MessageYaml;
import pers.zhangyang.easylibrary.base.ExecutorBase;
import pers.zhangyang.easylibrary.util.MessageUtil;
import pers.zhangyang.easylibrary.util.PlayerUtil;

import java.util.List;

public class ImportChipExecutor extends ExecutorBase {
    public ImportChipExecutor(@NotNull CommandSender sender, String commandName, @NotNull String[] args) {
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



        try {
            Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            MessageUtil.invalidArgument(player, args[0]);
            return;
        }

        int amount;
        try {
           amount= Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            MessageUtil.invalidArgument(player, args[1]);
            return;
        }

        Chip chip = ChipYaml.INSTANCE.getChip("chip." + args[0]);

        if (chip == null) {
            List<String> list = MessageYaml.INSTANCE.getStringList("message.chat.notExistChipWhenImportChip");
            MessageUtil.sendMessageTo(sender, list);
            return;
        }


        ItemStack itemStack = chip.getItemStack().clone();
        if (PlayerUtil.checkSpace(player, itemStack) < itemStack.getAmount()) {
            List<String> list = MessageYaml.INSTANCE.getStringList("message.chat.notEnoughSpaceWhenImportChip");
            MessageUtil.sendMessageTo(sender, list);
            return;
        }

        PlayerUtil.addItem(player, itemStack, itemStack.getAmount()*amount);


        List<String> list = MessageYaml.INSTANCE.getStringList("message.chat.importChip");
        MessageUtil.sendMessageTo(sender, list);
    }


}
