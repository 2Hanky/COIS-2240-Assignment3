import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class RentalSystem {
    private List<Vehicle> vehicles = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private RentalHistory rentalHistory = new RentalHistory();
    private static RentalSystem instance;
    
    private RentalSystem() {
    	loadData();
    }
    
    public static RentalSystem getInstance() {
    	if (instance == null) {
    		instance = new RentalSystem();
    	}
    	return instance;
    }
    
    
    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
        saveVehicle(vehicle);
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
        saveCustomer(customer);
    }

    public void rentVehicle(Vehicle vehicle, Customer customer, LocalDate date, double amount) {
        if (vehicle.getStatus() == Vehicle.VehicleStatus.AVAILABLE) {
            vehicle.setStatus(Vehicle.VehicleStatus.RENTED);
            RentalRecord record = new RentalRecord(vehicle, customer, date, amount, "RENT");
            rentalHistory.addRecord(record);
            saveRecord(record);
            System.out.println("Vehicle rented to " + customer.getCustomerName());
        }
        else {
            System.out.println("Vehicle is not available for renting.");
        }
    }

    public void returnVehicle(Vehicle vehicle, Customer customer, LocalDate date, double extraFees) {
        if (vehicle.getStatus() == Vehicle.VehicleStatus.RENTED) {
            vehicle.setStatus(Vehicle.VehicleStatus.AVAILABLE);
            RentalRecord record = new RentalRecord(vehicle, customer, date, extraFees, "RETURN");
            rentalHistory.addRecord(record);
            saveRecord(record);
            System.out.println("Vehicle returned by " + customer.getCustomerName());
        }
        else {
            System.out.println("Vehicle is not rented.");
        }
    }    

    public void displayVehicles(boolean onlyAvailable) {
    	System.out.println("|     Type         |\tPlate\t|\tMake\t|\tModel\t|\tYear\t|");
    	System.out.println("---------------------------------------------------------------------------------");
    	 
        for (Vehicle v : vehicles) {
            if (!onlyAvailable || v.getStatus() == Vehicle.VehicleStatus.AVAILABLE) {
                System.out.println("|     " + (v instanceof Car ? "Car          " : "Motorcycle   ") + "|\t" + v.getLicensePlate() + "\t|\t" + v.getMake() + "\t|\t" + v.getModel() + "\t|\t" + v.getYear() + "\t|\t");
            }
        }
        System.out.println();
    }
    
    public void displayAllCustomers() {
        for (Customer c : customers) {
            System.out.println("  " + c.toString());
        }
    }
    
    public void displayRentalHistory() {
        for (RentalRecord record : rentalHistory.getRentalHistory()) {
            System.out.println(record.toString());
        }
    }
    
    public Vehicle findVehicleByPlate(String plate) {
        for (Vehicle v : vehicles) {
            if (v.getLicensePlate().equalsIgnoreCase(plate)) {
                return v;
            }
        }
        return null;
    }
    
    public Customer findCustomerById(String id) {
        for (Customer c : customers)
            if (c.getCustomerId() == Integer.parseInt(id))
                return c;
        return null;
    }
    
    public Customer findCustomerByName(String name) {
        for (Customer customer : customers) {
            if (customer.getCustomerName().equalsIgnoreCase(name)) {
                return customer;
            }
        }
        return null;
    }
    
    private void saveVehicle(Vehicle vehicle) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("vehicles.txt", true))) {
            String type = (vehicle instanceof Car) ? "Car" : "Motorcycle";
            writer.write(type + "," + vehicle.getInfo());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void saveCustomer(Customer customer) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("customers.txt", true))) {
            writer.write(customer.getCustomerId() + "," + customer.getCustomerName());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void saveRecord(RentalRecord record) {
        try (FileWriter writer = new FileWriter("rental_records.txt", true)) {
            writer.write(record.toString() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void loadData() {
        // Load Vehicles
    	try (BufferedReader vehicleReader = new BufferedReader(new FileReader("vehicles.txt"))) {
    	    String line;
    	    while ((line = vehicleReader.readLine()) != null) {
    	        line = line.trim();
    	        if (line.isEmpty()) continue;

    	        String[] mainParts = line.split(",", 2); // Split into type and rest
    	        if (mainParts.length < 2) continue;

    	        String type = mainParts[0].trim();
    	        String rest = mainParts[1].trim();

    	        // Now split by "|"
    	        String[] details = rest.split("\\|");
    	        if (details.length < 5) continue; // Minimum fields

    	        String licensePlate = details[1].trim();
    	        String make = details[2].trim();
    	        String model = details[3].trim();
    	        int year = Integer.parseInt(details[4].trim());
    	        // String status = details[5].trim(); // not using for now

    	        // Optional extra info
    	        int seats = 4; // default
    	        boolean hasSidecar = false; // default

    	        if (type.equals("Car")) {
    	            if (details.length > 7) {
    	                String seatsInfo = details[7].trim(); // "Seats: 4"
    	                seats = Integer.parseInt(seatsInfo.replace("Seats: ", ""));
    	            }
    	            Car car = new Car(make, model, year, seats);
    	            car.setLicensePlate(licensePlate);
    	            vehicles.add(car);
    	        } else if (type.equals("Motorcycle")) {
    	            if (details.length > 7) {
    	                String sidecarInfo = details[7].trim(); // "Sidecar: Yes"
    	                hasSidecar = sidecarInfo.equalsIgnoreCase("Sidecar: Yes");
    	            }
    	            Motorcycle moto = new Motorcycle(make, model, year, hasSidecar);
    	            moto.setLicensePlate(licensePlate);
    	            vehicles.add(moto);
    	        }
    	        // You can add Truck support later if needed
    	    }
    	} catch (IOException e) {
    	    e.printStackTrace();
    	}

        // Load Customers
        try (BufferedReader customerReader = new BufferedReader(new FileReader("customers.txt"))) {
            String line;
            while ((line = customerReader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length < 2) {
                    continue;
                }

                int id = Integer.parseInt(parts[0]);
                String name = parts[1];

                customers.add(new Customer(id, name));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Load Rental Records
        try (BufferedReader reader = new BufferedReader(new FileReader("rental_records.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" \\| ");
                if (parts.length == 5) {
                    String recordType = parts[0].trim();
                    String plate = parts[1].replace("Plate: ", "").trim();
                    String customerName = parts[2].replace("Customer: ", "").trim();
                    LocalDate date = LocalDate.parse(parts[3].replace("Date: ", "").trim());
                    double amount = Double.parseDouble(parts[4].replace("Amount: $", "").trim());

                    Vehicle vehicle = findVehicleByPlate(plate);
                    Customer customer = findCustomerByName(customerName); // <-- Find by name, not ID

                    if (customer != null && vehicle != null) {
                        RentalRecord record = new RentalRecord(vehicle, customer, date, amount, recordType);
                        rentalHistory.addRecord(record);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}