package com.primesys.VehicalTracking.GoogleDirection;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.primesys.VehicalTracking.Dto.DirToatalInfo;
import com.primesys.VehicalTracking.Dto.Geo_DirDTO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DirectionsJSONParser {

	private Geo_DirDTO geo_dir;
	public  static DirToatalInfo dirinfo;
	/** Receives a JSONObject and returns a list of lists containing latitude and longitude */
	public List<List<HashMap<String,String>>> parse(JSONObject jObject){

		List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String,String>>>() ;
		JSONArray jRoutes = null;
		JSONArray jLegs = null;
		JSONArray jSteps = null;

		try {

			jRoutes = jObject.getJSONArray("routes");

			ArrayList<Geo_DirDTO> GeodirDataList=new ArrayList<>();
			Log.e("-------------------------jRoutes.length()jRoutes.length()----------",""+jRoutes.length());
			/** Traversing all routes */
			for(int i=0;i<jRoutes.length();i++){
				jLegs = ( (JSONObject)jRoutes.get(i)).getJSONArray("legs");
				List path = new ArrayList<HashMap<String, String>>();

				/** Traversing all legs */
				for(int j=0;j<jLegs.length();j++){
				JSONObject joleg = (JSONObject) jLegs.get(i);

					 dirinfo=new DirToatalInfo();
					dirinfo.setDistance(joleg.getJSONObject("distance").getString("text"));
					dirinfo.setDuration(joleg.getJSONObject("duration").getString("text"));
					dirinfo.setDuration_in_traffic(joleg.getJSONObject("duration_in_traffic").getString("text"));
					dirinfo.setEnd_address(joleg.getString("end_address"));
					dirinfo.setEnd_location(new LatLng(joleg.getJSONObject("end_location").getDouble("lat"),joleg.getJSONObject("end_location").getDouble("lng")));
					dirinfo.setStart_address(joleg.getString("start_address"));
					dirinfo.setStart_location(new LatLng(joleg.getJSONObject("start_location").getDouble("lat"),joleg.getJSONObject("end_location").getDouble("lng")));
					dirinfo.setOverview_polyline(joleg.getString("overview_polyline"));
					dirinfo.setSummary(joleg.getString("summary"));
					dirinfo.setVia_waypoint(joleg.getString("via_waypoint"));
					dirinfo.setWarnings(joleg.getString("warnings"));

					jSteps = ( (JSONObject)jLegs.get(j)).getJSONArray("steps");

					/** Traversing all steps */
					for(int k=0;k<jSteps.length();k++){

						geo_dir=new Geo_DirDTO();
						String html_instructions = ((JSONObject)jSteps.get(k)).getString("html_instructions");
						geo_dir.setHtml_instructions(html_instructions);

						String travel_mode = ((JSONObject)jSteps.get(k)).getString("travel_mode");
						geo_dir.setTravel_mode(travel_mode);

						//String maneuver = ((JSONObject)jSteps.get(k)).getString("maneuver");
						//geo_dir.setmaneuver(maneuver);


						String distance_text = ((JSONObject)jSteps.get(k)).getJSONObject("distance").getString("text");
						String distance_value = ((JSONObject)jSteps.get(k)).getJSONObject("distance").getString("value");
						geo_dir.setDistance_text(distance_text);
						geo_dir.setDistance_value(distance_value);


						String duration_text = ((JSONObject)jSteps.get(k)).getJSONObject("duration").getString("text");
						String duration_value = ((JSONObject)jSteps.get(k)).getJSONObject("duration").getString("value");
						geo_dir.setDuration_text(duration_text);
						geo_dir.setDuration_value(duration_value);


						String start_lat = ((JSONObject)jSteps.get(k)).getJSONObject("start_location").getString("lat");
						String start_lon = ((JSONObject)jSteps.get(k)).getJSONObject("start_location").getString("lng");						geo_dir.setHtml_instructions(html_instructions);
						geo_dir.setStart_lat(start_lat);
						geo_dir.setStart_lon(start_lon);



						String end_lat = ((JSONObject)jSteps.get(k)).getJSONObject("end_location").getString("lat");
						String end_lon = ((JSONObject)jSteps.get(k)).getJSONObject("end_location").getString("lng");
						geo_dir.setEnd_lat(end_lat);
						geo_dir.setEnd_lon(end_lon);


						String polyline = "";
						polyline = (String)((JSONObject)((JSONObject)jSteps.get(k)).get("polyline")).get("points");
						geo_dir.setPolyline(polyline);

						List<LatLng> list = decodePoly(polyline);
						geo_dir.setMylist(list);

						//Log.e("GET dir =======",new Gson().toJson(html_instructions));

						/** Traversing all points */
						for(int l=0;l<list.size();l++){
							HashMap<String, String> hm = new HashMap<String, String>();
							hm.put("lat", Double.toString(((LatLng)list.get(l)).latitude) );
							hm.put("lng", Double.toString(((LatLng)list.get(l)).longitude) );
							path.add(hm);
						}

						geo_dir.setPath(path);

						GeodirDataList.add(geo_dir);


					}
					dirinfo.setSteps(GeodirDataList);
					routes.add(path);


				}//leg end
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}catch (Exception e){
		}

		Log.e("GET dir All Data=======",new Gson().toJson(routes));
		return routes;
	}

	
	/**
	 * Method to decode polyline points 
	 * Courtesy : http://jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java 
	 * */
    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }
}
/*


	public List<List<HashMap<String,String>>> parse(JSONObject jObject){

		List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String,String>>>() ;
		JSONArray jRoutes = null;
		JSONArray jLegs = null;
		JSONArray jSteps = null;

		try {

		jRoutes = jObject.getJSONArray("routes");

		*/
