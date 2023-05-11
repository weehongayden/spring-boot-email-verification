package app.weehong.financeapi.dtos.requests;

public record UserRequestDto(
        String username,
        String name,
        String email,
        String password
) {
}
