package backend.academy;

import backend.academy.benchmark.ReflectionBenchmark;
import java.util.concurrent.TimeUnit;
import lombok.experimental.UtilityClass;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

@UtilityClass
public class Main {
    public static void main(String[] args) throws RunnerException {

        final int FORKS = 3;
        final int WARMUP_FORKS = 3;
        final int WARMUP_ITERATIONS = 3;
        final int WARMUP_TIME_SECONDS = 7;
        final int MEASUREMENT_ITERATIONS = 5;
        final int MEASUREMENT_TIME_SECONDS = 7;

        // Build options for the benchmark run
        Options options = new OptionsBuilder()
            .include(ReflectionBenchmark.class.getSimpleName())  // Include the current benchmark class
            .shouldFailOnError(true)  // Fail the benchmark if an error occurs
            .shouldDoGC(true)  // Perform garbage collection between iterations
            .mode(Mode.AverageTime)  // Measure average time per operation
            .timeUnit(TimeUnit.NANOSECONDS)  // Time unit for results (nanoseconds)
            .forks(FORKS)  // Number of forks (processes) to run
            .warmupForks(WARMUP_FORKS)  // Number of forks to use for warmup
            .warmupIterations(WARMUP_ITERATIONS)  // Number of warmup iterations
            .warmupTime(TimeValue.seconds(WARMUP_TIME_SECONDS))  // Duration of warmup
            .measurementIterations(MEASUREMENT_ITERATIONS)  // Number of measurement iterations
            .measurementTime(TimeValue.seconds(MEASUREMENT_TIME_SECONDS))  // Duration of measurement
            .build();

        // Run the benchmark with the specified options
        new Runner(options).run();
    }
}
