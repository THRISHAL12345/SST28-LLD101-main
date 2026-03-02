package com.example.tickets;

import java.util.*;

public final class IncidentTicket {

    private final String id;
    private final String reporterEmail;
    private final String title;
    private final String description;
    private final String priority;
    private final List<String> tags;
    private final String assigneeEmail;
    private final Boolean customerVisible;
    private final Integer slaMinutes;
    private final String source;

    private IncidentTicket(Builder builder) {
        this.id = builder.id;
        this.reporterEmail = builder.reporterEmail;
        this.title = builder.title;
        this.description = builder.description;
        this.priority = builder.priority;
        this.tags = builder.tags == null
                ? Collections.emptyList()
                : Collections.unmodifiableList(new ArrayList<>(builder.tags));
        this.assigneeEmail = builder.assigneeEmail;
        this.customerVisible = builder.customerVisible;
        this.slaMinutes = builder.slaMinutes;
        this.source = builder.source;
    }

    public String getId() { return id; }
    public String getReporterEmail() { return reporterEmail; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getPriority() { return priority; }
    public List<String> getTags() { return tags; }
    public String getAssigneeEmail() { return assigneeEmail; }
    public Boolean getCustomerVisible() { return customerVisible; }
    public Integer getSlaMinutes() { return slaMinutes; }
    public String getSource() { return source; }

    public Builder toBuilder() {
        return new Builder()
                .id(this.id)
                .reporterEmail(this.reporterEmail)
                .title(this.title)
                .description(this.description)
                .priority(this.priority)
                .tags(this.tags)
                .assigneeEmail(this.assigneeEmail)
                .customerVisible(this.customerVisible)
                .slaMinutes(this.slaMinutes)
                .source(this.source);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String id;
        private String reporterEmail;
        private String title;
        private String description;
        private String priority;
        private List<String> tags;
        private String assigneeEmail;
        private Boolean customerVisible;
        private Integer slaMinutes;
        private String source;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder reporterEmail(String reporterEmail) {
            this.reporterEmail = reporterEmail;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder priority(String priority) {
            this.priority = priority;
            return this;
        }

        public Builder tags(List<String> tags) {
            this.tags = tags == null ? null : new ArrayList<>(tags);
            return this;
        }

        public Builder assigneeEmail(String assigneeEmail) {
            this.assigneeEmail = assigneeEmail;
            return this;
        }

        public Builder customerVisible(Boolean customerVisible) {
            this.customerVisible = customerVisible;
            return this;
        }

        public Builder slaMinutes(Integer slaMinutes) {
            this.slaMinutes = slaMinutes;
            return this;
        }

        public Builder source(String source) {
            this.source = source;
            return this;
        }

        public IncidentTicket build() {

            Validation.requireValidId(id);
            Validation.requireValidEmail(reporterEmail);
            Validation.requireNonEmpty(title, 80);

            if (assigneeEmail != null)
                Validation.requireValidEmail(assigneeEmail);

            if (priority != null)
                Validation.requireValidPriority(priority);

            if (slaMinutes != null)
                Validation.requireValidSLA(slaMinutes);

            return new IncidentTicket(this);
        }
    }
}