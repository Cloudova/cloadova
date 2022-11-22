package com.cloudova.service.jwt;

import com.cloudova.service.jwt.models.JwtKey;
import com.cloudova.service.jwt.services.RSAKeyService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/.well-known")
public class JwtController {

    private final RSAKeyService rsaKeyService;

    @Autowired
    public JwtController(RSAKeyService rsaKeyService) {
        this.rsaKeyService = rsaKeyService;
    }

    @GetMapping("/jwks")
    public Map<String, ArrayList<JwtKey>> get() {
        Map<String, ArrayList<JwtKey>> map = new HashMap<>();
        ArrayList<JwtKey> keys = new ArrayList<>();
        PublicKey publicKey = this.rsaKeyService.getPublicKey();
        keys.add(new JwtKey("RSA", "sig", null, "RSA256", Base64.encodeBase64String(publicKey.getEncoded())));
        map.put("keys", keys);
        return map;
    }

}
