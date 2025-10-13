package com.youtube.demos.jdk25;

import java.util.Map;
import java.util.Scanner;

/**
 * Main entry point for JDK 25 Features Demo Suite
 * Interactive runner for all JEP demonstrations
 */
public class DemoRunner {
    
    private static final Map<String, DemoInfo> DEMOS = Map.of(
        "1", new DemoInfo("JEP 505: Structured Concurrency", 
                         "com.youtube.demos.jdk25.jep505.StructuredConcurrencyDemo"),
        "2", new DemoInfo("JEP 506: Scoped Values", 
                         "com.youtube.demos.jdk25.jep506.ScopedValuesDemo"),
        "3", new DemoInfo("JEP 507: Primitive Patterns", 
                         "com.youtube.demos.jdk25.jep507.PrimitivePatternsDemo"),
        "4", new DemoInfo("JEP 508: Vector API", 
                         "com.youtube.demos.jdk25.jep508.VectorApiDemo"),
        "5", new DemoInfo("JEP 510: PBKDF2 Fallback", 
                         "com.youtube.demos.jdk25.jep510.Pbkdf2FallbackDemo"),
        "6", new DemoInfo("JEP 513: Flexible Constructors (Before)", 
                         "com.youtube.demos.jdk25.jep513.BeforeFlexibleConstructor"),
        "7", new DemoInfo("JEP 513: Flexible Constructors (After)", 
                         "com.youtube.demos.jdk25.jep513.AfterFlexibleConstructor"),
        "8", new DemoInfo("JEP 470: PEM Certificate Reader", 
                         "com.youtube.demos.jdk25.jep470.PemCertReader"),
        "9", new DemoInfo("Common Workload (for GC/JFR demos)", 
                         "com.youtube.demos.jdk25.common.Workload")
    );
    
    public static void main(String[] args) {
        if (args.length > 0) {
            runSpecificDemo(args[0]);
            return;
        }
        
        showInteractiveMenu();
    }
    
    private static void showInteractiveMenu() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("🚀 JDK 25 Features Demo Suite");
        System.out.println("===============================");
        System.out.println();
        
        while (true) {
            System.out.println("Available Demos:");
            DEMOS.forEach((key, demo) -> 
                System.out.println("  " + key + ". " + demo.name()));
            System.out.println("  0. Exit");
            System.out.println();
            
            System.out.print("Select demo (0-9): ");
            String choice = scanner.nextLine().trim();
            
            if ("0".equals(choice)) {
                System.out.println("👋 Goodbye!");
                break;
            }
            
            DemoInfo demo = DEMOS.get(choice);
            if (demo != null) {
                runDemo(demo);
            } else {
                System.out.println("❌ Invalid choice. Please try again.");
            }
            
            System.out.println("\n" + "=".repeat(50) + "\n");
        }
        
        scanner.close();
    }
    
    private static void runSpecificDemo(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            var method = clazz.getMethod("main", String[].class);
            System.out.println("🎬 Running: " + clazz.getSimpleName());
            System.out.println("-".repeat(40));
            method.invoke(null, (Object) new String[0]);
        } catch (Exception e) {
            System.err.println("❌ Failed to run demo: " + className);
            e.printStackTrace();
        }
    }
    
    private static void runDemo(DemoInfo demo) {
        try {
            System.out.println("\n🎬 Running: " + demo.name());
            System.out.println("-".repeat(50));
            
            Class<?> clazz = Class.forName(demo.className());
            var method = clazz.getMethod("main", String[].class);
            method.invoke(null, (Object) new String[0]);
            
            System.out.println("\n✅ Demo completed successfully!");
            
        } catch (Exception e) {
            System.err.println("❌ Failed to run demo: " + demo.name());
            System.err.println("Error: " + e.getMessage());
            if (e.getCause() instanceof UnsupportedOperationException) {
                System.err.println("💡 This feature might not be available in your JDK build");
            }
        }
    }
    
    private record DemoInfo(String name, String className) {}
}