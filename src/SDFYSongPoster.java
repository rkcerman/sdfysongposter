
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.exceptions.JumblrException;
import com.tumblr.jumblr.types.VideoPost;

/**
 * The main class executing all the essential methods
 * @author Richard Kaliarik
 * @version 30-11-2015
 */

public class SDFYSongPoster {

    //TODO make it load from sdfy2.cfg
    private static final String BLOG_NAME = "somedopamineforyou.tumblr.com";

    /**
     * The main() method. Checks all the arguments and categorizes them. 
     * If the syntax is correct, it executes the mainApp() method.
     * @param args  
     * @throws JSONException
     * @throws Exception
     */
    
    public static void main(String[] args) throws JSONException, Exception {
        // TODO Auto-generated method stub
    	Song song = new Song();
    	
    	System.out.println("YouTube URL: ");
		Scanner input = new Scanner(System.in);
		URL youtubeURL = new URL(input.nextLine());
		song.setURL(youtubeURL);
		
		System.out.println("Leave empty to automatically fetch from YouTube");

		System.out.println("Artist: ");
//		Scanner inputArtist = new Scanner(System.in);
		String artist = input.nextLine();

		System.out.println("Song name: ");
//		Scanner inputSongName = new Scanner(System.in);
		String songName = input.nextLine();

//		inputArtist.close();
//		inputURL.close();
//		inputSongName.close();
		
		if(artist == null 	|| songName == null   ||
		   artist.isEmpty() || songName.isEmpty() ||
		   artist == ""		|| songName == ""){
			
			// If the Artist and Song were not specified, we obtain it from YouTube
			System.out.println("Fetching artist and song from YouTube.");
			String [] artistSong = fetchFromYoutube(youtubeURL);
			System.out.println("Fetched!");
			
			song.setArtist(artistSong[0]);
			song.setSongName(artistSong[1]);
		} else {
			song.setArtist(artist);
			song.setSongName(songName);
		}
		
		System.out.println("Artist: " + song.getArtist());
		System.out.println("Song: " + song.getSongName());
		
		mainApp(song);
	}	

    /**
     * The main core which executes tasks after initial argument check in the main() function
     * 
     * @param url - YouTube URL of the post
     * @param description - Additional description of the song, user-specified
     * @param artist - Artist name
     * @param song - Song name
     * @param year - Release year
     * @param label - Label it was released on
     * @param genre - Genre of the song
     * @param type - What type of entry on Discogs should be searched for: "all", "release" or "master"
     * @throws JSONException
     * @throws Exception
     */

    //TODO Too many params, lower to three
    //TODO Break the methods to multiple methods (15 lines MAX!)

    public static void mainApp(Song song) throws JSONException, Exception{
    	System.out.println("(M)aster/(R)elease/(A)ll?: ");
    	Scanner input = new Scanner(System.in);
    	String inputStringType = input.nextLine();
    	String type = typeReturn(inputStringType.toLowerCase());
    	
        ArrayList<ReleaseArray> releases = queryDiscogs(song, type); 

        releaseWriter(releases);
        
        System.out.println("I----===========================---I");
        System.out.println("Which release should I use? : ");

//        Scanner input = new Scanner(System.in);
        int num = input.nextInt();

        System.out.print("Using release number: " + num);
        ReleaseArray release = releases.get(num);

        //TODO below just a test value, DELETE!
        String description = "";

        String completeCaption = String.format(mockupTumblrPost(releases.get(num), song.getArtist(), song.getSongName(), description)); 
        // System.out.println(completeCaption);
        System.out.println("I----------==================================================================================---------I");
        System.out.println("Any tweet caption? : ");

        String genreHashtag = release.getGenres().get(0).toString().replace(" ", "");

        //TODO put this one to sdfy.cfg
        Scanner tweetCapScan = new Scanner(System.in);
        String tweetCaption = song.getArtist() + " - " + song.getSongName() + " -- " + tweetCapScan.nextLine() + " : " + song.getURL().toString() + " #" + genreHashtag;

        postToTumblr(song.getURL(), completeCaption, tweetCaption);

        tweetCapScan.close();
        input.close();
    }

