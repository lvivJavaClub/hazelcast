package com.lohika.jclub.hazelcast.jet;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.Job;
import com.hazelcast.jet.pipeline.BatchStage;
import com.hazelcast.jet.pipeline.Pipeline;
import com.hazelcast.jet.pipeline.SinkStage;
import com.hazelcast.jet.pipeline.Sinks;
import com.hazelcast.jet.pipeline.Sources;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import static com.hazelcast.jet.Traversers.traverseArray;
import static com.hazelcast.jet.aggregate.AggregateOperations.counting;
import static com.hazelcast.jet.function.DistributedFunctions.wholeItem;

@Slf4j
@Controller
@AllArgsConstructor
public class ReportController {

  private static final String LIST_NAME = "name-list-for-jet";
  private static final String MAP_NAME1 = "tmp-res-1";
  private static final String MAP_NAME2 = "tmp-res-2";

  private final JetInstance jetInstance;

  @PostConstruct
  public void init() {
    List<String> sentences = Arrays.asList("a ab c ad a b a", "t a b a ds a ");
    List<String> textList = jetInstance.getList(LIST_NAME);
    textList.addAll(sentences);
  }

  @GetMapping("/add/{word}")
  @ResponseBody
  public void addWord(@PathVariable String word) {
    List<String> textList = jetInstance.getList(LIST_NAME);
    textList.add(word);
  }

  @GetMapping("/{word}")
  @ResponseBody
  public ResponseEntity<String> getSlowReport(@PathVariable String word) {

    countWord();

    Map<String, Long> counts1 = jetInstance.getMap(MAP_NAME1);
    Long countWord1 = counts1.get(word);

    Map<Integer, Long> counts2 = jetInstance.getMap(MAP_NAME2);
    Long countWord2 = counts2.get(word.length());

    return new ResponseEntity<>(countWord1 + "/" + countWord2, HttpStatus.OK);
  }


  public void countWord() {
    Pipeline p = Pipeline.create();
    BatchStage<String> s1 = createBasePipeLine(p);
    SinkStage s2 = createCountPipeLine(s1);
    SinkStage s3 = createLenPipeLine(s1);

    jetInstance.getMap(MAP_NAME1).clear();
    jetInstance.getMap(MAP_NAME2).clear();

    Job job = jetInstance.newJob(p);

    log.info("Job started");
    long startTime = System.currentTimeMillis();

    job.join();

    long stopTime = System.currentTimeMillis();
    long elapsedTime = stopTime - startTime;
    log.info("Job end " + elapsedTime);
  }

  private BatchStage<String> createBasePipeLine(Pipeline p) {
    return p.drawFrom(Sources.<String>files(LIST_NAME))
        .flatMap(
            word -> traverseArray(word.toLowerCase().split("\\W+")))
        .filter(word -> !word.isEmpty());
  }

  private SinkStage createCountPipeLine(BatchStage<String> s) {
    return s
        .groupingKey(wholeItem())
        .aggregate(counting())
        .drainTo(Sinks.map(MAP_NAME1));
  }

  private SinkStage createLenPipeLine(BatchStage<String> s) {
    return s
        .map(String::length)
        .groupingKey(wholeItem())
        .aggregate(counting())
        .drainTo(Sinks.map(MAP_NAME2));
  }
}
