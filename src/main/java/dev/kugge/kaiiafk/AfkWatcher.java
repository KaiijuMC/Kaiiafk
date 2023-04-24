package dev.kugge.kaiiafk;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.concurrent.ConcurrentHashMap;

public class AfkWatcher implements Runnable {

    private static final ConcurrentHashMap<Player, Object[]> statHashMap = new ConcurrentHashMap<>();

    @Override
    public void run () {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.getScheduler().run(KaiiAfk.instance, task -> {
                Object[] lastData = statHashMap.get(player);


                // Not in stats: Create player stats
                if (lastData == null) {
                    createStat(player);
                    return;
                }

                // Changed pos: refresh pos
                if (!lastData[0].equals(player.getLocation())) {
                    refreshPos(player);
                    return;
                }

                // Same pos: set AFK if needed
                if (!(boolean) lastData[1] && System.currentTimeMillis() - (long) lastData[2] >= 60000L) {
                    player.playerListName(Component.text(player.getName(), NamedTextColor.GRAY));
                    player.setSleepingIgnored(true);
                    lastData[1] = true;
                }
            }, null);
        }
    }

    public void createStat(Player player) {
        statHashMap.put(player, new Object[]{player.getLocation(), false, System.currentTimeMillis()});
    }

    public void refreshPos(Player player) {
        statHashMap.get(player)[0] = player.getLocation();
        statHashMap.get(player)[2] = System.currentTimeMillis();
        if ((boolean) statHashMap.get(player)[1]) {
            statHashMap.get(player)[1] = false;
            player.playerListName(Component.text(player.getName(), NamedTextColor.WHITE));
            player.setSleepingIgnored(false);
        }
    }
}