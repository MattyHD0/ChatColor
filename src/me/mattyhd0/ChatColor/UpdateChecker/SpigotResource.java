package me.mattyhd0.ChatColor.UpdateChecker;

public class SpigotResource {

    private int id;
    private String title;
    private String tag;
    private String current_version;
    private String native_minecraft_version;
    private String[] supported_minecraft_versions;
    private String icon_link;
    private Author author;
    private Premium premium;
    private Stats stats;
    private String description;

    private static class Author{

        private int id;
        private String username;

        public int getId(){
            return id;
        }

        public String getUsername(){
            return username;
        }

    }

    private static class Premium{

        private String price;
        private String currency;

        public String getPrice(){
            return price;
        }

        public String getCurrency(){
            return currency;
        }

    }

    private static class Stats{

        private int downloads;
        private int updates;
        private Reviews reviews;
        private int rating;

        public int getDownloads() {
            return downloads;
        }

        public int getUpdates() {
            return updates;
        }

        public Reviews getReviews() {
            return reviews;
        }

        public int getRating() {
            return rating;
        }

        private static class Reviews{

            private int unique;
            private int total;

            public int getUnique() {
                return unique;
            }

            public int getTotal() {
                return total;
            }
        }

    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getTag() {
        return tag;
    }

    public String getCurrentVersion() {
        return current_version;
    }

    public String getNativeMinecraftVersion() {
        return native_minecraft_version;
    }

    public String[] getSupportedMinecraftVersions() {
        return supported_minecraft_versions;
    }

    public String getIconLink() {
        return icon_link;
    }

    public Author getAuthor() { return author; }

    //public int getAuthorId() { return author.id; }

    //public String getAuthorUsername() { return author.username; }

    public boolean isPremium(){
        return !(premium.price.equals("0.00") && premium.currency.equals(""));
    }

    //public Premium getPremium() { return premium; }

    public String getPrice(){
        return premium.price;
    }

    public String getPriceCurrency(){
        return premium.currency;
    }

    public Stats getStats() {
        return stats;
    }

    public String getDescription() {
        return description;
    }

    public String getDownloadUrl(){

        String url = ("https://www.spigotmc.org/resources/"+title+"."+id+"/").replaceAll(" ", "-");
        String validChars = "abcdefghijklmnñopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789:/.-_";
        String validUrl = "";

        for(int i = 0; i < url.length(); i++){

            String c = Character.toString(url.charAt(i));

            if(validChars.contains(c)){
                validUrl = validUrl+c;
            }

        }

        return validUrl;
    }

}
