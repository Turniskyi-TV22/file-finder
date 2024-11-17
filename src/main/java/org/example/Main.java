package org.example;

import java.io.File;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String directoryPath = "foldertest";
        System.out.print("Enter the character or word to search for: ");
        String keyword = scanner.nextLine();

        File directory = new File(directoryPath);

        if (!directory.exists() || !directory.isDirectory()) {
            System.out.println("Invalid directory path!");
            return;
        }

        File[] files = directory.listFiles();
        if (files == null || files.length == 0) {
            System.out.println("The directory is empty.");
            return;
        }

        int numThreads = 3;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        AtomicInteger count = new AtomicInteger(0);

        int part = (int) Math.ceil((double) files.length / numThreads);

        for (int i = 0; i < numThreads; i++) {
            int start = i * part;
            int end = Math.min(start + part, files.length);

            executor.submit(() -> {
                for (int j = start; j < end; j++) {
                    if (files[j].isFile() && containsKeyword(files[j].getName(), keyword)) {
                        count.incrementAndGet();
                    }
                }
            });
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
        }

        System.out.println("Number of files found: " + count.get());
    }
    private static boolean containsKeyword(String fileName, String keyword) {
        String nameWithoutExtension = fileName.contains(".")
                ? fileName.substring(0, fileName.lastIndexOf("."))
                : fileName;
        return nameWithoutExtension.contains(keyword);
    }
}