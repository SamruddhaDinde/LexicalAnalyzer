public class Runner {
    public static void main(String[] args) {
        try {
            System.out.println(LexicalAnalyser.analyse(""));
        }
        catch (NumberException e) {
            System.out.println("Number Exception: " + e.getMessage());
        }
        catch (ExpressionException e) {
            System.out.println("Expression Exception: " + e.getMessage());
        }
    }
}
