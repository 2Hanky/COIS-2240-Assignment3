public class Truck extends Vehicle implements Rentable {
    private double cargoCapacity;

    public Truck(String make, String model, int year, double cargoCapacity) {
        super(make, model, year);
        if (cargoCapacity <= 0) throw new IllegalArgumentException("Cargo capacity must be > 0");
        this.cargoCapacity = cargoCapacity;
    }

    public double getCargoCapacity() {
        return cargoCapacity;
    }

    @Override
    public String getInfo() {
        return super.getInfo() + " | Cargo Capacity: " + cargoCapacity;
    }

    @Override
    public void rentVehicle() {
        setStatus(VehicleStatus.RENTED);
        System.out.println("Truck " + getLicensePlate() + " has been rented.");
    }

    @Override
    public void returnVehicle() {
        setStatus(VehicleStatus.AVAILABLE);
        System.out.println("Truck " + getLicensePlate() + " has been returned.");
    }
}