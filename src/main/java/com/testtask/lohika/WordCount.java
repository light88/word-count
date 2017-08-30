package com.testtask.lohika;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class WordCount {

  private static final String DELIMITERS = " ,.";

  private static final Consumer<Map.Entry<String, Integer>> resultConsumer =
    entry -> System.out.println(entry.getKey() + "=" + entry.getValue());

  private final Path filePath;
  private final Integer limit;

  public WordCount(String[] arguments) {
    final int length = arguments.length;
    if (length < 2) {
      throw new IllegalArgumentException("Please provide file path and word count to display");
    }

    String pathValue = arguments[0];
    Path path = Paths.get(pathValue);

    if (!Files.exists(path)) {
      throw new IllegalArgumentException("There is no file for specified path");
    }

    Integer limit = Integer.valueOf(arguments[1]);

    this.filePath = path;
    this.limit = limit;
  }

  public Map<String, Integer> process() {
    try {
      List<String> lines = Files.readAllLines(this.filePath).stream().map(String::toLowerCase).collect(Collectors.toList());
      Map<String, AtomicInteger> incrementWordCounter = new HashMap<>();

      lines.forEach(line -> {
        StringTokenizer tokenizer = new StringTokenizer(line, DELIMITERS);
        while (tokenizer.hasMoreTokens()) {
          String word = tokenizer.nextToken();
          incrementWordCounter.computeIfAbsent(word, (k) -> new AtomicInteger(0));
          incrementWordCounter.computeIfPresent(word, (k, v) -> {
            v.incrementAndGet();
            return v;
          });
        }
      });

      Map<String, Integer> wordCounter =
        incrementWordCounter.entrySet().stream()
          .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get()));

      Map<String, Integer> collect = wordCounter.entrySet().stream()
        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
        .limit(limit)
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
          (oldValue, newValue) -> oldValue, LinkedHashMap::new));

      collect.entrySet().forEach(resultConsumer);

      return collect;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}