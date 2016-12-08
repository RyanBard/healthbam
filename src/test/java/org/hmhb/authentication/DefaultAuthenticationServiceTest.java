package org.hmhb.authentication;

import java.io.IOException;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.services.plus.model.Person;
import org.hmhb.config.ConfigService;
import org.hmhb.config.PrivateConfig;
import org.hmhb.config.PublicConfig;
import org.hmhb.exception.authentication.ClientIdMismatchException;
import org.hmhb.exception.oauth.GoogleOauthException;
import org.hmhb.oauth.GoogleOauthService;
import org.hmhb.oauth.GoogleResponseData;
import org.hmhb.user.User;
import org.hmhb.user.UserService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.env.Environment;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link DefaultAuthenticationService}.
 */
public class DefaultAuthenticationServiceTest {

    private static final Long USER_ID = 123L;
    private static final String USER_EMAIL = "test-email";
    private static final String CODE = "test-code";
    private static final String CLIENT_ID = "test-client-id";
    private static final String CLIENT_SECRET = "test-client-secret";
    private static final String PREFIX = "test-prefix/views/oauth-callback";
    private static final String TOKEN = "test-token";

    private ConfigService configService;
    private GoogleOauthService googleOauthService;
    private JwtAuthenticationService jwtAuthService;
    private UserService userService;

    private DefaultAuthenticationService toTest;

    @Before
    public void setUp() throws Exception {
        configService = mock(ConfigService.class);
        googleOauthService = mock(GoogleOauthService.class);
        jwtAuthService = mock(JwtAuthenticationService.class);
        userService = mock(UserService.class);
        toTest = new DefaultAuthenticationService(
                configService,
                googleOauthService,
                jwtAuthService,
                userService
        );
    }

    private GoogleOauthAccessRequestInfo prepGoogleCall() throws Exception {
        GoogleTokenResponse googleTokenResponse = mock(GoogleTokenResponse.class);
        GoogleIdToken googleIdToken = mock(GoogleIdToken.class);
        GoogleIdToken.Payload payload = mock(GoogleIdToken.Payload.class);
        Person gPlusProfile = new Person();
        GoogleResponseData googleData = new GoogleResponseData(
                googleTokenResponse,
                gPlusProfile
        );

        Environment environment = mock(Environment.class);

        /* Train the config. */
        when(environment.getProperty("google.oauth.client.id")).thenReturn(CLIENT_ID);
        when(environment.getProperty("google.oauth.client.secret")).thenReturn(CLIENT_SECRET);
        when(environment.getProperty("hmhb.jwt.domain")).thenReturn("jwtDomain");
        when(environment.getProperty("hmhb.jwt.secret")).thenReturn("jwtSecret");

        PublicConfig publicConfig = new PublicConfig(environment);
        PrivateConfig privateConfig = new PrivateConfig(environment);

        /* Train the mocks. */
        when(configService.getPublicConfig()).thenReturn(publicConfig);
        when(configService.getPrivateConfig()).thenReturn(privateConfig);
        when(
                googleOauthService.getUserDataFromGoogle(
                        CLIENT_ID,
                        CLIENT_SECRET,
                        CODE,
                        PREFIX
                )
        ).thenReturn(
                googleData
        );
        when(googleTokenResponse.parseIdToken()).thenReturn(googleIdToken);
        when(googleIdToken.getPayload()).thenReturn(payload);
        when(payload.getEmail()).thenReturn(USER_EMAIL);

        GoogleOauthAccessRequestInfo input = new GoogleOauthAccessRequestInfo();
        input.setClientId(CLIENT_ID);
        input.setCode(CODE);
        input.setRedirectUri(PREFIX);
        return input;
    }

    @Test(expected = ClientIdMismatchException.class)
    public void testAuthenticateClientIdMismatch() throws Exception {
        GoogleOauthAccessRequestInfo input = prepGoogleCall();
        input.setClientId(CLIENT_ID + "-different");

        /* Make the call. */
        toTest.authenticate(input);

    }

    @Test(expected = GoogleOauthException.class)
    public void testAuthenticateTokenParseException() throws Exception {
        GoogleOauthAccessRequestInfo input = prepGoogleCall();

        GoogleTokenResponse googleTokenResponse = mock(GoogleTokenResponse.class);
        GoogleResponseData googleData = new GoogleResponseData(
                googleTokenResponse,
                new Person()
        );

        /* Train the mocks. */
        when(
                googleOauthService.getUserDataFromGoogle(
                        anyString(),
                        anyString(),
                        anyString(),
                        anyString()
                )
        ).thenReturn(
                googleData
        );
        when(googleTokenResponse.parseIdToken()).thenThrow(new IOException("test-io-exception"));

        /* Make the call. */
        toTest.authenticate(input);
    }

    @Test
    public void testAuthenticate() throws Exception {
        GoogleOauthAccessRequestInfo input = prepGoogleCall();

        TokenResponse expected = new TokenResponse(TOKEN);

        User user = new User();
        user.setId(USER_ID);
        user.setEmail(USER_EMAIL);
        user.setAdmin(true);

        /* Finish training the mocks. */
        when(userService.saveWithGoogleData(eq(USER_EMAIL), any(Person.class))).thenReturn(user);
        when(jwtAuthService.generateJwtToken(user)).thenReturn(TOKEN);

        /* Make the call. */
        TokenResponse actual = toTest.authenticate(input);

        /* Verify the results. */
        assertEquals(expected, actual);
    }

}
