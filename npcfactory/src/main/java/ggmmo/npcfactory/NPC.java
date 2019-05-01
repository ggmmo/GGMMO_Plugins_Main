package ggmmo.npcfactory;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_13_R2.DataWatcher;
import net.minecraft.server.v1_13_R2.DataWatcherObject;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public class NPC {
    private static int entityID = 0;
    private int ID = 0;

    private Location pos;
    private GameProfile profile;

    // Whether the NPC can be seen by specific players or all players
    private List<Player> recipients;
    private Recipient recipientType = Recipient.ALL;

    private String displayName;

    // Create a data watcher to add interactivity with the NPC
    private DataWatcher dataWatcher;
    private DataWatcherObject<Byte> objectEntityState;
    private DataWatcherObject<String> objectCustomName;

    enum Recipient { ALL, LISTED }
}
