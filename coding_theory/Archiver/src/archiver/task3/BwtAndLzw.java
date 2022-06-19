package archiver.task3;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import archiver.api.Coding;
import archiver.api.EncoderFileReaderAndWriter;
import archiver.api.SourceFileReaderAndWriter;
import archiver.api.Utils;

public class BwtAndLzw implements Coding {

  private final Bwt bwt;
  private final Lzw lzw;
  private final EncoderFileReaderAndWriter encoderFile;

  public BwtAndLzw() {
    bwt = new Bwt();
    lzw = new Lzw();
    encoderFile = new EncoderFileReaderAndWriter();
  }

  @Override
  public Path decode(Path file) {
    Path path = Utils.createDecodedPath(file);
    try (FileWriter writer = new FileWriter(path.toFile())) {
      DecodedMetaInf metaInf = encoderFile.readWord(file.toFile());
      String code = lzw.decode(metaInf.getAlphabet(), metaInf.getCodes());
      String sourceWord = bwt.decode(code);
      writer.write(sourceWord);
      writer.flush();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    return path;
  }

  @Override
  public Path encode(Path file) {
    try (SourceFileReaderAndWriter sourceFile = new SourceFileReaderAndWriter(file.toFile())) {
      String sourceWord = sourceFile.readAllFile();
      // bwt part
      String bwtCode = bwt.encode(sourceWord);
      // lzw part
      Lzw.LzwResult lzwResult = lzw.encode(bwtCode);
      encoderFile.writeWord(file, lzwResult.getWord(), lzwResult.getSourceAlphabet());
    }
    return Utils.createEncodedPath(file);
  }
}
