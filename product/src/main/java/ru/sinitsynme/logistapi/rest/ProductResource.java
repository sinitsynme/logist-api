package ru.sinitsynme.logistapi.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Управление товарами")
@RestController
@RequestMapping("/product")
public class ProductResource {
}
