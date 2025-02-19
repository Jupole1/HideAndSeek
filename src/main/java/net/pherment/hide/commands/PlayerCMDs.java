package net.pherment.hide.commands;

import net.pherment.hide.HideAndSeek;
import net.pherment.hide.arena.HASArena;
import net.pherment.hide.files.ArenaConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class PlayerCMDs implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;

        if (player.isOp()) {
                if (args[0].equals("arena")) {
                    if (args[1].equals("create")) {
                        if (args.length < 4) {
                            player.sendMessage("Not enough arguments! /hs arena create name minPlayers maxPlayers");
                            return true;
                        }
                        String name = args[2];
                        int minPlayers, maxPlayers;
                        try {
                            minPlayers = Integer.parseInt(args[3]);
                            maxPlayers = Integer.parseInt(args[4]);
                        } catch(Exception e) {
                            System.out.println("Cannot be converted to type Int");
                            return true;
                        }

                        if (HASArena.getArenaByName(name) != null) {
                            player.sendMessage("Such an arena already exists!");
                            return true;
                        }

                        HASArena.arenasList.add(new HASArena(minPlayers, maxPlayers, 10, name));
                        ArenaConfig.getArenaConfig().addDefault("name", name);
                        ArenaConfig.getArenaConfig().addDefault("minPlayers", minPlayers);
                        ArenaConfig.getArenaConfig().addDefault("maxPlayers", maxPlayers);
                        ArenaConfig.getArenaConfig().addDefault("lobby", 0);
                        ArenaConfig.getArenaConfig().addDefault("seekersSpawn", 0);
                        ArenaConfig.getArenaConfig().addDefault("hidersSpawn", 0);
                        ArenaConfig.save();
                        player.sendMessage("The arena is created!");
                        return true;

                    } else if (args[1].equals("setLobby")) {
                        HASArena arena = HASArena.getArenaByName(args[2]);
                        if (arena == null) {
                            player.sendMessage("There is no such arena.");
                            return true;
                        }
                        arena.setLobbyLocation(player.getLocation());
                        ArenaConfig.getArenaConfig().set("lobby", player.getLocation());
                        ArenaConfig.save();
                        player.sendMessage("Lobby installed!");
                        return true;
                    } else if (args[1].equals("setSeekersSpawn")) {
                        HASArena arena = HASArena.getArenaByName(args[2]);
                        if (arena == null) {
                            player.sendMessage("such an arena does not exist!");
                            return true;
                        }
                        arena.setSeekersLocation(player.getLocation());
                        ArenaConfig.getArenaConfig().set("seekersSpawn", player.getLocation());
                        ArenaConfig.save();
                        player.sendMessage("Seeker spawn is set!");
                        return true;
                    } else if (args[1].equals("setHidersSpawn")) {
                        HASArena arena = HASArena.getArenaByName(args[2]);
                        if (arena == null) {
                            player.sendMessage("Hider Spawn Set!");
                            return true;
                        }
                        arena.setHidersLocation(player.getLocation());
                        ArenaConfig.getArenaConfig().set("hidersSpawn", player.getLocation());
                        ArenaConfig.save();
                        player.sendMessage("Hider Spawn Set!");
                        return true;
                    }
                } else if (args[0].equals("join")) {
                    HASArena arena = HASArena.getArenaByName(args[1]);
                    if (arena == null) {
                        player.sendMessage("such an arena does not exist!");
                        return true;
                    }

                    HASArena arenaPlayer = HASArena.getArenaOfPlayer(player);
                    if (arenaPlayer != null) {
                        player.sendMessage("You are already in the game!");
                        return true;
                    }

                    arena.joinPlayer(player);
                    return true;
                } else if (args[0].equals("leave")) {
                    HASArena arena = HASArena.getArenaOfPlayer(player);
                    if (arena == null) {
                        player.sendMessage("You are not in the game right now.!");
                        return true;
                    }
                    arena.leavePlayer(player);
                    return true;
                }
        } else {
            if (args[0].equals("join")) {
                HASArena arena = HASArena.getArenaByName(args[1]);
                if (arena == null) {
                    player.sendMessage("There is no such arena.");
                    return true;
                }
                arena.joinPlayer(player);
                return true;
            } else if (args[0].equals("leave")) {
                HASArena arena = HASArena.getArenaOfPlayer(player);
                if (arena == null) {
                    player.sendMessage("You are not in the game right now!");
                    return true;
                }
                arena.leavePlayer(player);
                return true;
            }
        }

        return false;
    }
}
