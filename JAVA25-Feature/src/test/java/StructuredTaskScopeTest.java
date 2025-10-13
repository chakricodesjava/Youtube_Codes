import java.util.concurrent.StructuredTaskScope;

public class StructuredTaskScopeTest {
    public static void main(String[] args) {
        System.out.println("StructuredTaskScope constructors:");
        for (var constructor : StructuredTaskScope.class.getConstructors()) {
            System.out.println("  " + constructor);
        }
    }
}