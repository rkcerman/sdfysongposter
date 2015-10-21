import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
import org.jsoup.nodes.Element;

import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.exceptions.JumblrException;
import com.tumblr.jumblr.types.QuotePost;
import com.tumblr.jumblr.types.Video;
import com.tumblr.jumblr.types.VideoPost;


public class SDFYSongPoster {

	private static final String BLOG_NAME = "somedopamineforyou.tumblr.com";
		
	public static void main(String[] args) throws JSONException, Exception {
		// TODO Auto-generated method stub

		
		String url = null;
		String description = "";
		String artist = null;
		String song = null;
		String year = null;
		String label = null;
		String genre = null;
		String type = null;
				
		if(args.length >0){
			if(args[0].equals("--help")){
				System.out.println("Usage: SDFYSongPoster.jar <OPTION> <URL> [description] [artist] [song] [year] [label] [genre]");
				System.out.println("Options:");
				System.out.println("--help - Displays help");
				System.out.println("--all - Search for all items");
				System.out.println("--master - Search for master");
				System.out.println("--release - Search for releases");
			} else if (args[0].equals("--all")){
				type = "all";
			} else if (args[0].equals("--master")){
				type = "master";
			} else if (args[0].equals("--release")){
				type = "release";
			}
			
			if(args.length > 1){
				url = args[1];
				if(args.length > 2){
					description = args[2];
					if(args.length > 3) {
					   artist = args[3];
					   if(args.length > 4) {
						   song = args[4];
						   if(args.length > 5) {
							   year = args[5];
							   if(args.length > 6) {
								   label = args[6];
								   if(args.length > 7) {
									   genre = args[7];
								   }
							   }
						   }
					   }
					}
				}
			}
		} else {
			System.out.println("Arguments necessary. Type --help for help.");
		}
		System.out.println("YouTube URL (type --help for help) : " + url);
		System.out.println("Description : " + description);
		System.out.println("Artist : " + artist);
		System.out.println("Song : " + song);
		System.out.println("Year : " + year);
		System.out.println("Label : " + label);
		System.out.println("Genre : " + genre);

		System.out.println("----------------------");
		System.out.println("Loading video title...");
		
		String[] videoName = getArtistSong(url);
		if(artist == null){
		artist = videoName[0];
		}
		if(song == null)
			song = videoName[1];
		
		System.out.println("Video Name : " + artist + " - "+ song);
		System.out.println("Artist (from YouTube) : " + artist);
		System.out.println("Song (from YouTube) : " + song);
		
		ArrayList<ReleaseArray> releases = queryDiscogs(artist, song, type); 
		ReleaseArray release = null;
		
		for(int i=0; i < releases.size(); i++){
			release = releases.get(i);
			if(release.getType().equals("master")){
			System.out.println("OooooooooooooooMASTERooooooooooooooO");
			System.out.println(i+ "   Release title: " + release.getTitle());
			System.out.println("               Year: " + release.getYear());
			System.out.print("             Labels: "); 
					for(int b=0;b < release.getLabel().length();b++){
						if(b+1 != release.getLabel().length()) {
						System.out.print(release.getLabel().get(b) + ", ");
						} else {
							System.out.println(release.getLabel().get(b));
							
						}
							
					}
			System.out.print("             Genres: "); 
					for(int b=0;b < release.getGenres().length();b++){
					if(b+1 != release.getGenres().length()) {
						System.out.print(release.getGenres().get(b) + ", ");
						} else {
							System.out.println(release.getGenres().get(b));
							
						}
							
					}			
			System.out.println("               Type: " + release.getType());
		} else {
			System.out.println("....................................");
			System.out.println(i+ ".   Release title: " + release.getTitle());
			System.out.println("               Year: " + release.getYear());
			System.out.print("             Labels: "); 
			for(int b=0;b < release.getLabel().length();b++){
				if(b+1 != release.getLabel().length()) {
				System.out.print(release.getLabel().get(b) + ", ");
				} else {
					System.out.println(release.getLabel().get(b));
					
				}
					
			}
			System.out.print("             Genres: "); 
			for(int b=0;b < release.getGenres().length();b++){
				if(b+1 != release.getGenres().length()) {
				System.out.print(release.getGenres().get(b) + ", ");
				} else {
					System.out.println(release.getGenres().get(b));
					
				}
					
			}
			System.out.println("               Type: " + release.getType());
			}
		}
		
		System.out.println("I----===========================---I");
		System.out.println("Which release should I use? : ");
		
		Scanner input = new Scanner(System.in);
		int num = input.nextInt();
						
		System.out.print("Using release number: " + num);
		
		String completeCaption = String.format(mockupTumblrPost(releases, artist, song, description, num)); 
		// System.out.println(completeCaption);
		System.out.println("I----------==================================================================================---------I");
		
		
		postToTumblr(url, completeCaption);
				
		
	}
	
	public static String[] getArtistSong(String url) throws IOException{
		String[] artistSong = new String[2];
		
		Document ytPage = Jsoup.connect(url).get();
		
		String videoTitle = ytPage.getElementsByTag("meta")
				.first()
				.attr("content");
		
		
		
		artistSong[0]= videoTitle.substring(0, videoTitle.indexOf(" - "));
		artistSong[1]= videoTitle.substring(videoTitle.indexOf(" - ")+3);
		
		return artistSong;
	}
	
	public static void postToTumblr(String url, String caption) throws IOException{
		// <iframe width="560" height="315" src="https://www.youtube.com/embed/Lds8BgDaS8g" frameborder="0" allowfullscreen></iframe>
		Document ytPage = Jsoup.connect(url).get();
		
		String embedURL = ytPage.select("meta[property=og:video:url]").attr("content");
		String embedCode = "<iframe width=\"560\" height=\"315\" src=\""+embedURL+"\" frameborder=\"0\" allowfullscreen></iframe>";
		
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
		
		if(postCheck.equalsIgnoreCase("Y") || postCheck.equalsIgnoreCase("Q")){
		
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
					videoPost.addTag(tags);
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
		} else if(postCheck.equalsIgnoreCase("Q")){
			
		}
	}
	
	public static ArrayList<ReleaseArray> queryDiscogs(String artist, String song, String type) throws JSONException, Exception {
	
		final String discogsAuth = "CYCVIMlhnUCNvmrruDzNxdaJwSApQkfdhzCQFOuW";
		DiscogsClient client = new DiscogsClient();
		
		String discogsURL = ("https://api.discogs.com/database/search?q="+artist+"%20"+song+"&type="+type+"&token="+discogsAuth).replace(" ", "%20");
		JSONObject mainDisc = new JSONObject(client.sendGet(discogsURL));
		JSONArray arrayDisc = new JSONArray();
		arrayDisc = mainDisc.getJSONArray("results");
		
		ArrayList<ReleaseArray> releases = ReleaseArray.fromJson(arrayDisc);
		
		return releases;
	}
	
	public static String mockupTumblrPost(ArrayList<ReleaseArray> releases, String artist, String song, String description, int rNum){
		ReleaseArray release = releases.get(rNum);
		
		System.out.println();
		//System.out.println("Mockup of the post: ");
		
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
