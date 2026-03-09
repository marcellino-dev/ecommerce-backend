package com.ecommerce.controller;

import com.ecommerce.dto.ProductRequest;
import com.ecommerce.entity.Product;
import com.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * POST /api/products  [ADMIN]
     * Cria um novo produto.
     */
    @PostMapping
    public ResponseEntity<Product> create(@Valid @RequestBody ProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(request));
    }

    /**
     * GET /api/products  [PÚBLICO]
     * Lista produtos ativos com paginação.
     * Parâmetros: page, size, sort (ex: ?page=0&size=10&sort=price,asc)
     * Filtro opcional por term: ?term=camiseta
     */
    @GetMapping
    public ResponseEntity<Page<Product>> list(
            @RequestParam(required = false) String term,
            @PageableDefault(size = 10, sort = "name") Pageable pageable) {

        if (term != null && !term.isBlank()) {
            return ResponseEntity.ok(productService.search(term, pageable));
        }
        return ResponseEntity.ok(productService.listActive(pageable));
    }

    /**
     * GET /api/products/{id}  [PÚBLICO]
     * Busca produto por ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    /**
     * GET /api/products/category/{category}  [PÚBLICO]
     * Lista produtos por categoria.
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> findByCategory(@PathVariable String category) {
        return ResponseEntity.ok(productService.findByCategory(category));
    }

    /**
     * PUT /api/products/{id}  [ADMIN]
     * Atualiza um produto existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> update(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.update(id, request));
    }

    /**
     * DELETE /api/products/{id}  [ADMIN]
     * Desativa (soft delete) o produto.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        productService.deactivate(id);
        return ResponseEntity.noContent().build();
    }
}
