package ggmmo.npceditor;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

public class NpcManager {
    public void createNPC(Player player, String npcName) {
        MinecraftServer mmsServer = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer mmsWorld = ((CraftWorld) player.getWorld()).getHandle();

        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), npcName);
        EntityPlayer npc = new EntityPlayer(mmsServer, mmsWorld, gameProfile, new PlayerInteractManager(mmsWorld));
        Player npcPlayer = npc.getBukkitEntity().getPlayer();

        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc));

        Location location = player.getLocation();
        npc.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

        PacketPlayOutEntity.PacketPlayOutEntityLook packet = new PacketPlayOutEntity.PacketPlayOutEntityLook(npcPlayer.getEntityId(), getFixRotation(location.getYaw()), (byte) location.getPitch(), true);
        PacketPlayOutEntityHeadRotation packet_1 = new PacketPlayOutEntityHeadRotation();
        this.setField(packet_1, "a", npcPlayer.getEntityId());
        this.setField(packet_1, "b", getFixRotation(location.getYaw()));

        connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
        connection.sendPacket(packet);
        connection.sendPacket(packet_1);
        connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc));
    }

    private byte getFixRotation(float yawpitch) {
        return (byte) ((int) (yawpitch * 256.0F / 360.0F));
    }

    private void setField(Object obj, String field_name, Object value) {
        try {
            Field field = obj.getClass().getDeclaredField(field_name);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
