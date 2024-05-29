# masterserver
Our masterserver, offering account management and the according APIs for the launcher and apps.

## Authentication
The private auth key can be generated via `openssl genrsa -aes256 -passout stdin -out private.pem 2048`.