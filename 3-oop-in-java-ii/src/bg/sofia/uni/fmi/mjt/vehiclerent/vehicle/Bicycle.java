package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

import bg.sofia.uni.fmi.mjt.vehiclerent.exception.InvalidRentingPeriodException;

import java.time.LocalDateTime;
import java.time.Duration;

public class Bicycle extends Vehicle{

    public Bicycle(String id, String model, double pricePerDay, double pricePerHour) {
        super(id, model);
        super.pricePerDay = pricePerDay;
        super.pricePerHour = pricePerHour;
    }

    @Override
    protected boolean isValidRentalPeriod(LocalDateTime startTime, LocalDateTime endTime) {
        return startTime != null && endTime != null && Duration.between(startTime, endTime).toDays() < 7;
    }

    @Override
    public double calculateRentalPrice(LocalDateTime startTime, LocalDateTime endTime) throws InvalidRentingPeriodException {
        if(!isValidRentalPeriod(startTime, endTime) || endTime.isBefore(startTime)) {
            throw new InvalidRentingPeriodException("Invalid rental period when calculating rental price for bicycle.");
        }

        double rentalPrice = 0.0;

        long days = Duration.between(startTime, endTime).toDays();


        rentalPrice += days * pricePerDay;

        startTime = startTime.plusDays(days);

        long hours = Duration.between(startTime, endTime).toHours();
        long minutes = Duration.between(startTime.plusHours(hours), endTime).toMinutes();

        if(minutes > 0) {
            hours++;
        }

        rentalPrice += hours * pricePerHour;

        return rentalPrice;
    }
}
