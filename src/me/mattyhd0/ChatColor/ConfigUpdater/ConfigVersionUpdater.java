package me.mattyhd0.ChatColor.ConfigUpdater;

import me.mattyhd0.ChatColor.Configuration.YMLFile;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ConfigVersionUpdater {

    YMLFile ymlFile;
    FileConfiguration fileConfiguration;
    List<ConfigVersion> configVersions = new ArrayList<>();

    public ConfigVersionUpdater(YMLFile ymlFile){
        this.ymlFile = ymlFile;
        this.fileConfiguration = ymlFile.get();
    }

    public void addConfigVersion(ConfigVersion... configVersion){
        configVersions.addAll(Arrays.asList(configVersion));
    }

    public void update(){

        for(int i = 0; i < configVersions.size(); i++){

            int configVer = fileConfiguration.getInt("config-version");

            if(configVer == i){

                fileConfiguration.set("config-version", configVer+1);
                ConfigVersion configVersion = configVersions.get(i);

                for(Map.Entry<String, Object> entry: configVersion.objectMap.entrySet()){

                    String key = entry.getKey();
                    Object value = entry.getValue();

                    if(!fileConfiguration.contains(key)) fileConfiguration.set(key, value);
                }

                for(Map.Entry<String, Object> entry: configVersion.ignoreExistMap.entrySet()){

                    String key = entry.getKey();
                    Object value = entry.getValue();

                    fileConfiguration.set(key, value);
                }

            }

            ymlFile.save();

        }

    }

}