    /**
     * 
     * @param url - the YouTube URL of the song
     * @return artistSong - a String array which holds two fields: 1. Artist name 2. Song name 
     * @throws IOException
     */
    public static String[] fetchFromYoutube(URL url) throws IOException{
    	//TODO better splits, check for existence of "-" and " - "
    	
        String[] artistSong = new String[2];

        Document ytPage = Jsoup.connect(url.toString()).get();

        String videoTitle = ytPage.getElementsByTag("meta")
            .first()
            .attr("content");

        artistSong[0]= videoTitle.substring(0, videoTitle.indexOf(" - "));
        artistSong[1]= videoTitle.substring(videoTitle.indexOf(" - ")+3);

        return artistSong;
    }

    /**
     * Posts a song to Tumblr with an appropriate description. Also, sends a tweet caption
     * if there is any.
     * 
     * @param url - the YouTube URL of the song
     * @param caption - everything that will be included in the Description field on Tumblr
     * @param tweetCaption - additional comment that will be posted next to the link on Twitter 
     * @throws IOException
     */
    public static void postToTumblr(URL url, String caption, String tweetCaption) throws IOException{
        // <iframe width="560" height="315" src="https://www.youtube.com/embed/Lds8BgDaS8g" frameborder="0" allowfullscreen></iframe>
        Document ytPage = Jsoup.connect(url.toString()).get();

        String embedURL = ytPage.select("meta[property=og:video:url]").attr("content");
        String embedCode = "<iframe width=\"560\" height=\"315\" src=\""+embedURL+"\" frameborder=\"0\" allowfullscreen></iframe>";
        // Put above to a separate method, maybe together with fetchFromYoutube 

        String mainCaption= caption.substring(0, caption.indexOf("[TAG"));
        String tags= caption.substring(caption.indexOf("[TAG")+4).replace("]", "").replace(": ", "");

        System.out.println();
        System.out.println("Embed code : " + embedCode);
        System.out.println("Main caption : ");
        System.out.println(mainCaption);
        System.out.println("Tags : " + tags);

        System.out.println("Post? Y/N/Q: ");

        Scanner input = new Scanner(System.in);
        String postCheck = input.nextLine();

        input.close();
        
        if(postCheck.equalsIgnoreCase("Y") || postCheck.equalsIgnoreCase("Q")){
        	//TODO to sdfy.cfg
            JumblrClient client = new JumblrClient(
                    "JrZBe7ncslnygHnDImJ9FOSwsQfTlPHHSZDixe8JfRPD1fsx1W",
                    "QXmUT2vGnsdMlyrpWMomItIhsADYUKtM6UTdG4q3V57jMzmImH"
                    );
            client.setToken(
                    "VmDBMzrk1QOrAzf3TsbGaeZ4H3muDIohB7Oo5C7fFYlBIdD9Dw",
                    "FyMTN9GueSy3rr9D9SA4NBjoIPCiL1lrG83vYfeQWcYsPD1Uxf"
                       );

            // URL for a Video post
            String postURL = "http://api.tumblr.com/v2/blog/somedopamineforyou.tumblr.com/post?type=video&caption="+mainCaption+
                "&embed="+embedCode+"&tweet=off%format=html&tags="+tags.replace(", ", "");
            System.out.println("I----------==================================================================================---------I");
            System.out.println(postURL);
            System.out.println();

            VideoPost videoPost = null;

            try {
                videoPost = client.newPost(BLOG_NAME, VideoPost.class);
                videoPost.setEmbedCode(embedCode);
                videoPost.setCaption(mainCaption);
                videoPost.setFormat("html");
                videoPost.setTweet(tweetCaption);
                videoPost.addTag(tags);
                if(postCheck.equalsIgnoreCase("Q"))
                    videoPost.setState("queue");
                videoPost.save();

                System.out.println();
                System.out.println("Video posted.");

            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block

                e.printStackTrace();
            } catch (InstantiationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JumblrException e) {
                e.printStackTrace();
                System.out.println("(" + e.getResponseCode() + ") " + e.getMessage());
            }


        } else if(postCheck.equalsIgnoreCase("N")){
            System.exit(0);
        } 
    }

