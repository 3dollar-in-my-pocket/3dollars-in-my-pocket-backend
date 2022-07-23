FROM nginx:1.23

COPY conf/* /etc/nginx/

RUN mkdir -p /home/nginx

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
