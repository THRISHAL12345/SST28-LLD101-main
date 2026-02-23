import java.util.*;

public class FeeCalculator {

    public Money calculateMonthly(RoomPricing room, List<AddOnPricing> addOns) {

        Money total = room.monthlyCharge();

        for (AddOnPricing a : addOns) {
            total = total.plus(a.monthlyCharge());
        }

        return total;
    }
}