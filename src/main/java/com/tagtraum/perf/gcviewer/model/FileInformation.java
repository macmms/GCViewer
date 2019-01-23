package com.tagtraum.perf.gcviewer.model;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Contains information about a file.
 *
 * @author <a href="mailto:gcviewer@gmx.ch">Joerg Wuethrich</a>
 */
class FileInformation implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(FileInformation.class.getName());

    public long creationTime;
    public long lastModified;
    public long length;

    public FileInformation() {
        creationTime = 0;
        creationTime = 0;
        length = 0;
    }

    public FileInformation(File file) {
        if (file == null)
            throw new IllegalArgumentException("File must not  be null!");

        Optional<BasicFileAttributes> fileAttributes = getFileAttributes(file);
        this.lastModified = file.lastModified();
        this.creationTime = determineCreationDate(file, fileAttributes);
        this.length = file.length();
    }

    private Optional<BasicFileAttributes> getFileAttributes(File file) {
        try {
            return Optional.of(Files.readAttributes(file.toPath(), BasicFileAttributes.class));
        }
        catch (IOException ex) {
            logger.log(Level.WARNING, "Failed to read attributes of file " + file + ". Reason: " + ex.getMessage());
            logger.log(Level.FINER, "Details: ", ex);
        }
        return Optional.empty();
    }

    private long determineCreationDate(File file, Optional<BasicFileAttributes> fileAttributes) {
        if (fileAttributes.isPresent()) {
            return fileAttributes.get().creationTime().toMillis();
        }
        else {
            // Creation date is unavailable on unix based oS
            return file.lastModified();
        }
    }

    public void setFileInformation(FileInformation other) {
        this.creationTime = other.creationTime;
        this.lastModified = other.lastModified;
        this.length = other.length;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (!(other instanceof FileInformation)) {
            return false;
        }

        FileInformation fileInfo = (FileInformation) other;

        return fileInfo.lastModified == lastModified && fileInfo.creationTime == creationTime && fileInfo.length == length;
    }

    @Override
    public int hashCode() {
        int result = (int) (creationTime ^ (creationTime >>> 32));
        result = 31 * result + (int) (lastModified ^ (lastModified >>> 32));
        result = 31 * result + (int) (length ^ (length >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return FileInformation.class.toString() + "; lastModified=" + lastModified + ", length=" + length;
    }
}
