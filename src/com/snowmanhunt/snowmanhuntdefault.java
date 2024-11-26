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
import org.bukkit.scheduler.BukkitTask;

public class snowmanhuntdefault extends JavaPlugin {

    String seeker;

    //КОСТЫЛЬ 1
    final int[] roundLength = {180};
    int roundLength1 = roundLength[0];

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
                    roundLength[0] = roundLength1;

                    Bukkit.getPlayer(seeker).getAttribute(Attribute.GENERIC_JUMP_STRENGTH).setBaseValue(0.0);
                    Bukkit.getPlayer(seeker).getInventory().addItem(new ItemStack(Material.IRON_AXE));
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

                    //КОСТЫЛЬ 3
                    final boolean[] gameStarted = {false};

                    for(Player plr : Bukkit.getOnlinePlayers()){
                        plr.setLevel(roundLength[0]);
                    }

                    final int[] gameStartCount = {10};

                    //ТАЙМЕРТАЙМЕРТАЙМЕРТАЙМЕРТАЙМЕРТАЙМЕРТАЙМЕРТАЙМЕРТАЙМЕРТАЙМЕРТАЙМЕРТАЙМЕРТАЙМЕРТАЙМЕРТАЙМЕР
                    //БЛЯТЬ ЭТИ 37 СТРОК ДЕРЬМИЩА РАБОТАЮТ ЧЕРЕЗ ОЧКО БЕГЕМОТА
                    final BukkitTask task = Bukkit.getScheduler().runTaskTimer(this, ()->{
                        if(gameStartCount[0] > 0){
                            for(Player player : Bukkit.getOnlinePlayers()){
                                player.sendMessage(ChatColor.RED+"Игра начнется через... "+gameStartCount[0], "");
                            }
                            gameStartCount[0]--;
                        }
                        else{
                            if(!gameStarted[0]){
                                for(Player player : Bukkit.getOnlinePlayers()){
                                    player.sendTitle(ChatColor.RED+"Прячтесь!", "Игра началась!");
                                    gameStarted[0] = true;
                                }
                            }
                            else{
                                if(roundLength[0] > -1){
                                    for(Player player : Bukkit.getOnlinePlayers()){
                                        player.setLevel(roundLength[0]);
                                    }
                                    roundLength[0]--;
                                }
                                else{
                                    for(Player player : Bukkit.getOnlinePlayers()){
                                        player.sendTitle(ChatColor.RED+"Снеговик видит тебя!", "Последний выжившый побеждает");
                                        if(!player.getName().equals(seeker)){
                                            player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 1000000, 1));
                                        }
                                        else{
                                            player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 1000000, 255));
                                        }
                                    }
                                    Bukkit.getScheduler().cancelTasks(this);
                                }
                            }
                        }
                    }, 0, 20);
                }
                else{
                    sender.sendMessage(pluginLogo + "You should be operator to perform this command.");
                }
            }

            else if(label.equalsIgnoreCase("smhroundlength")){
                if(sender.isOp()){
                    roundLength[0] = Integer.parseInt(args[0]);
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