package me.mattyhd0.ChatColor.Utility;

import com.google.common.collect.Multimap;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.mattyhd0.ChatColor.XSeries.XMaterial;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.banner.PatternType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    public static String capitalize(String string){

        //Divide la id EJEMPLO: DIAMOND_SWORD en el array {"DIAMOND", "SWORD"}
        String[] strings = string.split("_");
        String finalString = "";

        //Transforma el array {"DIAMOND", "SWORD"} en el String "Diamond Sword "
        for(String str: strings){
            str = str.toLowerCase();
            str = str.replaceFirst(Character.toString(str.charAt(0)), Character.toString(str.charAt(0)).toUpperCase());
            finalString = finalString+str+" ";
        }

        //Elimina el " " (espacio) al final del texto si este existe
        if(finalString.charAt(finalString.length()-1) == ' ') finalString = finalString.substring(0, finalString.length()-1);

        return finalString;

    }

    public static String color(String text){

        text = text.replaceAll("&#", "#");

        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(text);

        if(text.length() > 0){

            while (matcher.find()) {

                String color = text.substring(matcher.start(), matcher.end());

                try {
                    text = text.replace(color, net.md_5.bungee.api.ChatColor.of(color) + "");
                } catch (NoSuchMethodError e){
                    text = text.replace(color, "");
                }

                matcher = pattern.matcher(text);

            }

        }

        return ChatColor.translateAlternateColorCodes('&', text);

    }

    public static String bukkitGradient(String text, java.awt.Color gradientStart, java.awt.Color gradientEnd, boolean bold, boolean italic, boolean underline, boolean magic, boolean strikethrough){

        float r1 = gradientEnd.getRed(); float r2 = gradientStart.getRed();
        float g1 = gradientEnd.getGreen(); float g2 = gradientStart.getGreen();
        float b1 = gradientEnd.getBlue(); float b2 = gradientStart.getBlue();

        float rMath = (r1+r2/2)/text.length(); float rMath2 = (r2-r1)/text.length();
        float gMath = (g1+g2/2)/text.length(); float gMath2 = (g2-g1)/text.length();
        float bMath = (b1+b2/2)/text.length(); float bMath2 = (b2-b1)/text.length();

        String coloredText = "";
        boolean format = (bold || italic || underline || magic || strikethrough);

        for(int i = 1; i < text.length()+1; i++){

            float r = r1 > r2 ? rMath * i : r2-(rMath2)*i;
            float g = g1 > g2 ? gMath * i : g2-(gMath2)*i;
            float b = b1 > b2 ? bMath * i : b2-(bMath2)*i;

            String c = Character.toString(text.charAt(i-1));

            if(format) {
                if (bold) c = net.md_5.bungee.api.ChatColor.BOLD + c;
                if (italic) c = net.md_5.bungee.api.ChatColor.ITALIC + c;
                if (underline) c = net.md_5.bungee.api.ChatColor.UNDERLINE + c;
                if (magic) c = net.md_5.bungee.api.ChatColor.MAGIC + c;
                if (strikethrough) c = net.md_5.bungee.api.ChatColor.STRIKETHROUGH + c;
            }

            java.awt.Color color;
            try {
                color = new java.awt.Color(Math.round(r), Math.round(g), Math.round(b));
            } catch (IllegalArgumentException e){

                if(r < 0) r = 0;
                if(r > 255) r = 255;
                if(g < 0) g = 0;
                if(g > 255) g = 255;
                if(b < 0) b = 0;
                if(b > 255) b = 255;

                color = new java.awt.Color(Math.round(r), Math.round(g), Math.round(b));
            }

            coloredText = coloredText + net.md_5.bungee.api.ChatColor.of(color) + c;

        }

        return coloredText;
    }

    public static String bukkitGradient(String text, List<java.awt.Color> colors, boolean bold, boolean italic, boolean underline, boolean magic, boolean strikethrough){

        int divisions = colors.size()-1;
        int divideEveryChars = text.length()/divisions > 0 ? text.length()/divisions : 1;
        List<String> substrings = new ArrayList<>();
        StringBuilder finalText = new StringBuilder();

        for(int i = 0; i <= text.length()+divideEveryChars; i += divideEveryChars){

            if(i+divideEveryChars > text.length() && text.length() > 0) {
                int lastSub = substrings.size()-1;
                String latestStr = substrings.get(lastSub);
                substrings.set(lastSub, latestStr+text.substring(i));
                break;
            }

            String sub = text.substring(i, (i+divideEveryChars));
            substrings.add(sub);

        }

        int color = 0;
        for(String s: substrings){

            java.awt.Color color1;
            java.awt.Color color2;

            try {
                color1 = colors.get(color);
                color2 = colors.get(color + 1);
            } catch (IndexOutOfBoundsException e){
                color1 = colors.get(colors.size()-1);
                color2 = colors.get(colors.size()-1);
            }

            finalText.append(
                    bukkitGradient(s,
                            color1,
                            color2,
                            bold,
                            italic,
                            underline,
                            magic,
                            strikethrough
                    )
            );

            color++;
        }

        return finalText.toString();
    }

    public static String getRandomString(String characters, int length){

        String[] chars = characters.split("");
        String text = "";

        for(int i = 0; i < length+1; i++){

            double c = Math.random()*(chars.length-1);
            text = text+chars[(int)Math.round(c)];

        }

        return text;

    }

    public static List<String> coloredList(List<String> stringList){

        List<String> coloredList = new ArrayList<>();
        for(String line: stringList){
            coloredList.add(Util.color(line));
        }
        return coloredList;

    }

    public static ItemStack getSkullFromValue(String value){

        ItemStack skull = XMaterial.PLAYER_HEAD.parseItem();
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();

        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "");

        gameProfile.getProperties().put("textures", new Property("textures", value));

        Field profileField = null;
        try {
            profileField = skullMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(skullMeta, gameProfile);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }

        skull.setItemMeta(skullMeta);
        return skull;

    }

    public static ItemStack getItemStackFromString(String material){

        if(material.startsWith("SKULL:")){
            material = material.replace("SKULL:", "");
            return getSkullFromValue(material);
        } else {
            XMaterial mat;

            try {
                mat = XMaterial.valueOf(material.split(":")[0]);
            } catch (IllegalArgumentException e){
                mat = XMaterial.STONE;
            }

            return mat.parseItem();
        }

    }

    public static ItemStack getItemFromConfig(FileConfiguration config, String key){

        Error error = new Error();
        error.setDescription("These errors were encountered when trying to create an ItemStack instance.");

        String material = config.getString(key+".material");

        ItemStack itemStack = material != null ? getItemStackFromString(material) : XMaterial.STONE.parseItem();
        int amount = config.getInt(key+".amount");
        String name = config.getString(key+".name");
        List<String> lore = config.getStringList(key+".lore");
        boolean unbreakable = config.getBoolean(key+".unbreakable");
        List<String> flags = config.getStringList(key+".flags");
        List<String> enchantments = config.getStringList(key+".enchantments");
        List<String> attributes = config.getStringList(key+".attributes");

        itemStack.setAmount(amount);

        ItemMeta itemMeta = itemStack.getItemMeta();

        if(itemMeta != null) {

            if (name != null) itemMeta.setDisplayName(Util.color(name));
            if (unbreakable == true) {
                try {
                    itemMeta.setUnbreakable(unbreakable);
                } catch (NoSuchMethodError exception){
                    error.addError("This version of Bukkit/Spigot does not support the Unbreakable tag.");
                }
            }

            if(config.contains(key+".custom_model_data")){
                try {
                    itemMeta.setCustomModelData(config.getInt(key+".custom_model_data"));
                } catch (NoSuchMethodError e){
                    error.addError("The setCustomModelData method does not exist in this version of Spigot");
                }
            }

            if (flags.size() > 0) {
                for (String flag : flags) {

                    try {
                        ItemFlag itemFlag = ItemFlag.valueOf(flag);
                        itemMeta.addItemFlags(itemFlag);
                    } catch (IllegalArgumentException exception){
                        error.addError("The ItemFlag ("+flag+") is not a valid ItemFlag.");
                    }
                }
            }

            if (enchantments.size() > 0) {
                for (String enchant : enchantments) {
                    String[] ench = enchant.split(" ");
                    int level = 0;
                    try {
                        Enchantment enchantment = Enchantment.getByName(ench[0]);
                        Integer.parseInt(ench[1]);
                        if(enchantment != null) {
                            itemMeta.addEnchant(enchantment, level, true);
                        } else {
                            error.addError("The Enchantment ("+enchant+") is not a valid Enchantment.");
                        }
                    } catch (NumberFormatException exception){
                        error.addError("The value ("+ench[1]+") is not a valid value.");
                    }

                }

            }

            if(attributes.size() > 0){

                for(String attr : attributes){
                    String[] att = attr.split(" ");

                    try {

                        Class clazz = Class.forName("org.bukkit.attribute.Attribute");
                        Attribute attribute = Attribute.valueOf(att[0]);
                        EquipmentSlot slot = EquipmentSlot.valueOf(att[2]);

                        double value = 0;
                        try {
                            value = Double.parseDouble(att[1]);
                        } catch (NumberFormatException exception) {
                            error.addError("The value (" + att[1] + ") is not a valid Double type value.");
                        }

                        if (attribute == null) {

                            error.addError("The Attribute (" + att[0] + ") is not a valid Attribute.");

                        } else if (slot == null) {

                            error.addError("The EquipmentSlot (" + att[2] + ") is not a valid EquipmentSlot.");

                        } else {

                            itemMeta.addAttributeModifier(
                                    attribute,
                                    new AttributeModifier(UUID.randomUUID(), "KatyLIBModifier", value, AttributeModifier.Operation.ADD_NUMBER, slot)
                            );

                        }

                        for(int i = 0;i <= lore.size();i++){

                            Multimap<Attribute, AttributeModifier> attributes1 = itemMeta.getAttributeModifiers();

                            for(Map.Entry<Attribute, AttributeModifier> entry: attributes1.entries()){

                                lore.set(i, lore.get(i).replaceAll(
                                        "\\{"+entry.getKey().toString()+"}",
                                        entry.getValue().getAmount()+""
                                        ).replaceAll(
                                        "\\{"+entry.getKey().toString()+"_INT}",
                                        (int)entry.getValue().getAmount()+""
                                        )
                                );

                            }

                        }

                    } catch (Exception exception){
                        if(!error.getErrors().contains("This version of Bukkit/Spigot does not support Attribute and AttributeModifier.")) {
                            error.addError("This version of Bukkit/Spigot does not support Attribute and AttributeModifier.");
                        }
                    }

                }

            }

            if (lore.size() > 0) itemMeta.setLore(Util.coloredList(lore));

            // LeatherMeta
            if(itemMeta instanceof LeatherArmorMeta){

                String colorStr = config.getString(key+".leatherarmor.color");

                if(colorStr != null){

                    if(colorStr.matches("[0-9]{1,3},( )[0-9]{1,3},( )[0-9]{1,3}")) {

                        String[] rgb = colorStr.replaceAll(" ", "").split(",");

                        int r = Integer.parseInt(rgb[0]);
                        int g = Integer.parseInt(rgb[1]);
                        int b = Integer.parseInt(rgb[2]);


                        Color color = Color.fromRGB(r, g, b);

                        ((LeatherArmorMeta) itemMeta).setColor(color);

                    } else {
                        error.addError("The color ("+colorStr+") is not a valid RGB color.");
                    }

                }

            }

            //PotionMeta
            if(itemMeta instanceof PotionMeta){

                PotionMeta potionMeta = (PotionMeta) itemMeta;

                String colorStr = config.getString(key+".potion.color");
                List<String> effectList = config.getStringList(key+".potion.effects");

                if(colorStr != null) {

                    if (colorStr.matches("[0-9]{1,3},( )[0-9]{1,3},( )[0-9]{1,3}")) {

                        String[] rgb = colorStr.replaceAll(" ", "").split(",");

                        int r = Integer.parseInt(rgb[0]);
                        int g = Integer.parseInt(rgb[1]);
                        int b = Integer.parseInt(rgb[2]);

                        Color color = Color.fromRGB(r, g, b);

                        try {
                            potionMeta.setColor(color);
                        } catch (NoSuchMethodError exception){
                            error.addError("This version of Bukkit/Spigot does not support PotionColor.");
                        }

                    } else {
                        error.addError("The color (" + colorStr + ") is not a valid RGB color.");
                    }

                }

                if(effectList.size() > 0){

                    for(String effectStr: effectList){

                        String[] effect = effectStr.split(" ");

                        PotionEffectType type = PotionEffectType.getByName(effect[0]);
                        int amplifier = Integer.parseInt(effect[1]);
                        int duration = Integer.parseInt(effect[2]);
                        boolean ambient = Boolean.parseBoolean(effect[3]);
                        boolean particles = Boolean.parseBoolean(effect[4]);

                        PotionEffect potionEffect = new PotionEffect(type, duration*20, amplifier, ambient, particles);
                        ((PotionMeta)itemMeta).addCustomEffect(potionEffect, true);


                    }

                }

            }

            if(itemMeta instanceof BannerMeta){

                String colorStr = config.getString(key+".banner.basecolor");
                DyeColor dyeColor = DyeColor.WHITE;

                try{
                    dyeColor = DyeColor.valueOf(colorStr);
                } catch (IllegalArgumentException exception){
                    error.addError("The DyeColor (" + colorStr + ") is not a valid DyeColor.");
                }

                ((BannerMeta) itemMeta).setBaseColor(dyeColor);

                List<String> patternList = config.getStringList(key+".banner.patterns");

                for(String patternStr: patternList){

                    String[] pattern = patternStr.split(" ");

                    DyeColor color = DyeColor.BLACK;

                    try{
                        color = DyeColor.valueOf(pattern[0]);
                    } catch (IllegalArgumentException exception){
                        error.addError("The DyeColor (" + pattern[0] + ") is not a valid DyeColor.");
                    }

                    try {
                        PatternType patternType = PatternType.valueOf(pattern[1]);
                        ((BannerMeta) itemMeta).addPattern(new org.bukkit.block.banner.Pattern(color, patternType));

                    } catch (IllegalArgumentException exception){
                        error.addError("The PatternType (" + pattern[1] + ") is not a valid PatternType.");
                    }

                }

            }

            if(itemMeta instanceof FireworkMeta){

                String type = config.getString(key+".firework.type");
                int power = config.getInt(key+".firework.power");
                boolean flicker = config.getBoolean(key+".firework.flicker");
                boolean trail = config.getBoolean(key+".firework.trail");
                List<String> explodeColors = config.getStringList(key+".firework.colors.explode");
                List<String> fadeColors = config.getStringList(key+".firework.colors.fade");


                FireworkEffect.Builder builder = FireworkEffect.builder();

                if(flicker) builder = builder.withFlicker();
                if(trail) builder = builder.withTrail();

                try {
                    FireworkEffect.Type explosion = FireworkEffect.Type.valueOf(type);
                    builder = builder.with(explosion);
                } catch (IllegalArgumentException exception){
                    error.addError("The FireworkEffect.Type (" + type + ") is not a valid FireworkEffect.Type.");
                }

                List<Color> explode = new ArrayList<>();
                List<Color> fade = new ArrayList<>();


                for(String colorStr: explodeColors){

                    if (colorStr.matches("[0-9]{1,3},( )[0-9]{1,3},( )[0-9]{1,3}")) {

                        String[] rgb = colorStr.replaceAll(" ", "").split(",");

                        int r = Integer.parseInt(rgb[0]);
                        int g = Integer.parseInt(rgb[1]);
                        int b = Integer.parseInt(rgb[2]);

                        Color color = Color.fromRGB(r, g, b);

                        try {
                            explode.add(color);
                        } catch (NoSuchMethodError exception){
                            error.addError("This version of Bukkit/Spigot does not support PotionColor.");
                        }

                    } else {
                        error.addError("The color (" + colorStr + ") is not a valid RGB color.");
                    }

                }

                for(String colorStr: fadeColors){

                    if (colorStr.matches("[0-9]{1,3},( )[0-9]{1,3},( )[0-9]{1,3}")) {

                        String[] rgb = colorStr.replaceAll(" ", "").split(",");

                        int r = Integer.parseInt(rgb[0]);
                        int g = Integer.parseInt(rgb[1]);
                        int b = Integer.parseInt(rgb[2]);

                        Color color = Color.fromRGB(r, g, b);

                        try {
                            explode.add(color);
                        } catch (NoSuchMethodError exception){
                            error.addError("This version of Bukkit/Spigot does not support PotionColor.");
                        }

                    } else {
                        error.addError("The color (" + colorStr + ") is not a valid RGB color.");
                    }

                }

                builder = builder.withColor(explode).withFade(fade);


                ((FireworkMeta)itemMeta).addEffect(builder.build());

            }

            if(itemStack instanceof BookMeta){

                String title = config.getString(key+".book.title");
                String author = config.getString(key+".book.author");
                String generation = config.getString(key+".book.generation");
                List<String> pages = config.getStringList(key+".book.pages");

                if(generation != null) {
                    try {
                        BookMeta.Generation gen = BookMeta.Generation.valueOf(generation);
                    } catch (IllegalArgumentException exception){
                        error.addError("The BookMeta.Generation (" + generation + ") is not a valid BookMeta.Generation.");
                    }
                }

                if(title != null) ((BookMeta)itemMeta).setTitle("Blank");
                if(author != null) ((BookMeta)itemMeta).setAuthor(Util.color(author));
                if(pages.size() > 0) ((BookMeta)itemMeta).setPages(Util.coloredList(pages));

            }

            itemStack.setItemMeta(itemMeta);

        }

        if(error.hasErrors()){
            error.showErrorsBukkit();
        }

        return itemStack;

    }

}
