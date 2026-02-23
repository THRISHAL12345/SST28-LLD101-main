import java.util.*;

public class CafeteriaSystem {

    private final Map<String, MenuItem> menu = new LinkedHashMap<>();
    private final PricingService pricingService = new PricingService(menu);
    private final TaxPolicy taxPolicy = new DefaultTaxPolicy();
    private final DiscountPolicy discountPolicy = new DefaultDiscountPolicy();
    private final InvoicePrinter printer = new InvoicePrinter();
    private final InvoiceRepository repository =
            new FileStoreRepository(new FileStore());

    private int invoiceSeq = 1000;

    public void addToMenu(MenuItem i) {
        menu.put(i.id, i);
    }

    public void checkout(String customerType, List<OrderLine> lines) {

        String invId = "INV-" + (++invoiceSeq);

        double subtotal = pricingService.computeSubtotal(lines);

        List<String> lineDescriptions =
                pricingService.buildLineDescriptions(lines);

        double taxPct = taxPolicy.taxPercent(customerType);
        double tax = subtotal * (taxPct / 100.0);

        double discount =
                discountPolicy.discountAmount(customerType, subtotal, lines);

        double total = subtotal + tax - discount;

        String printable = printer.format(
                invId,
                lineDescriptions,
                subtotal,
                taxPct,
                tax,
                discount,
                total
        );

        System.out.print(printable);

        repository.save(invId, printable);

        System.out.println("Saved invoice: " + invId +
                " (lines=" + repository.countLines(invId) + ")");
    }
}