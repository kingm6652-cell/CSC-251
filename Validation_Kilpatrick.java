import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Locale;

/** Shared input checks. Added during the AI-assisted Kilpatrick integration. */
public final class Validation_Kilpatrick {
    public static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("MM/dd/uuuu")
            .withResolverStyle(ResolverStyle.STRICT);
    public static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm")
            .withResolverStyle(ResolverStyle.STRICT);
    private Validation_Kilpatrick() { }

    public static String required(String value, String label) {
        if (value == null || value.trim().isEmpty())
            throw new IllegalArgumentException(label + " is required.");
        String result = value.trim();
        if (result.length() > 200) throw new IllegalArgumentException(label + " is too long (200 characters maximum).");
        return result;
    }
    public static int whole(String text, String label, int minimum) {
        try {
            int value = Integer.parseInt(text.trim());
            if (value < minimum || value > 1000000) throw new NumberFormatException();
            return value;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(label + " must be a whole number from " + minimum + " to 1,000,000.");
        }
    }
    public static int nonnegative(int value, String label) {
        return whole(Integer.toString(value), label, 0);
    }
    public static long cents(String text) {
        try {
            BigDecimal amount = new BigDecimal(text.trim()).setScale(2, RoundingMode.UNNECESSARY);
            if (amount.signum() < 0 || amount.compareTo(new BigDecimal("1000000.00")) > 0)
                throw new NumberFormatException();
            return amount.movePointRight(2).longValueExact();
        } catch (ArithmeticException | NumberFormatException ex) {
            throw new IllegalArgumentException("Enter a price from 0 to 1,000,000 with at most two decimal places (no $ sign).");
        }
    }
    public static long cents(double amount) { return cents(Double.toString(amount)); }
    public static String dollars(long cents) { return BigDecimal.valueOf(cents, 2).toPlainString(); }
    public static String money(long cents) { return (cents < 0 ? "-$" : "$") + dollars(Math.abs(cents)); }
    public static LocalDate date(String text) {
        try { return LocalDate.parse(text.trim(), DATE); }
        catch (RuntimeException ex) { throw new IllegalArgumentException("Enter a real date as MM/DD/YYYY, such as 10/15/2026."); }
    }
    public static LocalTime time(String text) {
        try { return LocalTime.parse(text.trim(), TIME); }
        catch (RuntimeException ex) { throw new IllegalArgumentException("Enter time as HH:mm (24-hour), such as 14:30."); }
    }
    public static String method(String value) {
        if (!"Cash".equals(value) && !"Card".equals(value) && !"Check".equals(value))
            throw new IllegalArgumentException("Choose Cash, Card, or Check.");
        return value;
    }
    public static String customer(String text) {
        return text == null || text.trim().isEmpty() ? "Walk-in" : required(text, "Customer");
    }
}
