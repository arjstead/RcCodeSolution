import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

// Solution to the RentalCars code test.
// Alexander Stead

public class Solution 
{
	// Class variable, passing such a common parameter by value would be inefficient.
	static ArrayList<Vehicle> vehicles;
	
	public static void main (String[] args)
	{
		// Populate data structure
		vehicles = populateVehicles("http://www.rentalcars.com/js/vehicles.json");
		// Welcome message
		System.out.println("Welcome...");
		// Get task from user until they quit.
		menuLoop(vehicles);
		// Obvious attempt to win over the asseser
		System.out.println("Thank you and goodbye!");
	}
	
	// User interface
	private static void menuLoop(ArrayList<Vehicle> vehicles)
	{
		String input;
		boolean continueLoop = true;
		boolean displayMenu = true;
		Scanner s = new Scanner(System.in);
		String menuText = "\n\nPlease select an option from the menu below to view the output for each task. Please enter a number:\n 1. Print the list of vehicles by ascending order of price. \n 2. Print the specifications of each vehicle based on SIPP. \n 3. Print the highest rated supplier of each type of vehicle. \n 4. Print vehicles in descending order of score. \n 5. Toggle between textual and JSON output. \n 6. Quit.";
		String formatString;
		boolean jsonOutput = false;
		
		// Loop until user selects 'q' to quit
		while(continueLoop)
		{
			// Wait for output to be read.
			System.out.println("Press enter to continue");
			input = s.nextLine();
			
			formatString = (jsonOutput) ? "JSON" : "Textual";
			// Does the menu need printing again?
			if(displayMenu){ System.out.println(menuText); System.out.println("Output format: " + formatString); }
			else displayMenu = true;
			
			// Read the user input.
			input = s.nextLine();
			
			// Decide action to take
			// printFields' first parameter is a list whose elements are the index of a vehicle variable and so specifies
			// which fields to print and in what order across the screen.
			switch(input)
			{
				case "1": 
					Vehicle.printFields(new int[] {1,2}, getPriceAscending(vehicles), jsonOutput); 
					break;
				case "2": 
					Vehicle.printFields(new int[] {1, 0, 7, 8, 9, 10, 11}, vehicles, jsonOutput);
					break;
				case "3": 
					Vehicle.printFields(new int[] {1, 7, 3, 4}, getHighestRatedSupplierPerCarType(vehicles), jsonOutput); 
					break;
				case "4": 
					Vehicle.printFields(new int[] {1, 4, 5, 6}, scoreVehiclesAndGetDescending(vehicles), jsonOutput); 
					break;
				case "5": 
					jsonOutput = !jsonOutput; 
					break;
				case "6":
					continueLoop = false;
					break;
				default: 
					System.out.printf("\'%s\' is an invalid input.\n", input); displayMenu = false;
			}
		}
		// Loop quit - return to main.
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
			System.exit(0);
		}		
		
		return vehicles;
	}

	private static ArrayList<Vehicle> getPriceAscending(ArrayList<Vehicle> vehicles)
	{
		// Sort collection and print
		Collections.sort(vehicles, Vehicle.priceAscSort);
		
		return vehicles;
	}
	
	private static ArrayList<Vehicle> getHighestRatedSupplierPerCarType(ArrayList<Vehicle> vehicles)
	{		
		// Holds a mapping of Type -> highest rating -> supplier, by storing a list of vehicles with only
		// relevant fields filled.
		ArrayList<Vehicle> map = new ArrayList<Vehicle>();
		
		// find the highest rated per type
		int index;
		for(Vehicle v:vehicles)
		{
			// Find the vehicle type in the map
			index = Collections.binarySearch(map, v, Vehicle.typeLexSort);
			// Is it already there? no - add it, yes - is the rating higher than the current highest? yes - update leader.
			if(index < 0)
				map.add(Math.abs(index)-1, v);
			else if(map.get(index).rating < v.rating)
				map.set(index, v);
		}
		
		// Add any joint leaders
		for(Vehicle v:vehicles)
		{
			// Find the vehicle type in the map
			index = Collections.binarySearch(map, v, Vehicle.typeLexSort);
			// If another supplier has a top rating, add it to the list 
			if((v.rating == map.get(index).rating) && (!map.get(index).supplier.equals(v.supplier)))
				map.add(index, v);
		}
		
		// Sort
		Collections.sort(vehicles, Vehicle.typeLexSort);
		return map;
	}
	
	private static ArrayList<Vehicle> scoreVehiclesAndGetDescending(ArrayList<Vehicle> vehicles)
	{
		// Score each vehicle
		for(Vehicle v:vehicles)
		{
			v.vehicleScore = Vehicle.carTransmissionScoreMap.get(v.sipp[2]) + Vehicle.carAirconScoreMap.get(v.sipp[3]);
			v.totalScore = v.vehicleScore + v.rating;
		}
		
		// Sort by descending score
		Collections.sort(vehicles, Vehicle.scoreDescSort);
		
		return vehicles;
	}
}
