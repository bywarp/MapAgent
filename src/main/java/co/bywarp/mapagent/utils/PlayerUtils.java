/*
 * Copyright (c) 2019-2020 Warp <legal@warp.pw>
 * All Rights Reserved.
 *
 * This software is proprietary and is designed and intended for internal use only.
 * Unauthorized use, replication, distribution, or modification of this software
 * in any capacity is unlawful and punishable by the full extent of the law.
 */

package co.bywarp.mapagent.utils;

import co.bywarp.mapagent.utils.text.Lang;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;

public class PlayerUtils {

    public static void sendServerMessage(String msg) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(Lang.colorMessage(msg));
        }
    }

    public static void sendActionBar(Player player, String message) {
        PacketPlayOutChat chat = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + message + "\"}"), (byte)2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(chat);
    }

    public static void sendActionBar(String message) {
        PacketPlayOutChat chat = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + message + "\"}"), (byte)2);
        for (Player client : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) client.getPlayer()).getHandle().playerConnection.sendPacket(chat);
        }
    }

    public static void sendServerTitle(String y, String sub, int in, int stay, int out) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            sendPlayerTitle(player, y, sub, in, stay, out);
        }
    }

    public static void sendPlayerTitle(Player player, String y, String sub, int in, int stay, int out) {
        in = in * 20;
        stay = stay * 20;
        out = out * 20;

        PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, IChatBaseComponent.ChatSerializer.a(Lang.colorMessage("{\"text\":\""+y+"\"}")), in, stay, out);
        PacketPlayOutTitle subtitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, IChatBaseComponent.ChatSerializer.a(Lang.colorMessage("{\"text\":\""+sub+"\"}")), in, stay, out);

        sendPacket(player, title);
        sendPacket(player, subtitle);
    }

    public static void sendPacket(Player player, @SuppressWarnings("rawtypes") Packet packet) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        craftPlayer.getHandle().playerConnection.sendPacket(packet);
    }

}
