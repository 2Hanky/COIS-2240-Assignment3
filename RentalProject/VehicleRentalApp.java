import java.util.Scanner;
import java.time.LocalDate;

public class VehicleRentalApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        RentalSystem rentalSystem = new RentalSystem();

        while (true) {
        	System.out.println("\n1: Add Vehicle\n2: Add Customer\n3: Rent Vehicle\n4: Return Vehicle\n5: Display Available Vehicles\n6: Show Rental History\n7: Exit");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("  1: Car\n  2: Motorcycle\n  3: Truck");
                    int type = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Enter license plate: ");
                    String plate = scanner.nextLine();
                    System.out.print("Enter make: ");
                    String make = scanner.nextLine();
                    System.out.print("Enter model: ");
                    String model = scanner.nextLine();
                    System.out.print("Enter year: ");
                    int year = scanner.nextInt();
                    scanner.nextLine();

                    Vehicle vehicle;
                    if (type == 1) {
                        System.out.print("Enter number of seats: ");
                        int seats = scanner.nextInt();
                        vehicle = new Car(make, model, year, seats);
                    } else if (type == 2) {
                        System.out.print("Has sidecar? (true/false): ");
                        boolean sidecar = scanner.nextBoolean();
                        vehicle = new Motorcycle(make, model, year, sidecar);
		            } else if (type == 3) {
		                System.out.print("Enter the cargo capacity: ");
		                double cargoCapacity = scanner.nextDouble();
		                vehicle = new Truck(make, model, year, cargoCapacity);
		            } else {
		            	vehicle = null;
		            }
                    
                    if (vehicle != null){
	                    vehicle.setLicensePlate(plate);
	                    rentalSystem.addVehicle(vehicle);
	                    System.out.println("Vehicle added.");
                    }
                    else {
	                    System.out.println("Vehicle not added.");
                    }
                    break;

                case 2:
                    System.out.print("Enter customer ID: ");
                    String cid = scanner.nextLine();
                    System.out.print("Enter name: ");
                    String cname = scanner.nextLine();

                    rentalSystem.addCustomer(new Customer(Integer.parseInt(cid), cname));
                    System.out.println("Customer added.");
                    break;
                    
                case 3:
                	System.out.println("List of Available Vehicles:");
                	rentalSystem.displayVehicles(true);

                    System.out.print("Enter license plate: ");
                    String rentPlate = scanner.nextLine().toUpperCase();

                	System.out.println("Registered Customers:");
                	rentalSystem.displayAllCustomers();

                    System.out.print("Enter customer ID: ");
                    String cidRent = scanner.nextLine();

                    System.out.print("Enter rental amount: ");
                    double rentAmount = scanner.nextDouble();
                    scanner.nextLine();

                    Vehicle vehicleToRent = rentalSystem.findVehicleByPlate(rentPlate);
                    Customer customerToRent = rentalSystem.findCustomerById(cidRent);

                    if (vehicleToRent == null || customerToRent == null) {
                        System.out.println("Vehicle or customer not found.");
                        break;
                    }

                    rentalSystem.rentVehicle(vehicleToRent, customerToRent, LocalDate.now(), rentAmount);
                    break;

                case 4:
                	System.out.println("List of Vehicles:");
                	rentalSystem.displayVehicles(false);

                	System.out.print("Enter license plate: ");
                    String returnPlate = scanner.nextLine().toUpperCase();
                    
                	System.out.println("Registered Customers:");
                	rentalSystem.displayAllCustomers();

                    System.out.print("Enter customer ID: ");
                    String cidReturn = scanner.nextLine();

                    System.out.print("Enter return fees: ");
                    double returnFees = scanner.nextDouble();
                    scanner.nextLine();

                    Vehicle vehicleToReturn = rentalSystem.findVehicleByPlate(returnPlate);
                    Customer customerToReturn = rentalSystem.findCustomerById(cidReturn);

                    if (vehicleToReturn == null || customerToReturn == null) {
                        System.out.println("Vehicle or customer not found.");
                        break;
                    }

                    rentalSystem.returnVehicle(vehicleToReturn, customerToReturn, LocalDate.now(), returnFees);
                    break;
                    
                case 5:
                    rentalSystem.displayVehicles(true);
                    break;
                
                case 6:
                    System.out.println("Rental History:");
                    rentalSystem.displayRentalHistory();
                    break;
                    
                case 0:
                	scanner.close();
                    System.exit(0);
            }
        }
    }
}