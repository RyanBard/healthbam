package org.hmhb.user;

import javax.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;

import org.hmhb.exception.user.UserIdMismatchException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link UserController}.
 */
public class UserControllerTest {

    private static final Long USER_ID = 123L;

    private UserService service;
    private UserController toTest;

    @Before
    public void setUp() throws Exception {
        service = mock(UserService.class);
        toTest = new UserController(service);
    }

    @Test
    public void testGetAll() throws Exception {
        List<User> expected = Collections.singletonList(new User());

        /* Train the mocks. */
        when(service.getAll()).thenReturn(expected);

        /* Make the call. */
        List<User> actual = toTest.getAll();

        /* Verify the results. */
        assertEquals(expected, actual);
    }

    @Test
    public void testGetAllAsCsv() throws Exception {
        String jwtToken = "test-jwt-token";
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter writer = mock(PrintWriter.class);

        String expected = "test-csv";

        /* Train the mocks. */
        when(service.getAllAsCsv(jwtToken)).thenReturn(expected);
        when(response.getWriter()).thenReturn(writer);

        /* Make the call. */
        toTest.getAllAsCsv(jwtToken, response);

        /* Verify the results. */
        verify(response).setStatus(200);
        verify(response).setHeader("Content-Type", "text/csv");
        verify(response).setHeader("Content-Disposition", "attachment; filename=all-users.csv");
        verify(writer).append(expected);
    }

    @Test
    public void testGetById() throws Exception {
        User expected = new User();
        expected.setId(USER_ID);

        /* Train the mocks. */
        when(service.getById(USER_ID)).thenReturn(expected);

        /* Make the call. */
        User actual = toTest.getById(USER_ID);

        /* Verify the results. */
        assertEquals(expected, actual);
    }

    @Test
    public void testCreate() throws Exception {
        User input = new User();
        input.setId(null);

        User expected = new User();
        expected.setId(USER_ID);

        /* Train the mocks. */
        when(service.save(input)).thenReturn(expected);

        /* Make the call. */
        User actual = toTest.create(input);

        /* Verify the results. */
        assertEquals(expected, actual);
   }

    @Test
    public void testUpdate() throws Exception {
        User input = new User();
        input.setId(USER_ID);

        User expected = new User();
        expected.setId(USER_ID);

        /* Train the mocks. */
        when(service.save(input)).thenReturn(expected);

        /* Make the call. */
        User actual = toTest.update(USER_ID, input);

        /* Verify the results. */
        assertEquals(expected, actual);
    }

    @Test(expected = UserIdMismatchException.class)
    public void testUpdate_IdMismatch() throws Exception {
        User input = new User();
        input.setId(USER_ID);

        /* Make the call. */
        toTest.update(USER_ID + 1, input);
    }

    @Test
    public void testDelete() throws Exception {
        User expected = new User();
        expected.setId(USER_ID);

        /* Train the mocks. */
        when(service.delete(USER_ID)).thenReturn(expected);

        /* Make the call. */
        User actual = toTest.delete(USER_ID);

        /* Verify the results. */
        assertEquals(expected, actual);
    }

}
