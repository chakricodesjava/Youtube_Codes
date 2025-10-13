package com.youtube.demos.jdk25.jep508;

import jdk.incubator.vector.IntVector;
import jdk.incubator.vector.FloatVector;
import jdk.incubator.vector.VectorSpecies;
import jdk.incubator.vector.VectorOperators;

import java.util.Arrays;
import java.util.Random;

/**
 * JEP 508: Vector API (Tenth Incubator)
 * 
 * Demonstrates expressing vector computations that compile at runtime to optimal SIMD instructions,
 * providing data-parallel speedups for numerical computations, analytics, and machine learning.
 * 
 * Key Benefits:
 * - SIMD (Single Instruction, Multiple Data) acceleration
 * - Platform-independent vector operations
 * - Automatic optimization by JIT compiler
 * - Significant performance gains for numerical computations
 */
public class VectorApiDemo {
    
    // Vector species for different data types
    private static final VectorSpecies<Integer> INT_SPEC = IntVector.SPECIES_PREFERRED;
    private static final VectorSpecies<Float> FLOAT_SPEC = FloatVector.SPECIES_PREFERRED;
    
    /**
     * Demonstrates basic vector arithmetic operations
     */
    public static void demonstrateBasicVectorArithmetic() {
        System.out.println("📋 Demo 1: Basic Vector Arithmetic");
        System.out.printf("Vector species: %s (lane count: %d)%n", 
            INT_SPEC.toString(), INT_SPEC.length());
        
        int length = 1000;
        int[] a = new int[length];
        int[] b = new int[length];
        int[] c = new int[length];
        int[] cScalar = new int[length];
        
        // Initialize arrays
        for (int i = 0; i < length; i++) {
            a[i] = i + 1;
            b[i] = 2 * i + 1;
        }
        
        // Vector computation: c = a * b + a
        long vectorStart = System.nanoTime();
        
        int i = 0;
        int upperBound = INT_SPEC.loopBound(length);
        
        for (; i < upperBound; i += INT_SPEC.length()) {
            var va = IntVector.fromArray(INT_SPEC, a, i);
            var vb = IntVector.fromArray(INT_SPEC, b, i);
            var vc = va.mul(vb).add(va); // c = a*b + a
            vc.intoArray(c, i);
        }
        
        // Handle remaining elements (scalar tail loop)
        for (; i < length; i++) {
            c[i] = a[i] * b[i] + a[i];
        }
        
        long vectorTime = System.nanoTime() - vectorStart;
        
        // Scalar computation for comparison
        long scalarStart = System.nanoTime();
        for (int j = 0; j < length; j++) {
            cScalar[j] = a[j] * b[j] + a[j];
        }
        long scalarTime = System.nanoTime() - scalarStart;
        
        // Verify results match
        boolean resultsMatch = Arrays.equals(c, cScalar);
        
        System.out.printf("✅ Results match: %s%n", resultsMatch);
        System.out.printf("🚀 Vector time: %.3f ms%n", vectorTime / 1_000_000.0);
        System.out.printf("🐌 Scalar time: %.3f ms%n", scalarTime / 1_000_000.0);
        System.out.printf("⚡ Speedup: %.2fx%n", (double) scalarTime / vectorTime);
        System.out.printf("📊 Sample results: c[123] = %d, sum = %d%n", 
            c[123], Arrays.stream(c).sum());
    }
    
