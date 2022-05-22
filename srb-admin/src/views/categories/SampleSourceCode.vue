<template>
  <div class="dashboard-container">
    <bar-chart ref='barChild'/>
    <dynamic-word-cloud ref='dynamicCouldChild'></dynamic-word-cloud>
    <word-cloud-chart ref='wordCloudChild'/>
  </div>
</template>

<script>
  import sample from '@/api/core/sample'
  import WordCloudChart from '@/layout/components/wordCloud/WordCloudChart'
  import BarChart from '@/layout/components/rankingList/BarChart'
  import DynamicWordCloud from '@/layout/components/wordCloud/DynamicWordCloud'
  import mashups from '@/api/core/mashups'
  export default {
    name: 'SampleSourceCode',
    components: {
      WordCloudChart,
      BarChart,
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
        wordData2: [],

      }
    },
    mounted() {
      this.getCategoriesCount(5);
      this.selectStaticApis(30);
      this.selectTag(50);

    },
    methods:{
      // 第一个条形图

      getCategoriesCount(num){
        sample.getCategoriesCount(num).then(res => {
          let data = res.data
          this.barNames = []
          this.barValues = []
          for(let i=0;i<data.length;i++){
            this.barNames.push(data[i].categories)
            this.barValues.push(data[i].total)
          }
          this.$nextTick(()=>{
            this.$refs.barChild.setData(this.barNames,this.barValues)
          })
        })
      },
      selectStaticApis(num){
        sample.selectStaticApis(num).then(res => {
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
            this.$refs.dynamicCouldChild.setData(this.wordData)
          })
        })
      },
      selectTag(num){
        sample.selectTag(num).then(res => {
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
