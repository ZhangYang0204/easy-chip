package pers.zhangyang.easychip.listener.mainoptionpage;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import pers.zhangyang.easychip.domain.MainOptionPage;
import pers.zhangyang.easylibrary.annotation.EventListener;
import pers.zhangyang.easylibrary.annotation.GuiDiscreteButtonHandler;

@EventListener
public class PlayerClickDepositProtector implements Listener {
    @GuiDiscreteButtonHandler(guiPage = MainOptionPage.class, slot = {26})
    public void on(InventoryClickEvent event) {

        MainOptionPage mainOptionPage = (MainOptionPage) event.getInventory().getHolder();
        Player player = (Player) event.getWhoClicked();
        assert mainOptionPage != null;
        new PlayerInputAfterClickMainOptionPageDepositProtector(player, mainOptionPage.getOwner(), mainOptionPage);
    }
}
