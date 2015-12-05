    /**
     * Accepts arguments URL, Description, Artist, Song, Year, Label, Genres, Type
     * @param args  
     * @throws JSONException
     * @throws Exception
     */
    public static void main(String[] args) throws JSONException, Exception {
        // TODO make each song a class
	
	System.out.println("YouTube URL: ");
	Scanner inputURL = new Scanner(System.in);
	String youtubeURL = input.nextLine();

	System.out.println("Leave empty to automatically fetch from YouTube");
	
	System.out.println("Artist: ");
	Scanner inputArtist = new Scanner(System.in);
	String artist = input.nextLine();

	System.out.println("Song: ");
	Scanner inputSong = new Scanner(System.in);
	String song = input.nextLine();

	if(artist == "" || song == ""){
	    fetchFromYoutube(youtubeURL);
	}
    }	    
