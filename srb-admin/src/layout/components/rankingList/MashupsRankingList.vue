<template>
  <div>
    <h3 style='font-size: 18px;text-align: center'>
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
    name: 'RankingList2',
    data(){
      return {
        names: [],
        values:[],
        chart: null,
        num: 5,
        defaultHeight: 400,
        chartHeight: 400,
        timer: null,
        timer2: null
      }
    },
    methods:{
      setData(names,values){
        this.names = names
        this.values = values
        console.log("条形图数据",names,values)
        this.$nextTick(()=>{
          this.init()
        })
      },
      numChange(){
        this.chartHeight = (this.num/6)*this.defaultHeight
        if(this.chartHeight<this.defaultHeight){
          this.chartHeight = this.defaultHeight;
        }
        console.log("高度",this.chartHeight)
        this.$nextTick(()=>{
          this.$parent.getCategoriesCount(this.num);
        })
      },
      init () {
        let chart
        if(this.chart == null){
          chart = Echarts.init(this.$refs.chart)
          this.chart = chart
        }else{
          chart = this.chart
        }
        let data = [];
        console.log(this.names.length)
        for (let i = 0; i < this.names.length; ++i) {
          data.push(0);
        }
        const option = {
          xAxis: {
            max: 'dataMax'
          },
          yAxis: {
            type: 'category',
            data: this.names,
            inverse: true,
            animationDuration: 200,
            animationDurationUpdate: 200,
            // max: this.num-1 // only the largest 3 bars will be displayed
          },
          series: [
            {
              realtimeSort: true,
              name: 'X',
              type: 'bar',
              data: data,
              label: {
                show: true,
                position: 'right',
                valueAnimation: true
              }
            }
          ],
          // legend: {
          //   show: true
          // },
          animationDuration: 0,
          animationDurationUpdate: 3000,
          animationEasing: 'linear',
          animationEasingUpdate: 'linear'
        };
        const values = this.values
        function run() {
          let temp = 0;
          for (let i = 0; i < data.length; ++i) {
              temp = data[i] + Math.round(Math.random() * 1000);
              if(temp<values[i]){
                data[i] = temp
              }else{
                data[i] = values[i]
              }
          }
          chart.setOption({
            series: [
              {
                type: 'bar',
                data
              }
            ]
          });
        }
        if(this.timer!=null){
          clearImmediate(this.timer)
        }
        if(this.timer2!=null){
          clearInterval(this.timer2)
        }
        this.timer = setTimeout(function () {
          run();
        }, 0);
        this.timer2 = setInterval(function () {
          run();
        }, 3000);
        chart.resize({
          height: this.chartHeight,
        })
        chart.setOption(option)
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
