package com.heygongc.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heygongc.auth.application.DeviceProvider;
import com.heygongc.auth.application.TokenProvider;
import com.heygongc.auth.application.UserProvider;
import com.heygongc.device.domain.repository.DeviceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;


//@Import(TestWebConfig.class)
abstract public class ControllerTest {

    protected final static String REFRESH_TOKEN = "refreshToken";
    protected final static String ACCESS_TOKEN = "Bearer aaaaa.bbbbb.ccccc";

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

//    @Autowired
//    protected LoginUserArgumentResolver loginUserArgumentResolver;

//    @Autowired
//    protected DeviceArgumentResolver deviceArgumentResolver;

//    @MockBean
//    protected JwtUtil jwtUtil;

//    @MockBean
//    protected UserRepository userRepository;

    @MockBean
    protected DeviceProvider deviceProvider;

    @MockBean
    protected UserProvider userProvider;

    @MockBean
    protected DeviceRepository deviceRepository;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setUp() {
//        mockMvc = MockMvcBuilders.webAppContextSetup(context)
//                .build();

//        given(jwtUtil.extractSubject(any())).willReturn("1");
//        given(jwtUtil.extractAudience(any())).willReturn("1");;
//        doNothing().when(jwtUtil).checkedValidTokenOrThrowException(any());
//        when(userRepository.findById(any())).thenReturn(Optional.of(mockUser()));
    }

}
