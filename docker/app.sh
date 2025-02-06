env

openssl rsa -in $AUTH_PRIVATE_KEY -passin pass:$AUTH_PRIVATE_KEY_PASSPHRASE -outform DER -out private.der
openssl rsa -in $AUTH_PRIVATE_KEY -passin pass:$AUTH_PRIVATE_KEY_PASSPHRASE -pubout -outform PEM -out public.pem

openssl pkcs12 -export -in /etc/letsencrypt/live/anselm-kuesters.de/cert.pem -inkey /etc/letsencrypt/live/anselm-kuesters.de/privkey.pem -certfile /etc/letsencrypt/live/anselm-kuesters.de/chain.pem -out keystore.p12 -passout pass:$SERVER_SSL_KEY-STORE-PASSWORD -name destrostudios

java -jar masterserver-1.0.0.jar