/*
 * -------------------------------------
 * Copyright Lorenzo0111 2021
 * https://github.com/Lorenzo0111
 * -------------------------------------
 */

package me.lorenzo0111.farms.tasks.worker;

import me.lorenzo0111.farms.api.objects.Farm;
import me.lorenzo0111.farms.data.DataManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DropsWorker implements Worker {

    @Override
    public void work(DataManager manager, Farm farm) {
        Entity minion = farm.getMinion();

        if (minion == null) return;

        int can = farm.getLevel() * 2;
        List<ItemStack> collected = new ArrayList<>();

        final List<Entity> nearbyEntities = new ArrayList<>(minion.getNearbyEntities(farm.getRadius(), 3, farm.getRadius()));
        List<Entity> collect = nearbyEntities.stream()
                .filter(entity -> entity instanceof Item)
                .collect(Collectors.toList());

        for (Entity entity : collect) {
            if (can >= collected.size()) {
                return;
            }

            Item item = (Item) entity;

            collected.add(item.getItemStack());
            item.remove();
        }

        this.collect(farm, collected);

    }

    @Override
    public boolean async() {
        return false;
    }

}