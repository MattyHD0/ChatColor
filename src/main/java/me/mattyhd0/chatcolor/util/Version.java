package me.mattyhd0.chatcolor.util;

import org.bukkit.Server;

public enum Version {

    LEGACY,
    V1_7,
    V1_8,
    V1_9,
    V1_10,
    V1_11,
    V1_12,
    V1_13,
    V1_14,
    V1_15,
    V1_16,
    V1_17,
    V1_18,
    V1_19,
    V1_20,
    UNKNOWN;

    /*private byte number;

    private Version(int number){
        this.number = (byte) number;
    }*/

    public static Version from(Server server){

        String[] version = server.getBukkitVersion().split("\\.");
        int versionNumber = Integer.parseInt(version[1]);

        if(versionNumber < 7){
            return LEGACY;
        }

        if(versionNumber > 20){
            return UNKNOWN;
        }

        return valueOf("V"+version[0]+"_"+version[1]);

    }

    public boolean isBetween(Version from, Version to){
        return this.ordinal() >= from.ordinal() && this.ordinal() <= to.ordinal();
    }

    public boolean isNewerThan(Version version){
        return this.ordinal() > version.ordinal();
    }

    public boolean isNewerOrEqualsThan(Version version){
        return this.ordinal() >= version.ordinal();
    }

    public boolean isOlderThan(Version version){
        return this.ordinal() < version.ordinal();
    }

    public boolean isOlderOrEqualsThan(Version version){
        return this.ordinal() <= version.ordinal();
    }

    public boolean isSupportingRGB(){
        return this.ordinal() >= V1_16.ordinal();
    }

    public boolean hasNewMaterials(){
        return this.ordinal() >= V1_13.ordinal();
    }

}
