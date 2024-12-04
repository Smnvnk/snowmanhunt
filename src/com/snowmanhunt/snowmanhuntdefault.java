package com.snowmanhunt;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;

public class snowmanhuntdefault extends JavaPlugin {

    String seeker;

    JavaPlugin plugin = this;

    //КОСТЫЛЬ 1
    int roundLength = 180;
    int roundStart = 10;
    int roundLength1 = roundLength;

    String pluginLogo = ChatColor.BLUE+"["+ChatColor.AQUA + "Snowman" + ChatColor.WHITE+"Hunt"+ChatColor.BLUE+"]"+ChatColor.WHITE + " ";
    String pluginLogoConsole = "[SnowmanHunt] ";

    public void reset(){
        for(Player player : Bukkit.getOnlinePlayers()){

            player.getAttribute(Attribute.GENERIC_JUMP_STRENGTH).setBaseValue(0.4);

            PotionEffectType[] effects = {PotionEffectType.GLOWING, PotionEffectType.BLINDNESS, PotionEffectType.SPEED, PotionEffectType.NIGHT_VISION, PotionEffectType.SLOWNESS, PotionEffectType.STRENGTH, PotionEffectType.HEALTH_BOOST};
            for(PotionEffectType effect : effects){
                player.removePotionEffect(effect);
            }
        }
    }

    public int getRunningTaskId(BukkitTask task){
        return task.getTaskId();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){


            // КОМАНДА /SEEKER
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

            //КОМАНДА /STARTHUNT
            else if(label.equalsIgnoreCase("starthunt")){
                if (sender.isOp()){

                    //КОСТЫЛЬ 2
                    roundLength = roundLength1;

                    reset();

                    Bukkit.getPlayer(seeker).getAttribute(Attribute.GENERIC_JUMP_STRENGTH).setBaseValue(0.0);

                    ItemStack axe = new ItemStack(Material.IRON_AXE);

                    Bukkit.getPlayer(seeker).getInventory().addItem(axe);

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

                    Bukkit.broadcastMessage(pluginLogo+"Игра начинается!");


                    for(Player player : Bukkit.getOnlinePlayers()){
                        player.setLevel(roundLength);
                    }



//                    BukkitRunnable abilityTimer = new BukkitRunnable() {
//                        int abilityCount = 5;
//
//                        @Override
//                        public void run() {
//                            if(abilityCount > 0){
//                                if(abilityCount < 6){
//                                    for(Player player : Bukkit.getOnlinePlayers()){
//                                        player.sendTitle(ChatColor.RED+"Снеговик использует просвет через...", ""+abilityCount);
//                                    }
//                                }
//                                abilityCount--;
//                            }
//                            else{
//                                for(Player player : Bukkit.getOnlinePlayers()){
//                                    player.sendTitle(ChatColor.RED+"Снеговик тебя видит!", "Замедление и свечение на 5 сек.");
//                                    if(!player.getName().equals(seeker)){
//                                        player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 5, 1), true);
//                                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 5, 2), true);
//                                    }
//                                    else{
//                                        player.addPotionEffect( new PotionEffect(PotionEffectType.SPEED, 5, 2), true);
//                                    }
//                                }
//                                abilityCount = 5;
//                                cancel();
//                            }
//                        }
//                    };

                    BukkitRunnable gameTimer = new BukkitRunnable() {

                        @Override
                        public void run() {
                            if(roundLength > -1){
                                for(Player player : Bukkit.getOnlinePlayers()){
                                    player.setLevel(roundLength);
                                }

                                if(roundLength % 20 == 0 & roundLength != 0 & roundLength != 180){
                                    for(Player plr : Bukkit.getOnlinePlayers()){
                                    plr.sendTitle(ChatColor.RED+"Снеговик использует просвет!", "Замедление и свечение на 5 сек.");
                                    if(!plr.getName().equals(seeker)){
                                        plr.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 120, 1), true);
                                        plr.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 120, 2), true);
                                    }
                                    else{
                                        plr.addPotionEffect( new PotionEffect(PotionEffectType.SPEED, 120, 2), true);
                                    }
                                }
                                }

                                roundLength--;
                            }
                            else{
                                for(Player player : Bukkit.getOnlinePlayers()){
                                    player.sendTitle(ChatColor.RED+"Снеговик тебя видит!", "Последний выживший побеждает");

                                    if(!player.getName().equals(seeker)){
                                        player.removePotionEffect(PotionEffectType.SPEED);
                                        player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 1000000, 1), true);
                                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 1000000, 1), true);
                                    }
                                    else{
                                        player.removePotionEffect(PotionEffectType.GLOWING);
                                        player.removePotionEffect(PotionEffectType.SLOWNESS);
                                        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000000, 1), true);
                                        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 1000000, 255), true);
                                    }
                                }
                                cancel();
                            }
                        }
                    };

                    BukkitRunnable roundStartTimer = new BukkitRunnable() {
                        @Override
                        public void run() {
                            if(roundStart > 0){
                                for(Player player : Bukkit.getOnlinePlayers()){
                                    player.sendTitle(ChatColor.RED+"Игра начнется через...", ""+roundStart);
                                }
                                Bukkit.broadcastMessage(ChatColor.RED+"Игра начнется через... "+roundStart);

                                roundStart--;
                            }
                            else{
                                for(Player player : Bukkit.getOnlinePlayers()){
                                    player.sendTitle(ChatColor.RED+"Игра началась!", "");
                                }
                                roundStart = 5;
                                gameTimer.runTaskTimer(plugin, 0, 20);
                                cancel();
                            }
                        }
                    };

                    roundStartTimer.runTaskTimer(plugin, 0, 20);
                }
                else{
                    sender.sendMessage(pluginLogo + "You should be operator to perform this command.");
                }
            }

            else if(label.equalsIgnoreCase("smhroundlength")){
                if(sender.isOp()){
                    roundLength = Integer.parseInt(args[0]);
                    roundLength1 = Integer.parseInt(args[0]);
                    sender.sendMessage(pluginLogo+"Round length is set to " + args[0] + " seconds.");
                }else{
                    sender.sendMessage(pluginLogo+"You should be an operator to perform this command.");
                }
            }

            else if(label.equalsIgnoreCase("smhstopround")){
                if(sender.isOp()){
                    Bukkit.getScheduler().cancelTasks(this);
                    reset();
                }else{
                    sender.sendMessage(pluginLogo+"You should be an operator to perform this command.");
                }
            }
        }
        else{
            sender.sendMessage(pluginLogoConsole + "Only player can perform this command.");
        }
        return true;
    }
}