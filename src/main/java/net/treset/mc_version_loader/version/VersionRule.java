package net.treset.mc_version_loader.version;

import net.treset.mc_version_loader.OsDetails;

import java.util.List;
import java.util.Objects;

public class VersionRule {
    private String action;
    private boolean allow;
    private String osName;
    private String osArch;
    private String osVersion;
    private VersionFeature feature;
    private boolean featureState;

    public VersionRule(String action, String osName, String osArch, String osVersion, VersionFeature feature, boolean featureState) {
        this.action = action;
        this.allow = Objects.equals(action, "allow");
        this.osName = osName;
        this.osArch = osArch;
        this.osVersion = osVersion;
        this.feature = feature;
        this.featureState = featureState;
    }

    public boolean isApplicable(List<VersionFeature> activeFeatures) {
        if(!isAllow()) {
            return false;
        }

        if(getFeature() != null && getFeature() != VersionFeature.NONE && activeFeatures.contains(getFeature()) != getFeatureState()) {
            return false;
        }

        if(getOsName() != null && !OsDetails.isOsName(getOsName())) {
            return false;
        }
        if(getOsVersion() != null && !OsDetails.isOsVersion(getOsVersion())) {
            return false;
        }
        if(getOsArch() != null && !OsDetails.isOsArch(getOsArch())) {
            return false;
        }
        return true;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public boolean isAllow() {
        return allow;
    }

    public void setAllow(boolean allow) {
        this.allow = allow;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getOsArch() {
        return osArch;
    }

    public void setOsArch(String osArch) {
        this.osArch = osArch;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public VersionFeature getFeature() {
        return feature;
    }

    public void setFeature(VersionFeature feature) {
        this.feature = feature;
    }

    public boolean getFeatureState() {
        return featureState;
    }

    public void setFeatureState(boolean featureState) {
        this.featureState = featureState;
    }
}
