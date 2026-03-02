import com.example.tickets.*;

import java.util.Arrays;

public class TryIt {

    public static void main(String[] args) {

        TicketService service = new TicketService();

        IncidentTicket ticket = service.createTicket(
                "INC-101",
                "user@email.com",
                "Login failure"
        );

        IncidentTicket assigned = service.assign(ticket, "agent@email.com");

        IncidentTicket tagged = service.addTags(assigned,
                Arrays.asList("login", "urgent"));

        System.out.println(ticket.getAssigneeEmail());
        System.out.println(assigned.getAssigneeEmail()); 
        System.out.println(tagged.getTags());
    }
}