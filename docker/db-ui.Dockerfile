FROM phpmyadmin:latest
RUN a2enmod ssl
RUN sed -ri -e 's,80,443,' /etc/apache2/sites-available/000-default.conf && \
    sed -i -e '/^<\/VirtualHost>/i SSLEngine on' /etc/apache2/sites-available/000-default.conf && \
    sed -i -e '/^<\/VirtualHost>/i SSLCertificateFile /etc/letsencrypt/live/anselm-kuesters.de/cert.pem' /etc/apache2/sites-available/000-default.conf && \
    sed -i -e '/^<\/VirtualHost>/i SSLCertificateKeyFile /etc/letsencrypt/live/anselm-kuesters.de/privkey.pem' /etc/apache2/sites-available/000-default.conf && \
    sed -i -e '/^<\/VirtualHost>/i SSLCertificateChainFile /etc/letsencrypt/live/anselm-kuesters.de/fullchain.pem' /etc/apache2/sites-available/000-default.conf
EXPOSE 443