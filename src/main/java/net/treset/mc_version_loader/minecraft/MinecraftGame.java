package net.treset.mc_version_loader.minecraft;

import net.treset.mc_version_loader.exception.FileDownloadException;
import net.treset.mc_version_loader.json.SerializationException;
import net.treset.mc_version_loader.util.DownloadStatus;
import net.treset.mc_version_loader.util.FileUtil;
import net.treset.mc_version_loader.util.OsUtil;
import net.treset.mc_version_loader.util.Sources;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MinecraftGame {

    private static boolean cacheVersions = false;
    private static List<MinecraftVersion> versions = null;


    /**
     * Downloads a minecraft download..
     * @param download the download to download
     * @param targetFile the file to download to
     * @throws FileDownloadException if there is an error downloading the download
     */
    public static void downloadVersionDownload(MinecraftFileDownloads.Downloads download, File targetFile) throws FileDownloadException {
        if(download == null || download.getUrl() == null || download.getUrl().isBlank() || targetFile == null || !targetFile.getParentFile().isDirectory()) {
            throw new FileDownloadException("Unmet requirements for version download: download=" + download);
        }

        URL downloadUrl;
        try {
            downloadUrl = new URL(download.getUrl());
        } catch (MalformedURLException e) {
            throw new FileDownloadException("Unable to convert version download url: download=" + download.getUrl(), e);
        }

        FileUtil.downloadFile(downloadUrl, targetFile);
    }

    /**
     * Downloads a list of minecraft libraries.
     * @param libraries the libraries to download
     * @param librariesDir the directory to download the libraries to
     * @param features the features to check for
     * @param statusCallback the callback to report download status
     * @return a list of the downloaded libraries
     * @throws FileDownloadException if there is an error downloading the libraries
     */
    public static List<String> downloadVersionLibraries(List<MinecraftLibrary> libraries, File librariesDir, List<String> features, Consumer<DownloadStatus> statusCallback) throws FileDownloadException {
        return downloadVersionLibraries(libraries, librariesDir, null, features, statusCallback);
    }

    /**
     * Downloads a list of minecraft libraries.
     * @param libraries the libraries to download
     * @param librariesDir the directory to download the libraries to
     * @param localLibraryDir the directory to check for local libraries, null if none
     * @param features the features to check for
     * @param statusCallback the callback to report download status
     * @return a list of the downloaded libraries
     * @throws FileDownloadException if there is an error downloading the libraries
     */
    public static List<String> downloadVersionLibraries(List<MinecraftLibrary> libraries, File librariesDir, File localLibraryDir, List<String> features, Consumer<DownloadStatus> statusCallback) throws FileDownloadException {
        ArrayList<String> result = new ArrayList<>();
        List<Exception> exceptionQueue = new ArrayList<>();
        int size = libraries.size();
        int current = 0;
        boolean failed = false;
        for(MinecraftLibrary l : libraries) {
            statusCallback.accept(new DownloadStatus(++current, size, l.getName(), failed));
            try {
                addVersionLibrary(l, librariesDir, localLibraryDir, result, features);
            } catch (FileDownloadException e) {
                exceptionQueue.add(e);
                failed = true;
            }
        }
        if(!exceptionQueue.isEmpty()) {
            throw new FileDownloadException("Unable to download " + exceptionQueue.size() + " libraries", exceptionQueue.get(0));
        }
        return result;
    }

    public static void addVersionLibrary(MinecraftLibrary library, File librariesDir, File localLibraryDir, ArrayList<String> result, List<String> features) throws FileDownloadException {
        if(library == null || library.getRules() != null && !library.getRules().stream().allMatch(r -> r.isApplicable(features))) {
            return;
        }

        if(library.getDownloads().getArtifacts().getPath() == null || library.getDownloads().getArtifacts().getPath().isBlank() || librariesDir == null || !librariesDir.isDirectory()) {
            throw new FileDownloadException("Unmet requirements for library download: library=" + library.getName());
        }

        File outDir = new File(librariesDir, library.getDownloads().getArtifacts().getPath().substring(0, library.getDownloads().getArtifacts().getPath().lastIndexOf('/')));
        if (!outDir.isDirectory() && !outDir.mkdirs()) {
            throw new FileDownloadException("Unable to make required dirs: library=" + library.getName());
        }

        if (library.getNatives() != null) {
            List<String> applicableNatives = new ArrayList<>();
            if (OsUtil.isOsName("windows") && library.getNatives().getWindows() != null) {
                applicableNatives.add(library.getNatives().getWindows());
            } else if (OsUtil.isOsName("linux") && library.getNatives().getLinux() != null) {
                applicableNatives.add(library.getNatives().getLinux());
            } else if (OsUtil.isOsName("osx") && library.getNatives().getOsx() != null) {
                applicableNatives.add(library.getNatives().getOsx());
            }

            for (String n : applicableNatives) {
                for (MinecraftLibrary.Downloads.Classifiers.Native na : library.getDownloads().getClassifiers().getNatives()) {
                    if (n.equals(na.getName())) {
                        URL nativeUrl;
                        try {
                            nativeUrl = new URL(na.getArtifact().getUrl());
                        } catch (MalformedURLException e) {
                            throw new FileDownloadException("Unable to convert native download url: library=" + library.getName() + ", native=" + na.getName(), e);
                        }

                        File nativeOutDir = new File(librariesDir, na.getArtifact().getPath().substring(0, na.getArtifact().getPath().lastIndexOf('/')));
                        if (!nativeOutDir.isDirectory() && !nativeOutDir.mkdirs()) {
                            throw new FileDownloadException("Unable to make required native dirs: library=" + library.getName() + ", native=" + na.getName());
                        }
                        File outFile = new File(outDir, na.getArtifact().getPath().substring(na.getArtifact().getPath().lastIndexOf('/')));
                        FileUtil.downloadFile(nativeUrl, outFile);
                        result.add(na.getArtifact().getPath());
                    }
                }
            }
        }

        URL downloadUrl;
        File outFile = new File(outDir, library.getDownloads().getArtifacts().getPath().substring(library.getDownloads().getArtifacts().getPath().lastIndexOf('/')));
        try {
            downloadUrl = new URL(library.getDownloads().getArtifacts().getUrl());
            FileUtil.downloadFile(downloadUrl, outFile);
        } catch (MalformedURLException | FileDownloadException e) {
            if(localLibraryDir != null) {
                File localFile = new File(localLibraryDir, library.getDownloads().getArtifacts().getPath());
                if (localFile.isFile()) {
                    try {
                        Files.copy(localFile.toPath(), outFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException ex) {
                        throw new FileDownloadException("Unable to copy local library: library=" + library.getName(), ex);
                    }
                }
            }
        }

        result.add(library.getDownloads().getArtifact().getPath());
    }

    /**
     * Gets a list of all minecraft versions
     * @return a list of all minecraft versions
     * @throws FileDownloadException if there is an error downloading the version manifest
     */
    public static List<MinecraftVersion> getVersions() throws FileDownloadException {
        if(cacheVersions && versions != null) {
            return versions;
        }
        try {
            List<MinecraftVersion> v = MinecraftVersion.fromVersionManifest(FileUtil.getStringFromUrl(Sources.getVersionManifestUrl()));
            if(cacheVersions) {
                versions = v;
            }
            return v;
        } catch (SerializationException e) {
            throw new FileDownloadException("Unable to parse version manifest", e);
        }
    }

    /**
     * Gets a list of all minecraft release versions
     * @return a list of all minecraft release version
     * @throws FileDownloadException if there is an error downloading the version manifest
     */
    public static List<MinecraftVersion> getReleases() throws FileDownloadException {
        return getVersions().stream().filter(MinecraftVersion::isRelease).toList();
    }

    /**
     * Gets a specific minecraft version.
     * @param url the url of the version manifest
     * @return the minecraft version
     * @throws FileDownloadException if there is an error downloading the version manifest
     */
    public static MinecraftVersion getVersion(String url) throws FileDownloadException {
        try {
            return MinecraftVersion.fromJson(FileUtil.getStringFromUrl(url));
        } catch (SerializationException e) {
            throw new FileDownloadException("Unable to parse version manifest", e);
        }
    }

    /**
     * Gets details for a specific minecraft version.
     * @param url the url of the details manifest
     * @return the minecraft version
     * @throws FileDownloadException if there is an error downloading the version manifest
     */
    public static MinecraftVersionDetails getVersionDetails(String url) throws FileDownloadException {
        try {
            return MinecraftVersionDetails.fromJson(FileUtil.getStringFromUrl(url));
        } catch (SerializationException e) {
            throw new FileDownloadException("Unable to parse version manifest", e);
        }
    }

    /**
     * If set to true a list of minecraft versions will be cached when @code{getVersions} or @code{getReleases} is first called and reused on subsequent calls. Else the versions will be fetched every time. Default is false.
     * @param useCache if true the versions will be cached
     */
    public static void useVersionCache(boolean useCache) {
        cacheVersions = useCache;
    }
 }
