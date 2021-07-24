<template>
<v-container fluid fill-height>
    <v-layout align-center justify-center>
      　 <v-flex xs12 sm8 md6>
          <div class="fill-height">
                <h3>スペース情報の更新/削除</h3>
                <div>
                  <p v-if="errors.length">
                    <b class="pink--text">エラーがあります:</b>
                    <ul>
                      <li v-for="error in errors" :key="error" class="pink--text">{{ error }}</li>
                    </ul>
                  </p>
                  <v-col cols="12" md="10">
                    <label>スペースID：
                      <input v-model="spaceId" placeholder="abcd1234" type="text" outlined>
                    </label>
                  </v-col>
                  <v-col cols="12" md="10">
                    <label>ドメイン：
                      <input v-model="domain" placeholder="xxxx.baclog.com" type="text" outlined>
                    </label>
                  </v-col>
                  <v-col cols="12" md="10">
                    <label>APIKey：
                      <input v-model="apiKey" placeholder="abcdefg" type="text" outlined>
                    </label>
                  </v-col>
                  <v-col cols="12" md="6">
                    <label>プロジェクトキー：
                      <input v-model="projectKey" placeholder="abcdefg" type="text" outlined>
                    </label>
                  </v-col>
                  <v-flex xs12 offset-xs8>
                    <v-col cols="12" md="8">
                        <v-btn color="success" @click="update">更新</v-btn>
                    </v-col>
                  </v-flex>
                  <v-flex xs12 offset-xs8>
                    <v-col cols="12" md="8">
                        <v-btn color="warning" @click="deleteSpace">スペースの削除</v-btn>
                    </v-col>
                  </v-flex>
                </div>
            </div>
        </v-flex>
    </v-layout>
  </v-container> 
</template>

<script>
// 確認ダイアログ
export default {
  // レイアウトの指定
  layout(context) {
    return 'home'
  },
  // データ取得
  data: () => ({
    errors: [],
    spaceId: '',
    domain: '',
    apiKey: '',
    projectKey: '', 
  }),
  // 初期表示
  created() {
    // idの取得
    const id = this.getId(this.$cookies)
    if(!id){
      alert("選択されたスペース情報がありません。スペース情報の登録もしくは選択を行ってください。")
      this.$router.push("/")
    }
    
    let self = this
    // データ取得
    const response = this.$axios.$get
      (process.env.baseUrl + "/space/" + id)
      .then(function(response) {
        // リクエスト成功
        console.log(response);
        self.spaceId = response.spaceId
        self.domain = response.domain
        self.apiKey = response.apiKey
        self.projectKey = response.projectKey
      })
      .catch(function(error) {
        // エラー時
        console.log(error)
      });
  },
  methods: {
    // 更新ボタンクリック時
    update (){
      // エラーメッセージの初期化
      this.errors = []

      // 入力チェック
      if(!this.spaceId){
        this.errors.push("スペースIDを入力してください")
      }
      if(!this.domain){
        this.errors.push("ドメインを入力してください")
      }
      if(!this.apiKey){
        this.errors.push("APIKeyを入力してください")
      }
      if(!this.projectKey){
        this.errors.push("プロジェクトキーを入力してください")
      }
      
      // エラーが存在する場合、返却
      if(this.errors.length){
        return
      }

      // ローディングスタート
      this.$nuxt.$loading.start()

      // idの取得
      const id = this.getId(this.$cookies)
      if(!id){
        alert("選択されたスペース情報がありません。スペース情報の登録もしくは選択を行ってください。")
        this.$router.push("/")
      }

      // 更新APIを呼び出し
      let self = this
      const response = this.$axios.$put
      (process.env.baseUrl + "/space/" + id, {
        spaceId: this.spaceId,
        domain: this.domain,
        apiKey: this.apiKey,
        projectKey: this.projectKey
      }).then(function(response) {
        // リクエスト成功
        alert("更新しました。")
      }).catch(function(error) {
        // エラー時
        console.log(error)
        if(error.response.status != 204){
          if(Array.isArray(error.response.data.message)){
            self.errors.push(...error.response.data.message)
          } else {
            self.errors.push(error.response.data.message)
          }
        }
      });
    },
    // 削除ボタンクリック時
    deleteSpace(){
      let result = window.confirm('スペース情報を削除してよろしいですか？');
      // いいえの場合、処理を終了
      if(!result){
        return
      }

      // idの取得
      const id = this.getId(this.$cookies)
      if(!id){
        alert("選択されたスペース情報がありません。スペース情報の登録もしくは選択を行ってください。")
        this.$router.push("/")
      }

      // 削除APIを呼び出し
      let self = this
      const response = this.$axios.$delete
      (process.env.baseUrl + "/space/" + id).then(function(response) {
        // リクエスト成功
        console.log(response);
        alert("削除しました。スペース選択画面へ遷移します。")
        self.$router.push("/")
      }).catch(function(error) {
        // エラー時
        console.log(error)
        if(response.status != 204){
          self.errors.push(response.data)
        }
      });
    },
    // 共通function
    getId(coocke){
      const target = coocke.get('bklgAppInfo')
      if (!coocke || !target || !target.id){
        return null
      }
      return target.id
      }
    },
}
</script>