    /**
     * Demonstrates floating-point vector operations
     */
    public static void demonstrateFloatVectorOperations() {
        System.out.println("\n📋 Demo 2: Floating-Point Vector Operations");
        
        int length = 1024;
        float[] input = new float[length];
        float[] output = new float[length];
        
        // Initialize with some mathematical function
        for (int i = 0; i < length; i++) {
            input[i] = (float) (Math.sin(i * 0.1) * 100);
        }
        
        // Vector computation: complex mathematical operations
        long start = System.nanoTime();
        
        int i = 0;
        int upperBound = FLOAT_SPEC.loopBound(length);
        
        for (; i < upperBound; i += FLOAT_SPEC.length()) {
            var v = FloatVector.fromArray(FLOAT_SPEC, input, i);
            
            // Complex computation: sqrt(abs(x)) + x^2 + 1
            var result = v.abs()
                          .lanewise(VectorOperators.SQRT)
                          .add(v.mul(v))
                          .add(1.0f);
            
            result.intoArray(output, i);
        }
        
        // Scalar tail loop
        for (; i < length; i++) {
            float x = input[i];
            output[i] = (float) (Math.sqrt(Math.abs(x)) + x * x + 1.0);
        }
        
        long elapsed = System.nanoTime() - start;
        
        System.out.printf("⚡ Complex math on %d floats took %.3f ms%n", 
            length, elapsed / 1_000_000.0);
        System.out.printf("📊 Sample: input[100]=%.2f -> output[100]=%.2f%n", 
            input[100], output[100]);
    }
    
    /**
     * Demonstrates vector reductions (sum, min, max)
     */
    public static void demonstrateVectorReductions() {
        System.out.println("\n📋 Demo 3: Vector Reductions");
        
        int length = 10000;
        int[] data = new int[length];
        Random random = new Random(42); // Fixed seed for reproducibility
        
        // Fill with random data
        for (int i = 0; i < length; i++) {
            data[i] = random.nextInt(1000);
        }
        
        // Vector sum reduction
        long sum = 0;
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        
        int i = 0;
        int upperBound = INT_SPEC.loopBound(length);
        
        // Initialize reduction vectors
        var sumVec = IntVector.zero(INT_SPEC);
        var minVec = IntVector.broadcast(INT_SPEC, Integer.MAX_VALUE);
        var maxVec = IntVector.broadcast(INT_SPEC, Integer.MIN_VALUE);
        
        for (; i < upperBound; i += INT_SPEC.length()) {
            var v = IntVector.fromArray(INT_SPEC, data, i);
            
            sumVec = sumVec.add(v);
            minVec = minVec.min(v);
            maxVec = maxVec.max(v);
        }
        
        // Reduce vector lanes to scalar values
        sum += sumVec.reduceLanes(VectorOperators.ADD);
        min = Math.min(min, minVec.reduceLanes(VectorOperators.MIN));
        max = Math.max(max, maxVec.reduceLanes(VectorOperators.MAX));
        
        // Handle remaining elements
        for (; i < length; i++) {
            sum += data[i];
            min = Math.min(min, data[i]);
            max = Math.max(max, data[i]);
        }
        
        // Verify with Arrays stream operations
        long expectedSum = Arrays.stream(data).asLongStream().sum();
        int expectedMin = Arrays.stream(data).min().orElse(Integer.MAX_VALUE);
        int expectedMax = Arrays.stream(data).max().orElse(Integer.MIN_VALUE);
        
        System.out.printf("✅ Sum: %d (expected: %d) - Match: %s%n", 
            sum, expectedSum, sum == expectedSum);
        System.out.printf("✅ Min: %d (expected: %d) - Match: %s%n", 
            min, expectedMin, min == expectedMin);
        System.out.printf("✅ Max: %d (expected: %d) - Match: %s%n", 
            max, expectedMax, max == expectedMax);
    }
    
