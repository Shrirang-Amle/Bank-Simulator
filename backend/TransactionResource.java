package api;

import model.Transaction;
import service.TransactionService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.List;

@Path("/transactions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TransactionResource {

    private final TransactionService transactionService;

    public TransactionResource() {
        this(new TransactionService());
    }

    public TransactionResource(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    public static class TransferRequest {
        public String senderAccountNumber;
        public String receiverAccountNumber;
        public BigDecimal amount;
        public String pin;
        public String description;
    }

    @POST
    @Path("/transfer")
    public Response transfer(TransferRequest req) {
        try {
            // --- FIX 1: Pass the pin and description from the request ---
            Transaction t = transactionService.transfer(
                    req.senderAccountNumber,
                    req.receiverAccountNumber,
                    req.amount,
                    req.pin, // <-- Was null
                    req.description // <-- Was null
            );
            return Response.status(Response.Status.CREATED).entity(t).build();

            // --- FIX 2: Add a catch block for SecurityException to return 401 ---
        } catch (SecurityException e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage()).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/get/{accountNumber}")
    public Response getByAccount(@PathParam("accountNumber") String accountNumber) {
        try {
            List<Transaction> list = transactionService.getTransactionsForAccount(accountNumber);
            return Response.ok(list).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }
}