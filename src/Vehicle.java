import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

public class Vehicle 
{
	// Variables are public as no need to control access.
	public char[] sipp;
	public String name;
	public float price;
	public String supplier;
	public float rating;
	public int vehicleScore;
	public float totalScore;
	
	// Comparators
    public static Comparator<Vehicle> priceAscSort = new Comparator<Vehicle>() {
        public int compare(Vehicle u1, Vehicle u2) {
            if(u2.price > u1.price)
            	return -1;
            else if (u2.price == u1.price)
            	return 0;
            else
            	return 1;
          }
        };
    public static Comparator<Vehicle> scoreDescSort = new Comparator<Vehicle>() {
        public int compare(Vehicle u1, Vehicle u2) {
            if(u2.totalScore > u1.totalScore)
            	return 1;
            else if (u2.totalScore == u1.totalScore)
            	return 0;
            else
            	return -1;
          }
        };
    public static Comparator<Vehicle> typeLexSort = new Comparator<Vehicle>() {
        public int compare(Vehicle u1, Vehicle u2) {
            return (new Character(u1.sipp[0]).compareTo(new Character(u2.sipp[0])));
          }
        };
	
	
	// Constructor
	public Vehicle(String sipp, String name, float price, String supplier, float rating)
	{
		// As input is from a specified JSON, and therefore assumed correct,
		// no validation is done on these values eg. negative price.
		
		this.sipp = sipp.toCharArray();
		this.name = name;
		this.price = price;
		this.supplier = supplier;
		this.rating = rating;
	}
		
	public String toString()
	{
		return ""+sipp[0] + ""+sipp[1] + ""+sipp[2] + ""+sipp[3] + "\t" + name + "\t" + price + "\t" + supplier + "\t" + rating;
	}
	
	// Sipp maps. Map.get(key) returns the value so carTypeMap.get('M') returns "Mini"
	public static final Map<Character, String> carTypeMap = new HashMap<Character, String>(){
        {
            put('M', "Mini");
            put('E', "Economy");
            put('C', "Compact");
            put('I', "Intermediate");
            put('S', "Standard");
            put('F', "Full Size");
            put('P', "Premium");
            put('L', "Luxury");
            put('X', "Special");
        }
    };
    public static final Map<Character, String> carDoorMap = new HashMap<Character, String>(){
        {
            put('B', "2 doors");
            put('C', "4 doors");
            put('D', "5 doors");
            put('W', "Estate");
            put('T', "Convertible");
            put('F', "SUV");
            put('P', "Pick Up");
            put('V', "Passenger Van");
        }
    };
    public static final Map<Character, String> carTransmissionMap = new HashMap<Character, String>(){
        {
            put('M', "Manual");
            put('A', "Automatic");
        }
    };
    public static final Map<Character, String> carFuelMap = new HashMap<Character, String>(){
        {
            put('N', "Petrol");
            put('R', "Petrol");
        }
    };
    public static final Map<Character, String> carAirconMap = new HashMap<Character, String>(){
        {
            put('N', "NO");
            put('R', "YES");
        }
    };
    
    // Score maps
    public static final Map<Character, Integer> carTransmissionScoreMap = new HashMap<Character, Integer>(){
        {
            put('M', 1);
            put('A', 5);
        }
    };
    public static final Map<Character, Integer> carAirconScoreMap = new HashMap<Character, Integer>(){
        {
            put('N', 0);
            put('R', 2);
        }
    };
    
	public static void printFields(int[] fields, ArrayList<Vehicle> vehicles, boolean jsonOutput)
	{
		// Extended to accommodate a JSon output as a RESTful interface
		if(jsonOutput)
		{
			System.out.println(toJson(vehicles));
			return;
		}
		
		// Continue for textual output
		
		// Define fields
		// Fields:
		// 0 - sipp
		// 1 - name
		// 2 - price
		// 3 - supplier
		// 4 - rating
		// 5 - vehicle score
		// 6 - total score
		// 7 - type
		// 8 - doors
		// 9 - transmission
		// 10 - fuel
		// 11 - aircon
		
		// Print the headings
		for(int i:fields)
		{
			switch(i)
			{
				case 0: System.out.printf("%s\t", "SIPP"); break;
				case 1: System.out.printf("%-18s\t", "Name"); break;
				case 2: System.out.printf("%s\t", "Price"); break;
				case 3: System.out.printf("%-10s\t", "Supplier"); break;
				case 4: System.out.printf("%s\t", "Rating"); break;
				case 5: System.out.printf("%s\t", "Spec Score"); break;
				case 6: System.out.printf("%s\t", "Total Score"); break;
				case 7: System.out.printf("%-15s\t", "Vehicle Type"); break;
				case 8: System.out.printf("%-10s\t", "Doors"); break;
				case 9: System.out.printf("%-10s\t", "Transmission"); break;
				case 10: System.out.printf("%s\t", "Fuel"); break;
				case 11: System.out.printf("%s\t", "A/C"); break;
			}
		}
		System.out.print("\n");
		
		// print the fields
		for(Vehicle v:vehicles)
		{
			for(int i:fields)
			{
				switch(i)
				{
					case 0: System.out.printf("%s%s%s%s\t", v.sipp[0], v.sipp[1], v.sipp[2], v.sipp[3]); break;
					case 1: System.out.printf("%-18s\t", v.name); break;
					case 2: System.out.printf("£%.2f\t", v.price); break;
					case 3: System.out.printf("%-10s\t", v.supplier); break;
					case 4: System.out.printf("%.1f\t", v.rating); break;
					case 5: System.out.printf("%d\t\t", v.vehicleScore); break;
					case 6: System.out.printf("%.1f\t", v.totalScore); break;
					case 7: System.out.printf("%-15s\t", Vehicle.carTypeMap.get(v.sipp[0])); break;
					case 8: System.out.printf("%-10s\t", Vehicle.carDoorMap.get(v.sipp[1])); break;
					case 9: System.out.printf("%-10s\t", Vehicle.carTransmissionMap.get(v.sipp[2])); break;
					case 10: System.out.printf("%s\t", Vehicle.carFuelMap.get(v.sipp[3])); break;
					case 11: System.out.printf("%s\t", Vehicle.carAirconMap.get(v.sipp[3])); break;
				}
			}
			System.out.print("\n");
		}	
		// End the line
		System.out.print("\n");
	}
	
	public static String toJson (ArrayList<Vehicle> vehicles)
	{
		// Use gson auto convert
		Gson gson = new Gson();
		String result = gson.toJson(vehicles);
		// Wrap into a json array of vehicles called query result
		return "{\"Query Result\":" + result + "}";
	}
}
