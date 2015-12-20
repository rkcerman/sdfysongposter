import java.net.URL;
import java.util.ArrayList;

public class Song {

   private String artist;
   private String songName;
   private String album;
   private String year;
   private String description;
   private URL url;
   private ArrayList<String> genres;
   private ArrayList<String> labels;
   
   public Song(){
	   
   }
   
   public String getArtist(){
	   return artist;
   }
   
   public String getSongName(){
	   return songName;
   }
   
   public String getAlbum(){
	   return album;
   }
   
   public String getYear(){
	   return year;
   }
   
   public String getDescription(){
	   return description;
   }
   
   public ArrayList<String> getGenres(){
	   return genres;
   }
   
   public ArrayList<String> getLabels(){
	   return labels;
   }
   
   public URL getURL(){
	   return url;
   }
   
   public void setArtist(String artist){
	   this.artist = artist;
   }
   
   public void setSongName(String songName){
	   this.songName = songName;
   }
   
   public void setAlbum(String album){
	   this.album = album;
   }
   
   public void setYear(String year){
	   this.year = year;  
   }
   
   public void setDescription(String description){
	   this.description = description;
   }
   
   public void setGenres(ArrayList<String> genres){
	   this.genres = genres;
   }
   
   public void setLabels(ArrayList<String> labels){
	   this.labels = labels;
   }
   
   public void setURL(URL url){
	   this.url = url;
   }
}
