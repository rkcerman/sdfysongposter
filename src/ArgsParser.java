import java.util.ArrayList;
import java.util.Scanner;

/**
 * Accepts arguments URL, Description, Artist, Song, Year, Label, Genres, Type
 * @param args  
 * @throws JSONException
 * @throws Exception
 */
public class ArgsParser{

	public static void main(String[] args) {
		// TODO make each song a class

		System.out.println("YouTube URL: ");
		Scanner inputURL = new Scanner(System.in);
		String youtubeURL = inputURL.nextLine();

		System.out.println("Leave empty to automatically fetch from YouTube");

		System.out.println("Artist: ");
		Scanner inputArtist = new Scanner(System.in);
		String artist = inputArtist.nextLine();

		System.out.println("Song: ");
		Scanner inputSong = new Scanner(System.in);
		String song = inputSong.nextLine();

		if(artist == "" || song == ""){
			fetchFromYoutube(youtubeURL);
		}
	}	    

	public static void fetchFromYoutube(String a){
		
	}

	public static void releaseWriter(ArrayList<ReleaseArray> releases){
		for(ReleaseArray release : releases){
			int i = releases.indexOf(release);
			 if(release.getType().equals("master")){

	                System.out.println("O---------------MASTER---------------O");
	                System.out.println(i+ "   Release title: " + release.getTitle());
	                System.out.println("           Year: " + release.getYear());
	                System.out.print("         Labels: "); 
	                for(int b=0;b < release.getLabel().length();b++){
	                    if(b+1 != release.getLabel().length()) {
	                        System.out.print(release.getLabel().get(b) + ", ");
	                    } else {
	                        System.out.println(release.getLabel().get(b));
	                    }
	                }
	                System.out.print("         Genres: "); 
	                for(int b=0;b < release.getGenres().length();b++){
	                    if(b+1 != release.getGenres().length()) {
	                        System.out.print(release.getGenres().get(b) + ", ");
	                    } else {
	                        System.out.println(release.getGenres().get(b));

	                    }

	                }           
	                System.out.println("           Type: " + release.getType());

	            } else {

	                System.out.println("....................................");
	                System.out.println(i+ ".   Release title: " + release.getTitle());
	                System.out.println("           Year: " + release.getYear());
	                System.out.print("         Labels: "); 
	                for(int b=0;b < release.getLabel().length();b++){
	                    if(b+1 != release.getLabel().length()) {
	                        System.out.print(release.getLabel().get(b) + ", ");
	                    } else {
	                        System.out.println(release.getLabel().get(b));

	                    }

	                }
	                System.out.print("         Genres: "); 
	                for(int b=0;b < release.getGenres().length();b++){
	                    if(b+1 != release.getGenres().length()) {
	                        System.out.print(release.getGenres().get(b) + ", ");
	                    } else {
	                        System.out.println(release.getGenres().get(b));

	                    }

	                }
	                System.out.println("           Type: " + release.getType());

	            }
		}
	}
}