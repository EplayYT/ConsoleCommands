package Main.Java.me.plugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("ConsoleCommands aktif!");
    }

    @Override
    public void onDisable() {
        getLogger().info("ConsoleCommands kapandı!");
    }

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] a) {

        String name = cmd.getName().toLowerCase();

        if (name.equals("ccstop")) {
            Bukkit.shutdown();
            return true;
        }

        if (name.equals("ccban")) {
            if (a.length < 1) { s.sendMessage("Kullanım: /ccban <oyuncu>"); return true; }
            Player t = Bukkit.getPlayer(a[0]);
            if (t == null) { s.sendMessage("Oyuncu bulunamadı!"); return true; }
            t.kickPlayer("Sunucudan yasaklandınız.");
            Bukkit.getBanList(org.bukkit.BanList.Type.NAME).addBan(t.getName(), "ConsoleCommands tarafından ban.", null, null);
            return true;
        }

        if (name.equals("ccunban")) {
            if (a.length < 1) { s.sendMessage("Kullanım: /ccunban <oyuncu>"); return true; }
            Bukkit.getBanList(org.bukkit.BanList.Type.NAME).pardon(a[0]);
            s.sendMessage("Ban kaldırıldı.");
            return true;
        }

        if (name.equals("ccheal")) {
            if (!(s instanceof Player)) return true;
            Player p = (Player) s;
            p.setHealth(20);
            p.sendMessage("İyileştirildin!");
            return true;
        }

        if (name.equals("ccfeed")) {
            if (!(s instanceof Player)) return true;
            Player p = (Player) s;
            p.setFoodLevel(20);
            p.sendMessage("Doyuruldun!");
            return true;
        }

        if (name.equals("ccgamemode")) {
            if (!(s instanceof Player)) return true;
            if (a.length < 1) { s.sendMessage("0/1/2/3 gir."); return true; }
            Player p = (Player) s;
            p.setGameMode(org.bukkit.GameMode.getByValue(Integer.parseInt(a[0])));
            return true;
        }

        if (name.equals("cctime")) {
            if (a.length < 1) { s.sendMessage("Kullanım: /cctime <zaman>"); return true; }
            Bukkit.getWorlds().get(0).setTime(Long.parseLong(a[0]));
            s.sendMessage("Zaman değişti.");
            return true;
        }

        if (name.equals("ccweather")) {
            if (a.length < 1) return true;
            if (a[0].equalsIgnoreCase("rain")) Bukkit.getWorlds().get(0).setStorm(true);
            if (a[0].equalsIgnoreCase("clear")) Bukkit.getWorlds().get(0).setStorm(false);
            s.sendMessage("Hava güncellendi.");
            return true;
        }

        if (name.equals("ccgive")) {
            if (!(s instanceof Player)) return true;
            if (a.length < 1) return true;
            Player p = (Player) s;
            p.getInventory().addItem(new org.bukkit.inventory.ItemStack(org.bukkit.Material.matchMaterial(a[0])));
            return true;
        }

        if (name.equals("ccfly")) {
            if (!(s instanceof Player)) return true;
            Player p = (Player) s;
            p.setAllowFlight(!p.getAllowFlight());
            p.sendMessage("Uçma modu: " + (p.getAllowFlight() ? "Açık" : "Kapalı"));
            return true;
        }

        if (name.equals("ccbroadcast")) {
            if (a.length < 1) return true;
            Bukkit.broadcastMessage(ChatColor.RED + String.join(" ", a));
            return true;
        }

        if (name.equals("ccday")) {
            Bukkit.getWorlds().get(0).setTime(1000);
            return true;
        }

        if (name.equals("ccnight")) {
            Bukkit.getWorlds().get(0).setTime(14000);
            return true;
        }

        if (name.equals("ccspeed")) {
            if (!(s instanceof Player)) return true;
            if (a.length < 1) return true;
            Player p = (Player) s;
            p.setWalkSpeed(Float.parseFloat(a[0]));
            return true;
        }

        if (name.equals("ccjump")) {
            if (!(s instanceof Player)) return true;
            Player p = (Player) s;
            p.setVelocity(p.getVelocity().setY(2));
            return true;
        }

        if (name.equals("ccmsg")) {
            if (a.length < 2) return true;
            Player t = Bukkit.getPlayer(a[0]);
            if (t == null) return true;
            t.sendMessage(ChatColor.YELLOW + "[Mesaj] " + String.join(" ", a).replace(a[0], ""));
            return true;
        }

        if (name.equals("ccinvsee")) {
            if (!(s instanceof Player)) return true;
            if (a.length < 1) return true;
            Player p = (Player) s;
            Player t = Bukkit.getPlayer(a[0]);
            if (t == null) return true;
            p.openInventory(t.getInventory());
            return true;
        }

        if (name.equals("ccspawn")) {
            if (!(s instanceof Player)) return true;
            Player p = (Player) s;
            p.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
            return true;
        }

        if (name.equals("ccping")) {
            if (!(s instanceof Player)) return true;
            Player p = (Player) s;
            p.sendMessage("Pingin: " + p.getPing());
            return true;
        }

        return false;
    }
}
