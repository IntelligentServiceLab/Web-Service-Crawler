<template>
  <div>
    <h3>
      tag前
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
    <div class='wordCloud' ref="mywordcloud" :data="worddata"></div>
  </div>
</template>

<script>
  const Echarts = require('echarts/lib/echarts')
  require('echarts/lib/chart/bar')
  import "echarts-wordcloud/dist/echarts-wordcloud";
  import "echarts-wordcloud/dist/echarts-wordcloud.min";
  export default {
    name: "VueWordCloud",
    data () {
      return {
        worddata: [],
        color: ['#2D4DB6', '#04B67C', '#D1AF07', '#E27914', '#CB4A4D', '#B02690'],
        num: 50,
        defaultHeight: 500,
        chartHeight: 500,
        chart: null
      }
    },
    methods: {
      numChange(){
        this.$nextTick(()=>{
          this.$parent.selectTag(this.num);
        })
      },
      setData(data){
        this.worddata = data;
        this.$nextTick(()=>{
          this.initChart();
        })
      },
      initChart() {
        if(this.chart==null){
          this.chart = Echarts.init(this.$refs.mywordcloud)
        }
        const option = {
          backgroundColor: "#fff",
          // tooltip: {
          //   pointFormat: "{series.name}: <b>{point.percentage:.1f}%</b>"
          // },
          series: [
            {
              type: "wordCloud",
              //用来调整词之间的距离
              gridSize: 10,
              //用来调整字的大小范围
              // Text size range which the value in data will be mapped to.
              // Default to have minimum 12px and maximum 60px size.
              sizeRange: [20, 60],
              // Text rotation range and step in degree. Text will be rotated randomly in range [-90,                                                                             90] by rotationStep 45
              //用来调整词的旋转方向，，[0,0]--代表着没有角度，也就是词为水平方向，需要设置角度参考注释内容
              // rotationRange: [-45, 0, 45, 90],
              rotationRange: [-(Math.round(Math.random() * 45)) ,Math.round(Math.random() * 45)],
              // rotationRange: [0, 0],
              //随机生成字体颜色
              // maskImage: maskImage,
              textStyle: {
                color: function() {
                  return (
                    "rgb(" +
                    Math.round(Math.random() * 255) +
                    ", " +
                    Math.round(Math.random() * 255) +
                    ", " +
                    Math.round(Math.random() * 255) +
                    ")"
                  );
                }
              },
              //位置相关设置
              // Folllowing left/top/width/height/right/bottom are used for positioning the word cloud
              // Default to be put in the center and has 75% x 80% size.
              left: "center",
              top: "center",
              right: null,
              bottom: null,
              width: "100%",
              height: "100%",
              //数据
              data: this.worddata
            }
          ]
        };
        this.chart.setOption(option);
      },
    }
  }
</script>
<style lang='scss' scoped>
  .wordCloud{
    margin: 0 auto;
    width: 100%;
    height: 500px;
  }
</style>
