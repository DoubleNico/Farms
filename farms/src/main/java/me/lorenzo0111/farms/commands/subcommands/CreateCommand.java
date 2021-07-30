package me.lorenzo0111.farms.commands.subcommands;

import com.cryptomorin.xseries.XMaterial;
import de.tr7zw.changeme.nbtapi.NBTItem;
import de.tr7zw.changeme.nbtapi.utils.MinecraftVersion;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import me.lorenzo0111.farms.Farms;
import me.lorenzo0111.farms.commands.FarmsCommand;
import me.lorenzo0111.farms.commands.SubCommand;
import me.lorenzo0111.farms.hooks.VaultHook;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CreateCommand extends SubCommand {
    private static NBTItem item;

    public CreateCommand(FarmsCommand command) {
        super(command);
    }

    @Override
    public String[] getName() {
        return new String[]{"create","setup","buy"};
    }

    @Override
    public @Nullable String getPermission() {
        return "farms.create";
    }

    @Override
    public void execute(@NotNull Player player, String[] args) {
        Player target = player;

        if (args.length == 2 && player.hasPermission(""))
            target = Bukkit.getPlayer(args[1]);

        if (target == null) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', getCommand().getPlugin().getMessages().getString("prefix") + getCommand().getPlugin().getMessages().getString("not-found")));
            return;
        }

        if (target.equals(player) && !VaultHook.withdraw(player,getCommand().getPlugin().getConfig().getDouble("vault.price"))) {
            target.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getCommand().getPlugin().getMessages().getString("prefix") + this.getCommand().getPlugin().getMessages().getString("commands.no-money")));
            return;
        }

        target.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getCommand().getPlugin().getMessages().getString("prefix") + this.getCommand().getPlugin().getMessages().getString("commands.setup")));

        NBTItem item = getItem();
        item.setInteger("farm_level", 1);
        target.getInventory().addItem(item.getItem());
    }

    public static NBTItem getItem() {
        if (item == null) {
            Material material = XMaterial.END_ROD.parseMaterial() == null ? Material.TORCH : XMaterial.END_ROD.parseMaterial();

            Component name = Component.text(ChatColor.translateAlternateColorCodes('&', "" + Farms.getInstance().getMessages().getString("setup.item-name")));
            Component lore = Component.text(ChatColor.translateAlternateColorCodes('&', "" + Farms.getInstance().getMessages().getString("setup.item-lore")));

            item = new NBTItem(ItemBuilder.from(material)
                    .name(name)
                    .lore(lore)
                    .build());
            item.setString("custom_item", "farms");
        }

        return item;
    }
}
