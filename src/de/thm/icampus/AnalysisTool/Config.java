package de.thm.icampus.AnalysisTool;

public class Config {
    private String phpcpdPath = "";
    private String phplocPath = "";
    private String phpmdPath = "";
    private String qmPath = "";
    private String outputFormatValue = "";

    public String getPhpcpdPath() {
        return phpcpdPath;
    }
    public void setPhpcpdPath(String phpcpdPath) {
        this.phpcpdPath = phpcpdPath;
    }
    public String getPhplocPath() {
        return phplocPath;
    }
    public void setPhplocPath(String phplocPath) {
        this.phplocPath = phplocPath;
    }
    public String getPhpmdPath() {
        return phpmdPath;
    }
    public void setPhpmdPath(String phpmdPath) {
        this.phpmdPath = phpmdPath;
    }
    public String getQmPath() {
        return qmPath;
    }
    public void setQmPath(String qmPath) {
        this.qmPath = qmPath;
    }
    public String getOutputFormatValue() {
        return outputFormatValue;
    }
    public void setOutputFormatValue(String outputFormatValue) {
        this.outputFormatValue = outputFormatValue;
    }
}
