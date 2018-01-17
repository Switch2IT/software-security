package be.ehb.switch2it.rest.responses;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author Guillaume Vandecasteele
 * @since 2018
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SystemStatus {

    private String version;
    private String buildDate;
    private String startUpDate;
    private String upTime;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBuildDate() {
        return buildDate;
    }

    public void setBuildDate(String buildDate) {
        this.buildDate = buildDate;
    }

    public String getStartUpDate() {
        return startUpDate;
    }

    public void setStartUpDate(String startUpDate) {
        this.startUpDate = startUpDate;
    }

    public String getUpTime() {
        return upTime;
    }

    public void setUpTime(String upTime) {
        this.upTime = upTime;
    }

    @Override
    public String toString() {
        return "SystemStatus{" +
                "version='" + version + '\'' +
                ", buildDate='" + buildDate + '\'' +
                ", startUpDate='" + startUpDate + '\'' +
                ", upTime='" + upTime + '\'' +
                '}';
    }
}