    /**
     * Demonstrates vector masking and conditional operations
     */
    public static void demonstrateVectorMasking() {
        System.out.println("\n📋 Demo 4: Vector Masking and Conditional Operations");
        
        int length = 1000;
        float[] data = new float[length];
        float[] result = new float[length];
        
        // Initialize data
        for (int i = 0; i < length; i++) {
            data[i] = (i - length / 2) * 0.1f; // Range from negative to positive
        }
        
        // Conditional operation: if x > 0 then sqrt(x) else x^2
        int i = 0;
        int upperBound = FLOAT_SPEC.loopBound(length);
        
        for (; i < upperBound; i += FLOAT_SPEC.length()) {
            var v = FloatVector.fromArray(FLOAT_SPEC, data, i);
            
            // Create mask for positive values
            var positiveMask = v.compare(VectorOperators.GT, 0.0f);
            
            // Apply different operations based on mask
            var sqrtResult = v.lanewise(VectorOperators.SQRT);
            var squareResult = v.mul(v);
            
            // Blend results based on mask
            var blended = sqrtResult.blend(squareResult, positiveMask.not());
            
            blended.intoArray(result, i);
        }
        
        // Scalar tail loop
        for (; i < length; i++) {
            float x = data[i];
            result[i] = x > 0 ? (float) Math.sqrt(x) : x * x;
        }
        
        // Show some results
        System.out.println("Sample conditional results:");
        for (int j = length/2 - 2; j <= length/2 + 2; j++) {
            String operation = data[j] > 0 ? "sqrt" : "square";
            System.out.printf("  data[%d]=%.3f -> %s -> %.3f%n", 
                j, data[j], operation, result[j]);
        }
    }
    
    /**
     * Demonstrates performance comparison for matrix operations
     */
    public static void demonstrateMatrixMultiplication() {
        System.out.println("\n📋 Demo 5: Matrix Operations Performance");
        
        int size = 256; // Small matrix for demo
        float[] matrixA = new float[size * size];
        float[] matrixB = new float[size * size];
        float[] result = new float[size * size];
        
        // Initialize matrices
        Random random = new Random(42);
        for (int i = 0; i < size * size; i++) {
            matrixA[i] = random.nextFloat() * 10;
            matrixB[i] = random.nextFloat() * 10;
        }
        
        long start = System.nanoTime();
        
        // Simple row-wise vector multiplication (not full matrix multiply)
        for (int row = 0; row < size; row++) {
            int rowOffset = row * size;
            
            int i = 0;
            int upperBound = FLOAT_SPEC.loopBound(size);
            
            for (; i < upperBound; i += FLOAT_SPEC.length()) {
                var va = FloatVector.fromArray(FLOAT_SPEC, matrixA, rowOffset + i);
                var vb = FloatVector.fromArray(FLOAT_SPEC, matrixB, rowOffset + i);
                var vr = va.mul(vb);
                vr.intoArray(result, rowOffset + i);
            }
            
            // Scalar tail
            for (; i < size; i++) {
                result[rowOffset + i] = matrixA[rowOffset + i] * matrixB[rowOffset + i];
            }
        }
        
        long elapsed = System.nanoTime() - start;
        
        System.out.printf("⚡ %dx%d matrix element-wise multiplication: %.3f ms%n", 
            size, size, elapsed / 1_000_000.0);
        System.out.printf("📊 Sample result[0][0] = %.3f%n", result[0]);
        System.out.printf("💡 Vector API enables SIMD acceleration for numerical computations%n");
    }
    
    public static void main(String[] args) {
        System.out.println("🚀 JEP 508: Vector API (Tenth Incubator) Demo");
        System.out.println("==============================================");
        
        demonstrateBasicVectorArithmetic();
        demonstrateFloatVectorOperations();
        demonstrateVectorReductions();
        demonstrateVectorMasking();
        demonstrateMatrixMultiplication();
        
        System.out.println("\n💡 Key Benefits of Vector API:");
        System.out.println("   • SIMD acceleration for data-parallel operations");
        System.out.println("   • Platform-independent vector programming");
        System.out.println("   • Automatic JIT compiler optimizations");
        System.out.println("   • Significant speedups for numerical computations");
        System.out.println("   • Type-safe vector operations");
        System.out.println("   • Support for masking and conditional operations");
        
        System.out.println("\n⚠️  Note: Vector API is in incubator status");
        System.out.println("   Requires: --add-modules jdk.incubator.vector");
    }
}