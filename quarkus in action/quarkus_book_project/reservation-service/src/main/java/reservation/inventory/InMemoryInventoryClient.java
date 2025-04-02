package reservation.inventory;

import java.util.ArrayList;
import java.util.List;

public class InMemoryInventoryClient implements InventoryClient {

    public static final List<Car> ALL_CARS = List.of(
            new Car(1L, "ABC-123", "Toyota", "Corolla"),
            new Car(2L, "ABC-987", "Honda", "Jazz"),
            new Car(3L, "XYZ-123", "Renault", "Clio"),
            new Car(4L, "XYZ-987", "Ford", "Focus")
    );


    @Override
    public List<Car> allCars() {
        return ALL_CARS;
    }
}
