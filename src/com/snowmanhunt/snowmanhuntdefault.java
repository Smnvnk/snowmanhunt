package com.snowmanhunt;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class snowmanhuntdefault extends JavaPlugin {

    String seeker;

    String pluginLogo = ChatColor.BLUE+"["+ChatColor.AQUA + "Snowman" + ChatColor.WHITE+"Hunt"+ChatColor.BLUE+"]"+ChatColor.WHITE + " ";
    String pluginLogoConsole = "[SnowmanHunt] ";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            //=================
            // /SEEKER COMMAND
            //=================
            if(label.equalsIgnoreCase("seeker")){
                if(sender.isOp()){
                    if(args.length != 0){

                        String givenUsername = args[0];

                        System.out.println(pluginLogoConsole + "Used /seeker command with given username " + givenUsername);
                        System.out.println(pluginLogoConsole + "Searching online player \"" + givenUsername + "\"...");

                        for( Player player : Bukkit.getOnlinePlayers()){
                            if(player.getName().equals(givenUsername)){

                                System.out.println(pluginLogoConsole + "Found a match: \"" + player.getName() + "\"");
                                System.out.println(pluginLogoConsole + "Success! Made " + givenUsername + " a seeker.");
                                sender.sendMessage(pluginLogo + "Success! " + givenUsername + " is now a seeker.");
                                Bukkit.getPlayer(givenUsername).sendTitle(pluginLogo, "Ты теперь снеговик!");

                                seeker = player.getName();

                                for( Player hider : Bukkit.getOnlinePlayers()){
                                    if(!hider.getName().equals(seeker)){
                                        System.out.println(pluginLogoConsole + hider.getName() + " is now a hider.");
                                        hider.sendTitle(pluginLogo, "Теперь ты прячешся!");
                                    }
                                }
                            }
                        }
                        if(seeker == null){
                            sender.sendMessage(pluginLogo + "Player \"" + givenUsername + "\" was not found.");
                            System.out.println(pluginLogoConsole + "Player! \"" + givenUsername + "\" was not found.");
                        }

                    }
                    else{
                        throw new Error("This command should have arguments.");
                    }
                }
                else{
                    sender.sendMessage(pluginLogo + "You should be operator to perform this command.");
                }
            }
            //====================
            // /STARTHUNT COMMAND
            //====================
            else if(label.equalsIgnoreCase("starthunt")){
                if (sender.isOp()){

                    Bukkit.getPlayer(seeker).getAttribute(Attribute.GENERIC_JUMP_STRENGTH).setBaseValue(0.0);
                    Bukkit.getPlayer(seeker).getInventory().addItem(new ItemStack(Material.IRON_SWORD));
                    Bukkit.getPlayer(seeker).addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 1000000, 1), true);
                    Bukkit.getPlayer(seeker).addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 1000000, 1), true);
                    Bukkit.getPlayer(seeker).addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 1000000, 2), true);
                    Bukkit.getPlayer(seeker).addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 1000000, 1), true);


                    for(Player hider : Bukkit.getOnlinePlayers()){
                        if(!hider.getName().equals(seeker)){
                            hider.getAttribute(Attribute.GENERIC_JUMP_STRENGTH).setBaseValue(0.0);
                            hider.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000000, 1), true);
                            hider.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 1000000, 1), true);
                        }
                    }

                }
                else{
                    sender.sendMessage(pluginLogo + "You should be operator to perform this command.");
                }
            }
        }
        else{
            sender.sendMessage(pluginLogoConsole + "Only player can perform this command.");
        }
        return true;
    }
}
