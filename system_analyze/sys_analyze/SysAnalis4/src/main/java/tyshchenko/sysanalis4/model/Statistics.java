package tyshchenko.sysanalis4.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Statistics {

    double average;
    double dispersion;
}
