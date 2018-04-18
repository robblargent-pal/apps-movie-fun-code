package org.superbiz.moviefun.blobstore;

import org.apache.tika.Tika;
import org.apache.tika.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

import static java.lang.String.format;

public class FileStore implements BlobStore {

    private final Tika tika = new Tika();

    @Override
    public void put(Blob blob) throws IOException {
        File targetFile = getCoverFile(blob.name);
        targetFile.delete();
        targetFile.getParentFile().mkdirs();
        targetFile.createNewFile();

        try (FileOutputStream outputStream = new FileOutputStream(targetFile)) {
            IOUtils.copy(blob.inputStream, outputStream);
        }
    }

    @Override
    public Optional<Blob> get(String name) throws IOException {
        File file = new File(name);

        if (!file.exists()) {
            return Optional.empty();
        }

        return Optional.of(new Blob(name, new FileInputStream(file), tika.detect(file)));
    }

    @Override
    public void deleteAll() {

    }

    private File getCoverFile(String name) {
        String coverFileName = format("covers/%s", name);
        return new File(coverFileName);
    }

}
