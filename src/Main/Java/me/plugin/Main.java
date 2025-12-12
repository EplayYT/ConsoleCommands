package Main.Java.me.plugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class Main extends JavaPlugin implements Listener {

    private Map<UUID, Double> balance = new HashMap<>();
    private Map<String, Integer> prices = new HashMap<>();

    @Override
    public void onEnable() {


        prices.put("Elmas Kılıç", 500);
        prices.put("Demir Kılıç", 150);
        prices.put("Elmas", 100);
        prices.put("Demir", 40);

        for (Player p : Bukkit.getOnlinePlayers()) {
            balance.put(p.getUniqueId(), 0.0);
        }

        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("EcoMarket aktif!");
    }

    private double getBalance(Player p) {
        if (!balance.containsKey(p.getUniqueId())) balance.put(p.getUniqueId(), 0.0);
        return balance.get(p.getUniqueId());
    }

    private void setBalance(Player p, double newBal) {
        balance.put(p.getUniqueId(), newBal);
    }

    private void openMarket(Player p) {
        Inventory gui = Bukkit.createInventory(null, 27, "Market");

        ItemStack ks = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta ksM = ks.getItemMeta();
        ksM.setDisplayName("§bElmas Kılıç");
        ksM.setLore(Arrays.asList("§aSol tık: Satın al (" + prices.get("Elmas Kılıç") + ")", "§cSağ tık: Sat (" + prices.get("Elmas Kılıç")/2 + ")"));
        ks.setItemMeta(ksM);
        gui.setItem(10, ks);

        ItemStack ik = new ItemStack(Material.IRON_SWORD);
        ItemMeta ikM = ik.getItemMeta();
        ikM.setDisplayName("§fDemir Kılıç");
        ikM.setLore(Arrays.asList("§aSol tık: Satın al (" + prices.get("Demir Kılıç") + ")", "§cSağ tık: Sat (" + prices.get("Demir Kılıç")/2 + ")"));
        ik.setItemMeta(ikM);
        gui.setItem(12, ik);

        ItemStack d = new ItemStack(Material.DIAMOND);
        ItemMeta dM = d.getItemMeta();
        dM.setDisplayName("§bElmas");
        dM.setLore(Arrays.asList("§aSol tık: Satın al (" + prices.get("Elmas") + ")", "§cSağ tık: Sat (" + prices.get("Elmas")/2 + ")"));
        d.setItemMeta(dM);
        gui.setItem(14, d);

        ItemStack iron = new ItemStack(Material.IRON_INGOT);
        ItemMeta ironM = iron.getItemMeta();
        ironM.setDisplayName("§7Demir");
        ironM.setLore(Arrays.asList("§aSol tık: Satın al (" + prices.get("Demir") + ")", "§cSağ tık: Sat (" + prices.get("Demir")/2 + ")"));
        iron.setItemMeta(ironM);
        gui.setItem(16, iron);

        p.openInventory(gui);
    }

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {

        if (!(s instanceof Player)) return true;
        Player p = (Player) s;

        if (cmd.getName().equalsIgnoreCase("market")) {
            openMarket(p);
            return true;
        }

        if (cmd.getName().equalsIgnoreCase("balance")) {
            p.sendMessage("§eBakiyen: §a" + getBalance(p));
            return true;
        }

        if (cmd.getName().equalsIgnoreCase("pay")) {
            if (args.length != 2) {
                p.sendMessage("§cKullanım: /pay oyuncu miktar");
                return true;
            }

            Player hedef = Bukkit.getPlayer(args[0]);
            if (hedef == null) {
                p.sendMessage("§cOyuncu bulunamadı!");
                return true;
            }

            double miktar;
            try {
                miktar = Double.parseDouble(args[1]);
            } catch (Exception e) {
                p.sendMessage("§cSayı gir!");
                return true;
            }

            if (getBalance(p) < miktar) {
                p.sendMessage("§cYeterli paran yok!");
                return true;
            }

            setBalance(p, getBalance(p) - miktar);
            setBalance(hedef, getBalance(hedef) + miktar);

            p.sendMessage("§a" + hedef.getName() + " oyuncusuna " + miktar + " gönderdin.");
            hedef.sendMessage("§a" + p.getName() + " sana " + miktar + " gönderdi!");

            return true;
        }

        return true;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getView().getTitle() == null) return;
        if (!e.getView().getTitle().equals("Market")) return;

        e.setCancelled(true);

        if (!(e.getWhoClicked() instanceof Player)) return;
        Player p = (Player) e.getWhoClicked();

        ItemStack item = e.getCurrentItem();
        if (item == null || !item.hasItemMeta()) return;

        String name = item.getItemMeta().getDisplayName();
        ClickType click = e.getClick();

        if (click.isLeftClick()) {

            if (!prices.containsKey(strip(name))) {
                p.sendMessage("§cBu item satılık değil.");
                return;
            }

            int price = prices.get(strip(name));

            if (getBalance(p) < price) {
                p.sendMessage("§cYeterli paran yok!");
                return;
            }

            setBalance(p, getBalance(p) - price);
            p.getInventory().addItem(new ItemStack(item.getType()));
            p.sendMessage("§aSatın aldın: " + strip(name));
            return;
        }

        if (click.isRightClick()) {

            if (!prices.containsKey(strip(name))) {
                p.sendMessage("§cBu item satılamaz.");
                return;
            }

            int sell = prices.get(strip(name)) / 2;

            setBalance(p, getBalance(p) + sell);
            p.sendMessage("§aSattın: " + strip(name) + " +" + sell);
        }
    }

    private String strip(String s) {
        return s.replace("§b", "").replace("§f", "").replace("§7", "");
    }
}