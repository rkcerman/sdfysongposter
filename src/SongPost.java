import java.util.ArrayList;

public class SongPost {

   private String artist;
   private String song;
   private String album;
   private String year;
   private String description;
   private ArrayList<String> genres;
   private ArrayList<String> labels;
   
   public SongPost(){
	   
   }
   
   public String getArtist(){
	   return artist;
   }
   
   public String getSong(){
	   return song;
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
}
