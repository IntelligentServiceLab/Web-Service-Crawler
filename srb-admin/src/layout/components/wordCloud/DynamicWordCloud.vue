<template>
  <div>
    <h3 style='font-size: 18px;text-align: center'>
      相关联的apis前
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
    <div
      class="wordCloud__tagBall"
      :style="{width:`${this.width}px`,height:`${this.height}px`}"
      @mouseenter="stop"
      @mouseleave="start"
    >
      <span
        class="wordCloud__tag"
        v-for="(item, index) of wordData"
        :key="index"
        :style="{color:color[index % color.length],...contentEle[index].style}"
        :title="item.name+item.value"
      >{{item.name}}</span>
    </div>
  </div>
</template>


<script>
  export default {
    name: 'DynamicWordCloud',
    props: {
      width: {
        type: Number,
        default: 600
      },
      height: {
        type: Number,
        default: 600
      },
      //测试数据
      data: {
        type: Array,
        default: () => [{
          name:'',
          value: ''
        }]
      }
    },
    data(){
      return {
        color: ['#2D4DB6', '#04B67C', '#D1AF07', '#E27914', '#CB4A4D', '#B02690'],
        contentEle: [],
        direction: '-1',
        speed: 1000,
        animateID: null,
        wordData: null,
        num: 30,
      }
    },
    created() {
      // this.contentEle = this.data.map(() => ({
      //   x: 0,
      //   y: 0,
      //   z: 0,
      //   style: {}
      // }));
    },
    computed:{
    },
    methods: {
      setData(data){
        this.wordData = data
        console.log("data数据",this.wordData)
        this.$nextTick(()=>{
          this.init();
        })
      },
      numChange(){
        this.$nextTick(()=>{
          this.$parent.selectStaticApis(this.num);
        })
      },
      init() {
        this.contentEle = this.wordData.map(() => ({
          x: 0,
          y: 0,
          z: 0,
          style: {}
        }));
        const RADIUSX = (this.width - 50) / 2;
        const RADIUSY = (this.height - 50) / 2;
        this.contentEle = [];
        for (let i = 0; i < this.wordData.length; i += 1) {
          const k = -1 + (2 * (i + 1) - 1) / this.wordData.length;
          const a = Math.acos(k);
          const b = a * Math.sqrt(this.wordData.length * Math.PI);
          const x = RADIUSX * Math.sin(a) * Math.cos(b);
          const y = RADIUSY * Math.sin(a) * Math.sin(b);
          const z = RADIUSX * Math.cos(a);
          const singleEle = {
            x,
            y,
            z,
            style: {}
          };
          this.contentEle.push(singleEle);
        }
        this.animate();
      },
      animate() {
        this.rotateX();
        this.rotateY();
        this.move();
        this.animateID = window.requestAnimationFrame(this.animate);
      },
      rotateX() {
        const angleX = ['-1', '1'].includes(this.direction)
          ? Math.PI / Infinity
          : Math.PI / ((Number(this.direction) / 2) * Number(this.speed));
        const cos = Math.cos(angleX);
        const sin = Math.sin(angleX);

        this.contentEle = this.contentEle.map((t) => {
          const y1 = t.y * cos - t.z * sin;
          const z1 = t.z * cos + t.y * sin;
          return {
            ...t,
            y: y1,
            z: z1
          };
        });
      },
      rotateY() {
        const angleY = ['-2', '2'].includes(this.direction)
          ? Math.PI / Infinity
          : Math.PI / (Number(this.direction) * Number(this.speed));
        const cos = Math.cos(angleY);
        const sin = Math.sin(angleY);
        this.contentEle = this.contentEle.map((t) => {
          const x1 = t.x * cos - t.z * sin;
          const z1 = t.z * cos + t.x * sin;
          return {
            ...t,
            x: x1,
            z: z1
          };
        });
      },
      move() {
        const CX = this.width / 2;
        const CY = this.height / 2;
        this.contentEle = this.contentEle.map((singleEle) => {
          const { x, y, z } = singleEle;
          const fallLength = 500;
          const RADIUS = (this.width - 50) / 2;
          const scale = fallLength / (fallLength - z);
          const alpha = (z + RADIUS) / (2 * RADIUS);
          const left = `${x + CX - 80}px`;
          const top = `${y + CY - 15}px`;
          const transform = `translate(${left}, ${top}) scale(${scale})`;
          const style = {
            ...singleEle.style,
            opacity: alpha + 0.5,
            zIndex: parseInt(scale * 100, 10),
            transform
          };
          return {
            x,
            y,
            z,
            style
          };
        });
      },
      // 鼠标移入暂停
      stop() {
        window.cancelAnimationFrame(this.animateID);
      },
      // 鼠标离开恢复
      start() {
        this.animate();
      }
    }
  };
</script>


<style  scoped>
  button {
    margin: 20px;
  }
  .wordCloud__tagBall {
    margin: 50px auto;
    position: relative;
  }

  .wordCloud__tag {
    display: block;
    position: absolute;
    left: 0px;
    top: 0px;
    color: green;
    text-decoration: none;
    font-size: 15px;
    font-family: '微软雅黑';
    font-weight: bold;
  }
  .wordCloud__tag :hover {
    color: red;
  }

  .wordCloud__home {
    display: flex;
    justify-content: center;
  }
</style>
