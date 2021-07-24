<template>
  <v-container fluid fill-height>
    <v-layout align-center justify-center>
      　 <v-flex xs12 sm8 md6>
          <div class="fill-height">
                <h3>ひな形をダウンロードする</h3>
                <v-flex xs12 offset-xs8>
                  <v-col cols="12" md="10">
                      <v-btn color="info" @click="templateCsvFileDownload">ダウンロード</v-btn>
                  </v-col>
                </v-flex>
                <br/>
                <h3>課題登録用CSVファイルを選択して、アップロードしてください。</h3>
                <v-col cols="12" md="10">
                  <label>ファイルを選択：
                   <input type="file" name="uploadFile" @change="onChange" />
                  </label>
               </v-col>
                <v-flex xs12 offset-xs8>
                  <v-col cols="12" md="10">
                      <v-btn color="success" @click="fileUoload">アップロード</v-btn>
                  </v-col>
                </v-flex>
                <p v-if="errors.length">
                  <b class="pink--text">エラーがあります:</b>
                  <ul>
                    <li v-for="error in errors" :key="error" class="pink--text">{{ error }}</li>
                  </ul>
                </p>
            </div>
        </v-flex>
    </v-layout>
  </v-container> 
</template>

<script>
export default {
  data() {
    return {
      text: '',
      uploadFile: null,
      errors: [],
    }
  },
  // レイアウトの指定
  layout(context) {
    return 'home'
  },
  // 初期表示
  created() {
    // データ取得
  },
   methods: {
    onChange(event) {
      this.uploadFile = event.target.files[0]
    },
    // ひな形ファイルダウンロード
    templateCsvFileDownload(){
      this.$axios.$get(process.env.baseUrl + "/file/template_download", {
          responseType: "blob"
        })
        .then(response => {
          let link = document.createElement('a')
          link.href = window.URL.createObjectURL(response)
          link.download = 'Backlog-template.csv'
          link.click()
        });
    },
    // ファイルアップロード
    fileUoload(){
      // エラーメッセージの初期化
      this.errors = []
      // ファイル未選択
      if(!this.uploadFile){
        alert("ファイルを選択してください")
        return
      }

      // idの取得
      const id = this.getId(this.$cookies)
      if(!id){
        alert("選択されたスペース情報がありません。スペース情報の登録もしくは選択を行ってください。")
        this.$router.push("/")
      }

      let config = {
        headers: {
         'Content-Type': 'multipart/form-data'
        }
      }
      let self = this
      let formData = new FormData()
      formData.append('file',  this.uploadFile)
      const response = this.$axios.$post(process.env.baseUrl + "/file/" + id + "/file_upload" 
        ,formData
        ,config
      ).then(response => {
        alert("アップロードに成功しました。")
        self.uploadFile = null
      }).catch(function(error) {
        // エラー時
        alert("アップロードに失敗しました")
        console.log("Array.isArray:", Array.isArray(error.response.data.message))
        if(Array.isArray(error.response.data.message)){
          self.errors.push(...error.response.data.message)
        } else {
          self.errors.push(error.response.data.message)
        }
      });
      
    },// 共通function
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