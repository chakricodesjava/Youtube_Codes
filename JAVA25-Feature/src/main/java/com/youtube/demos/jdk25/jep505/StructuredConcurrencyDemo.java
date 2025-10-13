package com.youtube.demos.jdk25.jep505;

import java.util.concurrent.*;
import java.time.Instant;

/**
 * JEP 505: Structured Concurrency (Fifth Preview) - Concept Demo
 * 
 * Demonstrates treating related tasks running in different threads as a single unit of work,
 * improving cancellation, error handling, and observability.
 * 
 * Key Benefits:
 * - Safer and clearer concurrency with virtual threads
 * - Automatic cleanup of child threads when parent scope completes
 * - Better error propagation and cancellation
 * 
 * Note: Using conceptual demonstration as StructuredTaskScope API may vary by JDK build
 */
public class StructuredConcurrencyDemo {
    
    /**
     * Simulates a slow computation that might fail
     */
    private static int slowCompute(String taskName, int value, int delayMs, boolean shouldFail) throws InterruptedException {
        System.out.printf("🔄 %s: Starting computation (value=%d, delay=%dms)%n", taskName, value, delayMs);
        Thread.sleep(delayMs);
        
        if (shouldFail) {
            System.out.printf("❌ %s: Task failed!%n", taskName);
            throw new RuntimeException("Simulated failure in " + taskName);
        }
        
        int result = value * 2;
        System.out.printf("✅ %s: Completed (result=%d)%n", taskName, result);
        return result;
    }
    
