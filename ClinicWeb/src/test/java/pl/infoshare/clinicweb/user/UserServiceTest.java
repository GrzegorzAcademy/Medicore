package pl.infoshare.clinicweb.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import pl.infoshare.clinicweb.user.mapper.UserMapper;
import pl.infoshare.clinicweb.user.registration.UserDto;
import pl.infoshare.clinicweb.user.repository.UserRepository;
import pl.infoshare.clinicweb.user.service.UserService;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static pl.infoshare.clinicweb.utlis.Data.createUser;
import static pl.infoshare.clinicweb.utlis.Data.createUserDTO;


@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    private static final String EMAIL = "marek.kozlowski@wp.pl";
    private static final String PASSWORD = "Puszek423";

    Map<Long, UserDto> users = new HashMap<>();

    @Mock
    UserRepository userRepository;
    @Mock
    UserMapper userMapper;

    @InjectMocks
    UserService service;

    @Test
    public void shouldCorecttlySaveUserWhenHaveData() {
        // given
        var user = createUser(EMAIL, PASSWORD);
        var userDTO = createUserDTO(EMAIL, PASSWORD);

        when(userMapper.toEntity(userDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        // when
        users.put(1L, service.saveUser(userDTO));

        // then
        assertThat(users.get(1L)).isEqualTo(user);
    }

//    @Test
//    public void testFindUserByEmail() {
//
//        var user = Data.createUser();
//
//        when(userRepository.findUserByEmail("marek.kozlowski@wp.pl")).thenReturn(Optional.ofNullable(user));
//
//        var savedUser = service.findUserByEmail(user.getEmail());
//
//        assertThat(savedUser).isNotNull();
//    }
//
//    @Test
//    public void testSuccessUserRemovalById() {
//
//        var user = Data.createUser();
//
//        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
//
//        Assertions.assertAll(() -> service.deleteUserById(1L));
//    }
}