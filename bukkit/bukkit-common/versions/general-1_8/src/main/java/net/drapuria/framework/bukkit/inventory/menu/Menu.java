/*
 * Copyright (c) 2022. Drapuria
 */

package net.drapuria.framework.bukkit.inventory.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;


public abstract class Menu extends AbstractMenu {

    private final Map<Player, Map<Integer, IButton>> playerButtons = new HashMap<>();
    private final Map<Player, Inventory> inventories = new HashMap<>();
    private Button placeholderButton = Button.placeholder();


    @Override
    public Map<Integer, IButton> getCachedButtons(Player player) {
        return playerButtons.get(player);
    }

    public void setCachedButtons(Player player, Map<Integer, IButton> map) {
        this.playerButtons.put(player, map);
    }

    @Override
    public void openMenu(Player player) {
        final Map<Integer, IButton> buttons = getButtons(player);
        final int size = getSize(player);
        final String title = getTitle(player);
        buildInventory(player, getBukkitInventoryType(player), size, title, buttons, (inventory, aBoolean) -> {
            this.inventories.put(player, inventory);
            if (!aBoolean)
                player.openInventory(inventory);
            setCachedButtons(player, buttons);
            MenuService.getService.addOpenedMenu(player.getName(), this);
        });
        onOpen(player);
    }

    @Override
    public Inventory getInventory(Player player) {
        return this.inventories.get(player);
    }

    @Override
    public void removePlayerButtons(Player player) {
        this.playerButtons.remove(player);
    }

    private void buildInventory(final Player player, final InventoryType inventoryType, int size, final String title,
                                final Map<Integer, IButton> buttons, BiConsumer<Inventory, Boolean> consumer) {
        Inventory inventory = null;
        if (size == -1) {
            size = this.size(buttons);
        }
        boolean update = false;

        if (inventories.containsKey(player)) {
            inventory = inventories.get(player);
            if (inventory.getSize() != size)
                inventory = null;
            else {
                inventory.setContents(new ItemStack[inventory.getSize()]);
                if (player.getOpenInventory().getTopInventory().equals(inventory))
                    update = true;
            }
        }

        IMenu previousMenu = MenuService.getService.getOpenedMenu(player.getName());
        if (inventory == null) {
            if (player.getOpenInventory() != null) {
                if (previousMenu == null)
                    player.closeInventory();
                else if (previousMenu instanceof Menu) {
                    final int previousSize = player.getOpenInventory().getTopInventory().getSize();
                    if (previousSize == size) {
                        inventory = player.getOpenInventory().getTopInventory();
                        inventory.setContents(new ItemStack[inventory.getSize()]);
                        update = true;
                    } else {
                        previousMenu.setClosedByMenu(true);
                        MenuService.getService.removePlayer(player.getName());
                    }
                }
            }
        }

        if (inventory == null) {
            if (inventoryType != null)
                inventory = Bukkit.createInventory(null, inventoryType, title);
            else
                inventory = Bukkit.createInventory(null, size, title);
        }
        if (hasPlaceholder()) {
            final ItemStack placeHolderItem = placeholderButton.getIcon(player);
            for (int i = 0; i < inventory.getSize(); i++)
                inventory.setItem(i, placeHolderItem);
        }
        for (Map.Entry<Integer, IButton> entry : buttons.entrySet()) {
            inventory.setItem(entry.getKey(), entry.getValue().getIcon(player));
        }
        consumer.accept(inventory, update);
    }

    public void setPlaceholderButton(Button placeholderButton) {
        this.placeholderButton = placeholderButton;
    }

    public Button getPlaceholderButton() {
        return placeholderButton;
    }

    @Override
    protected void updateButtons(Player player) {
        final Inventory inventory = this.getInventory(player);
        if (inventory != null) {
            inventory.setContents(new ItemStack[inventory.getSize()]);
            Map<Integer, IButton> buttons = getButtons(player);
            playerButtons.put(player, buttons);
            for (Map.Entry<Integer, IButton> entry : buttons.entrySet()) {
                inventory.setItem(entry.getKey(), entry.getValue().getIcon(player));
            }
            player.updateInventory();
        } else
            openMenu(player);
    }
}