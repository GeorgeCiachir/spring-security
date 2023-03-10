## Create the symmetric key

For the `jwt-symmetric-key-token-store` profile you need a symmetric (and private) key for signing the JWT:
- You can use: `openssl enc -aes-256-cbc -k secret -P -md sha1`. Just add `winpty` in front, in case you run it in GitBash on Windows
- You can use an external tool, such as: `https://www.allkeysgenerator.com/Random/Security-Encryption-Key-Generator.aspx`

- The `NimbusJwtDecoder` class only accept keys with a minimum 256 bits length



## Create a pair of private-public keys

1. Create private key
   `keytool -genkeypair -alias chapter_13_14_15_key -keyalg RSA -keypass the_key_passowrd -keystore chapter_13_14_15_key.jks -storepass the_store_password`

2. Create public key from the previously created private key
   `keytool -list -rfc --keystore chapter_13_14_15_key.jks | openssl x509 -inform pem -pubkey`