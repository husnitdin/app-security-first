package uz.pdp.appsecurityfirst.cotroller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appsecurityfirst.entity.Product;
import uz.pdp.appsecurityfirst.repository.ProductRepository;

import java.util.Optional;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    ProductRepository productRepository;

    //director, manager
    // @PreAuthorize("hasAnyRole('MANAGER', 'DIRECTOR')")
     @PreAuthorize("hasAuthority('READ_ALL_PRODUCT')")
    @GetMapping
    public HttpEntity<?> getProduct(){
        return ResponseEntity.ok(productRepository.findAll());
    }

    //director
    // @PreAuthorize("hasRole('DIRECTOR')")
     @PreAuthorize("hasAuthority('ADD_PRODUCT')")
    @PostMapping
    public HttpEntity<?> addProduct(@RequestBody Product product){
        return ResponseEntity.ok(productRepository.save(product));
    }

    //director
//    @PreAuthorize("hasRole('DIRECTOR')")
     @PreAuthorize("hasAuthority('EDIT_PRODUCT')")
    @PutMapping("/{id}")
    public HttpEntity<?> editProduct(@PathVariable Integer id, @RequestBody Product product){
        Optional<Product> optionalProduct = productRepository.findById(id);
        if(optionalProduct.isPresent()){
            Product editingProduct = optionalProduct.get();
            editingProduct.setName(product.getName());
            productRepository.save(editingProduct);
            return ResponseEntity.ok(editingProduct);
        }
        return ResponseEntity.status(404).build();
    }

    //director
//    @PreAuthorize("hasRole('DIRECTOR')")
    @PreAuthorize("hasAuthority('DELETE_PRODUCT')")
    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteProduct(@PathVariable Integer id){
       productRepository.deleteById(id);
            return ResponseEntity.ok().build();
    }

    //director, manager, user
//    @PreAuthorize("hasAnyRole('DIRECTOR', 'MANAGER', 'USER')")
    @PreAuthorize("hasAuthority('READ_ONE_PRODUCT')")
    @GetMapping("/{id}")
    public HttpEntity<?> getProduct(@PathVariable Integer id){
        Optional<Product> optionalProduct = productRepository.findById(id);
        return ResponseEntity.status(optionalProduct.isPresent()? 200:400).body(optionalProduct.orElse(null));
    }
}