    /**
     * Sends a query to Discogs to search for releases containing the artist name
     * and the song name. Returns a list of releases to choose from.
     * 
     * @param artist - the Artist name (specified in args, or obtained from YouTube)
     * @param song - the Song name (specified in args, or obtained from YouTube)
     * @param type - three possible: "all", "release" or "master"
     * @return releases - an ArrayList containing release name, label, year of release and genres
     * @throws JSONException
     * @throws Exception
     */
    public static ArrayList<ReleaseArray> queryDiscogs(Song song, String type) throws JSONException, Exception {

    	// TODO to sdfy.cfg
        final String discogsAuth = "CYCVIMlhnUCNvmrruDzNxdaJwSApQkfdhzCQFOuW";
        DiscogsClient client = new DiscogsClient();

        String discogsURL = ("https://api.discogs.com/database/search?q="+ song.getArtist() +"%20"+
        					song.getSongName() + "&type="+ type + "&token=" + discogsAuth).replace(" ", "%20");
        JSONObject mainDisc = new JSONObject(client.sendGet(discogsURL));
        JSONArray arrayDisc = new JSONArray();
        arrayDisc = mainDisc.getJSONArray("results");

        ArrayList<ReleaseArray> releases = ReleaseArray.fromJson(arrayDisc);

        return releases;
    }

    /**
     * Outputs into CLI a mockup of a Tumblr post, including all the 
     * necessary formatting retrieved from the config file.
     * 
     * @param release - Release used for the mockup  
     * @param artist - Artist name used for mocking up (exists in case the album artist in the discogs release name is e.g. 'Various Artists')
     * @param song - Song name used for mocking up
     * @param description - Specified description (if any, otherwise null or empty string "")
     * @return cfgText - String mockup of the post
     */
    public static String mockupTumblrPost(ReleaseArray release, String artist, String song, String description){

        System.out.println();

        StringBuffer dataBuffer = loadConfig();

        String cfgText = dataBuffer.toString().replace("[Artist]", "<b>" + artist + "</b>")
            .replace("[Song]", "<b>" + song + "</b>")
            .replace("[Release]", "<i>" + (release.getTitle().substring(release.getTitle().indexOf(" - ")+3)) + "</i>")
            .replace("[Year]", release.getYear())
            .replace("[Label]", (CharSequence) release.getLabel().get(0))
            .replace("[Caption]", description)
            .replace("#ArtistName", "#"+artist)
            .replace("#Year", "#"+release.getYear())
            .replace("#Label", "#"+(CharSequence) release.getLabel().get(0));

        //TODO This assumes that there are three genre tags in cfg file. Make it dynamic.
        if(release.getGenres().length() < 3) {
            for (int i=3; i>release.getGenres().length(); i--){
                cfgText = cfgText.replace("#Genre"+i+", ","");
            }
        }

        for(int i = 0; i < release.getGenres().length(); i++){
            int b = i+1;
            cfgText = cfgText.replace("#Genre"+b, "#"+(CharSequence) release.getGenres().get(i));
        }
        return cfgText;
    }

    /**
     * Outputs fetched releases from Discogs to CLI based on the query
     * @param releases
     */
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
    /**
     * Method for loading the config file which contains information about post format
     * @return dataBuffer - Contents of the sdfy.cfg file
     */
	
	public static String typeReturn(String inputChar){
		String type = null;
		switch (inputChar){
		case "m":
			type = "master";
		case "r":
			type = "release";
		case "a":
			type = "all";
		}
		return type;
	}
	
    public static StringBuffer loadConfig(){
        StringBuffer dataBuffer = new StringBuffer();
        try {
            System.out.println(System.lineSeparator() + "...Reading from File...");
            //System.out.println((new java.io.File(".").getCanonicalPath()));
            System.out.println();

            InputStream is = SDFYSongPoster.class.getClassLoader().getResourceAsStream("sdfy.cfg");
            InputStreamReader fis = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(fis);

            String newLine;
            while((newLine = br.readLine()) != null) {
                dataBuffer.append(newLine);                 
            }

            br.close();

            return dataBuffer;

        } catch (FileNotFoundException e) {
            System.out.println("sdfy.cfg not found.");
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }
        
    
}
