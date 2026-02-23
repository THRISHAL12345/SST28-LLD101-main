import java.util.*;

public class InvoicePrinter {

    public String format(String invId,
                         List<String> lineDescriptions,
                         double subtotal,
                         double taxPct,
                         double tax,
                         double discount,
                         double total) {

        StringBuilder out = new StringBuilder();

        out.append("Invoice# ").append(invId).append("\n");

        for (String line : lineDescriptions) {
            out.append(line).append("\n");
        }

        out.append(String.format("Subtotal: %.2f\n", subtotal));
        out.append(String.format("Tax(%.0f%%): %.2f\n", taxPct, tax));
        out.append(String.format("Discount: -%.2f\n", discount));
        out.append(String.format("TOTAL: %.2f\n", total));

        return out.toString();
    }
}