package net.treset.mc_version_loader.exception;

public class FileDownloadException extends Exception {
    public FileDownloadException(String message) {
        super(message);
    }
    public FileDownloadException(String message, Exception parent) {
        super(message, parent);
    }
}
