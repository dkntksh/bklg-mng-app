<template>
  <v-container fluid fill-height>
    <v-layout align-center justify-center>
         <v-flex xs12 sm11 md10>
          <div class="fill-height">
                <template>
                    <div id="app">
                        <h1>CSV登録情報</h1>
                        <h2>課題の種別</h2>
                        <div class="itemContainer">
                            <Item
                            v-for="(issueTypeItem, index) in issueTypeItems" :key="index"
                            v-bind:id="issueTypeItem.id"
                            v-bind:name="issueTypeItem.name"
                            >
                            </Item>
                        </div>
                        <h2>課題の優先度</h2>
                        <div class="itemContainer">
                            <Item
                            v-for="(priorityItem, index) in priorityItems" :key="index"
                            v-bind:id="priorityItem.id"
                            v-bind:name="priorityItem.name"
                            >
                            </Item>
                        </div>
                        <h2>課題の担当者</h2>
                        <div class="itemContainer">
                            <Item
                            v-for="(userItem, index) in userItems" :key="index"
                            v-bind:id="userItem.id"
                            v-bind:name="userItem.name"
                            >
                            </Item>
                        </div>
                    </div>
                </template>
            </div>
        </v-flex>
    </v-layout>
  </v-container> 
</template>

<script>
import Item from '~/components/Item.vue'
export default {
  components: { Item },
  data: () => ({
    issueTypeItems: [],
    priorityItems: [],
    userItems: [],
  }),
  // レイアウトの指定
  layout(context) {
    return 'home'
  },
  // 初期表示
  created() {
    // データ取得
  },
  methods: {
      // 共通function
      getId(coocke){
        const target = coocke.get('bklgAppInfo')
        if (!coocke || !target || !target.id){
          return null
        }
        return target.id
      }
  },
  async fetch() {
    // idの取得
    const id = this.getId(this.$cookies)
    if(!id){
      alert("選択されたスペース情報がありません。スペース情報の登録もしくは選択を行ってください。")
      this.$router.push("/")
    }
    // 課題の種別
    this.issueTypeItems = await fetch(
          process.env.baseUrl + "/csv_info/" + id + "/issue_type"
    ).then(res => res.json())
    // 課題の優先度
    this.priorityItems = await fetch(
          process.env.baseUrl + "/csv_info/" + id + "/priorities"
    ).then(res => res.json())
    // 担当者情報の取得
    this.userItems = await fetch(
          process.env.baseUrl + "/csv_info/" + id + "/users"
    ).then(res => res.json())
  }
}
</script>
<style scoped>
.itemContainer{
    display: flex;
}
</style>