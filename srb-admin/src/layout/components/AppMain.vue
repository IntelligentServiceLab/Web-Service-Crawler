<template>
  <div>
    <el-row :gutter="20">
      <el-col :span="6">
        <div class="grid-content bg-purple">
          <span class='span-top'>APIModules数量</span><br><p class='p-right'>{{this.counts.apisCount}}</p>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="grid-content bg-purple">
          <span class='span-top'>MashupsCount数量</span><br><p class='p-right'>{{this.counts.mashupsCount}}</p>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="grid-content bg-purple">
          <span class='span-top'>SampleSourceCodeCount数量</span><br><p class='p-right'>{{this.counts.sampleSourceCodeCount}}</p>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="grid-content bg-purple">
          <span class='span-top'>SDK数量</span><br><p class='p-right'>{{this.counts.sdksCount}}</p>
        </div>
      </el-col>
    </el-row>
    <div style="width: 100%; height: 300%" ref="chart"></div>
  </div>
</template>

<script>
  import common from '@/api/common'
  const Echarts = require('echarts/lib/echarts')
  require('echarts/lib/chart/bar')
  export default {
  name: 'AppMain',
  mounted() {
    this.getCount();
    // console.log("准备执行初始化发放",this.count)
    // this.init()
  },

  data(){
    return{
      counts:{
        apisCount: 0,
        mashupsCount: 0,
        sampleSourceCodeCount: 0,
        sdksCount: 0
      },


      chart: null
    }
  },
  created() {

  },
  methods:{
    getCount(){
      common.getCount().then(response=>{
        this.counts = response.data;
        this.$nextTick(()=>{
          this.init();
        })
      })
    },
    init () {
      this.chart = Echarts.init(this.$refs.chart)
      const option = {
        title: {
          text: '数据量占比',
          // subtext: 'Fake Data',
          left: 'center',
          textStyle:{
            fontSize: '20',		//字体大小
            fontWeight: 'bolder'		//字体加粗
          },
        },
        tooltip: {
          trigger: 'item'
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {
            name: '占比',
            type: 'pie',
            radius: '50%',
            data: [
              { value: this.counts.apisCount, name: 'apis' },
              { value: this.counts.mashupsCount, name: 'mashups' },
              { value: this.counts.sampleSourceCodeCount, name: 'sampleSourceCode' },
              { value: this.counts.sdksCount, name: 'SDKs' },
            ],

            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)',
              },
            }
          }
        ]
      };
      this.chart.setOption(option)
    }
  }
}
</script>

<style lang='scss'>
  .el-row {
    margin-bottom: 20px;
    padding: 10px;
  &:last-child {
     margin-bottom: 0;
   }
  }
  .el-col {
    border-radius: 4px;
  }
  .bg-purple-dark {
    background: #99a9bf;
  }
  .bg-purple {
    background: #409EFF;
  }
  .bg-purple-light {
    background: #e5e9f2;
  }
  .grid-content {
    border-radius: 4px;
    padding: 10px;
    height: 80px;
    .span-left{

    }
    .p-right{
      text-align: center;
    }
  }
  .row-bg {
    padding: 10px 0;
    background-color: #f9fafc;
  }
</style>

