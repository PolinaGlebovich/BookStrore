package com.example.market.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.apache.kafka.common.message.CreateTopicsRequestData;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "BookMarket",
                description = "Online book market API for managing books",
                version = "1.0.0"
        )
)
public class OpenApiConfig {

    @Bean
    public OperationCustomizer userOperationCustomizer() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            String methodName = handlerMethod.getMethod().getName();
            switch (methodName) {
                case "findUserById" -> {
                    operation.summary("Get user by ID")
                            .description("Retrieve user by his unique ID");
                    operation.responses(createUserResponses());
                }
                case "updateUser" -> {
                    operation.summary("Update user by ID")
                            .description("Update an existing user by its unique ID");
                    operation.responses(createUpdateUserResponses());
                }
                case "deleteUser" -> {
                    operation.summary("Delete user by ID")
                            .description("Delete user by its unique ID");
                    operation.responses(createDeleteUserResponses());
                }
                case "getAllUsers" -> {
                    operation.summary("Get all users")
                            .description("Retrieve a list of all users");
                    operation.responses(createGetAllUsersResponses());
                }
            }
            return operation;
        };
    }

    private ApiResponses createUserResponses() {
        return new ApiResponses()
                .addApiResponse("200", new ApiResponse().description("User found"))
                .addApiResponse("404", new ApiResponse().description("User not found"))
                .addApiResponse("500", new ApiResponse().description("Internal server error"));
    }

    private ApiResponses createUpdateUserResponses() {
        return new ApiResponses()
                .addApiResponse("200", new ApiResponse().description("User successfully updated"))
                .addApiResponse("400", new ApiResponse().description("Invalid input data"))
                .addApiResponse("500", new ApiResponse().description("Internal server error"));
    }

    private ApiResponses createDeleteUserResponses() {
        return new ApiResponses()
                .addApiResponse("204", new ApiResponse().description("Book successfully deleted"))
                .addApiResponse("400", new ApiResponse().description("Invalid input data"))
                .addApiResponse("500", new ApiResponse().description("Internal server error"));
    }

    private ApiResponses createGetAllUsersResponses() {
        return new ApiResponses()
                .addApiResponse("200", new ApiResponse().description("List of users retrieved successfully"))
                .addApiResponse("500", new ApiResponse().description("Internal server error"));
    }

    @Bean
    public OperationCustomizer bookOperationCustomizer() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            String methodName = handlerMethod.getMethod().getName();
            switch (methodName) {
                case "getBookById" -> {
                    operation.summary("Get a book by ID")
                            .description("Retrieve a book by its unique ID");
                    operation.responses(createBookResponses());
                }
                case "saveBook" -> {
                    operation.summary("Create a new book")
                            .description("Create a new book with an optional image file");
                    operation.responses(createSaveBookResponses());
                }
                case "updateBook" -> {
                    operation.summary("Update a book by ID")
                            .description("Update an existing book by its unique ID");
                    operation.responses(createUpdateBookResponses());
                }
                case "deleteBook" -> {
                    operation.summary("Delete a book by ID")
                            .description("Delete a book by its unique ID");
                    operation.responses(createDeleteBookResponses());
                }
                case "getAllBooks" -> {
                    operation.summary("Get all books")
                            .description("Retrieve a list of all books");
                    operation.responses(createGetAllBooksResponses());
                }
                case "searchBooks" -> {
                    operation.summary("Search books")
                            .description("Search books by title, author, or genre with pagination");
                    operation.responses(createSearchBooksResponses());
                }
                case "getBookByImageId" -> {
                    operation.summary("Get a book by image ID")
                            .description("Retrieve a book by its associated image ID");
                    operation.responses(createGetBookByImageIdResponses());
                }
            }
            return operation;
        };
    }

    private ApiResponses createBookResponses() {
        return new ApiResponses()
                .addApiResponse("200", new ApiResponse().description("Book found"))
                .addApiResponse("404", new ApiResponse().description("Book not found"))
                .addApiResponse("500", new ApiResponse().description("Internal server error"));
    }

    private ApiResponses createSaveBookResponses() {
        return new ApiResponses()
                .addApiResponse("200", new ApiResponse().description("Book successfully created"))
                .addApiResponse("400", new ApiResponse().description("Invalid input data"))
                .addApiResponse("500", new ApiResponse().description("Internal server error"));
    }

    private ApiResponses createUpdateBookResponses() {
        return new ApiResponses()
                .addApiResponse("200", new ApiResponse().description("Book successfully updated"))
                .addApiResponse("400", new ApiResponse().description("Invalid input data"))
                .addApiResponse("500", new ApiResponse().description("Internal server error"));
    }

    private ApiResponses createDeleteBookResponses() {
        return new ApiResponses()
                .addApiResponse("204", new ApiResponse().description("Book successfully deleted"))
                .addApiResponse("400", new ApiResponse().description("Invalid input data"))
                .addApiResponse("500", new ApiResponse().description("Internal server error"));
    }

    private ApiResponses createGetAllBooksResponses() {
        return new ApiResponses()
                .addApiResponse("200", new ApiResponse().description("List of books retrieved successfully"))
                .addApiResponse("500", new ApiResponse().description("Internal server error"));
    }

    private ApiResponses createSearchBooksResponses() {
        return new ApiResponses()
                .addApiResponse("200", new ApiResponse().description("Books found successfully"))
                .addApiResponse("500", new ApiResponse().description("Internal server error"));
    }

    private ApiResponses createGetBookByImageIdResponses() {
        return new ApiResponses()
                .addApiResponse("200", new ApiResponse().description("Book found by image ID"))
                .addApiResponse("404", new ApiResponse().description("Book not found"))
                .addApiResponse("500", new ApiResponse().description("Internal server error"));
    }
}