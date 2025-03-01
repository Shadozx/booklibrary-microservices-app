package com.shadoww.parserservice;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class ExperimentTests {


    @Test
    public void testTryParallelList() {
//        Assertions.assertThrows(RuntimeException.class, () -> Stream.of(1, 2, 3, 4, 5, 6, 7, 8).parallel()
//                .forEach(n -> {
//                    System.out.println(n);
//                    if (n == 6) {
//                        throw new RuntimeException("Error number!");
//                    }
//                }));
//
        ExecutorService executor = Executors.newFixedThreadPool(4);
        List<Callable<String>> tasks = Arrays.asList(
                () -> {
//                    Thread.sleep(2000);
                    System.out.println("Task 1 started in \t" + getCurrentDateString());
                    Thread.sleep(4000);


                    return "Task 1 finished in \t" + getCurrentDateString();
                },
                () -> {
//                    Thread.sleep(5000);
//                    throw new RuntimeException("Error in Task 2");
                    System.out.println("Task 2 started in \t" + getCurrentDateString());

                    Thread.sleep(1000);

                    throw new RuntimeException("Interrupting!");
//                    return "Task 2 finished in \t" + getCurrentDateString();
                },
                () -> {
//                    Thread.sleep(2000);
//                    return "Task 3";
                    System.out.println("Task 3 started in \t" + getCurrentDateString());

                    Thread.sleep(6000);
                    return "Task 3 finished in \t" + getCurrentDateString();
                }
        );

        List<Future<String>> futures;

        try {
            futures = executor.invokeAll(tasks); // Запуск всіх задач
            for (Future<String> future : futures) {
                try {
                    String result = future.get(); // Очікуємо результат
                    System.out.println("Result: " + result);
                } catch (ExecutionException e) {
                    System.err.println("Exception: " + e.getCause());
                    // Якщо помилка — зупиняємо всі інші
                    for (Future<String> f : futures) {
                        f.cancel(true);
                    }
                    break;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            executor.shutdownNow(); // Закриваємо Executor
        }


    }

    private String getCurrentDateString() {
        // Отримуємо поточну дату і час
        LocalDateTime now = LocalDateTime.now();

        // Формат з дефісами
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");

        // Виводимо результат
        return now.format(formatter);
    }
}
