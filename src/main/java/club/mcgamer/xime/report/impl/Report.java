package club.mcgamer.xime.report.impl;

import lombok.AllArgsConstructor;

import java.util.Date;

@AllArgsConstructor
public class Report {

    private final String reportedDisplayName;
    private final String reportedName;
    private final String reportedUUID;
    private final String reportedReason;
    private final String reportDescription;
    private final String reportedDateTime;

    private final String reporterDisplayName;
    private final String reporterName;
    private final String reporterUUID;

}
