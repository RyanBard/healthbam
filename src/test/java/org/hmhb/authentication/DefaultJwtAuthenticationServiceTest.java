package org.hmhb.authentication;

import javax.servlet.http.HttpServletRequest;

import io.jsonwebtoken.SignatureException;
import org.hmhb.config.ConfigService;
import org.hmhb.config.PrivateConfig;
import org.hmhb.config.PublicConfig;
import org.hmhb.exception.authentication.AuthHeaderHasTooManyPartsException;
import org.hmhb.exception.authentication.AuthHeaderMissingTokenException;
import org.hmhb.exception.authentication.AuthHeaderUnknownException;
import org.hmhb.user.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.env.Environment;

import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link DefaultJwtAuthenticationService}.
 */
public class DefaultJwtAuthenticationServiceTest {

    private static final String TEST_DOMAIN = "test-domain";
    private static final String TEST_SECRET = "test-secret";
    private static final Long USER_ID = 123L;
    private static final String USER_EMAIL = "john.doe@mailinator.com";

    private Environment environment;
    private HttpServletRequest request;
    private ConfigService configService;

    private DefaultJwtAuthenticationService toTest;

    @Before
    public void setUp() throws Exception {
        configService = mock(ConfigService.class);
        request = mock(HttpServletRequest.class);
        toTest = new DefaultJwtAuthenticationService(configService);

        environment = mock(Environment.class);

        /* Train the config. */
        when(environment.getProperty("google.oauth.client.id")).thenReturn("oauthClientId");
        when(environment.getProperty("google.oauth.client.secret")).thenReturn("oauthClientSecret");
        when(environment.getProperty("hmhb.jwt.domain")).thenReturn(TEST_DOMAIN);
        when(environment.getProperty("hmhb.jwt.secret")).thenReturn(TEST_SECRET);
    }

    @Test
    public void testJwtToken() throws Exception {
        User user = new User();
        user.setId(USER_ID);
        user.setEmail(USER_EMAIL);
        user.setAdmin(false);

        PublicConfig publicConfig = new PublicConfig(environment);
        PrivateConfig privateConfig = new PrivateConfig(environment);

        /* Train the config. */
        when(configService.getPublicConfig()).thenReturn(publicConfig);
        when(configService.getPrivateConfig()).thenReturn(privateConfig);

        /* Make the generate call. */
        String token = toTest.generateJwtToken(user);

        /* Train the mocks. */
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);

        /* Make the validate call. */
        toTest.validateAuthentication(request);

        /* Verify the results. */
        verify(request).setAttribute("loggedInUser", user);
    }

    @Test(expected = SignatureException.class)
    public void testJwtToken_Tampered() throws Exception {
        User user = new User();
        user.setId(USER_ID);
        user.setEmail(USER_EMAIL);
        user.setAdmin(false);

        PublicConfig publicConfig = new PublicConfig(environment);
        PrivateConfig privateConfig = new PrivateConfig(environment);

        /* Train the config. */
        when(configService.getPublicConfig()).thenReturn(publicConfig);
        when(configService.getPrivateConfig()).thenReturn(privateConfig);

        /* Make the generate call. */
        String token = toTest.generateJwtToken(user);

        /* Train the mocks. */
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token + "tampered");

        /* Make the validate call. */
        toTest.validateAuthentication(request);
    }

    @Test
    public void testJwtToken_NoAuthHeader() throws Exception {
        /* Train the mocks. */
        when(request.getHeader("Authorization")).thenReturn(null);

        /* Make the validate call. */
        toTest.validateAuthentication(request);

        /* Verify nothing was done. */
        verify(request, never()).setAttribute(anyString(), anyObject());
    }

    @Test(expected = AuthHeaderMissingTokenException.class)
    public void testJwtToken_AuthHeaderNotEnoughParts() throws Exception {
        User user = new User();
        user.setId(USER_ID);
        user.setEmail(USER_EMAIL);
        user.setAdmin(false);

        /* Train the mocks. */
        when(request.getHeader("Authorization")).thenReturn("Bearer");

        /* Make the validate call. */
        toTest.validateAuthentication(request);
    }

    @Test(expected = AuthHeaderHasTooManyPartsException.class)
    public void testJwtToken_AuthHeaderTooManyParts() throws Exception {
        User user = new User();
        user.setId(USER_ID);
        user.setEmail(USER_EMAIL);
        user.setAdmin(false);

        PublicConfig publicConfig = new PublicConfig(environment);
        PrivateConfig privateConfig = new PrivateConfig(environment);

        /* Train the config. */
        when(configService.getPublicConfig()).thenReturn(publicConfig);
        when(configService.getPrivateConfig()).thenReturn(privateConfig);

        /* Make the generate call. */
        String token = toTest.generateJwtToken(user);

        /* Train the mocks. */
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token + " " + token);

        /* Make the validate call. */
        toTest.validateAuthentication(request);
    }

    @Test(expected = AuthHeaderUnknownException.class)
    public void testJwtToken_UnknownAuthHeader() throws Exception {
        User user = new User();
        user.setId(USER_ID);
        user.setEmail(USER_EMAIL);
        user.setAdmin(false);

        PublicConfig publicConfig = new PublicConfig(environment);
        PrivateConfig privateConfig = new PrivateConfig(environment);

        /* Train the config. */
        when(configService.getPublicConfig()).thenReturn(publicConfig);
        when(configService.getPrivateConfig()).thenReturn(privateConfig);

        /* Make the generate call. */
        String token = toTest.generateJwtToken(user);

        /* Train the mocks. */
        when(request.getHeader("Authorization")).thenReturn("test-unknown-auth-header " + token);

        /* Make the validate call. */
        toTest.validateAuthentication(request);
    }

}
