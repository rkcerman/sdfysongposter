import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ReleaseArray {

	public String year;
	public JSONArray genres;
	public JSONArray label;
	public String album;
	public String type;


	public String getYear() {
		return year; 
	}
	
	public JSONArray getGenres() {
		return genres; 
	}
	
	public JSONArray getLabel() {
		return label; 
	}
	
	public String getTitle() {
		return album; 
	}
	
	public String getType() {
		return type;
	}
	
	public static ReleaseArray fromJson(JSONObject jsonobject) {
		
		
		ReleaseArray e = new ReleaseArray();
		
		try {
		e.year = jsonobject.getString("year");
		} catch (JSONException je) {
			e.year = "";
					}
		try {
			e.label = jsonobject.getJSONArray("label");
			} catch (JSONException je) {
				e.label = null;
						}		
		try {

			e.genres = jsonobject.getJSONArray("style");
			} catch (JSONException je) {
				e.genres = null;
						}	
		try {
				
			e.album = jsonobject.getString("title");	
			e.type = jsonobject.getString("type");
			
			} catch (JSONException je) {
			je.printStackTrace();
				//e.year = "N/A";
			return null;
			}
		return e;
	}
	
	public static ArrayList<ReleaseArray> fromJson(JSONArray jsonarray){
		ArrayList<ReleaseArray> objects = new ArrayList<ReleaseArray>(jsonarray.length());		
		
		for(int i=0; i < jsonarray.length(); i++){
			JSONObject jsonobject = null;
			
			try {
				jsonobject = jsonarray.getJSONObject(i);
				
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			
			ReleaseArray object = ReleaseArray.fromJson(jsonobject);
			
			if (object != null){
				objects.add(object);
			}
			
		}
		
		return objects;
	}
}
