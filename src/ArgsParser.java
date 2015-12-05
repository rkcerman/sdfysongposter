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

}