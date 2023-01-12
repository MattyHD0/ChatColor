package me.mattyhd0.chatcolor.pattern.api;

import java.util.List;

public interface IPattern {

    public String getText(String text);

    public String getName(boolean formatted);

    public String getPermission();

    public List<String> getColors();

}
