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
    <div class='chart' id='chart' ref="chart"></div>
  </div>
</template>

<script>
  import * as echarts from 'echarts';
  export default {
    name: 'RankingList',
    data(){
      return {
        names: null,
        values: null,
        num: 6,

        defaultWidth: 1075,
        defaultHeight:500,
        chartWidth: 1075,
        chart: null
      }
    },
    methods:{
      numChange(){
        this.chartWidth = (this.num/8)*this.defaultWidth
        if(this.chartWidth<this.defaultWidth){
          this.chartWidth = this.defaultWidth;
        }
        // if(height>this.chartHeight){
        //   this.chartHeight = height;
        // }
        console.log("宽度",this.chartWidth)
        this.$nextTick(()=>{
          this.$parent.getCategoriesCount(this.num);
        })
      },
      setData(names,values){
        this.names = names
        this.values = values
        console.log("names:"+names,"values:"+values)
        console.log("names:"+this.names,"values:"+this.values)
        this.$nextTick(()=>{
          this.init()
        })
      },
      init () {
        let chartDom = document.getElementById('chart');
        let myChart = echarts.init(chartDom);
        let option;

// Generate data
        let category = [];
        let dottedBase = +new Date();
        let lineData = [];
        let barData = [];
        for (let i = 0; i < 20; i++) {
          let date = new Date((dottedBase += 3600 * 24 * 1000));
          category.push(
            [date.getFullYear(), date.getMonth() + 1, date.getDate()].join('-')
          );
          let b = Math.random() * 200;
          let d = Math.random() * 200;
          barData.push(b);
          lineData.push(d + b);
        }
// option
        option = {
          grid: {  // 设置柱状图整体偏移量
            top: "2%",
            left: "3%",
            containLabel: true
          },
          backgroundColor: '#0f375f',
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'shadow'
            }
          },
          legend: {
            data: ['line', 'bar'],
            textStyle: {
              color: '#ccc'
            }
          },
          xAxis: {
            data: this.names,
            axisLabel: {
              formatter: val => {
                let txt = val
                if (val.length > 10) {
                  txt = val.substr(0, 10)+"..."
                }
                return txt
              },
              textStyle: {
                align:'left',
                color:'#fff'
              },
            },
            axisLine: {
              lineStyle: {
                color: '#ccc'
              },
            }
          },
          yAxis: {
            splitLine: { show: false },
            axisLine: {
              lineStyle: {
                color: '#ccc'
              }
            }
          },
          series: [
            {
              name: 'line',
              type: 'line',
              smooth: true,
              showAllSymbol: true,
              symbol: 'emptyCircle',
              symbolSize: 15,
              data: this.values
            },
            {
              name: 'bar',
              type: 'bar',
              barWidth: 50,
              itemStyle: {
                borderRadius: 5,
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                  { offset: 0, color: '#14c8d4' },
                  { offset: 1, color: '#43eec6' }
                ])
              },
              data: this.values
            },
            {
              name: 'line',
              type: 'bar',
              barGap: '-100%',
              barWidth: 10,
              itemStyle: {
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                  { offset: 0, color: 'rgba(20,200,212,0.5)' },
                  { offset: 0.2, color: 'rgba(20,200,212,0.2)' },
                  { offset: 1, color: 'rgba(20,200,212,0)' }
                ])
              },
              z: -12,
              data: this.values
            },
            {
              name: 'dotted',
              type: 'pictorialBar',
              symbol: 'rect',
              itemStyle: {
                color: '#0f375f'
              },
              symbolRepeat: true,
              symbolSize: [12, 4],
              symbolMargin: 1,
              z: -10,
              data: this.values
            }
          ]
        };
        myChart.resize({
          height: this.defaultHeight,
          width: this.chartWidth,
        })
        option && myChart.setOption(option);
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
