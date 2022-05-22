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
    <div class='chart' ref="chart"></div>
  </div>
</template>

<script>
  const Echarts = require('echarts/lib/echarts')
  require('echarts/lib/chart/bar')
  export default {
    name: 'BarChart',
    data(){
      return {
        names: null,
        values: null,
        num: 5,
        defaultWidth: 800,
        defaultHeight:400,
        chartWidth: 800,
        chart: null
      }
    },
    methods:{
      setData(names,values){
        this.names = names;
        this.values = values
        console.log("name",names)
        console.log("values",values)
        this.$nextTick(()=>{
          this.init()
        })
      },
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
      init () {
        if(this.chart==null){
          this.chart = Echarts.init(this.$refs.chart)
        }
        const option = {
          grid: {  // 设置柱状图整体偏移量
            x: '29',
            y: '30',
            x2: '1',
            y2: '17'
          },
          xAxis: {
            type: 'category',
            data: this.names,
            // rotate: -25 // 旋转角度
            axisLabel: {
              formatter: val => {
                let txt = val
                if (val.length > 10) {
                  txt = val.substr(0, 10)+"..."
                }
                return txt
              }
            }
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              data: this.values,
              type: 'bar',
              showBackground: true,
              backgroundStyle: {
                color: 'rgba(180, 180, 180, 0.2)'
              },
              label: {
                show: true,
                position: 'inside',
                color:'black'
              },
            },
          ]
        }
        this.chart.resize({
          height: this.defaultHeight,
          width: this.chartWidth,
        })
        this.chart.setOption(option)
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
    /*margin: 0 auto;*/
  }
</style>
