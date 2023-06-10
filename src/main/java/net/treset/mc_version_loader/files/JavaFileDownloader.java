package net.treset.mc_version_loader.files;

import net.treset.mc_version_loader.exception.FileDownloadException;
import net.treset.mc_version_loader.java.JavaFile;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class JavaFileDownloader {

    public static void downloadJavaFile(JavaFile file, File baseDir) throws FileDownloadException {
        if(file == null || file.getType() == null || file.getType().isBlank() || file.getName() == null || file.getName().isBlank() || (file.isFile() && (file.getRaw() == null || file.getRaw().getUrl() == null || file.getRaw().getUrl().isBlank())) || baseDir == null || !baseDir.isDirectory()) {
            throw new FileDownloadException("Unmet requirements for java file download: file=" + file);
        }


        File outDir = null;
        if(file.isDir()) {
            outDir = new File(baseDir, file.getName());
        } else if(file.isFile()) {
            outDir = new File(baseDir, file.getName().substring(0, file.getName().lastIndexOf('/') == -1 ? file.getName().length() : file.getName().lastIndexOf('/')));
        }

        if (outDir == null || (!outDir.isDirectory() && !outDir.mkdirs())) {
            throw new FileDownloadException("Unable to make required dirs for file download: file=" + file.getName());
        }

        if(file.isDir()) {
            return;
        }
        if(file.isFile()) {
            URL downloadUrl;
            try {
                downloadUrl = new URL(file.getRaw().getUrl());
            } catch (MalformedURLException e) {
                throw new FileDownloadException("Unable to convert download url: file=" + file.getName(), e);
            }

            File outFile = new File(outDir, file.getName().substring(file.getName().lastIndexOf('/') == -1 ? 0 : file.getName().lastIndexOf('/')));
            FileUtils.downloadFile(downloadUrl, outFile);
            return;
        }
        throw new FileDownloadException("Unable to determine file type: file=" + file.getName());
    }
}
