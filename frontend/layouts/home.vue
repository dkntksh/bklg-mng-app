<template>
  <v-app id="app" :style="{ background: $vuetify.theme.themes.light.background }">
    <v-navigation-drawer
      v-model="drawer"
      :mini-variant="miniVariant"
      :clipped="clipped"
      fixed
      app
    >
      <v-list>
        <v-list-item
          v-for="(item, i) in items"
          :key="i"
          :to="item.to"
          router
          exact
        >
          <v-list-item-content>
            <v-list-item-title v-text="item.title" />
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>
    <v-app-bar
      :clipped-left="clipped"
      fixed
      app
    >
      <v-app-bar-nav-icon @click.stop="drawer = !drawer" />
      <v-toolbar-title v-text="title" />
    </v-app-bar>
    <v-content color="background">
      <v-container>
        <nuxt />
      </v-container>
    </v-content>
    <v-footer
      :fixed="fixed"
      app
    >
      <span>&copy; {{ new Date().getFullYear() }}</span>
    </v-footer>
  </v-app>
</template>

<script>
export default {
  data () {
    return {
      clipped: false,
      drawer: false,
      fixed: false,
      items: [
        {
          icon: 'mdi-apps',
          title: 'スペース選択/登録',
          to: '/'
        },
        {
          icon: 'mdi-chart-bubble',
          title: '課題登録CSVアップロード',
          to: '/home'
        },
        {
          icon: 'mdi-bubble',
          title: 'スペース情報更新/削除',
          to: '/spaceInfo'
        },
        {
          icon: 'mdi-bubble',
          title: 'CSV登録情報',
          to: '/csvInfo'
        },
      ],
      miniVariant: false,
      right: true,
      rightDrawer: false,
      title: 'Backlog CSV App'
    }
  }
}
</script>
