package backend.academy;

import backend.academy.benchmark.ReflectionBenchmark;
import lombok.experimental.UtilityClass;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;
import java.util.concurrent.TimeUnit;

@UtilityClass
public class Main {
    public static void main(String[] args) throws RunnerException {
        // Build options for the benchmark run
        Options options = new OptionsBuilder()
            .include(ReflectionBenchmark.class.getSimpleName())  // Include the current benchmark class
            .shouldFailOnError(true)  // Fail the benchmark if an error occurs
            .shouldDoGC(true)  // Perform garbage collection between iterations
            .mode(Mode.AverageTime)  // Measure average time per operation
            .timeUnit(TimeUnit.NANOSECONDS)  // Time unit for results (nanoseconds)
            .forks(3)  // Number of forks (processes) to run
            .warmupForks(2)  // Number of forks to use for warmup
            .warmupIterations(3)  // Number of warmup iterations
            .warmupTime(TimeValue.seconds(7))  // Duration of warmup
            .measurementIterations(5)  // Number of measurement iterations
            .measurementTime(TimeValue.seconds(7))  // Duration of measurement
            .build();

        // Run the benchmark with the specified options
        new Runner(options).run();
    }
}
