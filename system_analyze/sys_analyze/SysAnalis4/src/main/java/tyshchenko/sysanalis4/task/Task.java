package tyshchenko.sysanalis4.task;

import tyshchenko.sysanalis4.model.Statistics;

import java.util.List;

public interface Task {

    List<Statistics> execute();
}
