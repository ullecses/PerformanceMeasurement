package backend.academy.benchmark;

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

// Specifies the mode for benchmarking (average time per operation) and the time unit (nanoseconds)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)  // Each thread gets its own instance of the state (e.g., student)
public class ReflectionBenchmark {

    private static final String NAME = "name";

    // Instance variables for benchmarking
    private Student student;  // The student object
    private Method method;  // Method object for reflection-based invocation
    private MethodHandle methodHandle;  // MethodHandle for dynamic method invocation
    private StudentInterface lambdaMetafactory;  // LambdaMetafactory for generating a lambda-based method invocation

    // This method is executed before the benchmarks to initialize necessary resources
    @Setup
    public void setup() throws Throwable {
        // Initialize the student object
        student = new Student("Alexander", "Biryukov");

        // Set up reflection: get the 'name' method from the Student class and make it accessible
        method = Student.class.getMethod(NAME);
        method.setAccessible(true);

        // Set up MethodHandles: dynamic method invocation for the 'name' method
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        methodHandle = lookup.findVirtual(Student.class, NAME, MethodType.methodType(String.class));

        // Set up LambdaMetafactory: create a lambda expression that calls the 'name' method dynamically
        CallSite callSite = LambdaMetafactory.metafactory(
            lookup,  // MethodHandles lookup
            "getName",  // Name of the method in the functional interface
            MethodType.methodType(StudentInterface.class),  // Type of the lambda
            MethodType.methodType(String.class, Student.class),  // Method signature for the lambda
            methodHandle,  // Method handle to bind the method
            MethodType.methodType(String.class, Student.class)  // Method type to be called by the lambda
        );
        lambdaMetafactory = (StudentInterface) callSite.getTarget().invokeExact();  // Create the lambda
    }

    // Benchmark method: Direct access to the 'name' method
    @Benchmark
    public void directAccess(Blackhole bh) {
        String name = student.name();  // Direct method call
        bh.consume(name);  // Consuming the result to prevent optimization
    }

    // Benchmark method: Reflection-based access to the 'name' method
    @Benchmark
    public void reflection(Blackhole bh) throws Throwable {
        String name = (String) method.invoke(student);  // Reflection-based method invocation
        bh.consume(name);  // Consuming the result to prevent optimization
    }

    // Benchmark method: MethodHandles-based access to the 'name' method
    @Benchmark
    public void methodHandles(Blackhole bh) throws Throwable {
        String name = (String) methodHandle.invoke(student);  // MethodHandle-based method invocation
        bh.consume(name);  // Consuming the result to prevent optimization
    }

    // Benchmark method: LambdaMetafactory-based access to the 'name' method
    @Benchmark
    public void lambdaMetafactory(Blackhole bh) {
        String name = lambdaMetafactory.getName(student);  // Lambda invocation
        bh.consume(name);  // Consuming the result to prevent optimization
    }

    // A record class to represent a Student with 'name' and 'surname'
    record Student(String name, String surname) {}

    // A functional interface for method invocation via LambdaMetafactory
    @FunctionalInterface
    interface StudentInterface {
        String getName(Student student);  // Method to get the student's name
    }
}
