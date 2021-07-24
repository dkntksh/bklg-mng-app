# アプリケーションの目的と概要
このアプリケーションは、クライアントPCから、CSVアップロードをすることで、Backlogの課題の登録を行うことを目的としています。

下記の通り、BacklogにはCSVから課題を登録する機能が提供されていません。
[CSV から課題を追加することができますか？](https://support-ja.backlog.com/hc/ja/articles/360036149513-CSV-%E3%81%8B%E3%82%89%E8%AA%B2%E9%A1%8C%E3%82%92%E8%BF%BD%E5%8A%A0%E3%81%99%E3%82%8B%E3%81%93%E3%81%A8%E3%81%8C%E3%81%A7%E3%81%8D%E3%81%BE%E3%81%99%E3%81%8B-)

Googleスプレッドシートでの登録方法もありますが、IP制限がかかっている場合、使用できません。

そこで、本アプリケーションを使用し、CSVファイルをアップロードすることで、指定したプロジェクトに対して課題を登録することが可能となります。


ローカルPCから課題を登録するという最低限の機能を目的とするため、本アプリケーションは、認証機能を持ちません。
PublicなWebに公開したり、マルチユーザを想定する場合、別途認証機能との連携が必要となります。


# 技術スタック
* 開発言語
  * Java 11
  * SpringBoot 2.5.0
  * Nuxt.js

* DB  
  * MySQL5.7

* 開発環境  
  * Docker、Docker-Compose

# 環境構築
・Docker、Docker-Composeのインストール

・git clone

・backend\src\main\resources\security.propertiesのkeyとinit_vectorを設定する
security.keyに半角英数32桁の文字列を設定する
security.init_vectorに半角英数の16桁の文字列を設定する

'
# 初回起動
git clone https://nulab-exam.backlog.jp/git/IIDUKA/app.git
docker-compose up --build

# 2回目以降の起動
docker-compose up -d

# 終了
docker-compose down
'

# DBのテーブルが作成されていない場合、下記の対応を行う
## doker-compose でdbコンテナに入る
docker-compose exec db sh

## mysqlにコマンドラインから接続する
mysql -u root -prootpassword

## db\mysql\initdb\init_db.sqlのDDLを全て実行する

# 起動後、front画面のURL
http://localhost







