<template>
  <div class="dashboard-container">
    <RankingList ref='rankingList'></RankingList>
    <word-cloud-chart ref='wordCloudChild'/>
  </div>
</template>

<script>
import apisData from '@/api/core/apisData'
import RankingList from '@/layout/components/rankingList/ApisRankingList'
import WordCloudChart from '@/layout/components/wordCloud/WordCloudChart'
export default {
  name: 'Apis',
  components:{
    RankingList,
    WordCloudChart
  },
  data(){
    return {
      names: [],
      totals: [],
      rankData:[],
      colorRange: 100,
      wordData2: [],
    }
  },
  mounted() {
    this.getCategoriesCount(5);
    this.selectTag(50);
  },
  methods:{
    //收到子组件的数据
    getCategoriesCount(num){
      apisData.getCategoriesCount(num).then(res => {
        this.rankData = [];
        let data = res.data
        let instance
        this.rankData.push(['score', 'amount', 'product'])

        for(let i=0;i<data.length;i++){
          instance = []
          instance.push((Math.random()*this.colorRange).toFixed(1))
          instance.push(data[i].total)
          instance.push(data[i].categories)
          this.rankData.push(instance)
        }
        this.$nextTick(()=>{
          this.$refs.rankingList.setData(this.rankData)
        })
      })
    },
    selectTag(num){
      apisData.selectTag(num).then(res => {
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
