package org.abigtomato.nebula.flink.wc;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

/**
 * @author abigtomato
 */
public class StreamWorkCount {

    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        ParameterTool parameterTool = ParameterTool.fromArgs(args);

        // nc -l -p 9999
        DataStream<Tuple2<String, Integer>> dataStream = env.socketTextStream(parameterTool.get("host"), parameterTool.getInt("port"))
                .flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
                    @Override
                    public void flatMap(String value, Collector<Tuple2<String, Integer>> collector) throws Exception {
                        for (String elem : value.split(" ")) {
                            collector.collect(Tuple2.of(elem, 1));
                        }
                    }
                }).keyBy(value -> value.f0).window(TumblingProcessingTimeWindows.of(Time.seconds(5))).sum(1);

        dataStream.print();

        try {
            env.execute("Window WordCount");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
