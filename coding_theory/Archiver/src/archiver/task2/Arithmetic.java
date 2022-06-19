package archiver.task2;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import archiver.api.Coding;
import archiver.api.Utils;

public class Arithmetic implements Coding {

  private final long mult = 1_000_000_000_000_000L;
  private final long start_position = mult / 10;

  @Override
  public Path encode(Path file) {
    Path path = Utils.createEncodedPath(file);
    Utils.MetaInfo info = Utils.readFileAndPutInMap(file);
    Map<Character, DoubleInterval> intervalMap = getIntervalsMap(info.getMap(),
        info.getTotalCount()
    );
    try (BufferedReader reader = Files.newBufferedReader(file);
         DataOutputStream outputStream = new DataOutputStream(
             new FileOutputStream(path.toFile()))) {
      Utils.writeAlphabet(info.getMap(), outputStream);
      outputStream.writeLong(info.getTotalCount());
      Interval interval = codeFile(intervalMap, outputStream, reader);
      List<Byte> byteList = divideBytes(interval.getLeftBoard() + (interval.getRightBoard() - interval.getLeftBoard()) / 2);
      for (Byte symb : byteList) {
        outputStream.writeByte(symb);
      }
      outputStream.flush();
    }
    catch (IOException e) {
      //ignored
    }
    return path;
  }

  private Interval codeFile(Map<Character, DoubleInterval> intervalMap,
      DataOutputStream outputStream, BufferedReader reader)
      throws IOException {
    Interval currentInterval = new Interval(0, mult - 1);
    char symbol = (char) reader.read();
    while (symbol != (char) -1) {
        if (intervalMap.containsKey(symbol)) {
          DoubleInterval interval = intervalMap.get(symbol);
          currentInterval.calculateNewBoards(interval);
          while (currentInterval.hasEqualPart(start_position)) {
            byte sot = currentInterval.getEqualPart(start_position);
            outputStream.writeByte(sot);
          }
          symbol = (char) reader.read();
        }
      }
    return currentInterval;
  }

  private Map<Character, DoubleInterval> getIntervalsMap(Map<Character, Integer> map,
      long totalCount) {
    long current_left_board = 0;
    Map<Character, DoubleInterval> intervalMap = new HashMap<>();
    for (Map.Entry<Character, Integer> entry : map.entrySet()) {
      intervalMap.put(entry.getKey(), new DoubleInterval((double) current_left_board / totalCount,
          (double) (current_left_board + entry.getValue()) / totalCount
      ));
      current_left_board += entry.getValue();
    }
    return intervalMap;
  }

  public List<Byte> divideBytes(long current_part) {
    List<Byte> list = new LinkedList<>();
    byte delimiter = 10;
    for (int i = 0; i < 15; i++) {
      byte new_byte = (byte) (current_part % delimiter);
      current_part /= 10;
      list.add(0, new_byte);
    }
    return list;
  }


  @Override
  public Path decode(Path file) {
    Path path = Utils.createDecodedPath(file);
    try (DataInputStream inputStream = new DataInputStream(new FileInputStream(file.toFile()));
         BufferedWriter writer = Files.newBufferedWriter(path)) {
      Map<Character, Integer> alphabet = Utils.readAlphabetByCharacterKey(inputStream);
      long total_count = inputStream.readLong();
      Set<Map.Entry<Character, DoubleInterval>> intervalSet = getIntervalsMap(alphabet, total_count)
          .entrySet();
      long value = readStartValue(inputStream);
      Interval currentInterval = new Interval(0, mult - 1);
      char character;
      for (long i = 0; i < total_count; i++) {
        character = findSymbol(intervalSet, currentInterval, value);
        writer.write(character);
        while (currentInterval.hasEqualPart(start_position)) {
          currentInterval.add(start_position);
          value %= start_position;
          value = value * 10 + inputStream.readByte();
        }
      }
    }
    catch (IOException e) {
     //ignored
    }
    return path;
  }

  private char findSymbol(Set<Map.Entry<Character, DoubleInterval>> intervalSet, Interval currentInterval, long current_value) {
    Map.Entry<Character, DoubleInterval> needed = null;
    double index = ((double)current_value - currentInterval.getLeftBoard()) / (currentInterval.getRightBoard() - currentInterval.getLeftBoard());
    Iterator<Map.Entry<Character, DoubleInterval>> iterator = intervalSet.iterator();
    double right_board = -1;
    while (Double.compare(index, right_board) > 0) {
        needed = iterator.next();
        right_board = needed.getValue().getRightBoard();
    }
    if (needed == null) throw new RuntimeException();
    currentInterval.calculateNewBoards(needed.getValue());
    return needed.getKey();
  }

  private long readStartValue(DataInputStream inputStream) {
    long value = 0;
    for (int i = 0; i < 15; i++) {
      try {
        value += inputStream.readByte();
      }
      catch (IOException e) {
        e.printStackTrace();
      }
      value *= 10;
    }
    return value / 10;
  }
}