    /**
     * Demonstrates successful structured concurrency concept
     */
    public static void demonstrateSuccessfulExecution() {
        System.out.println("📋 Demo 1: Structured Concurrency Concept");
        
        long startTime = System.currentTimeMillis();
        
        // Conceptual demonstration using virtual threads and ExecutorService
        // In full StructuredTaskScope, this would be more elegant
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            
            var task1 = executor.submit(() -> slowCompute("Task-1", 21, 400, false));
            var task2 = executor.submit(() -> slowCompute("Task-2", 21, 300, false));
            var task3 = executor.submit(() -> slowCompute("Task-3", 10, 200, false));
            
            // Wait for all tasks to complete
            try {
                int sum = task1.get() + task2.get() + task3.get();
                long elapsed = System.currentTimeMillis() - startTime;
                
                System.out.printf("🎉 All tasks completed! Sum: %d (took %dms)%n", sum, elapsed);
                System.out.println("💡 In StructuredTaskScope, this would include automatic cleanup and cancellation");
            } catch (ExecutionException e) {
                System.err.println("❌ Task execution failed: " + e.getCause().getMessage());
            }
            
        } catch (InterruptedException e) {
            System.err.println("❌ Task interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Demonstrates error handling concept in structured concurrency
     */
    public static void demonstrateErrorHandling() {
        System.out.println("\n📋 Demo 2: Error Handling Concept");
        
        long startTime = System.currentTimeMillis();
        
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var task1 = executor.submit(() -> slowCompute("Task-1", 21, 400, false)); // Will succeed
            var task2 = executor.submit(() -> slowCompute("Task-2", 21, 200, true));  // Will fail
            
            // Try to get results - will show which succeeded/failed
            System.out.println("Task results:");
            try {
                int result1 = task1.get();
                System.out.printf("✅ Task-1 succeeded: %d%n", result1);
            } catch (ExecutionException e) {
                System.err.printf("❌ Task-1 failed: %s%n", e.getCause().getMessage());
            }
            
            try {
                int result2 = task2.get();
                System.out.printf("✅ Task-2 succeeded: %d%n", result2);
            } catch (ExecutionException e) {
                System.err.printf("❌ Task-2 failed: %s%n", e.getCause().getMessage());
            }
            
            long elapsed = System.currentTimeMillis() - startTime;
            System.out.printf("⏱️ Total time: %dms%n", elapsed);
            System.out.println("💡 StructuredTaskScope would provide better error propagation and automatic cleanup");
            
        } catch (InterruptedException e) {
            System.err.println("❌ Task interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Demonstrates multiple concurrent tasks concept
     */
    public static void demonstrateMultipleTasks() {
        System.out.println("\n📋 Demo 3: Multiple Concurrent Tasks Concept");
        
        long startTime = System.currentTimeMillis();
        
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            // Multiple servers/services responding
            var serverA = executor.submit(() -> {
                Thread.sleep(ThreadLocalRandom.current().nextInt(100, 300));
                return "Response from Server A at " + Instant.now();
            });
            
            var serverB = executor.submit(() -> {
                Thread.sleep(ThreadLocalRandom.current().nextInt(100, 300));
                return "Response from Server B at " + Instant.now();
            });
            
            var serverC = executor.submit(() -> {
                Thread.sleep(ThreadLocalRandom.current().nextInt(100, 300));
                return "Response from Server C at " + Instant.now();
            });
            
            // Collect results
            System.out.println("All server responses:");
            try {
                System.out.println("🔵 " + serverA.get());
                System.out.println("🟢 " + serverB.get());
                System.out.println("🔴 " + serverC.get());
            } catch (ExecutionException e) {
                System.err.println("❌ Server error: " + e.getCause().getMessage());
            }
            
            long elapsed = System.currentTimeMillis() - startTime;
            System.out.printf("🏆 All responses collected in %dms%n", elapsed);
            System.out.println("💡 StructuredTaskScope would provide better task lifecycle management");
            
        } catch (InterruptedException e) {
            System.err.println("❌ Server polling interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Demonstrates nested structured concurrency concept
     */
    public static void demonstrateNestedConcurrency() {
        System.out.println("\n📋 Demo 4: Nested Structured Concurrency Concept");
        
        try (var outerExecutor = Executors.newVirtualThreadPerTaskExecutor()) {
            
            // Parent task that spawns its own subtasks
            var parentTask = outerExecutor.submit(() -> {
                System.out.println("📦 Parent task: Starting nested operations");
                
                try (var innerExecutor = Executors.newVirtualThreadPerTaskExecutor()) {
                    var subtask1 = innerExecutor.submit(() -> slowCompute("Subtask-1", 5, 150, false));
                    var subtask2 = innerExecutor.submit(() -> slowCompute("Subtask-2", 7, 200, false));
                    
                    int subResult = subtask1.get() + subtask2.get();
                    System.out.printf("📦 Parent task: Subtasks completed, sum=%d%n", subResult);
                    return subResult;
                } catch (ExecutionException | InterruptedException e) {
                    throw new RuntimeException("Subtask failed", e);
                }
            });
            
            // Another independent task
            var independentTask = outerExecutor.submit(() -> slowCompute("Independent", 15, 300, false));
            
            try {
                int totalResult = parentTask.get() + independentTask.get();
                System.out.printf("🎯 Final result: %d%n", totalResult);
                System.out.println("💡 StructuredTaskScope would provide cleaner nesting and automatic cleanup");
            } catch (ExecutionException e) {
                System.err.println("❌ Nested concurrency failed: " + e.getCause().getMessage());
            }
            
        } catch (InterruptedException e) {
            System.err.println("❌ Nested concurrency interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
    
    public static void main(String[] args) {
        System.out.println("🚀 JEP 505: Structured Concurrency Demo");
        System.out.println("========================================");
        
        demonstrateSuccessfulExecution();
        demonstrateErrorHandling();
        demonstrateMultipleTasks();
        demonstrateNestedConcurrency();
        
        System.out.println("\n💡 Key Benefits of Structured Concurrency:");
        System.out.println("   • Automatic cleanup of child threads");
        System.out.println("   • Better error propagation");
        System.out.println("   • Clear lifecycle management");
        System.out.println("   • Improved observability");
        System.out.println("   • Prevention of thread leaks");
    }
}