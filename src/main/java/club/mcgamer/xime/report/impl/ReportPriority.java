package club.mcgamer.xime.report.impl;

public enum ReportPriority {

    LOW,
    MEDIUM,
    HIGH,
    CRITICAL;

    public String getName() {
        switch (this) {
            case LOW:
                return "Low";
            case MEDIUM:
                return "Medium";
            case HIGH:
                return "High";
            case CRITICAL:
                return "Critical";
        }
        return null;
    }

}
