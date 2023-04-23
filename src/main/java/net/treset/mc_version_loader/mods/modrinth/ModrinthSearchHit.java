package net.treset.mc_version_loader.mods.modrinth;

import com.google.gson.JsonObject;
import net.treset.mc_version_loader.VersionLoader;
import net.treset.mc_version_loader.format.FormatUtils;
import net.treset.mc_version_loader.json.JsonUtils;
import net.treset.mc_version_loader.mods.GenericModData;
import net.treset.mc_version_loader.mods.ModVersionData;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ModrinthSearchHit extends GenericModData {
    private String author;
    private List<String> categories;
    private String clientSide;
    private int color;
    private String dateCreated;
    private String dateModified;
    private String description;
    private List<String> displayCategories;
    private int downloads;
    private String featuredGallery;
    private int follows;
    private List<String> gallery;
    private String iconUrl;
    private String latestVersion;
    private String license;
    private String projectId;
    private String projectType;
    private String serverSide;
    private String slug;
    private String name;
    private List<String> versions;

    public ModrinthSearchHit(String author, List<String> categories, String clientSide, int color, String dateCreated, String dateModified, String description, List<String> displayCategories, int downloads, String featuredGallery, int follows, List<String> gallery, String iconUrl, String latestVersion, String license, String projectId, String projectType, String serverSide, String slug, String name, List<String> versions) {
        this.author = author;
        this.categories = categories;
        this.clientSide = clientSide;
        this.color = color;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;
        this.description = description;
        this.displayCategories = displayCategories;
        this.downloads = downloads;
        this.featuredGallery = featuredGallery;
        this.follows = follows;
        this.gallery = gallery;
        this.iconUrl = iconUrl;
        this.latestVersion = latestVersion;
        this.license = license;
        this.projectId = projectId;
        this.projectType = projectType;
        this.serverSide = serverSide;
        this.slug = slug;
        this.name = name;
        this.versions = versions;
    }

    public static ModrinthSearchHit fromJson(JsonObject hitObj) {
        return new ModrinthSearchHit(
                JsonUtils.getAsString(hitObj, "author"),
                JsonUtils.parseJsonStringArray(JsonUtils.getAsJsonArray(hitObj, "categories")),
                JsonUtils.getAsString(hitObj, "client_side"),
                JsonUtils.getAsInt(hitObj, "color"),
                JsonUtils.getAsString(hitObj, "date_created"),
                JsonUtils.getAsString(hitObj, "date_modified"),
                JsonUtils.getAsString(hitObj, "description"),
                JsonUtils.parseJsonStringArray(JsonUtils.getAsJsonArray(hitObj, "display_categories")),
                JsonUtils.getAsInt(hitObj, "downloads"),
                JsonUtils.getAsString(hitObj, "featured_gallery"),
                JsonUtils.getAsInt(hitObj, "follows"),
                JsonUtils.parseJsonStringArray(JsonUtils.getAsJsonArray(hitObj, "gallery")),
                JsonUtils.getAsString(hitObj, "icon_url"),
                JsonUtils.getAsString(hitObj, "latest_version"),
                JsonUtils.getAsString(hitObj, "license"),
                JsonUtils.getAsString(hitObj, "project_id"),
                JsonUtils.getAsString(hitObj, "project_type"),
                JsonUtils.getAsString(hitObj, "server_side"),
                JsonUtils.getAsString(hitObj, "slug"),
                JsonUtils.getAsString(hitObj, "title"),
                JsonUtils.parseJsonStringArray(JsonUtils.getAsJsonArray(hitObj, "versions"))
        );
    }

    @Override
    public List<String> getAuthors() {
        return List.of(author);
    }

    @Override
    public List<String> getCategories() {
        return categories;
    }

    @Override
    public LocalDateTime getDateCreated() {
        return FormatUtils.parseLocalDateTime(dateCreated);
    }

    @Override
    public LocalDateTime getDateModified() {
        return FormatUtils.parseLocalDateTime(dateModified);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int getDownloadsCount() {
        return downloads;
    }

    @Override
    public String getIconUrl() {
        return iconUrl;
    }

    @Override
    public List<String> getGameVersions() {
        return versions;
    }

    @Override
    public List<String> getModLoaders() {
        if(categories == null) {
            return null;
        }
        List<String> out = new ArrayList<>();
        for(String c : categories) {
            if(out.size() >= 2) {
                break;
            }
            if(c != null && (c.equals("fabric") || c.equals("forge"))) {
                out.add(c);
            }
        }
        return out;
    }

    @Override
    public String getSlug() {
        return slug;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<ModVersionData> getVersions() {
        List<ModrinthVersion> modrinthVersions = VersionLoader.getModrinthVersion(projectId, this, List.of(), List.of());
        return new ArrayList<>(modrinthVersions);
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public String getClientSide() {
        return clientSide;
    }

    public void setClientSide(String clientSide) {
        this.clientSide = clientSide;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getDisplayCategories() {
        return displayCategories;
    }

    public void setDisplayCategories(List<String> displayCategories) {
        this.displayCategories = displayCategories;
    }

    public int getDownloads() {
        return downloads;
    }

    public void setDownloads(int downloads) {
        this.downloads = downloads;
    }

    public String getFeaturedGallery() {
        return featuredGallery;
    }

    public void setFeaturedGallery(String featuredGallery) {
        this.featuredGallery = featuredGallery;
    }

    public int getFollows() {
        return follows;
    }

    public void setFollows(int follows) {
        this.follows = follows;
    }

    public List<String> getGallery() {
        return gallery;
    }

    public void setGallery(List<String> gallery) {
        this.gallery = gallery;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public void setLatestVersion(String latestVersion) {
        this.latestVersion = latestVersion;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getServerSide() {
        return serverSide;
    }

    public void setServerSide(String serverSide) {
        this.serverSide = serverSide;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVersions(List<String> versions) {
        this.versions = versions;
    }
}