import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

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
            return (int) (u1.price - u2.price);
          }
        };
    public static Comparator<Vehicle> scoreDescSort = new Comparator<Vehicle>() {
        public int compare(Vehicle u1, Vehicle u2) {
            return (int) (u2.totalScore - u1.totalScore);
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
		return sipp.toString() + "\t" + name + "\t" + price + "\t" + supplier + "\t" + rating;
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
	
}
