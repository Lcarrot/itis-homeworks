package archiver.api;

import java.nio.file.Path;

public interface Decoding {

  Path decode(Path file);
}
