package app.weehong.financeapi.events;

import app.weehong.financeapi.dtos.requests.UserRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;

@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent {

    private UserRequestDto user;
    private String applicationUrl;

    public RegistrationCompleteEvent(UserRequestDto user, String applicationUrl) {
        super(user);
        this.user = user;
        this.applicationUrl = applicationUrl;
    }
}
