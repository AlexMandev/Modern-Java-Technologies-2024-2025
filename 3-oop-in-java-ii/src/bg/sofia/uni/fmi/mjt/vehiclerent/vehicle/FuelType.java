package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

public enum FuelType {
    DIESEL(3.0),
    PETROL(3.0),
    HYBRID(1.0),
    ELECTRICITY(0.0),
    HYDROGEN(0.0);

    private final double taxPerDay;
    FuelType(double taxPerDay) {
        this.taxPerDay = taxPerDay;
    }
    double getTaxPerDay() {
        return this.taxPerDay;
    }
}
