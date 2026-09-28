import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;

/** Shared controller: commit a complete action, save it, then refresh all screens. */
public class FarmController_Kilpatrick implements AutoCloseable {
    private FarmData_Kilpatrick data;
    private final Path dataFile;
    private final FileChannel channel;
    private final FileLock lock;
    private final List<Runnable> listeners = new ArrayList<>();
    public FarmController_Kilpatrick(Path file) throws IOException {
        dataFile = file.toAbsolutePath();
        Files.createDirectories(dataFile.getParent());
        channel = FileChannel.open(dataFile.resolveSibling(dataFile.getFileName()+".lock"), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        FileLock acquired = null;
        try {
            try { acquired = channel.tryLock(); }
            catch (OverlappingFileLockException ex) { throw new IOException("This data file is already open in another app window."); }
            if (acquired == null) throw new IOException("This data file is already open in another app window.");
            lock = acquired;
            if (Files.exists(dataFile)) data = DataStore_Kilpatrick.load(dataFile);
            else {
                data = FarmData_Kilpatrick.demo();
                DataStore_Kilpatrick.save(dataFile, data);
            }
        } catch (IOException | RuntimeException ex) {
            if (acquired != null) acquired.release();
            channel.close();
            throw ex;
        }
    }
    public FarmData_Kilpatrick data() { return data; }
    public Path getDataFile() { return dataFile; }
    public void onChange(Runnable listener) { listeners.add(listener); }
    public void refresh() { for (Runnable listener : listeners) listener.run(); }
    public void commit(Consumer<FarmData_Kilpatrick> action) {
        Properties before = DataStore_Kilpatrick.encode(data);
        try {
            action.accept(data);
            DataStore_Kilpatrick.save(dataFile, data);
        } catch (IOException ex) {
            data = DataStore_Kilpatrick.decode(before);
            throw new IllegalArgumentException("Nothing was saved; this action was rolled back. Check the data folder. " + ex.getMessage(), ex);
        } catch (RuntimeException ex) {
            data = DataStore_Kilpatrick.decode(before);
            throw ex;
        } finally { refresh(); }
    }
    @Override public void close() throws IOException { lock.release(); channel.close(); }
}
