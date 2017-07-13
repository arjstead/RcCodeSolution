import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

// Solution to the RentalCars code test.
// Alexander Stead

public class Solution 
{
	public static void main (String[] args)
	{
		// Collection to hold all the vehicles
		ArrayList<Vehicle> vehicles;
		// Populate data structure
		vehicles = populateVehicles("http://www.rentalcars.com/js/vehicles.json");
		
		// Task solutions
		//printPriceAscending(vehicles);
		//printVehicleSpecs(vehicles);
		//printHighestRaterSupplierPerCarType(vehicles);
		scoreVehiclesAndPrintDescending(vehicles);
		
	}
	
	private static ArrayList<Vehicle> populateVehicles(String urlString)
	{
		// Collection to return
		ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
		
		try
		{
			// Connect to document
			URL url = new URL(urlString);
			HttpURLConnection request = (HttpURLConnection) url.openConnection();
		    request.connect();
	
		    // Parser to read the JSON
		    JsonParser parser = new JsonParser(); 
		    // Element to hold the returned JSON
		    JsonElement jelement = parser.parse(new InputStreamReader((InputStream) request.getContent())); 
		    // GSON Java JSON object to hold data
		    JsonObject jobject = jelement.getAsJsonObject();
		    jobject = jobject.getAsJsonObject("Search");
		    // Json array of vehicles
		    JsonArray jarray = jobject.getAsJsonArray("VehicleList");
		    
		    // Populate collection
			for(JsonElement jo:jarray)
			{
				// Get json object
				jobject = jo.getAsJsonObject();
				// Add to collection
				// public Vehicle(String sipp, String name, float price, String supplier, float rating)
				vehicles.add(new Vehicle(jobject.get("sipp").getAsString(), jobject.get("name").getAsString()
										, jobject.get("price").getAsFloat(), jobject.get("supplier").getAsString()
										, jobject.get("rating").getAsFloat()));
			}

		}
		catch(Exception e)
		{
			System.err.println("JSON download failed.");
			e.printStackTrace();
		}		
		
		return vehicles;
	}

	private static void printPriceAscending(ArrayList<Vehicle> vehicles)
	{
		// Sort collection and print
		Collections.sort(vehicles, Vehicle.priceAscSort);
		// Print headings
		System.out.printf("%-15s \t %s \n", "Name", "Price");
		// Print data
		for(Vehicle v:vehicles)
			System.out.printf("%-15s \t £%.2f \n", v.name, v.price);
	}

	private static void printVehicleSpecs(ArrayList<Vehicle> vehicles)
	{
		// Print headings
		System.out.printf("%-15s \t %-4s \t %-8s \t %-10s \t %-10s \t %-6s \t %s \n"
					, "Vehicle Name", "SIPP", "Car Type","Doors", "Transmission", "Fuel", "A/C");
		// Print data
		for(Vehicle v:vehicles)
		{
			System.out.printf("%-15s \t %-4s \t %-8s \t %-10s \t %-10s \t %-6s \t %s \n" 
					,v.name, new String(v.sipp), Vehicle.carTypeMap.get(v.sipp[0]) 
					,Vehicle.carDoorMap.get(v.sipp[1]), Vehicle.carTransmissionMap.get(v.sipp[2]) 
					,Vehicle.carFuelMap.get(v.sipp[3]), Vehicle.carAirconMap.get(v.sipp[3]));
		}
	}
	
	// xxx
	
	private static void scoreVehiclesAndPrintDescending(ArrayList<Vehicle> vehicles)
	{
		// Score each vehicle
		for(Vehicle v:vehicles)
		{
			v.vehicleScore = Vehicle.carTransmissionScoreMap.get(v.sipp[2]) + Vehicle.carAirconScoreMap.get(v.sipp[3]);
			v.totalScore = v.vehicleScore + v.rating;
		}
		
		// Sort by descending score
		Collections.sort(vehicles, Vehicle.scoreDescSort);
		
		// Print headings
		System.out.printf("%-15s \t %s \t %s  %s \n", "Name", "Score", "Rating", "Total");
		// Print data
		for(Vehicle v:vehicles)
			System.out.printf("%-15s \t %d \t %.1f \t %.1f \n", v.name, v.vehicleScore, v.rating, v.totalScore);

	}

}
