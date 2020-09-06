package com.market.server.controller;

import com.market.server.aop.LoginCheck;
import com.market.server.dto.ProductDTO;
import com.market.server.dto.UserDTO;
import com.market.server.service.Impl.ProductServiceImpl;
import com.market.server.service.Impl.UserServiceImpl;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;

@Api(tags = {"3. products"})
@RestController
@RequestMapping("/products")
@Log4j2
public class ProductController {

    private final ProductServiceImpl productService;
    private final UserServiceImpl userService;
    private ProductResponse productResponse;

    public ProductController(ProductServiceImpl productService, UserServiceImpl userService) {
        this.productService = productService;
        this.userService = userService;
    }

    /**
     * 중고물품 등록 메서드.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @LoginCheck(type = LoginCheck.UserType.USER)
    public void registerProduct(String accountId, @RequestBody ProductDTO productDTO) {
        productService.register(accountId, productDTO);
    }

    /**
     * 본인 중고물품 검색 메서드.
     */
    @GetMapping("my-products")
    @LoginCheck(type = LoginCheck.UserType.USER)
    public ProductResponse myProductInfo(String accountId) {
        UserDTO memberInfo = userService.getUserInfo(accountId);
        List<ProductDTO> productDTOList = productService.getMyProducts(memberInfo.getAccountId());
        return new ProductResponse(productDTOList);
    }

    /**
     * 본인 중고물품 수정 메서드.
     */
    @PatchMapping("{productId}")
    @LoginCheck(type = LoginCheck.UserType.USER)
    public void updateProducts(String accountId,
                               @PathVariable(name = "productId") int productId,
                               @RequestBody ProductRequest productRequest) {
        UserDTO memberInfo = userService.getUserInfo(accountId);
        ProductDTO productDTO = ProductDTO.builder()
                .id(productId)
                .price(productRequest.getPrice())
                .accountId(memberInfo.getAccountId())
                .title(productRequest.getTitle())
                .contents(productRequest.getContents())
                .status(productRequest.getStatus())
                .istrade(productRequest.isTrade())
                .updatetime(new Date())
                .deliveryprice(productRequest.getDeliveryprice())
                .dibcount(productRequest.getDibcount())
                .categoryId(productRequest.getCategoryId())
                .build();
        productService.updateProducts(productDTO);
    }

    /**
     * 본인 중고물품 삭제 메서드.
     */
    @DeleteMapping("{productId}")
    @LoginCheck(type = LoginCheck.UserType.USER)
    public void deleteProducts(String accountId,
                               @PathVariable(name = "productId") int productId,
                               @RequestBody ProductDeleteRequest productDeleteRequest) {
        UserDTO memberInfo = userService.getUserInfo(accountId);
        productService.deleteProduct(memberInfo.getAccountId(), productId);
    }

    // -------------- response 객체 --------------

    @Getter
    @AllArgsConstructor
    private static class ProductResponse {
        private List<ProductDTO> productDTO;
    }

    // -------------- request 객체 --------------

    @Setter
    @Getter
    private static class ProductRequest {
        private long price;
        private String title;
        private String contents;
        private ProductDTO.Status status;
        private boolean trade;
        private Date updatetime;
        private long deliveryprice;
        private int dibcount;
        private int categoryId;
        private int fileId;
    }

    @Setter
    @Getter
    private static class ProductDeleteRequest {
        private int id;
        private int accountId;
    }
}
