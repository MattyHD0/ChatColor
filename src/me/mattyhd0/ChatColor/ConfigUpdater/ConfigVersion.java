package me.mattyhd0.ChatColor.ConfigUpdater;

import java.util.HashMap;
import java.util.Map;

public class ConfigVersion {

    public Map<String, Object> objectMap = new HashMap<>();
    public Map<String, Object> ignoreExistMap = new HashMap<>();


    public ConfigVersion(){
    }

    public ConfigVersion set(String key, Object object){
        objectMap.put(key, object);
        return this;
    }

    public ConfigVersion setIgnoreExist(String key, Object object){
        ignoreExistMap.put(key, object);
        return this;
    }

    public ConfigVersion delete(String key){
        ignoreExistMap.put(key, null);
        return this;
    }


}
