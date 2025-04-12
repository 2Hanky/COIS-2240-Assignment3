public class Motorcycle extends Vehicle implements Rentable {
    private boolean hasSidecar;

    public Motorcycle(String make, String model, int year, boolean hasSidecar) {
        super(make, model, year);
        this.hasSidecar = hasSidecar;
    }

    public boolean hasSidecar() {
        return hasSidecar;
    }

    @Override
    public String getInfo() {
        return super.getInfo() + " | Sidecar: " + (hasSidecar ? "Yes" : "No");
    }

    @Override
    public void rentVehicle() {
        setStatus(VehicleStatus.RENTED);
        System.out.println("Motorcycle " + getLicensePlate() + " has been rented.");
    }

    @Override
    public void returnVehicle() {
        setStatus(VehicleStatus.AVAILABLE);
        System.out.println("Motorcycle " + getLicensePlate() + " has been returned.");
    }
}