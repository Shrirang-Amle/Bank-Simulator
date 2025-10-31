package api;

import dto.Customerdto;
import model.Customer;
import service.CustomerService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {

    private final CustomerService service;

    // Dependency Injection for better testability
    public CustomerResource() {
        this.service = new CustomerService(null);
    }

    // Constructor for testing
    public CustomerResource(CustomerService customerService) {
        this.service = customerService;
    }

    @POST
    public Response createCustomer(Customerdto request) {
        try {
            long id = service.createCustomerAndReturnId(request);
            // Returning a proper JSON object is better than a raw string
            return Response.status(Response.Status.CREATED)
                    .entity("{\"message\":\"Customer added successfully\",\"customerId\":" + id + "}")
                    .build();
        } catch (java.sql.SQLIntegrityConstraintViolationException dup) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("{\"message\":\"Aadhar number or email already exists\"}")
                    .build();
        } catch (SQLException | IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"message\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getCustomer(@PathParam("id") long id) {
        try {
            Customer customer = service.getCustomer(id);
            if (customer != null) {
                return Response.ok(customer).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("{\"message\":\"Customer not found\"}")
                        .build();
            }
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"message\":\"Database error\"}")
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateCustomer(@PathParam("id") long id, Customerdto request) {
        try {
            String result = service.updateCustomer(id, request);
            return Response.ok("{\"message\":\"" + result + "\"}").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"message\":\"" + e.getMessage() + "\"}")
                    .build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"message\":\"Database error\"}")
                    .build();
        }
    }

    @GET
    public Response getAllCustomers() {
        try {
            List<Customer> customers = service.getAllCustomers();
            return Response.ok(customers).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"message\":\"Database error\"}")
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCustomer(@PathParam("id") long id) {
        try {
            boolean deleted = service.deleteCustomer(id);
            if (!deleted) {
                return Response.status(Response.Status.NOT_FOUND).entity("{\"message\":\"Customer not found\"}")
                        .build();
            }
            return Response.ok("{\"message\":\"Customer deleted\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\":\"Error deleting customer\"}").build();
        }
    }
}
