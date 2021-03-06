package com.chensq.bayesclassifier.train.clzdoc;

import com.chensq.bayesclassifier.Main;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;

/**
 * Created by chensq on 16-11-9.
 */
public class ClassDocRunTool extends Configured implements Tool {

    @Override
    public int run(String[] strings) throws Exception {

        Configuration conf=new Configuration();
        Job job=Job.getInstance(conf);
        job.setJobName("class-doc-train");
        job.setJarByClass(Main.class);

        job.setInputFormatClass(MyCDCombineFileInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        job.setMapperClass(TrainCDMapper.class);
        job.setCombinerClass(TrainCDReducer.class);
        job.setReducerClass(TrainCDReducer.class);



        for(int i=1;i<strings.length;i++){
            FileInputFormat.addInputPath(job,new Path(strings[i]));
        }

        FileOutputFormat.setOutputPath(job,new Path(strings[0]));


        return job.waitForCompletion(true)?0:1;
    }
}
