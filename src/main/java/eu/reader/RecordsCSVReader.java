package eu.reader;

import com.csvreader.CsvReader;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class RecordsCSVReader<T> {

    private final String filename;
    private Function<CsvReader, T> recordReader;

    public RecordsCSVReader(String filename, Function<CsvReader, T> recordReader) {
        this.filename = filename;
        this.recordReader = recordReader;
    }

    private static Runnable asUncheckedRunnable(CsvReader c) {
        return () -> {
            c.close();
        };
    }

    public Stream<T> products() throws IOException {
        CsvReader csvReader = new CsvReader(filename);

        csvReader.readHeaders();

        Iterator<T> iter = new Iterator<T>() {
            T nextRecord = null;

            @Override
            public boolean hasNext() {
                if (nextRecord != null) {
                    return true;
                } else {
                    try {


                        if (!csvReader.readRecord()) {
                            nextRecord = null;
                            return false;
                        }

                        nextRecord = recordReader.apply(csvReader);

                        return (nextRecord != null);
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                }
            }

            @Override
            public T next() {
                if (nextRecord != null || hasNext()) {
                    T record = nextRecord;
                    nextRecord = null;
                    return record;
                } else {
                    throw new NoSuchElementException();
                }
            }
        };
        return StreamSupport
                .stream(Spliterators.spliteratorUnknownSize(iter,
                        Spliterator.ORDERED | Spliterator.NONNULL), false)
                .onClose(asUncheckedRunnable(csvReader));
    }

}
