package factory;

import model.Vehicle;
import model.enums.VehicleType;

public class VehicleFactory {

    public static Vehicle createVehicle(String number, String type) {

        VehicleType vType;

        switch (type.toLowerCase()) {
            case "bike":
                vType = VehicleType.BIKE;
                break;
            case "car":
                vType = VehicleType.CAR;
                break;
            default:
                vType = VehicleType.BUS;
        }

        return new Vehicle(number, vType);
    }
}