/** Traversing all routes *//*

		for(int i=0;i<jRoutes.length();i++){
		jLegs = ( (JSONArray)jRoutes.get(i)).getJSONArray("legs");
		List path = new ArrayList<HashMap<String, String>>();

		*/
/** Traversing all legs *//*

		for(int j=0;j<jLegs.length();j++){
		jSteps = ( (JSONArray)jLegs.get(j)).getJSONArray("steps");

		*/
/** Traversing all steps *//*

		for(int k=0;k<jSteps.length();k++){

		String html_instructions = jSteps.get(k).getString("html_instructions");
		String travel_mode = jSteps.get(k).getString("travel_mode");
		String maneuver = jSteps.get(k).getString("maneuver");

		String distance_text = jSteps.get(k).getJSONObject("distance").getString("text");
		String distance_value = jSteps.get(k).getJSONObject("distance").getString("value");

		String duration_text = jSteps.get(k).getJSONObject("duration").getString("text");
		String duration_value = jSteps.get(k).getJSONObject("duration").getString("value");

		String start_lat = jSteps.get(k).getJSONObject("start_location").getString("lat");
		String start_lon = jSteps.get(k).getJSONObject("start_location").getString("lng");

		String end_lat = jSteps.get(k).getJSONObject("end_location").getString("lat");
		String end_lon = jSteps.get(k).getJSONObject("end_location").getString("lng");

		String polyline = "";
		polyline = (String)((JSONObject)((JSONObject)jSteps.get(k)).get("polyline")).get("points");
		List<LatLng> list = decodePoly(polyline);


		*/
/** Traversing all points *//*

		for(int l=0;l<list.size();l++){
		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put("lat", Double.toString(((LatLng)list.get(l)).latitude) );
		hm.put("lng", Double.toString(((LatLng)list.get(l)).longitude) );
		path.add(hm);
		}
		}
		routes.add(path);
		}
		}

		} catch (JSONException e) {
		e.printStackTrace();
		}catch (Exception e){
		}


		return routes;
		} */
