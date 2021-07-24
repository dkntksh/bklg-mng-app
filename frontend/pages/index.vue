<template>
  <v-container fluid fill-height>
    <v-layout align-center justify-center>
      　 <v-flex xs12 sm10 md10>
          <div>  
            <h1>スペースの選択/登録</h1>
          </div>
          <div class="fill-height">
              <p v-if="spaceSelectErrors.length">
                    <b class="pink--text">エラーがあります:</b>
                    <ul>
                      <li v-for="spaceSelectError in spaceSelectErrors" :key="spaceSelectError" class="pink--text">{{ spaceSelectError }}</li>
                    </ul>
                  </p>
              <h3>スペースIDを選択してください</h3>
                <v-col cols="12" md="8">
                  <v-select v-model="selectedSpace" :items="items.spaces" :item-text="item =>  item.spaceId + '(' + item.domain + '/' + item.projectKey + ')'" item-value="id" label="スペースを選択してください" outlined></v-select>
                </v-col>
                <v-flex xs12 offset-xs8>
                  <v-col cols="12" md="10" color="info">
                      <v-btn class="success" @click="selectSpace">選択</v-btn>
                  </v-col>
                </v-flex>
                <h3>スペースが無い場合は、新たに登録してください。</h3>
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
                  <v-col cols="12" md="12">
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
                    <v-col cols="12" md="6">
                        <v-btn @click="regist" class="info">登録</v-btn>
                    </v-col>
                  </v-flex>
                </div>
            </div>
        </v-flex>
    </v-layout>
  </v-container> 
</template>

<script>
export default {
  // データ取得
  data: () => ({
    items: [],
    errors: [],
    spaceSelectErrors: [],
    valid: '',
    spaceId: '',
    domain: '',
    apiKey: '',
    projectKey: '', 
    selectedSpace: '',
  }),
  // 初期表示
  created() {
  },
  methods: {
    // 登録ボタンクリック時
    regist :async function() {
      // エラーメッセージの初期化
      this.errors = []
      this.spaceSelectErrors = []

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

      // 登録APIを呼び出し
      let response
      try{
        response = await this.$axios.$post
          (process.env.baseUrl + "/space", {
            spaceId: this.spaceId,
            domain: this.domain,
            apiKey: this.apiKey,
            projectKey: this.projectKey
          })
      } catch(error){
        console.log(error)
        response = error.response
      }
      console.log(response)
      if(response.status != null && response.status != 201){
        if(Array.isArray(response.data.message)){
          this.errors.push(...response.data.message)
        } else {
          this.errors.push(response.data.message)
        }
        return
      }
      // 成功時
      const setValue = {id: response.id}
      console.log(setValue)
      this.$cookies.set('bklgAppInfo', setValue, {path: '/'
        ,maxAge: 60 * 60 * 24
      })
      // home画面へ遷移する
      this.$router.push("/home")
    },
    // スペースを選択して、選択ボタンクリック時
    selectSpace (){
      this.errors = []
      this.spaceSelectErrors = []
      // 選択されているかのチェック
      if(!this.selectedSpace){
        this.spaceSelectErrors.push("スペースを選択してください。")
        return
      }
      // 選択された選択肢からidを取得して、coockieに保存
      const setValue = {id: this.selectedSpace}
      this.$cookies.set('bklgAppInfo', setValue, {path: '/'
        ,maxAge: 60 * 60 * 24
      })
      // 画面遷移
      this.$router.push("/home")
    },
  },
  async fetch() {
      this.items = await fetch(
        process.env.baseUrl + "/space/space_list"
      ).then(res => res.json())
  }
}
</script>