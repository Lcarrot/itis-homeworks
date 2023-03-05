package com.example.generator;

import com.example.form.SourceData;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Generator {

    public static SourceData generateSourceData() {
        var source = new SourceData();
        var data = ThreadLocalRandom.current().nextInt(10, 100);
        var sourceData = new ArrayList<String>();
        source.setData(sourceData);
        for (int k = 0; k < data; k++) {
            StringBuilder builder = new StringBuilder();
            int size = ThreadLocalRandom.current().nextInt(17, 32);
            for (int i = 0; i < size; i++) {
                builder.append((char) ThreadLocalRandom.current().nextInt('A', 'Z'));
                sourceData.add(builder.toString());
            }
        }
        return source;
    }
}
