package mortgage;

public class MortgageUtils {
    public static String formatMoney(double money) {
        return String.format("%,.0f", money);
    }
}