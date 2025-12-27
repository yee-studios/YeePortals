package me.nachtok0.yeeportals;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.*;

public class YeePortals extends JavaPlugin implements Listener, CommandExecutor {

    private final Map<UUID, Location[]> selections = new HashMap<>();
    private final Set<UUID> selectionMode = new HashSet<>();
    private final List<Portal> portals = new ArrayList<>();
    private final Map<UUID, Long> teleportCooldown = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadPortals();
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("portal").setExecutor(this);
        getLogger().info("YeePortals activado correctamente.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Solo jugadores.");
            return true;
        }

        Player p = (Player) sender;
        if (!p.hasPermission("portal.admin")) {
            p.sendMessage(ChatColor.RED + "No tienes permiso.");
            return true;
        }

        if (args.length == 0) {
            p.sendMessage(ChatColor.GOLD + "--- Ayuda de YeePortals ---");
            p.sendMessage(ChatColor.YELLOW + "/portal region " + ChatColor.WHITE + "- Activa la varita de selección.");
            p.sendMessage(ChatColor.YELLOW + "/portal create <nombre> " + ChatColor.WHITE + "- Crea un portal en la selección.");
            p.sendMessage(ChatColor.YELLOW + "/portal connect <origen> <destino> " + ChatColor.WHITE + "- Conecta dos portales.");
            p.sendMessage(ChatColor.YELLOW + "/portal disconnect <nombre> " + ChatColor.WHITE + "- Quita el destino de un portal.");
            p.sendMessage(ChatColor.YELLOW + "/portal delete <nombre> " + ChatColor.WHITE + "- Borra un portal para siempre.");
            p.sendMessage(ChatColor.YELLOW + "/portal list " + ChatColor.WHITE + "- Ver lista de portales.");
            return true;
        }

        if (args[0].equalsIgnoreCase("region")) {
            if (selectionMode.contains(p.getUniqueId())) {
                selectionMode.remove(p.getUniqueId());
                p.sendMessage(ChatColor.YELLOW + "Modo selección desactivado.");
            } else {
                selectionMode.add(p.getUniqueId());
                p.sendMessage(ChatColor.GREEN + "Modo selección activado. Usa Clic Izq/Der para marcar esquinas.");
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("create")) {
            if (args.length < 2) {
                p.sendMessage(ChatColor.RED + "Uso: /portal create <nombre>");
                return true;
            }
            Location[] sel = selections.get(p.getUniqueId());
            if (sel == null || sel[0] == null || sel[1] == null) {
                p.sendMessage(ChatColor.RED + "Debes seleccionar una región primero (/portal region).");
                return true;
            }
            if (!sel[0].getWorld().equals(sel[1].getWorld())) {
                p.sendMessage(ChatColor.RED + "Los puntos deben estar en el mismo mundo.");
                return true;
            }

            String name = args[1];
            if (getPortal(name) != null) {
                p.sendMessage(ChatColor.RED + "Ya existe un portal con ese nombre.");
                return true;
            }

            createPortal(name, sel[0], sel[1]);
            p.sendMessage(ChatColor.GREEN + "Portal '" + name + "' creado!");
            return true;
        }

        if (args[0].equalsIgnoreCase("connect")) {
            if (args.length < 3) {
                p.sendMessage(ChatColor.RED + "Uso: /portal connect <origen> <destino>");
                return true;
            }
            Portal from = getPortal(args[1]);
            Portal to = getPortal(args[2]);

            if (from == null || to == null) {
                p.sendMessage(ChatColor.RED + "Uno de los portales no existe.");
                return true;
            }

            from.targetName = to.name;
            savePortals();
            p.sendMessage(ChatColor.GREEN + "Portales conectados: " + from.name + " -> " + to.name);
            return true;
        }

        if (args[0].equalsIgnoreCase("list")) {
            if (portals.isEmpty()) {
                p.sendMessage(ChatColor.YELLOW + "No hay portales creados.");
                return true;
            }
            p.sendMessage(ChatColor.GOLD + "--- Lista de Portales (" + portals.size() + ") ---");
            for (Portal portal : portals) {
                String target = (portal.targetName == null || portal.targetName.isEmpty())
                        ? ChatColor.RED + "Sin destino"
                        : ChatColor.GREEN + "-> " + portal.targetName;

                p.sendMessage(ChatColor.AQUA + portal.name + ChatColor.GRAY + " (" + portal.worldName + ") " + target);
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("disconnect")) {
            if (args.length < 2) {
                p.sendMessage(ChatColor.RED + "Uso: /portal disconnect <nombre>");
                return true;
            }
            Portal portal = getPortal(args[1]);
            if (portal == null) {
                p.sendMessage(ChatColor.RED + "El portal no existe.");
                return true;
            }

            portal.targetName = null;
            savePortals();
            p.sendMessage(ChatColor.GREEN + "El portal '" + portal.name + "' ha sido desconectado.");
            return true;
        }

        if (args[0].equalsIgnoreCase("delete")) {
            if (args.length < 2) {
                p.sendMessage(ChatColor.RED + "Uso: /portal delete <nombre>");
                return true;
            }
            String name = args[1];
            Portal portal = getPortal(name);

            if (portal == null) {
                p.sendMessage(ChatColor.RED + "El portal no existe.");
                return true;
            }

            portals.remove(portal);
            getConfig().set("portals." + name, null);
            saveConfig();

            p.sendMessage(ChatColor.GREEN + "Portal '" + name + "' eliminado correctamente.");
            return true;
        }

        return false;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (!selectionMode.contains(p.getUniqueId())) return;

        if (e.getAction() == Action.LEFT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            e.setCancelled(true);
            Location loc = e.getClickedBlock().getLocation();
            Location[] sel = selections.getOrDefault(p.getUniqueId(), new Location[2]);

            if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
                sel[0] = loc;
                p.sendMessage(ChatColor.GOLD + "Posición 1 establecida.");
            } else {
                sel[1] = loc;
                p.sendMessage(ChatColor.GOLD + "Posición 2 establecida.");
            }
            selections.put(p.getUniqueId(), sel);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Location to = e.getTo();
        Location from = e.getFrom();

        if (to.getBlockX() == from.getBlockX() && to.getBlockY() == from.getBlockY() && to.getBlockZ() == from.getBlockZ()) {
            return;
        }

        Player p = e.getPlayer();
        if (teleportCooldown.containsKey(p.getUniqueId())) {
            if (System.currentTimeMillis() < teleportCooldown.get(p.getUniqueId())) return;
        }

        for (Portal portal : portals) {
            if (portal.isInside(to)) {
                if (portal.targetName == null || portal.targetName.isEmpty()) continue;

                Portal targetPortal = getPortal(portal.targetName);
                if (targetPortal == null) {
                    p.sendMessage(ChatColor.RED + "El portal de destino no existe o está roto.");
                    return;
                }

                double offsetX = to.getX() - portal.min.getX();
                double offsetY = to.getY() - portal.min.getY();
                double offsetZ = to.getZ() - portal.min.getZ();

                World targetWorld = Bukkit.getWorld(targetPortal.worldName);
                if (targetWorld == null) {
                    return;
                }

                Location destLoc = targetPortal.min.toLocation(targetWorld);

                destLoc.add(offsetX, offsetY, offsetZ);

                destLoc.setYaw(p.getLocation().getYaw());
                destLoc.setPitch(p.getLocation().getPitch());

                p.teleport(destLoc);
                p.playSound(destLoc, Sound.ORB_PICKUP, 1f, 1f);

                teleportCooldown.put(p.getUniqueId(), System.currentTimeMillis() + 1500);
                return;
            }
        }
    }

    private void createPortal(String name, Location l1, Location l2) {
        Portal p = new Portal(name, l1.getWorld().getName(), l1.toVector(), l2.toVector());
        portals.add(p);
        savePortals();
    }

    private Portal getPortal(String name) {
        for (Portal p : portals) {
            if (p.name.equalsIgnoreCase(name)) return p;
        }
        return null;
    }

    private void savePortals() {
        for (Portal p : portals) {
            String path = "portals." + p.name;
            getConfig().set(path + ".world", p.worldName);
            getConfig().set(path + ".min", p.min);
            getConfig().set(path + ".max", p.max);
            getConfig().set(path + ".target", p.targetName);
        }
        saveConfig();
    }

    private void loadPortals() {
        portals.clear();
        ConfigurationSection sec = getConfig().getConfigurationSection("portals");
        if (sec == null) return;

        for (String key : sec.getKeys(false)) {
            String world = sec.getString(key + ".world");
            Vector min = sec.getVector(key + ".min");
            Vector max = sec.getVector(key + ".max");
            String target = sec.getString(key + ".target");

            Portal p = new Portal(key, world, min, max);
            p.targetName = target;
            portals.add(p);
        }
    }

    private static class Portal {
        String name;
        String worldName;
        Vector min;
        Vector max;
        String targetName;

        public Portal(String name, String worldName, Vector v1, Vector v2) {
            this.name = name;
            this.worldName = worldName;
            this.min = Vector.getMinimum(v1, v2);
            this.max = Vector.getMaximum(v1, v2);
        }

        public boolean isInside(Location loc) {
            if (!loc.getWorld().getName().equals(worldName)) return false;
            return loc.getX() >= min.getX() && loc.getX() <= max.getX() + 1 &&
                    loc.getY() >= min.getY() && loc.getY() <= max.getY() + 1 &&
                    loc.getZ() >= min.getZ() && loc.getZ() <= max.getZ() + 1;
        }
    }
}