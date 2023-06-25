package com.example.electronicstore.product;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.example.electronicstore.deal.None;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;

@WebMvcTest(AdminController.class)
class AdminControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    AdminService adminService;
    @Autowired
    ObjectMapper objectMapper;

    @Nested
    public class GetProduct {
        @Test
        void Given_ProductIsFound_When_getProduct_Then_returnOk() throws Exception {
            when(adminService.getProduct(1L)).thenReturn(new Product(1L, "keyboard", new BigDecimal("30.9"), new None()));
            mockMvc.perform(MockMvcRequestBuilders.get("/admin/product/{id}", 1L))
                    .andExpect(status().isOk())
                    .andExpect(content().json("""
                                                {
                                                  "id": 1,
                                                  "name": "keyboard",
                                                  "price": 30.9,
                                                  "deal": {
                                                      "dealType": "NONE"
                                                  }
                                                }
                            """.stripIndent(), true));
        }

        @Test
        void Given_ProductNotFound_When_getProduct_Then_returnNotFound() throws Exception {
            when(adminService.getProduct(1L)).thenThrow(ProductNotFoundException.class);
            mockMvc.perform(MockMvcRequestBuilders.get("/admin/product/{id}", 1L))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    public class CreateProduct {
        @Test
        void Given_validProduct_When_createProduct_Then_returnOk() throws Exception {
            when(adminService.createProduct(new Product(null, "keyboard", new BigDecimal("30.9"), new None())))
                    .thenReturn(new Product(1L, "keyboard", new BigDecimal("30.9"), new None()));
            mockMvc.perform(MockMvcRequestBuilders.post("/admin/product")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                                {
                                                  "id": null,
                                                  "name": "keyboard",
                                                  "price": 30.9,
                                                  "deal": {
                                                      "dealType": "NONE"
                                                  }
                                                }
                                    """.stripIndent()))
                    .andExpect(status().isOk())
                    .andExpect(content().json("""
                                                {
                                                  "id": 1,
                                                  "name": "keyboard",
                                                  "price": 30.9,
                                                  "deal": {
                                                      "dealType": "NONE"
                                                  }
                                                }
                            """, true));
        }

        @ParameterizedTest
        @CsvSource(useHeadersInDisplayName = true, textBlock = """
                name, price, field, validationMessage
                mouse, -1, price, must be greater than or equal to 0
                mouse, 100000001, price, must be less than or equal to 100000000
                Logitech mouse+HP keyboard+Intel i7 cpu+Ram DDR5 32G+hard disk HHD 500G, 300, name, size must be between 0 and 50
                """)
        void Given_invalidProduct_When_createProduct_Then_returnBadRequest(String name, BigDecimal price, String field, String validationMessage) throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.post("/admin/product")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                                {
                                                  "id": null,
                                                  "name": "%s",
                                                  "price": %s,
                                                  "deal": {
                                                      "dealType": "NONE"
                                                  }
                                                }
                                    """.stripIndent().formatted(name, price.toString())))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json("""
                                                {
                                                  "%s": "%s"
                                                }
                            """.stripIndent().formatted(field, validationMessage), true));
        }
    }

    @Nested
    public class UpdateProduct {
        @Test
        void Given_ValidProduct_When_updateProduct_Then_returnOk() throws Exception {
            when(adminService.updateProduct(new Product(null, "keyboard win", new BigDecimal("40.9"), new None()), 1L))
                    .thenReturn(new Product(1L, "keyboard win", new BigDecimal("40.9"), new None()));
            mockMvc.perform(MockMvcRequestBuilders.put("/admin/product/{id}", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                                {
                                                  "id": null,
                                                  "name": "keyboard win",
                                                  "price": 40.9,
                                                  "deal": {
                                                      "dealType": "NONE"
                                                  }
                                                }
                                    """))
                    .andExpect(status().isOk())
                    .andExpect(content().json("""
                                                {
                                                  "id": 1,
                                                  "name": "keyboard win",
                                                  "price": 40.9,
                                                  "deal": {
                                                      "dealType": "NONE"
                                                  }
                                                }
                            """, true));
        }

        @Test
        void Given_ProductNotExist_When_updateProduct_Then_returnNotFound() throws Exception {
            when(adminService.updateProduct(new Product(null, "keyboard win", new BigDecimal("40.9"), new None()), 1L))
                    .thenThrow(ProductNotFoundException.class);
            mockMvc.perform(MockMvcRequestBuilders.put("/admin/product/{id}", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                                {
                                                  "id": null,
                                                  "name": "keyboard win",
                                                  "price": 40.9,
                                                  "deal": {
                                                      "dealType": "NONE"
                                                  }
                                                }
                                    """))
                    .andExpect(status().isNotFound());
        }

        @ParameterizedTest
        @CsvSource(useHeadersInDisplayName = true, textBlock = """
                name, price, field, validationMessage
                mouse, -1, price, must be greater than or equal to 0
                mouse, 100000001, price, must be less than or equal to 100000000
                Logitech mouse+HP keyboard+Intel i7 cpu+Ram DDR5 32G+hard disk HHD 500G, 300, name, size must be between 0 and 50
                """)
        void Given_InvalidProduct_When_updateProduct_Then_returnOk(String name, BigDecimal price, String field, String validationMessage) throws Exception {
            when(adminService.updateProduct(new Product(null, "keyboard win", new BigDecimal("40.9"), new None()), 1L))
                    .thenReturn(new Product(1L, "keyboard win", new BigDecimal("40.9"), new None()));
            mockMvc.perform(MockMvcRequestBuilders.put("/admin/product/{id}", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                                {
                                                  "id": null,
                                                  "name": "%s",
                                                  "price": %s,
                                                  "deal": {
                                                      "dealType": "NONE"
                                                  }
                                                }
                                    """.stripIndent().formatted(name, price.toString())))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json("""
                                                {
                                                  "%s": "%s"
                                                }
                            """.stripIndent().formatted(field, validationMessage), true));
        }
    }

    @Nested
    public class DeleteProduct {
        @Test
        void Given_Product_When_deleteProduct_Then_returnOk() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.delete("/admin/product/{id}", 1L))
                    .andExpect(status().isOk());
        }
    }

}