package reservation.rest;

import io.quarkus.logging.Log;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.RestQuery;
import reservation.inventory.Car;
import reservation.inventory.InventoryClient;
import reservation.rental.Rental;
import reservation.rental.RentalClient;
import reservation.reservation.Reservation;
import reservation.reservation.ReservationsRepository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("reservation")
@Produces(MediaType.APPLICATION_JSON)
public class ReservationResource {

    private final ReservationsRepository reservationsRepository;
    private final InventoryClient inventoryClient;
    private final RentalClient rentalClient;

    public ReservationResource(ReservationsRepository reservationsRepository, InventoryClient inventoryClient, @RestClient RentalClient rentalClient) {
        this.reservationsRepository = reservationsRepository;
        this.inventoryClient = inventoryClient;
        this.rentalClient = rentalClient;
    }

    @GET
    @Path("availability")
    public Collection<Car> availability(@RestQuery LocalDate startDate, @RestQuery LocalDate endDate) {
        // obtain all cars from inventory
        List<Car> availableCars = inventoryClient.allCars();
        // create a map from id to car
        Map<Long, Car> carsByid = new HashMap<>();
        for (Car car : availableCars) {
            carsByid.put(car.id, car);
        }

        // get all current reservations
        List<Reservation> reservations = reservationsRepository.findAll();
        // for each reservation, remove the car from the map
        for (Reservation reservation : reservations) {
            if (reservation.isReserved(startDate, endDate)) {
                carsByid.remove(reservation.carId);
            }
        }
        return carsByid.values();
    }

    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public Reservation make(Reservation reservation) {
        Reservation result = reservationsRepository.save(reservation);
        // This is just a dummy value for the time being
        String userId = "x";

        if (reservation.startDay.equals(LocalDate.now())) {
            Rental rental = rentalClient.start(userId, result.id);
            Log.info("Successfully started rental: " + rental);
        }
        return result;
    }

}
