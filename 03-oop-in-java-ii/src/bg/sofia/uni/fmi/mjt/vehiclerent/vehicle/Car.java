package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

import bg.sofia.uni.fmi.mjt.vehiclerent.exception.InvalidRentingPeriodException;

import java.time.LocalDateTime;
import java.time.Duration;

public class Car extends Vehicle {
    private final FuelType fuelType;
    private final int numberOfSeats;

    public Car(String id, String model, FuelType fuelType, int numberOfSeats,
               double pricePerWeek, double pricePerDay, double pricePerHour) {
        super(id, model);
        this.fuelType = fuelType;
        this.pricePerWeek = pricePerWeek;
        this.pricePerDay = pricePerDay;
        this.pricePerHour = pricePerHour;
        this.numberOfSeats = numberOfSeats;
    }

    @Override
    public double calculateRentalPrice(LocalDateTime startTime, LocalDateTime endTime)
            throws InvalidRentingPeriodException {
        if(endTime.isBefore(startTime)) {
            throw new InvalidRentingPeriodException("Invalid rental period when calculating rental price for car.");
        }

        double rentalPrice = 0.0;

        Duration rentDuration = Duration.between(startTime, endTime);
        long days = rentDuration.toDays();

        // adding the weeks
        if(days >= 7) {
            long weeks = days / 7;
            rentalPrice += weeks * pricePerWeek;
        }

        rentalPrice += fuelType.getTaxPerDay() * days;

        // adding the other days
        long leftDays = days % 7;
        rentalPrice += days * pricePerDay;

        // updating the time period
        startTime = startTime.plusDays(days);

        long hours = Duration.between(startTime, endTime).toHours();

        long minutes = Duration.between(startTime.plusHours(hours), endTime).toMinutes();

        // if there are any hours or minutes after subtracting the days, add one more day of fuelType tax
        if(hours > 0 || minutes > 0) {
            rentalPrice += fuelType.getTaxPerDay();
        }
        if(minutes > 0) {
            hours++;
        }

        rentalPrice += pricePerHour * hours;
        rentalPrice += numberOfSeats * seatPrice;
        rentalPrice += currentDriver.group().getTax();


        return rentalPrice;
    }

    @Override
    protected boolean isValidRentalPeriod(LocalDateTime startTime, LocalDateTime endTime) {
        return true;
    }
}
