package com.soroosh.auth.grpc;

import com.soroosh.auth.BaseTest;
import com.soroosh.auth.grpc.dto.User;
import org.junit.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserGrpcTest extends BaseTest {
    @Test
    public void testUserProtoBuf(){
        String firstName = this.faker.name().firstName();
        String lastName = this.faker.name().lastName();
        String phone = this.faker.phoneNumber().cellPhone();
        String id = UUID.randomUUID().toString();
        User user = User.newBuilder().
                setId(id)
                .setFirstName(firstName)
                .setLastName(lastName)
                .setMobile(phone)
                .build();

        assertEquals(user.getId(), id);
        assertEquals(user.getFirstName(), firstName);
        assertEquals(user.getLastName(), lastName);
        assertEquals(user.getMobile(), phone);
    }
}
