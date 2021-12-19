package org.abigtomato.nebula.flink.wc;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

/**
 * @author abigtomato
 */
public class WorkCount {

    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        int parallelism = ExecutionEnvironment.getDefaultLocalParallelism();
        System.out.println("parallelism = " + parallelism);

        DataSource<String> dataSource = env.readTextFile("D:\\WorkSpace\\javaproject\\Nebula\\nebula-base-flink\\src\\main\\resources\\wc.csv");

        dataSource.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public void flatMap(String value, Collector<Tuple2<String, Integer>> collector) throws Exception {
                for (String elem : value.split(" ")) {
                    collector.collect(Tuple2.of(elem, 1));
                }
            }
        }).groupBy(0).sum(1).print();
    }
}
