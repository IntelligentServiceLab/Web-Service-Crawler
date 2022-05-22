<template>
<div class="dashboard-container">
  <mashups-ranking-list ref='mashupsRankingList'></mashups-ranking-list>
  <dynamic-word-cloud ref='dynamicWordCloud'></dynamic-word-cloud>
  <word-cloud-chart ref='wordCloudChild'/>
</div>
</template>

<script>
import mashups from '@/api/core/mashups'
import MashupsRankingList from '@/layout/components/rankingList/MashupsRankingList'
import DynamicWordCloud from '@/layout/components/wordCloud/DynamicWordCloud'
import WordCloudChart from '@/layout/components/wordCloud/WordCloudChart'
export default {
  name: 'Mashups',
  components: {
    MashupsRankingList,
    DynamicWordCloud,
    WordCloudChart
  },
  data(){
    return {
      barData: {
        names: [],
        values: []
      },
      //用来存放类别的数据
      wordData: [],
      barNames:[],
      barValues:[],
      //用来存放tag的数据
      wordData2: []
    }
  },
  beforeMount() {
    this.getCategoriesCount(5);
    this.selectStaticApis(30);
    this.selectTag(50);
  },
  methods:{
    // 第一个条形图

    getCategoriesCount(num){
      mashups.getCategoriesCount(num).then(res => {
        let data = res.data
        this.barNames = []
        this.barValues = []
        for(let i=0;i<data.length;i++){
          this.barNames.push(data[i].categories)
          this.barValues.push(data[i].total)
        }
        this.$nextTick(()=>{
          this.$refs.mashupsRankingList.setData(this.barNames,this.barValues)
        })
      })
    },
    selectStaticApis(num){
      mashups.selectStaticApis(num).then(res => {
        let data = res.data
        let instance
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
      mashups.selectTag(num).then(res => {
        let data = res.data
        console.log("tag数据",res.data)
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
