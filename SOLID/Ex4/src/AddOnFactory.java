import java.util.*;

public class AddOnFactory {

    public static List<AddOnPricing> createAll(List<AddOn> addOns) {
        List<AddOnPricing> result = new ArrayList<>();

        for (AddOn a : addOns) {
            switch (a) {
                case MESS -> result.add(new MessAddOn());
                case LAUNDRY -> result.add(new LaundryAddOn());
                case GYM -> result.add(new GymAddOn());
            }
        }

        return result;
    }
}