<template>
    <div class="item">
        <div>ID: {{id}}</div>
        <div>名称: {{name}}</div>
    </div>
</template>

<script>
    export default {
        name: "Item",
        props: {
            id: String,
            name: String
        }
    }
</script>

<style scoped>
.item {
    border: 1px solid;
    margin: 5px;
    background-color: white;
}
</style>