package bg.sofia.uni.fmi.mjt.vehiclerent;

import bg.sofia.uni.fmi.mjt.vehiclerent.driver.Driver;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.InvalidRentingPeriodException;
import bg.sofia.uni.fmi.mjt.vehiclerent.vehicle.Vehicle;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.VehicleNotRentedException;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.VehicleAlreadyRentedException;

import java.time.LocalDateTime;

public class RentalService {
    /**
     * Simulates renting of the vehicle. Makes all required validations and then the provided driver "rents" the provided
     * vehicle with start time @startOfRent
     *
     * @param driver      the designated driver of the vehicle
     * @param vehicle     the chosen vehicle to be rented
     * @param startOfRent the start time of the rental
     * @throws IllegalArgumentException      if any of the passed arguments are null
     * @throws VehicleAlreadyRentedException in case the provided vehicle is already rented
     */
    public void rentVehicle(Driver driver, Vehicle vehicle, LocalDateTime startOfRent) {
        if(driver == null) {
            throw new IllegalArgumentException("Driver cannot be null");
        }
        if(vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null.");
        }
        if(startOfRent == null) {
            throw new IllegalArgumentException("StartOfRent cannot be null.");
        }
        if(vehicle.rented()) {
            throw new VehicleAlreadyRentedException("Vehicle is already rented.");
        }

        vehicle.rent(driver, startOfRent);
    }

    /**
     * This method simulates rental return - it includes validation of the arguments that throw the listed exceptions
     * in case of errors. The method returns the expected total price for the rental - price for the vehicle plus
     * additional tax for the driver, if it is applicable
     *
     * @param vehicle   the rented vehicle
     * @param endOfRent the end time of the rental
     * @return price for the rental
     * @throws IllegalArgumentException      in case @endOfRent or @vehicle is null
     * @throws VehicleNotRentedException     in case the vehicle is not rented at all
     * @throws InvalidRentingPeriodException in case the endOfRent is before the start of rental, or the vehicle
     *                                       does not allow the passed period for rental, e.g. Caravans must be rented for at least a day.
     */
    public double returnVehicle(Vehicle vehicle, LocalDateTime endOfRent) throws InvalidRentingPeriodException {
        if(vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null.");
        }
        if(endOfRent == null) {
            throw new IllegalArgumentException("End rental time cannot be null.");
        }
        if(!vehicle.rented()) {
            throw new VehicleNotRentedException("Cannot return vehicle because it isn't rented.");
        }

        double price = vehicle.calculateRentalPrice(vehicle.getRentalStartTime(), endOfRent);
        vehicle.returnBack(endOfRent);

        return price;
    }

    /*public static void main(String[] args) {
        try {
            RentalService rentalService = new RentalService();
            LocalDateTime rentStart = LocalDateTime.of(2024, 10, 10, 0, 0, 0);
            Driver experiencedDriver = new Driver(AgeGroup.EXPERIENCED);

            Vehicle electricCar = new Car("1", "Tesla Model 3", FuelType.ELECTRICITY, 4, 1000, 150, 10);
            rentalService.rentVehicle(experiencedDriver, electricCar, rentStart);
            double priceToPay = rentalService.returnVehicle(electricCar, rentStart.minusDays(5)); // 770.0
            System.out.println(priceToPay);
            Vehicle dieselCar = new Car("2", "Toyota Auris", FuelType.DIESEL, 4, 500, 80, 5);
            rentalService.rentVehicle(experiencedDriver, dieselCar, rentStart);
            priceToPay = rentalService.returnVehicle(dieselCar, rentStart.plusDays(5)); // 80*5 + 3*5 + 4*5 = 435.0
            System.out.println(priceToPay);

            rentalService.rentVehicle(experiencedDriver, dieselCar, LocalDateTime.now());
            System.out.println(rentalService.returnVehicle(dieselCar, LocalDateTime.now().minusHours(2)));
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }*/
}
