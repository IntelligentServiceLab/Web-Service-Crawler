<template>
  <div>
    <h3>
      所属类别排名前
      <el-input
      class='inputNum'
      type='number'
      maxlength='5'
      v-model='num'
      oninput="if(value.length>2)value=value.slice(0,2)"
      label-width='50px'
      @change='numChange'
    ></el-input>
    </h3>
    <div class="chart" ref="chart"></div>
  </div>
</template>

<script>
  const Echarts = require('echarts/lib/echarts')
  require('echarts/lib/chart/bar')
  export default {
    name: 'RankingList',
    data(){
      return {
        rankData: [],
        chart: null,
        num: 5,
        defaultHeight: 500,
        chartHeight: 500,

      }
    },
    props:['receive'],
    methods:{
      setData(data){
        this.rankData = data;
        console.log("数据",this.rankData)
        this.$nextTick(()=>{
          this.init()
        })
      },
      numChange(){
        this.chartHeight = (this.num/8)*this.defaultHeight
        if(this.chartHeight<this.defaultHeight){
          this.chartHeight = this.defaultHeight;
        }
        // if(height>this.chartHeight){
        //   this.chartHeight = height;
        // }
        console.log("高度",this.chartHeight)
        this.$nextTick(()=>{
          this.$parent.getCategoriesCount(this.num);
        })
      },
      init () {
        if(this.chart==null){
          this.chart = Echarts.init(this.$refs.chart)
        }

        const option = {
          dataset: {
            source: this.rankData
          },
          grid: { containLabel: true },
          xAxis: {},
          yAxis: { type: 'category'},
          visualMap: {
            orient: 'horizontal',
            left: 'center',
            min: 10,
            max: 100,
            text: ['color', ''],
            // Map the score column to color
            dimension: 0,
            inRange: {
              color: ['#65B581', '#FFCE34', '#FD665F']
            }
          },
          series: [
            {
              type: 'bar',
              barWidth:30,
              encode: {
                // Map the "amount" column to X axis.
                x: 'amount',
                // Map the "product" column to Y axis
                y: 'product'
              },
              label: {
                show: true,
                position: 'right',
              }
            }
          ]
        };
        this.chart.resize({
          height: this.chartHeight,
        })
        this.chart.setOption(option,true)
      }
    }
  }
</script>

<style lang='scss'>
  h3{
    font-size: 18px;
    text-align: center;
    .inputNum{
      width: 100px;

    }
  }
  .chart{
    margin: 0 auto;
  }
</style>
