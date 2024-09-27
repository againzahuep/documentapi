package com.example.documentapi.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

public class JwtConfig {
	public static final String LLAVE_SECRETA ="alguna.clave.secreta.12345678";

	public static final String RSA_PRIVADA = "-----BEGIN RSA PRIVATE KEY-----\r\n" +
			"MIIEowIBAAKCAQEAp2OM8ZkYFV2PAVjFJFZt71Q0gfc1zbeEB6nIVW7K/xYh1LuQ\r\n" +
			"M2D5uTpySmnt82+EZVdZ+fCLwCk0x3zGMg0xqd3e5gQZXnnwzawJARvuBkPM5AuB\r\n" +
			"ZYTT8OOZDyJTZ/HYYv2VaZqG/HCkHhHGB00sZ51dXczn0A+3ROrx9/77pOjHfp8R\r\n" +
			"zUnQAwfS2WlbcclaS1KQQvRq8xDI76jEX2e8dKgduXz4EzXcHpQUTAAMFSAKtnjD\r\n" +
			"SDXz8Y4vAtRHt23EsP039ijQrgEZI4IQgeQUSA3kI8vWi0EVMdLj1eQnDS291UB+\r\n" +
			"LJypmVllz/cBquQpIYLvBTZhXfHP9TX8fr4gMwIDAQABAoIBAAUFeX7t5skNS3Lw\r\n" +
			"5MkYRqf9UK3tapHHRZCtSTM25aQNV+p3zfWcnSEvXapZzyeUWtZnbm2PB8I1+j1Z\r\n" +
			"wuBRgQHlhABGczRgWBbzaDm+Sr+pXQYiSjlvaKQ7T6Yik3n0yVb+4HKQjigYgPPd\r\n" +
			"+KDTcdeR4vEjanjVX9wTaDrWTVo/DQAFh9jdV8Z5it+ZeQnvWfufqAWnsgb060Hp\r\n" +
			"ZsyWTEbSzIctFAxLWy3urLH9+zeEpAz+XzqAJ1R5xF7MEmjvmo/cEOVca42eoD+w\r\n" +
			"cD3zu1sqRC5sA7Oym8dyZoigm2EeJVSKCZeEYGhNA+Agse4RAFxiohyrX6YnF8KG\r\n" +
			"jBBeV5ECgYEA1XXWyk7FhfKupQswRQyB5+Fpe3qkuN/OerWsZ/mSXy1NvN7jp1y3\r\n" +
			"QTWmQgOKCN5GoHp0uC2XxELWGTtVFdiv40vbVi1KMBEn2vR8bjpzpzqv+FXjYYoW\r\n" +
			"spQY2CWqyyu/WSi2lcxWCmho6QM4xPrB6E2vq2xPLUtcTfrssaqmUFUCgYEAyL9F\r\n" +
			"0oLKACBSwMxa3CMUafP74MljyjXFyx+YyP9XudhFXkC81ZzKMpI5yNIjj2OpSsU0\r\n" +
			"tt9SKd8EBQnZJT0DvYalTKJqW5gjOKx1Xbxf2UXtwxaKr+YKa7lPAziKFynkTvgl\r\n" +
			"GvT745hnetP19gNZaH4D5jHXRp/mQGZS2fG6lmcCgYEAqiaUYA8KRAt+SvDwORoP\r\n" +
			"MtbrThIk23kLDaVjGD5H/rYOEU9aCD5PlRs+u8A2d3s2sRTrlSRCpZ7hKpuw5wki\r\n" +
			"qvv8imTvjgEe09mule7h4ZtWSY2tAkMgdJi4dRMRYYIttNRPNnj63LD9bz+yMNHx\r\n" +
			"X0sNjId5VZGMslwh8D0m9UECgYB34xsszCUdDDC/qYt9uol/eVFsfnlVEuYMF9Oj\r\n" +
			"/jKC77EFx/2+SMPMjd1qnGzWPnulBr8jk507GlAJPkdrYNPyBQ6FBN4l02b9eqkh\r\n" +
			"ENXhKU0EXFr5D5YbnXCdiM+XY9JQgwuN3M7trl/mT92tTXTA3bkwaYWaTPYycal7\r\n" +
			"NUzscwKBgClrQsbVYnTAarLd5CfaJPBkKagK/E/7c01necxfgPr2yXxhJFUcSCOf\r\n" +
			"p2rQ7zO8CfLcPKf/h3Jdv9NQ0SNTC3nkVUDxwe7LVxU/9uJCiShMuXlHBXbi/DN9\r\n" +
			"Q/LBpffJReC64dIhsCp/X4vFr6RPxaEzcBv+lxsd/HMLiBQrnbO1\r\n" +
			"-----END RSA PRIVATE KEY-----";

	public static final String RSA_PUBLICA = "-----BEGIN PUBLIC KEY-----\r\n" +
			"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAp2OM8ZkYFV2PAVjFJFZt\r\n" +
			"71Q0gfc1zbeEB6nIVW7K/xYh1LuQM2D5uTpySmnt82+EZVdZ+fCLwCk0x3zGMg0x\r\n" +
			"qd3e5gQZXnnwzawJARvuBkPM5AuBZYTT8OOZDyJTZ/HYYv2VaZqG/HCkHhHGB00s\r\n" +
			"Z51dXczn0A+3ROrx9/77pOjHfp8RzUnQAwfS2WlbcclaS1KQQvRq8xDI76jEX2e8\r\n" +
			"dKgduXz4EzXcHpQUTAAMFSAKtnjDSDXz8Y4vAtRHt23EsP039ijQrgEZI4IQgeQU\r\n" +
			"SA3kI8vWi0EVMdLj1eQnDS291UB+LJypmVllz/cBquQpIYLvBTZhXfHP9TX8fr4g\r\n" +
			"MwIDAQAB\r\n" +
			"-----END PUBLIC KEY-----";

}
