package me.mattyhd0.chatcolor.updatechecker;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class SpigotAPI {

    public static SpigotResource getSpigotResource(int id){

        StringBuilder response = new StringBuilder();

        try {

            URL urlObject = new URL("https://api.spigotmc.org/simple/0.2/index.php?action=getResource&id="+id);
            URLConnection urlConnection = urlObject.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;

            while ((line = bufferedReader.readLine()) != null) {

                response.append(line);

            }

            bufferedReader.close();

        } catch (IOException e){
            return null;
        }


        Gson gson = new Gson();
        return gson.fromJson(response.toString(), SpigotResource.class);

    }


}
