package org.example.productservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", configuration = FeignClientConfig.class)
public interface UserServiceClient {
    @GetMapping("/api/users/{id}")
    UserDto getUserById(@PathVariable("id") Long id);
}