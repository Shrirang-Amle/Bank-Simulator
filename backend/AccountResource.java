package api;

import model.Account;
import service.AccountService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccountResource {
    private AccountService accountService = new AccountService();

    @POST
    @Path("/create")
    public Response createAccount(Account account) {
        try {
            Account created = accountService.createAccount(account);
            return Response.status(Response.Status.CREATED).entity(created).build();

            // --- THIS BLOCK IS NEW ---
            // Handles validation errors (like "account exists" or "customer not found")
            // by returning a 400 Bad Request, which is more accurate.
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error creating account: " + e.getMessage()).build();
            // -------------------------

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error creating account: " + e.getMessage()).build();
        }
    }

    @PUT
    @Path("/update/{accountNumber}")
    public Response updateAccount(@PathParam("accountNumber") String accountNumber, Account account) {
        Account updated = accountService.updateAccount(accountNumber, account);
        if (updated == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Account not found").build();
        }
        return Response.ok(updated).build();
    }

    @DELETE
    @Path("/delete/{accountNumber}")
    public Response deleteAccountByNumber(@PathParam("accountNumber") String accountNumber) {
        boolean deleted = accountService.deleteAccountByNumber(accountNumber);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND).entity("Account not found").build();
        }
        return Response.ok("Account deleted").build();
    }

    @GET
    @Path("/get/{accountNumber}")
    public Response getAccountByNumber(@PathParam("accountNumber") String accountNumber) {
        Account account = accountService.getAccountByNumber(accountNumber);
        if (account == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Account not found").build();
        }
        return Response.ok(account).build();
    }

    @GET
    @Path("/getAll")
    public Response getAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        return Response.ok(accounts).build();
    }
}