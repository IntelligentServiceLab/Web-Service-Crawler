<template>
  <div class="dashboard-container">
    <sdks-ranking-list ref='sdksRankingList'></sdks-ranking-list>
    <dynamic-word-cloud ref='dynamicWordCloud'></dynamic-word-cloud>
    <word-cloud-chart ref='wordCloudChild'/>
  </div>
</template>

<script>
  import sdks from '@/api/core/sdks'
  import WordCloudChart from '@/layout/components/wordCloud/WordCloudChart'
  import SdksRankingList from '@/layout/components/rankingList/SdksRankingList'
  import DynamicWordCloud from '@/layout/components/wordCloud/DynamicWordCloud'
  import mashups from '@/api/core/mashups'
  export default {
    name: 'SDKs',
    components: {
      WordCloudChart,
      SdksRankingList,
      DynamicWordCloud
    },
    data(){
      return {
        barData: {
          names: [],
          values: []
        },
        wordData: [],
        barNames:[],
        barValues:[],
        //用来存放tag的数据
        wordData2: []
      }
    },
    mounted() {
      this.getCategoriesCount(6);
      this.selectStaticApis(30);
      this.selectTag(50);
    },
    methods:{
      // 第一个条形图
      getCategoriesCount(num){
        sdks.getCategoriesCount(num).then(res => {
          this.barNames = []
          this.barValues = [];
          let data = res.data
          for(let i=0;i<data.length;i++){
            this.barNames.push(data[i].categories)
            this.barValues.push(data[i].total)
          }
          // console.log("数据",this.barNames,this.barValues)
          this.$nextTick(()=>{
            this.$refs.sdksRankingList.setData(this.barNames,this.barValues)
          })
        })
      },
      selectStaticApis(num){
        sdks.selectStaticApis(num).then(res => {
          let data = res.data
          let instance;
          this.wordData = []
          for(let i=0;i<data.length;i++){
            instance = {
              name: data[i].relatedApis,
              value: data[i].total
            }
            this.wordData.push(instance)
          }
          this.$nextTick(()=>{
            this.$refs.dynamicWordCloud.setData(this.wordData)
          })
        })
      },
      selectTag(num){
        sdks.selectTag(num).then(res => {
          let data = res.data
          let instance
          this.wordData2 = []
          for(let i=0;i<data.length;i++){
            instance = {
              name: data[i].tag2,
              value: data[i].total
            }
            this.wordData2.push(instance)
          }
          this.$nextTick(()=>{
            this.$refs.wordCloudChild.setData(this.wordData2)
          })
        })
      }
    }

  }
</script>

<style lang="scss" scoped>
  .dashboard {

    &-container {
      margin: 30px;
    }
    &-text {
      font-size: 30px;
      line-height: 46px;
    }
  }
</style>
