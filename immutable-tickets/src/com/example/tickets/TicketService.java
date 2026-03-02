package com.example.tickets;

import java.util.List;

public class TicketService {

    public IncidentTicket createTicket(String id, String email, String title) {
        return IncidentTicket.builder()
                .id(id)
                .reporterEmail(email)
                .title(title)
                .build();
    }

    public IncidentTicket assign(IncidentTicket ticket, String assignee) {
        return ticket.toBuilder()
                .assigneeEmail(assignee)
                .build();
    }

    public IncidentTicket addTags(IncidentTicket ticket, List<String> tags) {
        return ticket.toBuilder()
                .tags(tags)
                .build();
    }
}