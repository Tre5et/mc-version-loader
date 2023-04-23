package net.treset.mc_version_loader.minecraft;

import com.google.gson.JsonObject;
import net.treset.mc_version_loader.json.JsonUtils;

public class MinecraftAssetIndex {
    private String id;
    private String cha1;
    private int size;
    private int totalSize;
    private String url;

    public MinecraftAssetIndex(String id, String cha1, int size, int totalSize, String url) {
        this.id = id;
        this.cha1 = cha1;
        this.size = size;
        this.totalSize = totalSize;
        this.url = url;
    }

    public static MinecraftAssetIndex fromJson(JsonObject assetIndexObj) {
        return new MinecraftAssetIndex(
                JsonUtils.getAsString(assetIndexObj, "id"),
                JsonUtils.getAsString(assetIndexObj, "sha1"),
                JsonUtils.getAsInt(assetIndexObj, "size"),
                JsonUtils.getAsInt(assetIndexObj, "totalSize"),
                JsonUtils.getAsString(assetIndexObj, "url")
        );
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCha1() {
        return cha1;
    }

    public void setCha1(String cha1) {
        this.cha1 = cha1;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
