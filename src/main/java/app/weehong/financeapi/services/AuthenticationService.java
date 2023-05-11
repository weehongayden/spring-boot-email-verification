package app.weehong.financeapi.services;

import app.weehong.financeapi.dtos.requests.UserRequestDto;

public interface AuthenticationService {

    void register(UserRequestDto userRequestDto);

    boolean verify(String token);

    void createVerificationToken(UserRequestDto userRequestDto, String token);
}
