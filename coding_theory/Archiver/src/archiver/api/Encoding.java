package archiver.api;

import java.nio.file.Path;

public interface Encoding {

  Path encode(Path file);
}
