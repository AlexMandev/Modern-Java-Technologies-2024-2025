package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

import bg.sofia.uni.fmi.mjt.vehiclerent.exception.InvalidRentingPeriodException;

import java.time.Duration;
import java.time.LocalDateTime;

public class Caravan extends Vehicle {
    private final FuelType fuelType;
    private final int numberOfSeats;
    private final int numberOfBeds;
    private static final double bedPrice = 10.0;

    public Caravan(String id, String model, FuelType fuelType, int numberOfSeats, int numberOfBeds,
                   double pricePerWeek, double pricePerDay, double pricePerHour) {
        super(id, model);
        this.fuelType = fuelType;
        this.numberOfSeats = numberOfSeats;
        this.numberOfBeds = numberOfBeds;
        this.pricePerWeek = pricePerWeek;
        this.pricePerDay = pricePerDay;
        this.pricePerHour = pricePerHour;
    }

    @Override
    protected boolean isValidRentalPeriod(LocalDateTime startTime, LocalDateTime endTime) {
        return startTime != null && endTime != null && Duration.between(startTime, endTime).toDays() >= 1;
    }

    @Override
    public double calculateRentalPrice(LocalDateTime startTime, LocalDateTime endTime) throws InvalidRentingPeriodException {
        if(endTime.isBefore(startTime) || !isValidRentalPeriod(startTime, endTime)) {
            throw new InvalidRentingPeriodException("Invalid rental period when calculating rental price for caravan.");
        }

        double rentalPrice = 0.0;

        Duration rentDuration = Duration.between(startTime, endTime);
        long days = rentDuration.toDays();
        long remainingHours = rentDuration.minusDays(days).toHours();

        long weeks = days / 7;
        rentalPrice += weeks * pricePerWeek;

        long remainingDays = days % 7;
        rentalPrice += remainingDays * pricePerDay;

        rentalPrice += fuelType.getTaxPerDay() * days;

        if (remainingHours > 0) {
            rentalPrice += pricePerHour * remainingHours;
            rentalPrice += fuelType.getTaxPerDay();
        }

        // adding taxes for seats and beds
        rentalPrice += numberOfBeds * bedPrice;
        rentalPrice += numberOfSeats * seatPrice;

        rentalPrice += currentDriver.group().getTax();

        return rentalPrice;
    }
}
