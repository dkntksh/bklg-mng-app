# イメージの指定
FROM node:14.7.0-alpine

# コンテナ内で cd /app しているようなもの
WORKDIR /app

ENV HOME="/app/frontend"

# コマンド実行
# linux 最新化,gitのインストール、npm最新化、vue-cli インストール　をしている。
RUN apk update && \
    apk add git && \
    npm install -g npm && \
    npm install -g vue-cli && \
    npm install -g create-nuxt-app@2.15.0 && \
    npm install --save nuxt

ENV HOST 0.0.0.0
EXPOSE 3000

CMD ["/bin/ash"]
ENTRYPOINT ["npm", "run", "dev"]